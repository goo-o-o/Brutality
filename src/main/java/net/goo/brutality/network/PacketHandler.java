package net.goo.brutality.network;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    private static final SimpleChannel NETWORK_CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Brutality.MOD_ID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static int id = 0;

    public static void register() {
//        NETWORK_CHANNEL.registerMessage(id++, c2sDamageItemPacket.class, c2sDamageItemPacket::encode, c2sDamageItemPacket::new, c2sDamageItemPacket::handle);
//        NETWORK_CHANNEL.registerMessage(id++, s2cCustomExplosionPacket.class, s2cCustomExplosionPacket::encode, s2cCustomExplosionPacket::new, s2cCustomExplosionPacket::handle);
//        NETWORK_CHANNEL.registerMessage(id++, s2cEnhancedExactParticlePacket.class, s2cEnhancedExactParticlePacket::encode, s2cEnhancedExactParticlePacket::new, s2cEnhancedExactParticlePacket::handle);
//        NETWORK_CHANNEL.registerMessage(id++, c2sChangeNBTPacket.class, c2sChangeNBTPacket::encode, c2sChangeNBTPacket::new, c2sChangeNBTPacket::handle);
//        NETWORK_CHANNEL.registerMessage(id++, c2sSwordBeamPacket.class, c2sSwordBeamPacket::encode, c2sSwordBeamPacket::new, c2sSwordBeamPacket::handle);

        NETWORK_CHANNEL.messageBuilder(s2cCustomExplosionPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(s2cCustomExplosionPacket::encode)
                .decoder(s2cCustomExplosionPacket::new)
                .consumerMainThread(s2cCustomExplosionPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(c2sTriggerAnimPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(c2sTriggerAnimPacket::encode)
                .decoder(c2sTriggerAnimPacket::new)
                .consumerMainThread(c2sTriggerAnimPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(s2cEnhancedExactParticlePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(s2cEnhancedExactParticlePacket::encode)
                .decoder(s2cEnhancedExactParticlePacket::new)
                .consumerMainThread(s2cEnhancedExactParticlePacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(c2sSwordBeamPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(c2sSwordBeamPacket::encode)
                .decoder(c2sSwordBeamPacket::new)
                .consumerMainThread(c2sSwordBeamPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(c2sChangeNBTPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(c2sChangeNBTPacket::encode)
                .decoder(c2sChangeNBTPacket::new)
                .consumerMainThread(c2sChangeNBTPacket::handle)
                .add();

        NETWORK_CHANNEL.messageBuilder(s2cEnvironmentColorManagerPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(s2cEnvironmentColorManagerPacket::encode)
                .decoder(s2cEnvironmentColorManagerPacket::new)
                .consumerMainThread(s2cEnvironmentColorManagerPacket::handle)
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

    public static void sendToNearbyClients(Object msg) {
        NETWORK_CHANNEL.send(PacketDistributor.NEAR.noArg(), msg);
    }

}