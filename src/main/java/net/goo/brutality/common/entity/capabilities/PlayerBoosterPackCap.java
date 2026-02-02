package net.goo.brutality.common.entity.capabilities;


import net.goo.brutality.common.item.curios.charm.BoosterPack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;

import java.util.function.Predicate;

@AutoRegisterCapability
public class PlayerBoosterPackCap implements IBrutalityData {
    private static final String BOOSTER_PACK_TYPE_KEY = "booster_pack_type";

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Player;
    }

    private BoosterPack.BoosterType boosterType = BoosterPack.BoosterType.NONE;

    public BoosterPack.BoosterType getBoosterType() {
        return this.boosterType;
    }

    public void setBoosterType(BoosterPack.BoosterType boosterType) {
        this.boosterType = boosterType;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(BOOSTER_PACK_TYPE_KEY, boosterType.name());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        String name = nbt.getString(BOOSTER_PACK_TYPE_KEY);
        try {
            setBoosterType(BoosterPack.BoosterType.valueOf(name));
        } catch (IllegalArgumentException e) {
            setBoosterType(BoosterPack.BoosterType.NONE);
        }
    }
}