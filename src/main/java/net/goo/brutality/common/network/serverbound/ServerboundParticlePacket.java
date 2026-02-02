package net.goo.brutality.common.network.serverbound;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;
import java.util.function.Supplier;

public class ServerboundParticlePacket {
    private final double x, y, z, xDelta, yDelta, zDelta;
    private final ParticleOptions particle;
    private final int count;
    private final float speed;

    public ServerboundParticlePacket(double pX, double pY, double pZ, double xDelta, double yDelta, double zDelta, ParticleOptions particle, int count, float speed) {
        this.x = pX;
        this.y = pY;
        this.z = pZ;
        this.xDelta = xDelta;
        this.yDelta = yDelta;
        this.zDelta = zDelta;
        this.particle = particle;
        this.count = count;
        this.speed = speed;
    }

    public ServerboundParticlePacket(FriendlyByteBuf pBuffer) {
        ParticleType<?> type = readParticleType(pBuffer);
        this.x = pBuffer.readDouble();
        this.y = pBuffer.readDouble();
        this.z = pBuffer.readDouble();
        this.xDelta = pBuffer.readDouble();
        this.yDelta = pBuffer.readDouble();
        this.zDelta = pBuffer.readDouble();
        this.particle = readParticle(pBuffer, type);
        this.count = pBuffer.readInt();
        this.speed = pBuffer.readFloat();
    }

    public void write(FriendlyByteBuf pBuffer) {
        writeParticleType(pBuffer, particle.getType());
        pBuffer.writeDouble(this.x);
        pBuffer.writeDouble(this.y);
        pBuffer.writeDouble(this.z);
        pBuffer.writeDouble(this.xDelta);
        pBuffer.writeDouble(this.yDelta);
        pBuffer.writeDouble(this.zDelta);
        this.particle.writeToNetwork(pBuffer);
        pBuffer.writeInt(this.count);
        pBuffer.writeFloat(this.speed);

    }

    private <T extends ParticleOptions> T readParticle(FriendlyByteBuf pBuffer, ParticleType<T> pParticleType) {
        return pParticleType.getDeserializer().fromNetwork(pParticleType, pBuffer);
    }

    private static void writeParticleType(FriendlyByteBuf buf, ParticleType<?> type) {
        buf.writeResourceLocation(Objects.requireNonNull(ForgeRegistries.PARTICLE_TYPES.getKey(type)));
    }

    private static ParticleType<?> readParticleType(FriendlyByteBuf buf) {
        ResourceLocation id = buf.readResourceLocation();

        ParticleType<?> type = ForgeRegistries.PARTICLE_TYPES.getValue(id);
        if (type != null) return type;

        return ParticleTypes.EXPLOSION;
    }

    public static void handle(ServerboundParticlePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player sender = ctx.get().getSender();
            assert sender != null;
            if (sender.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(packet.particle, packet.x, packet.y, packet.z, packet.count, packet.xDelta, packet.yDelta, packet.zDelta, packet.speed);
            }
        });

        ctx.get().setPacketHandled(true);
    }

}
