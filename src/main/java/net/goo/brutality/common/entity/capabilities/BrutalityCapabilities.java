package net.goo.brutality.common.entity.capabilities;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundGenericSyncPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class BrutalityCapabilities {
    public static final Capability<PlayerManaCap> MANA = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerSpellCooldownsCap> SPELL_COOLDOWNS = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<EntityShouldRotateCap> SHOULD_ROTATE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerBoosterPackCap> BOOSTER_PACK = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<EntitySealTypeCap> SEAL_TYPE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<EntityStickyBombCap> STICKY_BOMB = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerRageCap> RAGE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerBloodCap> BLOOD = CapabilityManager.get(new CapabilityToken<>() {
    });
    private static final Map<Capability<?>, String> CAP_TO_KEY = new HashMap<>();

    private record CapEntry<T extends IBrutalityData>(Capability<T> cap, Supplier<T> factory, T dummy) {
    }

    private static final Map<String, CapEntry<?>> REGISTRY = new HashMap<>();

    private static <T extends IBrutalityData> void register(String key, Capability<T> cap, Supplier<T> factory) {
        CapEntry<T> entry = new CapEntry<>(cap, factory, factory.get());
        REGISTRY.put(key, entry);
        CAP_TO_KEY.put(cap, key);
    }

    static {
        register("mana", MANA, PlayerManaCap::new);
        register("spell_cooldowns", SPELL_COOLDOWNS, PlayerSpellCooldownsCap::new);
        register("should_rotate", SHOULD_ROTATE, EntityShouldRotateCap::new);
        register("booster_pack", BOOSTER_PACK, PlayerBoosterPackCap::new);
        register("seal_type", SEAL_TYPE, EntitySealTypeCap::new);
        register("sticky_bomb", STICKY_BOMB, EntityStickyBombCap::new);
        register("rage", RAGE, PlayerRageCap::new);
        register("blood", BLOOD, PlayerBloodCap::new);
    }

    public static Optional<Capability<? extends IBrutalityData>> getByKey(String key) {
        return Optional.ofNullable(REGISTRY.get(key)).map(CapEntry::cap);
    }

    @SubscribeEvent
    public static void attach(AttachCapabilitiesEvent<Entity> event) {
        REGISTRY.forEach((name, entry) -> {
            if (entry.dummy().predicate().test(event.getObject())) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, name),
                        new GenericProvider(entry.cap(), entry.factory().get()));
            }
        });
    }

    /**
     * Synchronizes a capability from the server to all players tracking a specific entity.
     * Use this when an entity's data changes (e.g., a mob gets a sticky bomb) so that
     * all nearby clients can see the change.
     *
     * @param entity The entity whose data is being synchronized.
     * @param cap    The capability instance to sync.
     */
    public static <T extends IBrutalityData> void syncToAllTracking(Entity entity, Capability<T> cap) {
        if (entity.level().isClientSide) return;

        entity.getCapability(cap).ifPresent(data -> {
            ClientboundGenericSyncPacket packet = new ClientboundGenericSyncPacket(
                    entity.getId(),
                    CAP_TO_KEY.get(cap),
                    data.serializeNBT()
            );

            // Sync to everyone watching this entity
            PacketHandler.sendToTracking(packet, entity);

            // Manual self-sync: Players don't "track" themselves in Forge
            if (entity instanceof ServerPlayer serverPlayer) {
                PacketHandler.sendToPlayerClient(packet, serverPlayer);
            }
        });
    }

    /**
     * Synchronizes a specific entity's capability data to a single specific player.
     * Primarily used by the 'StartTracking' event to initialize data for a player
     * who just walked into range of an entity.
     *
     * @param player The recipient of the sync packet.
     * @param toSync The entity whose data we are sending.
     * @param cap    The capability to synchronize.
     */
    public static <T extends IBrutalityData> void syncEntityToPlayer(ServerPlayer player, Entity toSync, Capability<T> cap) {
        toSync.getCapability(cap).ifPresent(data -> {
            PacketHandler.sendToPlayerClient(
                    new ClientboundGenericSyncPacket(toSync.getId(), CAP_TO_KEY.get(cap), data.serializeNBT()),
                    player
            );
        });
    }

    /**
     * Synchronizes a player's private capability data to their own client.
     * Use this for personal stats like Mana, Rage, or Quest progress that other
     * players do not need to know about.
     *
     * @param player The player whose data is being synced to themselves.
     * @param cap    The private capability to synchronize.
     */
    public static <T extends IBrutalityData> void sync(Player player, Capability<T> cap) {
        if (player instanceof ServerPlayer serverPlayer)
            player.getCapability(cap).ifPresent(data -> PacketHandler.sendToPlayerClient(
                    new ClientboundGenericSyncPacket(player.getId(), CAP_TO_KEY.get(cap), data.serializeNBT()),
                    serverPlayer
            ));
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            Entity target = event.getTarget();

            // Loop through all registered capabilities
            REGISTRY.forEach((name, entry) -> {
                // Check if this specific entity actually HAS this capability
                // (e.g., don't try to sync Mana for a Cow)
                if (entry.dummy().predicate().test(target)) {
                    syncToPlayer(serverPlayer, target, entry.cap());
                }
            });
        }
    }

    // Helper to sync a specific entity's cap to a specific player
    public static <T extends IBrutalityData> void syncToPlayer(ServerPlayer player, Entity target, Capability<T> cap) {
        target.getCapability(cap).ifPresent(data -> PacketHandler.sendToPlayerClient(
                new ClientboundGenericSyncPacket(target.getId(), CAP_TO_KEY.get(cap), data.serializeNBT()),
                player
        ));
    }
}