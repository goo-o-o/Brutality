package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LeafBlowerItem extends BrutalityGenericItem {
    private static final String ACTIVE_KEY = "LeafBlowerActive";
    private int tickCounter;

    public LeafBlowerItem(String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(identifier, rarity, descriptionComponents);
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

    @SubscribeEvent
    public static void cancelFallDamage(LivingFallEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            ItemStack mainStack = player.getMainHandItem();
            ItemStack offStack = player.getOffhandItem();
            Item mainItem = mainStack.getItem();
            Item offItem = offStack.getItem();

            if (mainItem instanceof LeafBlowerItem && offItem instanceof LeafBlowerItem) {
                if (mainStack.getOrCreateTag().getBoolean(ACTIVE_KEY) && offStack.getOrCreateTag().getBoolean(ACTIVE_KEY)) {
                    event.setCanceled(true);

                }
            }
        }
    }
}
