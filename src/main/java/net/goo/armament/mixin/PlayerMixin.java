package net.goo.armament.mixin;

import net.goo.armament.registry.ModItems;
import net.goo.armament.registry.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(Player.class)
public abstract class PlayerMixin {

    @Inject(method = "sweepAttack", at = @At("HEAD"), cancellable = true)
    private void onSweepAttack(CallbackInfo ci) {
        // Cast 'this' to Player
        Player player = (Player) (Object) this;

        // Check if the player is holding the specific item
        ItemStack heldItem = player.getMainHandItem();
        if (heldItem.getItem() == ModItems.FALLEN_SCYTHE.get()) {
            // Cancel the original sweep attack particle
            ci.cancel();

            if (player.getAttackStrengthScale(0.5F) >= 1.0F) {
                if (player.level() instanceof ServerLevel world) {
                    double d0 = -Math.sin(Math.toRadians(player.getYRot())) * 2;
                    double d1 = Math.cos(Math.toRadians(player.getYRot())) * 2;

                    ParticleOptions customParticle = ModParticles.SOUL_SWEEP_PARTICLE.get();
                    world.sendParticles(
                            customParticle,
                            player.getX() + d0,
                            player.getY(0.5D),
                            player.getZ() + d1,
                            0,
                            d0, 0.0D, d1, 0.0D
                    );
                }
            }
        }
    }
}