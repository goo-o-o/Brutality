package net.goo.brutality.common.entity.capabilities;

import net.goo.brutality.common.item.generic.augments.BrutalityAugmentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Predicate;

@AutoRegisterCapability
public class EntityAugmentCap implements IBrutalityData {
    private static final String AUGMENTS_KEY = "augments";
    private final List<BrutalityAugmentItem> augments = new ArrayList<>();

    public List<BrutalityAugmentItem> getAugments() {
        return this.augments;
    }

    public Map<BrutalityAugmentItem, Integer> getAugmentCounts() {
        if (this.augments.isEmpty()) {
            return Collections.emptyMap();
        }

        // Initialize with the size of the list to minimize rehashing
        Map<BrutalityAugmentItem, Integer> counts = new HashMap<>(this.augments.size());

        for (BrutalityAugmentItem augment : this.augments) {
            counts.merge(augment, 1, Integer::sum);
        }

        return counts;
    }

    public void addAugment(BrutalityAugmentItem augment) {
        if (!this.augments.contains(augment)) {
            this.augments.add(augment);
        }
    }

    public void clearAugments() {
        this.augments.clear();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        for (BrutalityAugmentItem augment : augments) {
            ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(augment);
            if (registryName != null) {
                list.add(StringTag.valueOf(registryName.toString())); // Stores as "namespace:item_name"
            }
        }

        tag.put(AUGMENTS_KEY, list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.augments.clear();
        if (nbt.contains(AUGMENTS_KEY, Tag.TAG_LIST)) {
            ListTag list = nbt.getList(AUGMENTS_KEY, Tag.TAG_STRING);
            for (int i = 0; i < list.size(); i++) {
                ResourceLocation id = ResourceLocation.tryParse(list.getString(i));
                if (id != null) {
                    Item item = ForgeRegistries.ITEMS.getValue(id);
                    if (item instanceof BrutalityAugmentItem augment) {
                        this.augments.add(augment);
                    }
                }
            }
        }
    }

    @Override
    public Predicate<Entity> predicate() {
        return e -> e instanceof Projectile;
    }
}