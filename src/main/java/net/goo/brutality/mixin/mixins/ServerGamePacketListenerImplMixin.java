package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.goo.brutality.util.MixinInterfaces;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Inject(
            method = "handleInteract(Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;dispatch(Lnet/minecraft/network/protocol/game/ServerboundInteractPacket$Handler;)V",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void customHandleInteract(ServerboundInteractPacket pPacket, CallbackInfo ci, @Local ServerLevel serverlevel, @Local Entity entity) {
        try {
            ServerGamePacketListenerImpl thisInstance = (ServerGamePacketListenerImpl)(Object)this;

            // Replicate the checks from handleInteract
            if (entity == null || !serverlevel.getWorldBorder().isWithinBounds(entity.blockPosition())) {
                return;
            }

            if (!thisInstance.player.canReach(entity, 3)) {
                return;
            }

            // Custom handler to process interactions and attacks
            pPacket.dispatch(new ServerboundInteractPacket.Handler() {
                private void performInteraction(InteractionHand hand, MixinInterfaces.InteractionFunction interaction) {
                    ItemStack itemstack = thisInstance.player.getItemInHand(hand);
                    if (itemstack.isItemEnabled(serverlevel.enabledFeatures())) {
                        InteractionResult interactionResult = interaction.apply(thisInstance.player, entity, hand);
                        if (interactionResult.consumesAction()) {
                            // Track item usage stat
                            thisInstance.player.awardStat(Stats.ITEM_USED.get(itemstack.getItem()));
                            if (interactionResult.shouldSwing()) {
                                thisInstance.player.swing(hand, true);
                            }
                        }
                    }
                }

                @Override
                public void onInteraction(InteractionHand hand) {
                    performInteraction(hand, (player, entity, hand1) -> player.interactOn(entity, hand1));
                }

                @Override
                public void onInteraction(InteractionHand hand, Vec3 pos) {
                    performInteraction(hand, (player, entity, hand1) -> {
                        InteractionResult onInteractEntityAtResult = ForgeHooks.onInteractEntityAt(player, entity, pos, hand);
                        if (onInteractEntityAtResult != null) {
                            return onInteractEntityAtResult;
                        }
                        return entity.interactAt(player, pos, hand);
                    });
                }

                @Override
                public void onAttack() {
                    boolean isValidEntity = !(entity instanceof ItemEntity) &&
                            !(entity instanceof ExperienceOrb) &&
                            !(entity instanceof AbstractArrow) &&
                            entity != thisInstance.player;
                    boolean isCustomProjectile = entity instanceof BrutalityAbstractPhysicsThrowingProjectile;

                    if (isValidEntity || isCustomProjectile) {
                        ItemStack itemstack = thisInstance.player.getItemInHand(InteractionHand.MAIN_HAND);
                        if (itemstack.isItemEnabled(serverlevel.enabledFeatures())) {
                            thisInstance.player.attack(entity);
                        }
                    } else {
                        thisInstance.disconnect(Component.translatable("multiplayer.disconnect.invalid_entity_attacked"));
                        Brutality.LOGGER.warn("Player {} tried to attack an invalid entity", thisInstance.player.getName().getString());
                    }
                }
            });

            // Cancel the original dispatch to prevent duplicate handling
            ci.cancel();
        } catch (Exception e) {
            Brutality.LOGGER.error("Error in customHandleInteract: {}", e.getMessage(), e);
            ci.cancel();
        }
    }

}