package net.goo.brutality.common.network;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.network.clientbound.*;
import net.goo.brutality.common.network.serverbound.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

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
        registerPacket(ServerboundSetHealthPacket.class, ServerboundSetHealthPacket::new);
        registerPacket(ServerboundShootFromRotationPacket.class, ServerboundShootFromRotationPacket::new);
        registerPacket(ServerboundHandleThrowingProjectilePacket.class, ServerboundHandleThrowingProjectilePacket::new);
        registerPacket(ServerboundTriggerAnimationPacket.class, ServerboundTriggerAnimationPacket::new);
        registerPacket(ServerboundTableOfWizardryUpdatePacket.class, ServerboundTableOfWizardryUpdatePacket::new);
        registerPacket(ServerboundBetterCombatAttackStartListenerPacket.class, ServerboundBetterCombatAttackStartListenerPacket::new);
        registerPacket(ServerboundDamageEntityPacket.class, ServerboundDamageEntityPacket::new);
        registerPacket(ServerboundParticlePacket.class, ServerboundParticlePacket::new);
        registerPacket(ServerboundActivateRagePacket.class, ServerboundActivateRagePacket::new);
        registerPacket(ServerboundChangeSpellPacket.class, ServerboundChangeSpellPacket::new);
        registerPacket(ServerboundActiveAbilityPressPacket.class, ServerboundActiveAbilityPressPacket::new);
        registerPacket(ServerboundAddLoadoutPacket.class, ServerboundAddLoadoutPacket::new);
        registerPacket(ServerboundSwitchLoadoutPacket.class, ServerboundSwitchLoadoutPacket::new);
        registerPacket(ServerboundArmorSetBonusAbilityPressPacket.class, ServerboundArmorSetBonusAbilityPressPacket::new);
        registerPacket(ServerboundStartPlayerAnimationPacket.class, ServerboundStartPlayerAnimationPacket::new);
        registerPacket(ServerboundStopPlayerAnimationPacket.class, ServerboundStopPlayerAnimationPacket::new);
        registerPacket(ClientboundUpdateAbilityCooldownsPacket.class, ClientboundUpdateAbilityCooldownsPacket::new);
        registerPacket(ClientboundExactParticlePacket.class, ClientboundExactParticlePacket::new);
        registerPacket(ClientboundGenericSyncPacket.class, ClientboundGenericSyncPacket::new);
        registerPacket(ClientboundEnvironmentColorManagerPacket.class, ClientboundEnvironmentColorManagerPacket::new);
        registerPacket(ClientboundParticlePacket.class, ClientboundParticlePacket::new);
        registerPacket(ClientboundChainLightningPacket.class, ClientboundChainLightningPacket::new);
        registerPacket(ClientboundDodgePacket.class, ClientboundDodgePacket::new);
        registerPacket(ClientboundSyncItemCooldownPacket.class, ClientboundSyncItemCooldownPacket::new);
        registerPacket(ClientboundStartPlayerAnimationPacket.class, ClientboundStartPlayerAnimationPacket::new);
        registerPacket(ClientboundMaxAttackedPacket.class, ClientboundMaxAttackedPacket::new);
        registerPacket(ClientboundStopPlayerAnimationPacket.class, ClientboundStopPlayerAnimationPacket::new);
        registerPacket(ClientboundBrutalityExplodePacket.class, ClientboundBrutalityExplodePacket::new);
    }

    private static <T extends IBrutalityPacket<T>> void registerPacket(
            Class<T> packetClass,
            Function<FriendlyByteBuf, T> decoder
    ) {
        NETWORK_CHANNEL.registerMessage(
                id++,
                packetClass,
                IBrutalityPacket::write,
                decoder,
                (msg, ctx) -> msg.handle(msg, ctx) // Use a lambda for handle
        );
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

    public static void sendToNearbyClients(Object msg, ServerLevel level, double x, double y, double z, double radius) {
        PacketDistributor.TargetPoint targetPoint = new PacketDistributor.TargetPoint(x, y, z, radius, level.dimension());
        NETWORK_CHANNEL.send(PacketDistributor.NEAR.with(() -> targetPoint), msg);
    }


}