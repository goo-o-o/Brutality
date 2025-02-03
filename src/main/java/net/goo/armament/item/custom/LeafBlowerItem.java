package net.goo.armament.item.custom;

import net.goo.armament.item.custom.client.renderer.LeafBlowerItemRenderer;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sOffLeafBlowerPacket;
import net.goo.armament.sound.ModSounds;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.function.Consumer;

public class LeafBlowerItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final String ACTIVE_KEY = "LeafBlowerActive";
    private int tickCounter;

    public LeafBlowerItem(Properties pProperties) {
        super(pProperties);
    }

    int[] color1 = new int[]{212, 6, 6};
    int[] color2 = new int[]{255, 255, 255};

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.leaf_blower.desc.1").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.leaf_blower.desc.2").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color1))));
        pTooltipComponents.add(Component.translatable("item.armament.leaf_blower.desc.3").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.addGradientText((Component.translatable("item.armament.leaf_blower")), color1, color2).withStyle(Style.EMPTY.withBold(true));
    }

    public static void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean(ACTIVE_KEY, active);
    }

    public static boolean isActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(ACTIVE_KEY);
    }

    private PlayState predicate(AnimationState animationState) {
        ItemStack stack = (ItemStack) animationState.getData(DataTickets.ITEMSTACK);
            if (isActive(stack)) {
                animationState.getController().setAnimation(RawAnimation.begin().thenPlay("on").thenLoop("active"));
                return PlayState.CONTINUE;
            } else {
                animationState.getController().setAnimation(RawAnimation.begin().thenPlay("off"));
                return PlayState.STOP;
            }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private LeafBlowerItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    renderer = new LeafBlowerItemRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            pPlayer.startUsingItem(pUsedHand);
            pPlayer.playSound(ModSounds.LEAF_BLOWER_ON.get(), 0.25F, 1);
        }
        if (!pLevel.isClientSide) {
            setActive(pPlayer.getItemInHand(pUsedHand), true);
        }
        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
         if (pLivingEntity instanceof Player pPlayer) {
                pPlayer.playSound(ModSounds.LEAF_BLOWER_OFF.get(), 0.25F, 1);
                PacketHandler.sendToServer(new c2sOffLeafBlowerPacket());;
                tickCounter = 0;
            }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

        if (isActive(pStack)) {
            if (pLevel.isClientSide && pEntity instanceof Player pPlayer) {
                tickCounter++;
                if (tickCounter >= 28 && tickCounter % 5 == 0) {
                    pPlayer.playSound(ModSounds.LEAF_BLOWER_ACTIVE.get(), 0.25F, 1);
                }
            }

            if (!pLevel.isClientSide && pEntity instanceof Player pPlayer) {
                ItemStack mainHandStack = pPlayer.getMainHandItem();
                ItemStack offHandStack = pPlayer.getOffhandItem();

                boolean bothHandsActive = mainHandStack.getItem() instanceof LeafBlowerItem && offHandStack.getItem() instanceof LeafBlowerItem;
                boolean eitherHandsActive = mainHandStack.getItem() instanceof LeafBlowerItem || offHandStack.getItem() instanceof LeafBlowerItem;

                if (bothHandsActive || eitherHandsActive) {

                    float range = bothHandsActive ? 12.0F : 6F;
                    float pushScale = bothHandsActive ? 0.5F : 0.25F;
                    Vec3 viewVector = pPlayer.getViewVector(1.0F);
                    Vec3 eyePosition = pPlayer.getEyePosition();
                    Vec3 targetPosition = eyePosition.add(viewVector.scale(range));
                    AABB searchBox = new AABB(eyePosition, targetPosition).inflate(bothHandsActive ? 1.5F : 1.0F);

                    List<Entity> entities = pLevel.getEntities(pPlayer, searchBox, entity -> entity instanceof LivingEntity);

                    for (Entity target : entities) {
                        // Check if the target is in line of sight and not underwater
                        if (pPlayer.hasLineOfSight(target) && !pPlayer.isUnderWater()) {
                            Vec3 pushDirection = target.position().subtract(pPlayer.position()).normalize().scale(pushScale);

                            // Add delta movement to the target
                            target.addDeltaMovement(pushDirection);

                            // If the target is a ServerPlayer, send a motion packet to update its position on client
                            if (target instanceof ServerPlayer serverTarget) {
                                // Send the packet only for the target player, not for pPlayer
                                serverTarget.connection.send(new ClientboundSetEntityMotionPacket(target));
                            }
                        }
                    }

                    // Apply player recoil
                    if (bothHandsActive && pPlayer instanceof ServerPlayer) {
                        Vec3 oppositePushVector = new Vec3(-pPlayer.getLookAngle().x, -pPlayer.getLookAngle().y, -pPlayer.getLookAngle().z).normalize();
                        Vec3 playerRecoilVector = oppositePushVector.scale(0.04F);
                        pPlayer.addDeltaMovement(playerRecoilVector);
                        ((ServerPlayer) pPlayer).connection.send(new ClientboundSetEntityMotionPacket(pPlayer));
                    }

                } else {
                    setActive(pStack, false);
                }
            }
        }
    }
}
