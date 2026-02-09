package net.goo.brutality.common.magic;


import net.goo.brutality.Brutality;
import net.goo.brutality.util.tooltip.SpellTooltips;
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
    private final List<SpellTooltips.SpellStatComponent> statComponents;
    private final int descriptionCount;
    private final int baseCastTime;
    private Map<SpellTooltips.SpellStatComponents, SpellTooltips.SpellStatComponent> statMap = new HashMap<>();

    protected BrutalitySpell(MagicSchool school, List<SpellCategory> categories, String name,
                             int baseManaCost, float baseDamage, int baseCooldown, int baseCastTime, int descriptionCount,
                             @Nullable List<SpellTooltips.SpellStatComponent> statComponents) {
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
            this.statMap = this.statComponents.stream().collect(Collectors.toMap(SpellTooltips.SpellStatComponent::type, Function.identity()));
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
     * Retrieves the {@link SpellTooltips.SpellStatComponent} for the specified {@link SpellTooltips.SpellStatComponents} type.
     * The stat component contains detailed information, such as its base value, level scaling ({@code levelDelta}), and optional minimum/maximum bounds.
     *
     * @param type The {@link SpellTooltips.SpellStatComponents} type specifying the desired statistic (e.g., {@code RANGE}, {@code SPEED}, etc.).
     *             Must be non-null to retrieve a valid result.
     * @return The corresponding {@link SpellTooltips.SpellStatComponent} if available, or {@code null} if the given {@code type} is not mapped in {@code statMap}.
     */
    @Nullable
    public SpellTooltips.SpellStatComponent getStat(SpellTooltips.SpellStatComponents type) {
        //        throw new NullPointerException("Could not find " + type + " stat for spell: " + this);
        return this.statMap.get(type);
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


    public List<SpellTooltips.SpellStatComponent> getStatComponents() {
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