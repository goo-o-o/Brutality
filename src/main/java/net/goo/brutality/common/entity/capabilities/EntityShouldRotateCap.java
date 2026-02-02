package net.goo.brutality.common.entity.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

public class EntityShouldRotateCap implements IBrutalityData {
    private static final String SHOULD_ROTATE = "should_rotate";
    private boolean shouldRotate = false;

    public boolean isShouldRotate() {
        return this.shouldRotate;
    }

    public void setShouldRotate(boolean active) {
        this.shouldRotate = active;
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(SHOULD_ROTATE, isShouldRotate());
        return tag;
    }

    public void deserializeNBT(CompoundTag nbt) {
        setShouldRotate(nbt.getBoolean(SHOULD_ROTATE));
    }

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Entity;
    }
}
