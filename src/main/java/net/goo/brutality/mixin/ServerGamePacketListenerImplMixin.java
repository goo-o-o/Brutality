package net.goo.brutality.mixin;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.base.BrutalityAbstractPhysicsThrowingProjectile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Inject(
            method = "handleInteract(Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/network/protocol/game/ServerboundInteractPacket;dispatch(Lnet/minecraft/network/protocol/game/ServerboundInteractPacket$Handler;)V"
            ),
            cancellable = true
    )
    private void allowBrutalityTridentAttack(ServerboundInteractPacket packet, CallbackInfo ci) {
        ServerGamePacketListenerImpl instance = (ServerGamePacketListenerImpl) (Object) this;
        ServerLevel serverLevel = instance.player.serverLevel();
        Entity entity = packet.getTarget(serverLevel);

        // Create a custom handler to inspect the action
        ServerboundInteractPacket.Handler handler = new ServerboundInteractPacket.Handler() {
            @Override
            public void onInteraction(InteractionHand hand) {
            }

            @Override
            public void onInteraction(InteractionHand hand, Vec3 location) {
            }

            @Override
            public void onAttack() {
                boolean first = !(entity instanceof ItemEntity) && !(entity instanceof ExperienceOrb) && !(entity instanceof AbstractArrow) && entity != instance.player;

                boolean second = entity instanceof BrutalityAbstractPhysicsThrowingProjectile;

                if (first || second) {
                    ItemStack itemstack = instance.player.getItemInHand(InteractionHand.MAIN_HAND);
                    if (itemstack.isItemEnabled(serverLevel.enabledFeatures())) {
                        instance.player.attack(entity);
                    }
                } else {
                    instance.disconnect(Component.translatable("multiplayer.disconnect.invalid_entity_attacked"));
                    Brutality.LOGGER.warn("Player {} tried to attack an invalid entity", instance.player.getName().getString());
                }
            }
        };

        // Dispatch the packet with our custom handler
        packet.dispatch(handler);
        ci.cancel(); // Prevent default dispatch
    }
}