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
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static int packetId = 0;

    public static void register() {


        NETWORK_CHANNEL.messageBuilder(c2sOffLeafBlowerPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(c2sOffLeafBlowerPacket::encode)
                .decoder(c2sOffLeafBlowerPacket::new)
                .consumerMainThread(c2sOffLeafBlowerPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(c2sDamageItemPacket.class, packetId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(c2sDamageItemPacket::encode)
                .decoder(c2sDamageItemPacket::new)
                .consumerMainThread(c2sDamageItemPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(s2cCustomExplosionPacket.class, packetId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(s2cCustomExplosionPacket::write)
                .decoder(s2cCustomExplosionPacket::new)
                .consumerMainThread(s2cCustomExplosionPacket::handle)
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