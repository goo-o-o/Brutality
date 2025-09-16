package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityCapabilities {
    public static final Capability<EntityCapabilities.EntityEffectCap> ENTITY_EFFECT_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.EntityStarCountCap> ENTITY_STAR_COUNT_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.EntityShouldRotateCap> ENTITY_SHOULD_ROTATE_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.PlayerRageCap> PLAYER_RAGE_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.PlayerManaCap> PLAYER_MANA_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.PlayerComboCap> PLAYER_COMBO_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.RespawnCap> RESPAWN_CAP = CapabilityManager.get(new CapabilityToken<>(){});

    static {
        CapabilitySyncRegistry.register("effect", ENTITY_EFFECT_CAP, EntityCapabilities.EntityEffectCap::new);
        CapabilitySyncRegistry.register("star_count", ENTITY_STAR_COUNT_CAP, EntityCapabilities.EntityStarCountCap::new);
        CapabilitySyncRegistry.register("should_rotate", ENTITY_SHOULD_ROTATE_CAP, EntityCapabilities.EntityShouldRotateCap::new);
        CapabilitySyncRegistry.register("rage_value", PLAYER_RAGE_CAP, EntityCapabilities.PlayerRageCap::new);
        CapabilitySyncRegistry.register("mana_value", PLAYER_MANA_CAP, EntityCapabilities.PlayerManaCap::new);
        CapabilitySyncRegistry.register("combo", PLAYER_COMBO_CAP, EntityCapabilities.PlayerComboCap::new);
        CapabilitySyncRegistry.register("respawn", RESPAWN_CAP, EntityCapabilities.RespawnCap::new);
    }

    public static class CapabilitySyncRegistry {
        private static final Map<String, Capability<? extends INBTSerializable<CompoundTag>>> REGISTERED_CAPS = new HashMap<>();
        private static final Map<Capability<? extends INBTSerializable<CompoundTag>>, Supplier<? extends INBTSerializable<CompoundTag>>> INSTANCE_SUPPLIERS = new HashMap<>();

        public static <T extends INBTSerializable<CompoundTag>> void register(
                String id,
                Capability<T> cap,
                Supplier<T> instanceSupplier
        ) {
            REGISTERED_CAPS.put(id, cap);
            INSTANCE_SUPPLIERS.put(cap, instanceSupplier);
        }

        public static Map<String, Capability<? extends INBTSerializable<CompoundTag>>> getAll() {
            return REGISTERED_CAPS;
        }

        public static Capability<? extends INBTSerializable<CompoundTag>> get(String key) {
            return REGISTERED_CAPS.get(key);
        }

        public static String getName(Capability<? extends INBTSerializable<CompoundTag>> capability) {
            for (Map.Entry<String, Capability<? extends INBTSerializable<CompoundTag>>> entry : REGISTERED_CAPS.entrySet()) {
                if (entry.getValue() == capability) {
                    return entry.getKey();
                }
            }
            return null;
        }

        public static <T extends INBTSerializable<CompoundTag>> ICapabilityProvider getProvider(Capability<T> capability) {
            @SuppressWarnings("unchecked")
            Supplier<T> supplier = (Supplier<T>) INSTANCE_SUPPLIERS.get(capability);
            if (supplier == null) {
                throw new IllegalArgumentException("No instance supplier registered for capability: " + capability);
            }
            return new CapabilityProvider<>(capability, supplier);
        }
    }

}

