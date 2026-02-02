package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.ClientAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

import static net.goo.brutality.Brutality.LOGGER;

public class ClientboundParticlePacket {
    public final float x;
    public final float y;
    public final float z;
    public final float xDist;
    public final float yDist;
    public final float zDist;
    public final float xSpeed;
    public final float ySpeed;
    public final float zSpeed;
    public final int count;
    public final boolean overrideLimiter;
    public final ParticleOptions particle;

    /**
     * Allows the sending of particles from the client side to all other clients through the server
     * This is the non-exacting version, with Gaussian offset
     * Works exactly the same as ServerLevel#sendParticles() but called on the client
     */
    public <T extends ParticleOptions> ClientboundParticlePacket(T pParticle, boolean pOverrideLimiter, float pX, float pY, float pZ, float pXDist, float pYDist, float pZDist, float xSpeed, float ySpeed, float zSpeed, int pCount) {
        this.particle = pParticle;
        this.overrideLimiter = pOverrideLimiter;
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.xDist = pXDist;
        this.yDist = pYDist;
        this.zDist = pZDist;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        this.count = pCount;
    }

    public ClientboundParticlePacket(FriendlyByteBuf pBuffer) {
        ParticleType<?> particletype = pBuffer.readRegistryIdSafe(ParticleType.class);
        this.overrideLimiter = pBuffer.readBoolean();
        this.x = pBuffer.readFloat();
        this.y = pBuffer.readFloat();
        this.z = pBuffer.readFloat();
        this.xDist = pBuffer.readFloat();
        this.yDist = pBuffer.readFloat();
        this.zDist = pBuffer.readFloat();
        this.xSpeed = pBuffer.readFloat();
        this.ySpeed = pBuffer.readFloat();
        this.zSpeed = pBuffer.readFloat();
        this.count = pBuffer.readInt();
        this.particle = this.readParticle(pBuffer, particletype);
    }

    public void write(FriendlyByteBuf pBuffer) {
        pBuffer.writeRegistryId(ForgeRegistries.PARTICLE_TYPES, this.particle.getType());
        pBuffer.writeBoolean(this.overrideLimiter);
        pBuffer.writeFloat(this.x);
        pBuffer.writeFloat(this.y);
        pBuffer.writeFloat(this.z);
        pBuffer.writeFloat(this.xDist);
        pBuffer.writeFloat(this.yDist);
        pBuffer.writeFloat(this.zDist);
        pBuffer.writeFloat(this.xSpeed);
        pBuffer.writeFloat(this.ySpeed);
        pBuffer.writeFloat(this.zSpeed);
        pBuffer.writeInt(this.count);
        this.particle.writeToNetwork(pBuffer);
    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
        try {
            return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
        } catch (Exception e) {
            LOGGER.error("Failed to deserialize particle type {}: {}", pParticleType, e.getMessage());
            throw e; // Re-throw to help debug
        }
    }

    public static void handle(ClientboundParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientAccess.spawnParticles(packet));
        ctx.get().setPacketHandled(true);
    }

}
