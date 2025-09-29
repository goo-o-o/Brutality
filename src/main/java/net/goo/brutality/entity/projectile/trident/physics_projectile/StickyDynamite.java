package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class StickyDynamite extends Dynamite{
    public StickyDynamite(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public String model() {
        return "dynamite";
    }

    @Override
    protected int getBounceQuota() {
        return 0;
    }

    protected Vec3 getTridentBounceStrength() {
        return super.getTridentBounceStrength().scale(0);
    }

}
