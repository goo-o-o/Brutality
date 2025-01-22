package net.goo.armament.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class CruelSunEntity extends Entity {
    private int lifespan = 200; // Total lifespan (10 seconds)


    public CruelSunEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.lifespan = pCompound.getInt("Lifespan");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.putInt("Lifespan", this.lifespan);
    }

    @Override
    public void tick() {
        super.tick();

        lifespan--;

        // Check if the entity should despawn
        if (lifespan <= 0) {
            this.discard();
        }

        if (!level().isClientSide) {
            // Burn nearby entities
            double radius = 10.0; // 10-block radius
            level().getEntities(this, this.getBoundingBox().inflate(radius), entity -> entity instanceof Mob)
                    .forEach(entity -> {
                        entity.setSecondsOnFire(5); // Set the entity on fire for 5 seconds
                    });
        }
    }

}