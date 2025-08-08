package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityCapabilities {
    public static final Capability<EntityCapabilities.EntityEffectCap> ENTITY_EFFECT_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.EntityStarCountCap> ENTITY_STAR_COUNT_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.EntityShouldRotateCap> ENTITY_SHOULD_ROTATE_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.PlayerRageCap> PLAYER_RAGE_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.PlayerManaCap> PLAYER_MANA_CAP = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<EntityCapabilities.PlayerComboCap> PLAYER_COMBO_CAP = CapabilityManager.get(new CapabilityToken<>(){});

    static {
        CapabilitySyncRegistry.register("effect", ENTITY_EFFECT_CAP);
        CapabilitySyncRegistry.register("star_count", ENTITY_STAR_COUNT_CAP);
        CapabilitySyncRegistry.register("should_rotate", ENTITY_SHOULD_ROTATE_CAP);
        CapabilitySyncRegistry.register("rage_value", PLAYER_RAGE_CAP);
        CapabilitySyncRegistry.register("mana_value", PLAYER_MANA_CAP);
        CapabilitySyncRegistry.register("combo", PLAYER_COMBO_CAP);
    }

    public static class CapabilitySyncRegistry {

        private static final Map<String, Capability<? extends INBTSerializable<CompoundTag>>> REGISTERED_CAPS = new HashMap<>();

        public static <T extends INBTSerializable<CompoundTag>> void register(String id, Capability<T> cap) {
            REGISTERED_CAPS.put(id, cap);
        }

        public static Map<String, Capability<? extends INBTSerializable<CompoundTag>>> getAll() {
            return REGISTERED_CAPS;
        }

        public static Capability<? extends INBTSerializable<CompoundTag>> get(String key) {
            return REGISTERED_CAPS.get(key);
        }
    }

}

