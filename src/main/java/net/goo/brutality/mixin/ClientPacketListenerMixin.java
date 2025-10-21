package net.goo.brutality.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private ClientLevel level;


    /**
     * Thank you Team CoFH for the code, the reason I used this code can be found in {@link ClientboundSetEntityMotionPacketMixin}
     */
    @Inject(
            method = "handleSetEntityMotion",
            at = @At("HEAD"),
            cancellable = true
    )
    public void handle(ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
        if (!ModList.get().isLoaded("cofh-core")) {

            PacketUtils.ensureRunningOnSameThread(packet, (ClientPacketListener) (Object) this, minecraft);
            Entity entity = level.getEntity(packet.getId());
            if (entity != null) {
                entity.lerpMotion(brutality$toFloat(packet.getXa()), brutality$toFloat(packet.getYa()), brutality$toFloat(packet.getZa()));
            }
            ci.cancel();
        }
    }

    @Unique
    private static float brutality$toFloat(int hbits) {

        int mant = hbits & 0x03ff;
        int exp = hbits & 0x7c00;
        if (exp == 0x7c00) {
            exp = 0x3fc00;
        } else if (exp != 0) {
            exp += 0x1c000;
            if (mant == 0 && exp > 0x1c400) {
                return Float.intBitsToFloat((hbits & 0x8000) << 16 | exp << 13 | 0x3ff);
            }
        } else if (mant != 0) {
            exp = 0x1c400;
            do {
                mant <<= 1;
                exp -= 0x400;
            } while ((mant & 0x400) == 0);
            mant &= 0x3ff;
        }
        return Float.intBitsToFloat((hbits & 0x8000) << 16 | (exp | mant) << 13);
    }

}