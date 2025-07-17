package net.goo.brutality.entity.capabilities;

import net.minecraft.nbt.CompoundTag;

public class EntityCapabilities implements IEntityCapabilities {
    private boolean miracleBlighted = false;
    private int starCount;

    @Override
    public boolean isMiracleBlighted() {
        return this.miracleBlighted;
    }

    @Override
    public void setMiracleBlighted(boolean active) {
        this.miracleBlighted = active;
    }

    @Override
    public int starCount() {
        return this.starCount;
    }

    @Override
    public void setStarCount(int count) {
        this.starCount = count;
    }

    private final String SALTED = "isSalted", PEPPERED = "isPeppered", MIRACLE_BLIGHTED = "isMiracleBlighted";
    private final String HAS_STARS = "hasStars";

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(MIRACLE_BLIGHTED, isMiracleBlighted());
        tag.putInt(HAS_STARS, starCount());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        setMiracleBlighted(nbt.getBoolean(MIRACLE_BLIGHTED));
        setStarCount(nbt.getInt(HAS_STARS));
    }
}
