package net.goo.brutality.common.network;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.network.clientbound.*;
import net.goo.brutality.common.network.serverbound.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
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
        NETWORK_CHANNEL.registerMessage(id++, ServerboundSetHealthPacket.class, ServerboundSetHealthPacket::write, ServerboundSetHealthPacket::new, ServerboundSetHealthPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundShootFromRotationPacket.class, ServerboundShootFromRotationPacket::write, ServerboundShootFromRotationPacket::new, ServerboundShootFromRotationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundHandleThrowingProjectilePacket.class, ServerboundHandleThrowingProjectilePacket::write, ServerboundHandleThrowingProjectilePacket::new, ServerboundHandleThrowingProjectilePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundTriggerAnimationPacket.class, ServerboundTriggerAnimationPacket::write, ServerboundTriggerAnimationPacket::new, ServerboundTriggerAnimationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundTableOfWizardryUpdatePacket.class, ServerboundTableOfWizardryUpdatePacket::write, ServerboundTableOfWizardryUpdatePacket::new, ServerboundTableOfWizardryUpdatePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundChangeNBTPacket.class, ServerboundChangeNBTPacket::write, ServerboundChangeNBTPacket::new, ServerboundChangeNBTPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundBetterCombatAttackStartListenerPacket.class, ServerboundBetterCombatAttackStartListenerPacket::write, ServerboundBetterCombatAttackStartListenerPacket::new, ServerboundBetterCombatAttackStartListenerPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundDamageEntityPacket.class, ServerboundDamageEntityPacket::write, ServerboundDamageEntityPacket::new, ServerboundDamageEntityPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundParticlePacket.class, ServerboundParticlePacket::write, ServerboundParticlePacket::new, ServerboundParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundActivateRagePacket.class, ServerboundActivateRagePacket::write, ServerboundActivateRagePacket::new, ServerboundActivateRagePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundChangeSpellPacket.class, ServerboundChangeSpellPacket::write, ServerboundChangeSpellPacket::new, ServerboundChangeSpellPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundActiveAbilityPressPacket.class, ServerboundActiveAbilityPressPacket::write, ServerboundActiveAbilityPressPacket::new, ServerboundActiveAbilityPressPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundArmorSetBonusAbilityPressPacket.class, ServerboundArmorSetBonusAbilityPressPacket::write, ServerboundArmorSetBonusAbilityPressPacket::new, ServerboundArmorSetBonusAbilityPressPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ServerboundPlayerAnimationPacket.class, ServerboundPlayerAnimationPacket::write, ServerboundPlayerAnimationPacket::new, ServerboundPlayerAnimationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundUpdateAbilityCooldownsPacket.class, ClientboundUpdateAbilityCooldownsPacket::write, ClientboundUpdateAbilityCooldownsPacket::new, ClientboundUpdateAbilityCooldownsPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundExactParticlePacket.class, ClientboundExactParticlePacket::write, ClientboundExactParticlePacket::new, ClientboundExactParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundGenericSyncPacket.class, ClientboundGenericSyncPacket::write, ClientboundGenericSyncPacket::new, ClientboundGenericSyncPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundEnvironmentColorManagerPacket.class, ClientboundEnvironmentColorManagerPacket::write, ClientboundEnvironmentColorManagerPacket::new, ClientboundEnvironmentColorManagerPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundParticlePacket.class, ClientboundParticlePacket::write, ClientboundParticlePacket::new, ClientboundParticlePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundDodgePacket.class, ClientboundDodgePacket::write, ClientboundDodgePacket::new, ClientboundDodgePacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundSyncItemCooldownPacket.class, ClientboundSyncItemCooldownPacket::write, ClientboundSyncItemCooldownPacket::decode, ClientboundSyncItemCooldownPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundPlayerAnimationPacket.class, ClientboundPlayerAnimationPacket::write, ClientboundPlayerAnimationPacket::new, ClientboundPlayerAnimationPacket::handle);
        NETWORK_CHANNEL.registerMessage(id++, ClientboundBrutalityExplodePacket.class, ClientboundBrutalityExplodePacket::write, ClientboundBrutalityExplodePacket::new, ClientboundBrutalityExplodePacket::handle);
    }

    public static void sendToServer(Object msg) {
        NETWORK_CHANNEL.send(PacketDistributor.SERVER.noArg(), msg);
    }

    public static void sendToPlayerClient(Object msg, ServerPlayer player) {
        NETWORK_CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static void sendToTracking(Object msg, Entity tracked) {
        NETWORK_CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> tracked), msg);
    }

    public static void sendToAllClients(Object msg) {
        NETWORK_CHANNEL.send(PacketDistributor.ALL.noArg(), msg);
    }

    public static void sendToNearbyClients(ServerLevel level, double x, double y, double z, double radius, Object msg) {
        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(x, y, z, radius, level.dimension());
        NETWORK_CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), msg);
    }


}