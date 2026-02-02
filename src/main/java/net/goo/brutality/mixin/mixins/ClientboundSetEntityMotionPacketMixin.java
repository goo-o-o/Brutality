package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.ModUtils;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientboundSetEntityMotionPacket.class)
public abstract class ClientboundSetEntityMotionPacketMixin {

    @Shadow @Final @Mutable private int xa;
    @Shadow @Final @Mutable private int ya;
    @Shadow @Final @Mutable private int za;

    /**
     * @reason Overwrites vanilla velocity compression with half-precision floats
     * to prevent precision loss on fast projectiles.
     * @author goo (Logic adapted from CoFH Core / Fast Projectile Fix)
     */
    @Inject(
            method = "<init>(ILnet/minecraft/world/phys/Vec3;)V",
            at = @At("TAIL")
    )
    private void brutality$reencodeVelocity(int id, Vec3 velocity, CallbackInfo ci) {
        // CoFH Core already implements this fix. If it's present, we step back
        // to avoid double-processing or conflicts.
        if (!ModList.get().isLoaded("cofh_core")) {
            this.xa = ModUtils.packHalfFloat((float) velocity.x);
            this.ya = ModUtils.packHalfFloat((float) velocity.y);
            this.za = ModUtils.packHalfFloat((float) velocity.z);
        }
    }
}