package net.goo.armament.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public class s2cEnhancedExactParticlePacket {
    private final double x, y, z, xSpeed, ySpeed, zSpeed;
    private final ParticleOptions particle;


    public s2cEnhancedExactParticlePacket(double pX, double pY, double pZ, double xSpeed, double ySpeed, double zSpeed, ParticleOptions particle) {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        this.particle = particle;
    }

    public s2cEnhancedExactParticlePacket(FriendlyByteBuf pBuffer) {
        ParticleType<?> type = readParticleType(pBuffer);
        this.x = pBuffer.readDouble();
        this.y = pBuffer.readDouble();
        this.z = pBuffer.readDouble();
        this.xSpeed = pBuffer.readDouble();
        this.ySpeed = pBuffer.readDouble();
        this.zSpeed = pBuffer.readDouble();
        this.particle = readParticle(pBuffer, type);
    }

    public void encode(FriendlyByteBuf pBuffer) {
        writeParticleType(pBuffer, particle.getType());
        pBuffer.writeDouble(this.x);
        pBuffer.writeDouble(this.y);
        pBuffer.writeDouble(this.z);
        pBuffer.writeDouble(this.xSpeed);
        pBuffer.writeDouble(this.ySpeed);
        pBuffer.writeDouble(this.zSpeed);
        this.particle.writeToNetwork(pBuffer);
    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
        return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
    }

    private static void writeParticleType(FriendlyByteBuf buf, ParticleType<?> type) {
        // Always write the ResourceLocation
        buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(type)));
    }

    private static ParticleType<?> readParticleType(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();

        ParticleType<?> type = ForgeRegistries.PARTICLE_TYPES.getValue(id);
        if (type != null) return type;

        type = BuiltInRegistries.PARTICLE_TYPE.get(id);
        if (type != null) return type;

        return ParticleTypes.EXPLOSION;
    }

    public static void handle(s2cEnhancedExactParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null && Minecraft.getInstance().player != null) {
                Level level = Minecraft.getInstance().level;
                level.addParticle(packet.particle, true, packet.x, packet.y, packet.z, packet.xSpeed, packet.ySpeed, packet.zSpeed);
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
