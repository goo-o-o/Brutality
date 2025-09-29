package net.goo.brutality.entity.projectile.trident.physics_projectile;

import net.goo.brutality.entity.base.BrutalityAbstractTrident;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BouncyDynamite extends Dynamite{
    public BouncyDynamite(EntityType<? extends BrutalityAbstractTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public String model() {
        return "dynamite";
    }

    @Override
    protected float getBounciness() {
        return 0.9F;
    }

    protected Vec3 getTridentBounceStrength() {
        return super.getTridentBounceStrength().scale(9);
    }

}
