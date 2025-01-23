package net.goo.armament.item.custom;

import net.goo.armament.item.custom.client.renderer.LeafBlowerItemRenderer;
import net.goo.armament.sound.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
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
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.function.Consumer;

public class LeafBlowerItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final String TOGGLE_KEY = "LeafBlowerActive";
    private boolean isActive = false;

    public LeafBlowerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.leaf_blower.desc1"));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.leaf_blower.desc2"));
        pTooltipComponents.add(Component.translatable("item.armament.leaf_blower.desc3"));
        pTooltipComponents.add(Component.literal(""));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public boolean isActive(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        return tag.getBoolean(TOGGLE_KEY);
    }

    private PlayState predicate(AnimationState animationState) {
        ItemStack stack = (ItemStack) animationState.getData(DataTickets.ITEMSTACK);

        if (stack != null) {
            CompoundTag tag = stack.getOrCreateTag();
            boolean isActive = tag.getBoolean("LeafBlowerActive");

            if (isActive) {
                animationState.getController().setAnimation(RawAnimation.begin().then("active", Animation.LoopType.LOOP));
                return PlayState.CONTINUE;
            }
        }

        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 2, this::predicate));
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
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);

            CompoundTag tag = stack.getOrCreateTag();
            boolean isActive = tag.getBoolean("LeafBlowerActive");
            tag.putBoolean("LeafBlowerActive", !isActive);

            if (isActive) {
                // Play the turn-off sound when the item is deactivated
                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                        ModSounds.LEAF_BLOWER_OFF.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                tag.putBoolean(TOGGLE_KEY, false);
            } else {
                // Play the turn-on sound when the item is activated
                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                        ModSounds.LEAF_BLOWER_ON.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
                tag.putBoolean(TOGGLE_KEY, true);

                // Play the looped active sound once it's turned on


                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                        ModSounds.LEAF_BLOWER_ACTIVE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            }

            pPlayer.displayClientMessage(Component.translatable("item.armament.leaf_blower." + (isActive ? "off" : "on")), true);
        }

        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player pPlayer) {
            if (pStack.getOrCreateTag().getBoolean(TOGGLE_KEY) && pIsSelected) {
                Vec3 viewVector = pPlayer.getViewVector(1.0F);
                double range = 10.0D;

                Vec3 eyePosition = pPlayer.getEyePosition();
                Vec3 targetPosition = eyePosition.add(viewVector.scale(range));
                AABB searchBox = new AABB(eyePosition, targetPosition).inflate(1.0D);

                List<Entity> entities = pLevel.getEntities(pPlayer, searchBox, entity -> entity instanceof LivingEntity && entity != pPlayer);

                for (Entity target : entities) {
                    if (pPlayer.hasLineOfSight(target) && !pPlayer.isUnderWater()) {
                        Vec3 pushDirection = target.position().subtract(pPlayer.position()).normalize().scale(1.5D);
                        target.setDeltaMovement(pushDirection);
                    }
                }
            }
        }

        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }
}
