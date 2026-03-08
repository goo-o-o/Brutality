package net.goo.brutality.common.entity.capabilities;

import net.goo.brutality.util.item.SealUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.function.Predicate;

@AutoRegisterCapability
public class EntitySealTypeCap implements IBrutalityData {
    private static final String SEAL_TYPE_KEY = "seal_type";

    private SealUtils.SEAL_TYPE sealType = SealUtils.SEAL_TYPE.NONE;

    public SealUtils.SEAL_TYPE getSealType() {
        return this.sealType;
    }

    public void setSealType(SealUtils.SEAL_TYPE sealType) {
        this.sealType = sealType;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(SEAL_TYPE_KEY, sealType.name());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        String name = nbt.getString(SEAL_TYPE_KEY);
        try {
            setSealType(SealUtils.SEAL_TYPE.valueOf(name));
        } catch (IllegalArgumentException e) {
            setSealType(SealUtils.SEAL_TYPE.NONE);
        }
    }

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Projectile;
    }
}