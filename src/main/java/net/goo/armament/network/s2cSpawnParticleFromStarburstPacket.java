package net.goo.armament.network;

import net.goo.armament.particle.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class s2cSpawnParticleFromStarburstPacket {
    private final double x, y, z;

    public s2cSpawnParticleFromStarburstPacket(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public  s2cSpawnParticleFromStarburstPacket(FriendlyByteBuf buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    public static void handle(s2cSpawnParticleFromStarburstPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Minecraft.getInstance().level.addParticle(
                        ModParticles.STARBURST_PARTICLE.get(), packet.x, packet.y, packet.z, 0.0D, 0.0D, 0.0D
                );
            }
        });

        context.setPacketHandled(true);

    }

}
