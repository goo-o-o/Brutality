package net.goo.brutality.magic;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;

public interface IBrutalitySpell {
    static int getActualCooldown(Player player, IBrutalitySpell spell, int spellLevel) {
        float base = spell.getBaseCooldown() + spell.getCooldownLevelScaling() * spellLevel;
        base *= SpellUtils.getCurioCooldownMultiplier(player, spell, spellLevel);
        return Math.max((int) (base * (2 - player.getAttributeValue(BrutalityModAttributes.SPELL_COOLDOWN_REDUCTION.get()))), 1);
    }

    static float getActualManaCost(Player player, IBrutalitySpell spell, int spellLevel) {
        float base = spell.getBaseManaCost() + spell.getManaCostLevelScaling() * spellLevel;
        return (float) Math.max((int) base * player.getAttributeValue(BrutalityModAttributes.MANA_COST.get()), 1);
    }

    static int getActualCastTime(Player player, IBrutalitySpell spell, int spellLevel) {
        float base = spell.getBaseCastTime() + spell.getCastTimeLevelScaling() * spellLevel;
        base *= SpellUtils.getCurioCastTimeMultiplier(player, spell, spellLevel);
        return Math.max((int) (base * (2 - player.getAttributeValue(BrutalityModAttributes.CAST_TIME_REDUCTION.get()))), 1);
    }

    static int getActualSpellLevel(@Nullable Entity caster, IBrutalitySpell spell, int spellLevel) {
        MagicSchool school = spell.getSchool();
        if (caster == null) return spellLevel;
        if (caster instanceof LivingEntity livingCaster) {
            Attribute attribute = BrutalityModAttributes.getSpellSchoolAttributeMap().get(school);
            AttributeInstance attributeInstance = livingCaster.getAttribute(attribute);
            if (attributeInstance != null) {
                return Math.max((int) (spellLevel + attributeInstance.getValue()), 0);
            }
            return Math.max(spellLevel, 0);
        }
        return spellLevel;
    }

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

    default MutableComponent getTranslatedSpellName() {
        return Component.translatable("spell." + Brutality.MOD_ID + "." + getSpellName());
    }

    default MutableComponent getSpellDescription(int index) {
        return Component.translatable("spell." + Brutality.MOD_ID + "." + getSpellName() + ".description." + index);
    }

    int getDescriptionCount();

    List<BrutalityTooltipHelper.SpellStatComponent> getStatComponents();

    default boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        return true;
    }

    default boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        return true;
    }

    default void onEndCast(Player player, ItemStack stack, int spellLevel) {
    }

    default float getFinalDamage(@Nullable Entity caster, int spellLevel) {
        float dmg = getBaseDamage() + getDamageLevelScaling() * getActualSpellLevel(caster, this, spellLevel);
        dmg *= SpellUtils.getCurioDamageMultiplier(caster, null, spellLevel);

        if (caster instanceof LivingEntity livingCaster) {
            AttributeInstance spellDamage = livingCaster.getAttribute(BrutalityModAttributes.SPELL_DAMAGE.get());
            if (spellDamage != null) {
                return (float) (dmg * spellDamage.getValue());
            }
        }
        return dmg;

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
        UMBRANCY("umbrancy"),
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
        INSTANT("⏯"),
        CHANNELLING("🚹"),
        CONTINUOUS("↺"),
        TARGET("🎯"),
        AOE("◯"),
        SELF("🧍"),
        BUFF("⬆"),
        DEBUFF("⬇"),
        UTILITY("🔧");

        public final String icon;

        SpellCategory(String icon) {
            this.icon = icon;
        }
    }
}