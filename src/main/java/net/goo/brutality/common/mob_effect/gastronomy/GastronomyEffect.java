package net.goo.brutality.common.mob_effect.gastronomy;

import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.util.FastColor;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class GastronomyEffect extends MobEffect {
    public final Type type;
    public final float baseMult, perLevelMult;

    public GastronomyEffect(MobEffectCategory pCategory, Type type, float baseMult, float perLevelMult) {
        super(pCategory, FastColor.ARGB32.color(255, 255, 255, 255));
        this.type = type;
        this.baseMult = baseMult;
        this.perLevelMult = perLevelMult;
    }

    public void initAttribute(String name) {
        this.addAttributeModifier(
               type == Type.WET ? BrutalityAttributes.GASTRONOMY_WET_DEBUFF_DAMAGE_TAKEN_BOOST.get() : BrutalityAttributes.GASTRONOMY_DRY_DEBUFF_DAMAGE_TAKEN_BOOST.get(),
                UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8)).toString(), 0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public double getAttributeModifierValue(int pAmplifier, AttributeModifier pModifier) {
        return baseMult + perLevelMult * (pAmplifier + 1);
    }

    public enum Type {
        WET,
        DRY,
        BOTH
    }



    // for any secondary logic I might want to add in the future
    public void applyEffect(LivingEntity attacker, LivingEntity victim, int level) {
    }
}