package net.goo.brutality.common.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ColorUtils;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
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
     * Calculates the total damage for an entity.
     * Considers True Level, Curio Multipliers, and Global Spell Damage Attributes.
     */
    default float getActualDamage(@Nullable Entity caster, int spellLevel) {
        float dmg = getBaseDamage() + getDamageLevelScaling() * getActualSpellLevel(caster, spellLevel);
        dmg *= SpellUtils.getCurioDamageMultiplier(caster, null, spellLevel);

        if (caster instanceof LivingEntity livingCaster) {
            return (float) (dmg * livingCaster.getAttributeValue(BrutalityAttributes.SPELL_DAMAGE.get()));
        }
        return dmg;
    }


    /**
     * Calculates the final cooldown.
     * Logic: (Base + Scaling * Level) * Curio Multipliers * (Attribute Reduction).
     */
    default int getActualCooldown(LivingEntity livingEntity, int spellLevel) {
        float base = getBaseCooldown() + getCooldownLevelScaling() * spellLevel;
        base *= SpellUtils.getCurioCooldownMultiplier(livingEntity, this, spellLevel);
        return Math.max(1, (int) (base * livingEntity.getAttributeValue(BrutalityAttributes.SPELL_COOLDOWN.get())));
    }

    /**
     * Calculates the actual mana cost for casting a spell, based on the livingEntity's attributes
     * and the spell's base and level-scaling mana costs.
     *
     * @param livingEntity The livingEntity who is casting the spell. Used for retrieving the
     *                     relevant attributes to modify mana cost.
     * @param spellLevel   The level of the spell being cast. Impacts the scaling component
     *                     of mana cost.
     * @return The calculated mana cost for the spell, adjusted by livingEntity attributes and
     * constrained to a minimum of 1.
     * @implNote The actual spell level (affected by attributes) should be passed in
     */
    default float getActualManaCost(LivingEntity livingEntity, int spellLevel) {
        float base = getBaseManaCost() + getManaCostLevelScaling() * spellLevel;
        return (float) Math.max((int) base * livingEntity.getAttributeValue(BrutalityAttributes.MANA_COST.get()), 1);
    }

    /**
     * Calculates effective cast time.
     * Logic: (Base + Scaling * Level) * Curio Multipliers * Attribute Reduction.
     */
    default int getActualCastTime(LivingEntity livingEntity, int spellLevel) {
        float base = getBaseCastTime() + getCastTimeLevelScaling() * spellLevel;
        base *= SpellUtils.getCurioCastTimeMultiplier(livingEntity, this, spellLevel);
        return Math.max((int) (base * livingEntity.getAttributeValue(BrutalityAttributes.CAST_TIME.get())), 1);
    }

    /**
     * Calculates the "True" level of a spell by adding the caster's Magic School Attribute
     * to the spell's base level.
     */
    default int getActualSpellLevel(@Nullable Entity caster, int spellLevel) {
        MagicSchool school = getSchool();
        if (caster == null) return spellLevel;
        if (caster instanceof LivingEntity livingCaster) {
            int original = spellLevel;
            AttributeInstance attributeInstance = livingCaster.getAttribute(school.attribute.get());
            if (attributeInstance != null) {
                original += (int) attributeInstance.getValue();
            }
            AttributeInstance universal = livingCaster.getAttribute(BrutalityAttributes.UNIVERSAL_SCHOOL_LEVEL.get());
            if (universal != null) {
                original += (int) universal.getValue();
            }
            return Math.max(original, 0);
        }
        return spellLevel;
    }

    // --- Base Spell Properties ---

    int getBaseManaCost();

    default float getManaCostLevelScaling() {
        return 0;
    }

    float getBaseDamage();

    default float getDamageLevelScaling() {
        return 0;
    }

    int getBaseCooldown();

    default int getCooldownLevelScaling() {
        return 0;
    }

    int getBaseCastTime();

    default int getCastTimeLevelScaling() {
        return 0;
    }

    MagicSchool getSchool();

    ResourceLocation getIcon();

    List<SpellCategory> getCategories();

    String getSpellName();

    int getDescriptionCount();

    List<SpellTooltipRenderer.SpellStatComponent> getStatComponents();

    // --- Translation Helpers ---

    default MutableComponent getTranslatedSpellName() {
        return Component.translatable("spell." + Brutality.MOD_ID + "." + getSpellName());
    }

    default MutableComponent getSpellDescription(int index) {
        return Component.translatable("spell." + Brutality.MOD_ID + "." + getSpellName() + ".description." + index);
    }

    // --- Casting Life-Cycle Hooks ---

    /**
     * Checks if the spell can continue its current cast tick.
     * Useful for custom requirement checks (e.g. must be on ground).
     * Used mainly for {@link SpellCategory#CONTINUOUS} spells
     */
    default boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        return true;
    }

    /**
     * Triggered at the very start of the casting process.
     */
    default boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        return true;
    }

    /**
     * Triggered when the casting time successfully completes.
     */
    default void onEndCast(Player player, ItemStack stack, int spellLevel) {
    }


    /**
     * Resolves a stat component (like range or radius) to a final float,
     * applying level scaling and min/max clamping.
     */
    default float getFinalStat(int spellLevel, @Nullable SpellTooltipRenderer.SpellStatComponent stat) {
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
        DAEMONIC(TerramityModItems.DAEMONIUM, BrutalityItems.DAEMONIC_SPELL_SCROLL, BrutalityAttributes.DAEMONIC_SCHOOL_LEVEL, ColorUtils.ColorData.DAEMONIC),

        DARKIST(TerramityModItems.DIMLITE_INGOT, BrutalityItems.DARKIST_SPELL_SCROLL, BrutalityAttributes.DARKIST_SCHOOL_LEVEL, ColorUtils.ColorData.DARKIST),

        EVERGREEN(TerramityModItems.VIRENTIUM_ALLOY_INGOT, BrutalityItems.EVERGREEN_SPELL_SCROLL, BrutalityAttributes.EVERGREEN_SCHOOL_LEVEL, ColorUtils.ColorData.EVERGREEN),

        VOLTWEAVER(TerramityModItems.CONDUCTITE, BrutalityItems.VOLTWEAVER_SPELL_SCROLL, BrutalityAttributes.VOLTWEAVER_SCHOOL_LEVEL, ColorUtils.ColorData.VOLTWEAVER),

        COSMIC(TerramityModItems.COSMILITE_INGOT, BrutalityItems.COSMIC_SPELL_SCROLL, BrutalityAttributes.COSMIC_SCHOOL_LEVEL, ColorUtils.ColorData.COSMIC),

        CELESTIA(TerramityModItems.REVERIUM, BrutalityItems.CELESTIA_SPELL_SCROLL, BrutalityAttributes.CELESTIA_SCHOOL_LEVEL, ColorUtils.ColorData.CELESTIA),

        UMBRANCY(TerramityModItems.NYXIUM, BrutalityItems.UMBRAL_SPELL_SCROLL, BrutalityAttributes.UMBRANCY_SCHOOL_LEVEL, ColorUtils.ColorData.UMBRANCY),

        EXODIC(TerramityModItems.EXODIUM_SUPERALLOY, BrutalityItems.EXODIC_SPELL_SCROLL, BrutalityAttributes.EXODIC_SCHOOL_LEVEL, ColorUtils.ColorData.EXODIC),

        BRIMWIELDER(TerramityModItems.HELLSPEC_ALLOY, BrutalityItems.BRIMWIELDER_SPELL_SCROLL, BrutalityAttributes.BRIMWIELDER_SCHOOL_LEVEL, ColorUtils.ColorData.BRIMWIELDER),

        VOIDWALKER(TerramityModItems.VOID_ALLOY, BrutalityItems.VOIDWALKER_SPELL_SCROLL, BrutalityAttributes.VOIDWALKER_SCHOOL_LEVEL, ColorUtils.ColorData.VOIDWALKER);

        private final Supplier<Item> material, scrollItem;
        private final Supplier<Attribute> attribute;
        public final ColorUtils.ColorData colorData;

        MagicSchool(Supplier<Item> material, Supplier<Item> scrollItem, Supplier<Attribute> attribute, ColorUtils.ColorData colorData) {
            this.material = material;
            this.scrollItem = scrollItem;
            this.attribute = attribute;
            this.colorData = colorData;
        }

        public Item getScrollItem() {
            return scrollItem.get();
        }
    }

    enum SpellCategory {
        TARGETABLE("🎯"), AOE("◯"), SELF("🧍"), BUFF("⬆"), DEBUFF("⬇"),
        UTILITY("🔧"), INSTANT("⏯"), TOGGLE("🎚"), CHANNELLING("🚹"),
        CONTINUOUS("↺");

        public final MutableComponent icon;

        SpellCategory(String icon) {
            this.icon = Component.literal(icon).withStyle(s -> s.withFont(ModResources.ICON_FONT));
        }
    }
}