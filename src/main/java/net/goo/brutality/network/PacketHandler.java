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
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "main_channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static int id = 0;

    public static void register() {
        NETWORK_CHANNEL.registerMessage(id++, ServerboundSetHealthPacket.class, ServerboundSetHealthPacket::encode, ServerboundSetHealthPacket::new, ServerboundSetHealthPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundShootFromRotationPacket.class, ServerboundShootFromRotationPacket::encode, ServerboundShootFromRotationPacket::new, ServerboundShootFromRotationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundHandleThrowingProjectilePacket.class, ServerboundHandleThrowingProjectilePacket::encode, ServerboundHandleThrowingProjectilePacket::new, ServerboundHandleThrowingProjectilePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundTriggerAnimationPacket.class, ServerboundTriggerAnimationPacket::encode, ServerboundTriggerAnimationPacket::new, ServerboundTriggerAnimationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundChangeNBTPacket.class, ServerboundChangeNBTPacket::encode, ServerboundChangeNBTPacket::new, ServerboundChangeNBTPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundBetterCombatAttackStartListenerPacket.class, ServerboundBetterCombatAttackStartListenerPacket::encode, ServerboundBetterCombatAttackStartListenerPacket::new, ServerboundBetterCombatAttackStartListenerPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundDamageEntityPacket.class, ServerboundDamageEntityPacket::encode, ServerboundDamageEntityPacket::new, ServerboundDamageEntityPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundParticlePacket.class, ServerboundParticlePacket::encode, ServerboundParticlePacket::new, ServerboundParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundActivateRagePacket.class, ServerboundActivateRagePacket::encode, ServerboundActivateRagePacket::new, ServerboundActivateRagePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundChangeSpellPacket.class, ServerboundChangeSpellPacket::encode, ServerboundChangeSpellPacket::new, ServerboundChangeSpellPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundActiveAbilityPressPacket.class, ServerboundActiveAbilityPressPacket::encode, ServerboundActiveAbilityPressPacket::new, ServerboundActiveAbilityPressPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundPlayerAnimationPacket.class, ServerboundPlayerAnimationPacket::write, ServerboundPlayerAnimationPacket::new, ServerboundPlayerAnimationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundExactParticlePacket.class, ClientboundExactParticlePacket::encode, ClientboundExactParticlePacket::new, ClientboundExactParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundSyncCapabilitiesPacket.class, ClientboundSyncCapabilitiesPacket::encode, ClientboundSyncCapabilitiesPacket::new, ClientboundSyncCapabilitiesPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundEnvironmentColorManagerPacket.class, ClientboundEnvironmentColorManagerPacket::encode, ClientboundEnvironmentColorManagerPacket::new, ClientboundEnvironmentColorManagerPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundSyncTickPacket.class, ClientboundSyncTickPacket::encode, ClientboundSyncTickPacket::new, ClientboundSyncTickPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundSyncSpellCooldownPacket.class, ClientboundSyncSpellCooldownPacket::write, ClientboundSyncSpellCooldownPacket::new, ClientboundSyncSpellCooldownPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundParticlePacket.class, ClientboundParticlePacket::write, ClientboundParticlePacket::new, ClientboundParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundDodgePacket.class, ClientboundDodgePacket::write, ClientboundDodgePacket::new, ClientboundDodgePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundSyncItemCooldownPacket.class, ClientboundSyncItemCooldownPacket::write, ClientboundSyncItemCooldownPacket::decode, ClientboundSyncItemCooldownPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundPlayerAnimationPacket.class, ClientboundPlayerAnimationPacket::write, ClientboundPlayerAnimationPacket::new, ClientboundPlayerAnimationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundBrutalityExplodePacket.class, ClientboundBrutalityExplodePacket::write, ClientboundBrutalityExplodePacket::new, ClientboundBrutalityExplodePacket::handle);
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