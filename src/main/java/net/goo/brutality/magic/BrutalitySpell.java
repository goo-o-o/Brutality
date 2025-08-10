package net.goo.brutality.magic;


import net.goo.brutality.Brutality;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BrutalitySpell implements IBrutalitySpell {
    private final MagicSchool school;
    private final List<SpellCategory> categories;
    private final String name;
    private final int baseManaCost;
    private final float baseDamage;
    private final int baseCooldown;
    private final List<BrutalityTooltipHelper.SpellStatComponent> statComponents;
    private final int descriptionCount;
    private static final Map<Class<? extends BrutalitySpell>, Map<BrutalityTooltipHelper.SpellStatComponents, BrutalityTooltipHelper.SpellStatComponent>> SPELL_STATS = new HashMap<>();
    private final int baseCastTime;
    private Map<BrutalityTooltipHelper.SpellStatComponents, BrutalityTooltipHelper.SpellStatComponent> statMap = new HashMap<>();

    protected BrutalitySpell(MagicSchool school, List<SpellCategory> categories, String name,
                             int baseManaCost, float baseDamage, int baseCooldown, int baseCastTime, int descriptionCount,
                             @Nullable List<BrutalityTooltipHelper.SpellStatComponent> statComponents) {
        this.school = school;
        this.categories = categories;
        this.name = name;
        this.baseManaCost = baseManaCost;
        this.baseDamage = baseDamage;
        this.baseCooldown = baseCooldown;
        this.baseCastTime = baseCastTime;
        this.statComponents = statComponents != null ? statComponents : Collections.emptyList();
        this.descriptionCount = descriptionCount;

        if (!this.statComponents.isEmpty()) {
            this.statMap =
                    this.statComponents.stream()
                            .collect(Collectors.toMap(BrutalityTooltipHelper.SpellStatComponent::type, Function.identity()));
            SPELL_STATS.put(this.getClass(), statMap);
        }
    }

    @Override
    public String toString() {
        return "BrutalitySpell{" +
                "school=" + school +
                ", type=" + categories +
                ", name='" + name + '\'' +
                ", baseManaCost=" + baseManaCost +
                ", baseDamage=" + baseDamage +
                ", baseCooldown=" + baseCooldown +
                ", statComponents=" + statComponents +
                ", descriptionCount=" + descriptionCount +
                '}';
    }

    /**
     Static method for {@code getStat}
     */
    public static BrutalityTooltipHelper.SpellStatComponent getStat(Class<? extends BrutalitySpell> spellClazz, BrutalityTooltipHelper.SpellStatComponents type) {
        Map<BrutalityTooltipHelper.SpellStatComponents, BrutalityTooltipHelper.SpellStatComponent> statMap = SPELL_STATS.get(spellClazz);
        if (statMap == null) {
            throw new IllegalArgumentException("No stats defined for spell class: " + spellClazz.getSimpleName());
        }
        BrutalityTooltipHelper.SpellStatComponent stat = statMap.get(type);
        if (stat == null) {
            throw new IllegalArgumentException("Stat " + type + " not defined for spell class " + spellClazz.getSimpleName());
        }
        return stat;
    }

    /**
     Instance sensitive method for {@code getStat}
     */
    public BrutalityTooltipHelper.SpellStatComponent getStat(BrutalityTooltipHelper.SpellStatComponents type) {
        BrutalityTooltipHelper.SpellStatComponent stat = statMap.get(type);
        if (stat == null) {
            throw new IllegalArgumentException("Stat " + type + " not defined for spell " + name);
        }
        return stat;
    }

    @Override
    public MagicSchool getSchool() {
        return school;
    }

    @Override
    public ResourceLocation getIcon() {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + school.toString().toLowerCase(Locale.ROOT) + "/" + name + ".png");
    }

    @Override
    public String getSpellName() {
        return name;
    }

    @Override
    public int getBaseCastTime() {
        return baseCastTime;
    }

    @Override
    public float getDamageLevelScaling() {
        return getBaseDamage();
    }


    public List<BrutalityTooltipHelper.SpellStatComponent> getStatComponents() {
        return statComponents;
    }

    @Override
    public int getDescriptionCount() {
        return descriptionCount;
    }

    @Override
    public List<SpellCategory> getCategories() {
        return categories;
    }

    @Override
    public float getBaseDamage() {
        return baseDamage;
    }

    @Override
    public int getBaseCooldown() {
        return baseCooldown;
    }

    @Override
    public int getBaseManaCost() {
        return baseManaCost;
    }

}