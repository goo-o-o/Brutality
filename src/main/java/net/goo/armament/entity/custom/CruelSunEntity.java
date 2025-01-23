package net.goo.armament.entity.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

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
            double radius = 10.0; // Radius within which entities are affected
            List<Entity> entities = level().getEntities(this, this.getBoundingBox().inflate(radius),
                    e -> e instanceof LivingEntity);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity livingEntity) {
                    // Apply fire to the entity
                    livingEntity.setSecondsOnFire(5); // Burn for 5 seconds
                }
            }
        }
    }

}