package net.goo.brutality.magic;

import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public interface IBrutalitySpell {


    static int getCooldownWithoutReduction(Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * getActualSpellLevel(player, entry.spell(), entry.level());

        return Math.max(base, 1);
    }

    /**
     * Allows custom {@code spellLevel} for display purposes
     */
    static int getCooldownWithoutReduction(Player player, SpellStorage.SpellEntry entry, int spellLevel) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * getActualSpellLevel(player, entry.spell(), spellLevel);

        return Math.max(base, 1);
    }


    static int getManaCostWithoutReduction(Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * getActualSpellLevel(player, entry.spell(), entry.level());
        return base;
    }

    /**
     * Allows custom {@code spellLevel} for display purposes
     */
    static int getManaCostWithoutReduction(Player player, SpellStorage.SpellEntry entry, int spellLevel) {
        IBrutalitySpell spell = entry.spell();

        int base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * getActualSpellLevel(player, entry.spell(), spellLevel);
        return base;
    }

    static int getActualCooldown(@NotNull Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();

        AttributeInstance cdrInstance = player.getAttribute(ModAttributes.SPELL_COOLDOWN_REDUCTION.get());
        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * entry.level();

        return Math.max((int) (cdrInstance != null ? (base * (1 - cdrInstance.getValue())) : base), 1);
    }

    static int getActualCooldown(Player player, IBrutalitySpell spell, int spellLevel) {
        AttributeInstance cdrInstance = player.getAttribute(ModAttributes.SPELL_COOLDOWN_REDUCTION.get());
        int base = spell.getBaseCooldown();
        base += spell.getCooldownLevelScaling() * spellLevel;

        return Math.max((int) (cdrInstance != null ? (base * (1 - cdrInstance.getValue())) : base), 1);
    }

    static int getActualManaCost(Player player, SpellStorage.SpellEntry entry) {
        IBrutalitySpell spell = entry.spell();
        int base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * entry.level();
        AttributeInstance manaCostAttr = player.getAttribute(ModAttributes.MANA_COST.get());
        if (manaCostAttr != null) {
            return (int) (base * manaCostAttr.getValue());
        }
        return base;
    }

    static int getActualManaCost(Player player, IBrutalitySpell spell, int spellLevel) {
        AttributeInstance manaCostAttr = player.getAttribute(ModAttributes.MANA_COST.get());
        int base = spell.getBaseManaCost();
        base += spell.getManaCostLevelScaling() * spellLevel;

        return Math.max((int) (manaCostAttr != null ? base * manaCostAttr.getValue() : base), 1);
    }

    static int getActualCastTime(Player player, IBrutalitySpell spell, int spellLevel) {
        AttributeInstance castTimeAtr = player.getAttribute(ModAttributes.CAST_TIME_REDUCTION.get());
        int base = spell.getBaseCastTime();
        base += spell.getCastTimeLevelScaling() * spellLevel;
        return Math.max((int) (castTimeAtr != null ? (base * (1 - castTimeAtr.getValue())) : base), 1);
    }

    static int getActualSpellLevel(LivingEntity caster, IBrutalitySpell spell, int spellLevel) {
        MagicSchool school = spell.getSchool();
        AttributeInstance attributeInstance = caster.getAttribute(ModAttributes.getSpellSchoolAttributeMap().get(school));
        return Math.max((int) (spellLevel + (attributeInstance != null ? attributeInstance.getValue() : 0)), 0);
    }



    int getBaseManaCost();

    int getManaCostLevelScaling();

    float getBaseDamage();

    float getDamageLevelScaling();

    int getBaseCooldown();

    int getCooldownLevelScaling();

    MagicSchool getSchool();

    ResourceLocation getIcon();

    List<SpellCategory> getCategories();

    String getSpellName();

    int getDescriptionCount();

    default boolean interruptibleByDamage() {
        return false;
    }

    default int getBaseCastTime() {
        return 0;
    }

    default int getCastTimeLevelScaling() {
        return 0;
    }


    List<BrutalityTooltipHelper.SpellStatComponent> getStatComponents();

    boolean onCast(Player player, ItemStack stack, int spellLevel);


    default float getFinalDamage(LivingEntity caster, int spellLevel) {
        return getBaseDamage() + getDamageLevelScaling() * getActualSpellLevel(caster, this, spellLevel);
    }

    default float getFinalDamageWithoutAttributes(int spellLevel) {
        return getBaseDamage() + getDamageLevelScaling() * spellLevel;
    }

    default float getFinalStat(int spellLevel, BrutalityTooltipHelper.SpellStatComponent stat) {
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

    enum MagicSchool {
        DAEMONIC("daemonium"),
        DARKIST("dimlite"),
        EVERGREEN("virentium"),
        VOLTWEAVER("conductite"),
        COSMIC("cosmilite"),
        CELESTIA("reverium"),
        UMBRANCY("nyxium"),
        EXODIC("exodium"),
        VOIDWALKER("void"),
        BRIMWIELDER("hellspec");

        private final String material;

        MagicSchool(String material) {
            this.material = material;
        }

        public String getMaterial() {
            return material;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    enum SpellCategory {
        TARGET("🎯"),
        AOE("◯"),
        SELF("🧍"),
        BUFF("⬆"),
        DEBUFF("⬇"),
        UTILITY("🔧"),
        INSTANT("⏯"),
        TOGGLE("🎚"),
        CHANNELING("🚹"),
        CONTINUOUS("↺");

        public final String icon;
        SpellCategory(String icon) {
            this.icon = icon;
        }
    }

    // Main SpellType enum with category sets
    enum SpellType {
        INSTANT_AOE(EnumSet.of(SpellCategory.INSTANT, SpellCategory.AOE)),
        INSTANT_SELF_BUFF(EnumSet.of(SpellCategory.INSTANT, SpellCategory.SELF, SpellCategory.BUFF)),
        INSTANT_TARGET(EnumSet.of(SpellCategory.INSTANT, SpellCategory.TARGET)),
        INSTANT_UTILITY(EnumSet.of(SpellCategory.INSTANT, SpellCategory.UTILITY)),
        INSTANT_TARGET_BUFF(EnumSet.of(SpellCategory.INSTANT, SpellCategory.TARGET, SpellCategory.BUFF)),
        INSTANT_TARGET_DEBUFF(EnumSet.of(SpellCategory.INSTANT, SpellCategory.TARGET, SpellCategory.DEBUFF)),
        INSTANT_AOE_BUFF(EnumSet.of(SpellCategory.INSTANT, SpellCategory.AOE, SpellCategory.BUFF)),
        INSTANT_AOE_DEBUFF(EnumSet.of(SpellCategory.INSTANT, SpellCategory.AOE, SpellCategory.DEBUFF)),
        CHANNELING_AOE(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.AOE)),
        CHANNELING_SELF_BUFF(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.SELF, SpellCategory.BUFF)),
        CHANNELING_TARGET(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.TARGET)),
        CHANNELING_UTILITY(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.UTILITY)),
        CHANNELING_TARGET_BUFF(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.TARGET, SpellCategory.BUFF)),
        CHANNELING_TARGET_DEBUFF(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.TARGET, SpellCategory.DEBUFF)),
        CHANNELING_AOE_BUFF(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.AOE, SpellCategory.BUFF)),
        CHANNELING_AOE_DEBUFF(EnumSet.of(SpellCategory.CHANNELING, SpellCategory.AOE, SpellCategory.DEBUFF)),
        CONTINUOUS_AOE(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.AOE)),
        CONTINUOUS_SELF_BUFF(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.SELF, SpellCategory.BUFF)),
        CONTINUOUS_TARGET(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.TARGET)),
        CONTINUOUS_UTILITY(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.UTILITY)),
        CONTINUOUS_TARGET_BUFF(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.TARGET, SpellCategory.BUFF)),
        CONTINUOUS_TARGET_DEBUFF(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.TARGET, SpellCategory.DEBUFF)),
        CONTINUOUS_AOE_BUFF(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.AOE, SpellCategory.BUFF)),
        CONTINUOUS_AOE_DEBUFF(EnumSet.of(SpellCategory.CONTINUOUS, SpellCategory.AOE, SpellCategory.DEBUFF)),
        TOGGLE_SELF_BUFF(EnumSet.of(SpellCategory.TOGGLE, SpellCategory.SELF, SpellCategory.BUFF));

        public final Set<SpellCategory> categories;

        SpellType(Set<SpellCategory> categories) {
            this.categories = EnumSet.copyOf(categories); // Ensure immutability
        }

        public boolean hasCategory(SpellCategory category) {
            return categories.contains(category);
        }
    }

}
