package net.goo.brutality.network;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ClientboundExactParticlePacket {
    public final double x;
    public final double y;
    public final double z;
    public final ParticleOptions particle;

    /**
     * Allows the sending of particles from the client side to all other clients through the server
     * This is the exacting version, with no Gaussian offset
     * */
    public ClientboundExactParticlePacket(double pX, double pY, double pZ, ParticleOptions particle) {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.particle = particle;
    }

    public ClientboundExactParticlePacket(FriendlyByteBuf pBuffer) {
        ParticleType<?> type = pBuffer.readRegistryIdSafe(ParticleType.class);
        this.x = pBuffer.readDouble();
        this.y = pBuffer.readDouble();
        this.z = pBuffer.readDouble();
        this.particle = readParticle(pBuffer, type);
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeRegistryId(ForgeRegistries.PARTICLE_TYPES, this.particle.getType());
        pBuffer.writeDouble(this.x);
        pBuffer.writeDouble(this.y);
        pBuffer.writeDouble(this.z);
        this.particle.writeToNetwork(pBuffer);
    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
        return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
    }

    public static void handle(ClientboundExactParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender != null && sender.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(packet.particle, packet.x, packet.y, packet.z, 1, 0,0,0,0);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
