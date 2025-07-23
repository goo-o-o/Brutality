package net.goo.brutality.network;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
        NETWORK_CHANNEL.registerMessage(id++, c2sTriggerAnimPacket.class, c2sTriggerAnimPacket::encode, c2sTriggerAnimPacket::new, c2sTriggerAnimPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, s2cEnhancedExactParticlePacket.class, s2cEnhancedExactParticlePacket::encode, s2cEnhancedExactParticlePacket::new, s2cEnhancedExactParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, c2sShootProjectilePacket.class, c2sShootProjectilePacket::encode, c2sShootProjectilePacket::new, c2sShootProjectilePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, c2sChangeNBTPacket.class, c2sChangeNBTPacket::encode, c2sChangeNBTPacket::new, c2sChangeNBTPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, s2cEnvironmentColorManagerPacket.class, s2cEnvironmentColorManagerPacket::encode, s2cEnvironmentColorManagerPacket::new, s2cEnvironmentColorManagerPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, c2sDamageEntityPacket.class, c2sDamageEntityPacket::encode, c2sDamageEntityPacket::new, c2sDamageEntityPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, c2sParticlePacket.class, c2sParticlePacket::encode, c2sParticlePacket::new, c2sParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, s2cSyncCapabilitiesPacket.class, s2cSyncCapabilitiesPacket::encode, s2cSyncCapabilitiesPacket::new, s2cSyncCapabilitiesPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, c2sActivateRagePacket.class, c2sActivateRagePacket::encode, c2sActivateRagePacket::new, c2sActivateRagePacket::handle);
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

    public static void sendToNearbyClients(ServerLevel level, double x, double y, double z, double radius, Object msg) {
        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(x, y, z, radius, level.dimension());
        NETWORK_CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), msg);
    }


}