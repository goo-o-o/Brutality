package net.goo.armament.network;

import net.goo.armament.Armament;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Armament.MOD_ID, "main_channel"),
            () -> PROTOCOL_VERSION, // Protocol version
            PROTOCOL_VERSION::equals, // Validate version
            PROTOCOL_VERSION::equals // Validate version
    );

    public static void register() {
        int packetId = 0; // Use this to assign unique IDs to each packet.

        // Register the c2sSpawnParticleFromStarburstPacket
        NETWORK_CHANNEL.messageBuilder(s2cSpawnParticleFromStarburstPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(s2cSpawnParticleFromStarburstPacket::encode)
                .decoder(s2cSpawnParticleFromStarburstPacket::new)
                .consumerMainThread(s2cSpawnParticleFromStarburstPacket::handle)
                .add();
    }

    public static void sendToServer(Object msg) {
        NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), msg);
    }

    public static void sendToPlayer(Object msg, ServerPlayer player) {
        NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToAllClients(Object msg) {
        NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
    }
}