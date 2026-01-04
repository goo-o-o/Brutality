package net.goo.brutality.util;

import net.goo.brutality.gui.CooldownMeter;
import net.mcreator.terramity.potion.AbilityCooldownMobEffect;
import net.mcreator.terramity.potion.ArmorSetAbilityCooldownMobEffect;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;
import java.util.function.Supplier;

public class Constants {
    public static final Map<Class<? extends MobEffect>, CooldownType> COOLDOWN_BY_CLASS = Map.of(
            ArmorSetAbilityCooldownMobEffect.class, CooldownType.ARMOR_SET,
            AbilityCooldownMobEffect.class, CooldownType.ABILITY
    );
    public static final Map<CooldownType, Supplier<CooldownMeter>> REGISTRY = Map.of(
            CooldownType.ARMOR_SET, CooldownMeter.ArmorSetAbilityCooldownMeter::new,
            CooldownType.ABILITY,     CooldownMeter.AbilityCooldownMeter::new
    );
    public static final int itemWidth = 16;
    public static final int itemHeight = itemWidth;
    public static final int bigPad = 7;
    public static final int mediumPad = 4;
    public static final int smallPad = 2;
    public static final int offhandSlotSize = 20;

    public enum CooldownType {
        ARMOR_SET,
        ABILITY,
        DASH
    }
}
