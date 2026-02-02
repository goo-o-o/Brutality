package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.common.entity.explosion.BrutalityExplosion;
import net.goo.brutality.util.ModExplosionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ClientboundBrutalityExplodePacket extends ClientboundExplodePacket {
    private final String clazz;
    private final boolean spawnParticles;

    public ClientboundBrutalityExplodePacket(double pX, double pY, double pZ, float pPower, List<BlockPos> pToBlow, @Nullable Vec3 pKnockback, boolean spawnParticles, Class<? extends BrutalityExplosion> clazz) {
        super(pX, pY, pZ, pPower, pToBlow, pKnockback);
        this.clazz = clazz.getName();
        this.spawnParticles = spawnParticles;
    }

    public ClientboundBrutalityExplodePacket(FriendlyByteBuf buf) {
        super(buf);
        this.clazz = buf.readUtf();
        this.spawnParticles = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf) {
        super.write(buf);
        buf.writeUtf(this.clazz);
        buf.writeBoolean(this.spawnParticles);
    }

    public String getClazz() {
        return clazz;
    }
    public boolean isSpawnParticles() {
        return spawnParticles;
    }


    @Override
    public void handle(ClientGamePacketListener pHandler) {

    }

    public static void handle(ClientboundBrutalityExplodePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ModExplosionHelper.Client.handleExplosion(packet));
        ctx.get().setPacketHandled(true);
    }
}