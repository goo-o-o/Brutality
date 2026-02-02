package net.goo.brutality.common.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.util.magic.SpellStorage;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

/**
 * The core contract for all spells in the Brutality mod.
 * Handles damage scaling, resource costs, cooldowns, and school-based leveling.
 */
public interface IBrutalitySpell {

    // --- Static Utility Logic ---

    /**
     * Calculates the final cooldown.
     * Logic: (Base + Scaling * Level) * Curio Multipliers * (Attribute Reduction).
     */
    static int getActualCooldown(Player player, IBrutalitySpell spell, int spellLevel) {
        float base = spell.getBaseCooldown() + spell.getCooldownLevelScaling() * spellLevel;
        base *= SpellUtils.getCurioCooldownMultiplier(player, spell, spellLevel);
        return Math.max(1, (int) (base * player.getAttributeValue(BrutalityAttributes.SPELL_COOLDOWN.get())));
    }

    /**
     * Calculates the actual mana cost for casting a spell, based on the player's attributes
     * and the spell's base and level-scaling mana costs.
     * @implNote The actual spell level (affected by attributes) should be passed in
     *
     * @param player   The player who is casting the spell. Used for retrieving the
     *                 relevant attributes to modify mana cost.
     * @param spell    The spell being cast. Provides base mana cost and level-scaling
     *                 mana cost values.
     * @param spellLevel The level of the spell being cast. Impacts the scaling component
     *                   of mana cost.
     * @return The calculated mana cost for the spell, adjusted by player attributes and
     *         constrained to a minimum of 1.
     */
    static float getActualManaCost(Player player, IBrutalitySpell spell, int spellLevel) {
        float base = spell.getBaseManaCost() + spell.getManaCostLevelScaling() * spellLevel;
        return (float) Math.max((int) base * player.getAttributeValue(BrutalityAttributes.MANA_COST.get()), 1);
    }
    /**
     * Calculates the actual mana cost for a given spell entry, factoring in the player's traits,
     * the specific spell, and its level.
     *
     * @param player The player casting the spell.
     * @param spellEntry The spell entry containing the spell and its corresponding level.
     * @return The calculated mana cost for the spell.
     */
    static float getActualManaCost(Player player, SpellStorage.SpellEntry spellEntry) {
        return getActualManaCost(player, spellEntry.spell(), spellEntry.level());
    }

    /**
     * Calculates effective cast time.
     * Logic: (Base + Scaling * Level) * Curio Multipliers * Attribute Reduction.
     */
    static int getActualCastTime(Player player, IBrutalitySpell spell, int spellLevel) {
        float base = spell.getBaseCastTime() + spell.getCastTimeLevelScaling() * spellLevel;
        base *= SpellUtils.getCurioCastTimeMultiplier(player, spell, spellLevel);
        return Math.max((int) (base * player.getAttributeValue(BrutalityAttributes.CAST_TIME.get())), 1);
    }

    /**
     * Calculates the "True" level of a spell by adding the caster's Magic School Attribute
     * to the spell's base level.
     */
    static int getActualSpellLevel(@Nullable Entity caster, IBrutalitySpell spell, int spellLevel) {
        MagicSchool school = spell.getSchool();
        if (caster == null) return spellLevel;
        if (caster instanceof LivingEntity livingCaster) {
            AttributeInstance attributeInstance = livingCaster.getAttribute(school.attribute.get());
            if (attributeInstance != null) {
                return Math.max((int) (spellLevel + attributeInstance.getValue()), 0);
            }
            return Math.max(spellLevel, 0);
        }
        return spellLevel;
    }

    // --- Base Spell Properties ---

    int getBaseManaCost();

    default float getManaCostLevelScaling() { return 0; }

    float getBaseDamage();

    default float getDamageLevelScaling() { return 0; }

    int getBaseCooldown();

    default int getCooldownLevelScaling() { return 0; }

    int getBaseCastTime();

    default int getCastTimeLevelScaling() { return 0; }

    MagicSchool getSchool();

    ResourceLocation getIcon();

    List<SpellCategory> getCategories();

    String getSpellName();

    int getDescriptionCount();

    List<BrutalityTooltipHelper.SpellStatComponent> getStatComponents();

    // --- Translation Helpers ---

    default MutableComponent getTranslatedSpellName() {
        return Component.translatable("spell." + Brutality.MOD_ID + "." + getSpellName());
    }

    default MutableComponent getSpellDescription(int index) {
        return Component.translatable("spell." + Brutality.MOD_ID + "." + getSpellName() + ".description." + index);
    }

    // --- Casting Life-Cycle Hooks ---

    /** Checks if the spell can continue its current cast tick.
     * Useful for custom requirement checks (e.g. must be on ground).
     * Used mainly for {@link SpellCategory#CONTINUOUS} spells
     */
    default boolean onCastTick(Player player, ItemStack stack, int spellLevel) { return true; }

    /** Triggered at the very start of the casting process. */
    default boolean onStartCast(Player player, ItemStack stack, int spellLevel) { return true; }

    /** Triggered when the casting time successfully completes. */
    default void onEndCast(Player player, ItemStack stack, int spellLevel) { }

    // --- Final Value Resolution ---

    /**
     * Calculates the total damage for an entity.
     * Considers True Level, Curio Multipliers, and Global Spell Damage Attributes.
     */
    default float getFinalDamage(@Nullable Entity caster, int spellLevel) {
        float dmg = getBaseDamage() + getDamageLevelScaling() * getActualSpellLevel(caster, this, spellLevel);
        dmg *= SpellUtils.getCurioDamageMultiplier(caster, null, spellLevel);

        if (caster instanceof LivingEntity livingCaster) {
            AttributeInstance spellDamage = livingCaster.getAttribute(BrutalityAttributes.SPELL_DAMAGE.get());
            if (spellDamage != null) {
                return (float) (dmg * spellDamage.getValue());
            }
        }
        return dmg;
    }

    /**
     * Resolves a stat component (like range or radius) to a final float,
     * applying level scaling and min/max clamping.
     */
    default float getFinalStat(int spellLevel, @Nullable BrutalityTooltipHelper.SpellStatComponent stat) {
        if (stat != null) {
            float value = stat.base() + stat.levelDelta() * spellLevel;
            if (stat.max() != null && stat.min() != null) {
                return Mth.clamp(value, stat.min(), stat.max());
            } else if (stat.max() != null) {
                return Math.min(value, stat.max());
            } else if (stat.min() != null) {
                return Math.max(value, stat.min());
            }
            return value;
        }
        return 0;
    }

    // --- Magic Metadata Enums ---

    enum MagicSchool {
        DAEMONIC(TerramityModItems.DAEMONIUM, BrutalityItems.DAEMONIC_SPELL_SCROLL, BrutalityAttributes.DAEMONIC_SCHOOL_LEVEL),
        DARKIST(TerramityModItems.DIMLITE_INGOT, BrutalityItems.DARKIST_SPELL_SCROLL, BrutalityAttributes.DARKIST_SCHOOL_LEVEL),
        EVERGREEN(TerramityModItems.VIRENTIUM_ALLOY_INGOT, BrutalityItems.EVERGREEN_SPELL_SCROLL, BrutalityAttributes.EVERGREEN_SCHOOL_LEVEL),
        VOLTWEAVER(TerramityModItems.CONDUCTITE, BrutalityItems.VOLTWEAVER_SPELL_SCROLL, BrutalityAttributes.VOLTWEAVER_SCHOOL_LEVEL),
        COSMIC(TerramityModItems.COSMILITE_INGOT, BrutalityItems.COSMIC_SPELL_SCROLL, BrutalityAttributes.COSMIC_SCHOOL_LEVEL, FastColor.ARGB32.color(255, 253, 245, 95), FastColor.ARGB32.color(255, 160, 85, 234)),
        CELESTIA(TerramityModItems.REVERIUM, BrutalityItems.CELESTIA_SPELL_SCROLL, BrutalityAttributes.CELESTIA_SCHOOL_LEVEL),
        UMBRANCY(TerramityModItems.NYXIUM, BrutalityItems.UMBRAL_SPELL_SCROLL, BrutalityAttributes.UMBRANCY_SCHOOL_LEVEL),
        EXODIC(TerramityModItems.EXODIUM_SUPERALLOY, BrutalityItems.EXODIC_SPELL_SCROLL, BrutalityAttributes.EXODIC_SCHOOL_LEVEL),
        BRIMWIELDER(TerramityModItems.HELLSPEC_ALLOY, BrutalityItems.BRIMWIELDER_SPELL_SCROLL, BrutalityAttributes.BRIMWIELDER_SCHOOL_LEVEL),
        VOIDWALKER(TerramityModItems.VOID_ALLOY, BrutalityItems.VOIDWALKER_SPELL_SCROLL, BrutalityAttributes.VOIDWALKER_SCHOOL_LEVEL);

        private final Supplier<Item> material, scrollItem;
        private final Supplier<Attribute> attribute;
        public final int[] colors;

        MagicSchool(Supplier<Item> material, Supplier<Item> scrollItem, Supplier<Attribute> attribute, int... colors) {
            this.material = material;
            this.scrollItem = scrollItem;
            this.attribute = attribute;
            this.colors = colors;
        }

        public Item getScrollItem() {
            return scrollItem.get();
        }
    }

    enum SpellCategory {
        TARGET("🎯"), AOE("◯"), SELF("🧍"), BUFF("⬆"), DEBUFF("⬇"),
        UTILITY("🔧"), INSTANT("⏯"), TOGGLE("🎚"), CHANNELLING("🚹"),
        CONTINUOUS("↺");

        public final String icon;
        SpellCategory(String icon) { this.icon = icon; }
    }
}