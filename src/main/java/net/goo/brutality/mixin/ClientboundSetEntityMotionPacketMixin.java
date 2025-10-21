package net.goo.brutality.mixin;

import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ClientboundSetEntityMotionPacket.class)
public abstract class ClientboundSetEntityMotionPacketMixin {

    @Shadow
    @Final
    @Mutable
    private int xa;

    @Shadow
    @Final
    @Mutable
    private int ya;

    @Shadow
    @Final
    @Mutable
    private int za;

    // EDIT: Having incompatibilities with CoFH Core, since both Mixins try to achieve the same thing, I will just use their code with attribution.
    // So thank you Team CoFH for the probably better code
    // Also thank you https://www.curseforge.com/minecraft/mc-mods/hespercqs-fast-projectile-fix for the original code

        @Inject(
            method = {"<init>(ILnet/minecraft/world/phys/Vec3;)V"},
            at = {@At("TAIL")}
    )
    public void init(int id, Vec3 velocity, CallbackInfo ci) {
        // Only use if CoFH core is not loaded, since this originates from that mod
        if (!ModList.get().isLoaded("cofh-core")) {
            this.xa = brutality$fromFloat((float) velocity.x);
            this.ya = brutality$fromFloat((float) velocity.y);
            this.za = brutality$fromFloat((float) velocity.z);
        }
    }
    @Unique
    private static int brutality$fromFloat(float fval) {
        int fbits = Float.floatToIntBits(fval);
        int sign = fbits >>> 16 & 0x8000;
        int val = (fbits & 0x7fffffff) + 0x1000;
        if (val >= 0x47800000) {
            if ((fbits & 0x7fffffff) >= 0x47800000) {
                if (val < 0x7f800000) {
                    return sign | 0x7c00;
                }
                return sign | 0x7c00 | (fbits & 0x007fffff) >>> 13;
            }
            return sign | 0x7bff;
        }
        if (val >= 0x38800000) {
            return sign | val - 0x38000000 >>> 13;
        }
        if (val < 0x33000000) {
            return sign;
        }
        val = (fbits & 0x7fffffff) >>> 23;
        return sign | ((fbits & 0x7fffff | 0x800000) + (0x800000 >>> val - 102) >>> 126 - val);
    }


//    // Constructor from Vector
//    @Inject(method = "<init>(ILnet/minecraft/world/phys/Vec3;)V", at = @At(value = "RETURN"))
//    public void ClientboundSetEntityMotionPacket(int entityId, Vec3 motionVector, CallbackInfo ci) {
//        this.id = entityId;
//        this.xa = (int) (motionVector.x * 8000.0D);
//        this.ya = (int) (motionVector.y * 8000.0D);
//        this.za = (int) (motionVector.z * 8000.0D);
//    }
//
//    // Constructor from Packet
//    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V", at = @At(value = "RETURN"))
//    public void ClientboundSetEntityMotionPacket(FriendlyByteBuf buf, CallbackInfo ci) {
//        this.id = buf.readVarInt();
//        this.xa = buf.readInt();
//        this.ya = buf.readInt();
//        this.za = buf.readInt();
//    }
//
//    // Write Packet
//    @Inject(method = "write", at = @At(value = "HEAD"), cancellable = true)
//    public void readPacketData(FriendlyByteBuf buf, CallbackInfo ci) {
//        ci.cancel();
//        buf.writeVarInt(this.id);
//        buf.writeInt(this.xa);
//        buf.writeInt(this.ya);
//        buf.writeInt(this.za);
//    }
}