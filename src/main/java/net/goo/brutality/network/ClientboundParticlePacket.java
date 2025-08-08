package net.goo.brutality.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

import static net.goo.brutality.Brutality.LOGGER;

public class ClientboundParticlePacket {
    private final float x;
    private final float y;
    private final float z;
    private final float xDist;
    private final float yDist;
    private final float zDist;
    private final float xSpeed;
    private final float ySpeed;
    private final float zSpeed;
    private final int count;
    private final boolean overrideLimiter;
    private final ParticleOptions particle;

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
        ParticleType<?> particletype = pBuffer.readById(BuiltInRegistries.PARTICLE_TYPE);
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
        pBuffer.writeId(BuiltInRegistries.PARTICLE_TYPE, this.particle.getType());
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
        return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
    }

    public static void handle(ClientboundParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level == null) return;
            ClientLevel level = Minecraft.getInstance().level;

            for (int i = 0; i < packet.count; ++i) {
                float xOffset = (float) (level.random.nextGaussian() * packet.xDist);
                float yOffset = (float) (level.random.nextGaussian() * packet.yDist);
                float zOffset = (float) (level.random.nextGaussian() * packet.zDist);

                try {
                    level.addParticle(packet.particle, packet.overrideLimiter, packet.x + xOffset, packet.y + yOffset, packet.z + zOffset, packet.xSpeed, packet.ySpeed, packet.zSpeed);
                } catch (Throwable throwable) {
                    LOGGER.warn("Could not spawn particle effect {}", packet.particle);
                    return;
                }

            }
        });
        ctx.get().setPacketHandled(true);
    }

}
