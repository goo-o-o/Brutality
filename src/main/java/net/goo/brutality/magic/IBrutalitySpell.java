package net.goo.brutality.magic;

import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Locale;

public interface IBrutalitySpell {


    int getBaseManaCost();

    int getManaCostLevelScaling();

    float getBaseDamage();

    float getDamageLevelScaling();

    int getBaseCooldown();

    int getCooldownLevelScaling();

    MagicSchool getSchool();

    ResourceLocation getIcon();

    SpellType getType();

    String getSpellName();

    int getDescriptionCount();

    List<BrutalityTooltipHelper.SpellStatComponent> getStatComponents();

    boolean onCast(Player player, ItemStack stack, int spellLevel);

    default float getFinalDamage(LivingEntity caster, IBrutalitySpell spell, int spellLevel) {
        return getBaseDamage() + getDamageLevelScaling() * SpellCastingHandler.getCorrectSpellLevel(caster, spell, spellLevel);
    }

    default float getFinalDamage(int spellLevel) {
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

    enum SpellType {
        SINGLETON_NO_TARGET,
        SINGLETON_SELF_BUFF,
        SINGLETON_TARGET,
        SINGLETON_TARGET_BUFF,
        SINGLETON_TARGET_DEBUFF,
        SINGLETON_AOE,
        SINGLETON_AOE_BUFF,
        SINGLETON_AOE_DEBUFF,
        CHANNELING_TARGET,
        CHANNELING_NO_TARGET,
        TOGGLE_SELF_BUFF
    }

}
