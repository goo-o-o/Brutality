package net.goo.brutality.common.entity.spells.cosmic;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.magic.spells.cosmic.MeteorShowerSpell;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;

public class MeteorShowerEntity extends CosmicCataclysmEntity{
    public MeteorShowerEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        shouldApplyWaveEffect = false;
    }

    @Override
    public String texture() {
        return "cosmic_cataclysm";
    }

    @Override
    public String model() {
        return "cosmic_cataclysm";
    }

    @Override
    public String animation() {
        return "cosmic_cataclysm";
    }

    @Override
    public float getSizeScaling() {
        return 0;
    }

    @Override
    public BrutalitySpell getSpell() {
        return new MeteorShowerSpell();
    }

    @Override
    public float getFinalDamage(IBrutalitySpell spell, Entity owner, int spellLevel) {
        return 7F;
    }
}
