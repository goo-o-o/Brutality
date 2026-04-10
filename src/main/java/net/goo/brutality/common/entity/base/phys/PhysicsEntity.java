package net.goo.brutality.common.entity.base.phys;

import net.goo.brutality.util.math.phys.PhysicsProcessor;
import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

public class PhysicsEntity extends Entity {
    public final OrientedBoundingBox hitbox;
    private final PhysicsProcessor physics = new PhysicsProcessor();

    public PhysicsEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.hitbox = new OrientedBoundingBox(this.position(), 0.5f, 0.5f, 0.5f); //
    }

    @Override
    protected void defineSynchedData() {

    }

    public Matrix3f prevRotation = new Matrix3f();
    public Vec3 prevCenter = Vec3.ZERO;


    @Override
    public void tick() {
        this.prevRotation.set(this.hitbox.rotation);
        this.prevCenter = this.hitbox.center;

        int subTicks = 6; // Increases physics to 80Hz
        float subTickDelta = 1.0f / subTicks;

        for (int i = 0; i < subTicks; i++) {
            // 1. Apply a fraction of gravity
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.04 * subTickDelta, 0));

            // 2. Run the Solver
            physics.solvePhysics(this, this.hitbox);

            // 3. Move a small increment
            // Manual position update since move() is expensive to call 4x
            Vec3 movement = this.getDeltaMovement().scale(subTickDelta);
            this.setPos(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z);

            // Sync hitbox
            this.hitbox.center = this.position().add(0, this.getBbHeight() * 0.5, 0);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     *
     * @param pCompound
     */
    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}