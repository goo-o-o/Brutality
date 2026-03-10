package net.goo.brutality.common.loadouts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;

public record Loadout(ListTag items, String name, Item icon) {

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("Items", this.items);
        tag.putString("Name", this.name);

        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(this.icon);
        tag.putString("Icon", registryName != null ? registryName.toString() : "minecraft:air");

        return tag;
    }

    public static Loadout deserialize(CompoundTag tag) {
        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
        String name = tag.getString("Name");

        ResourceLocation iconRes = ResourceLocation.tryParse(tag.getString("Icon"));

        Item iconItem = ForgeRegistries.ITEMS.getValue(iconRes);

        // Fallback to air if the item no longer exists (e.g. uninstalled mod)
        if (iconItem == null) iconItem = Items.AIR;

        return new Loadout(items, name, iconItem);
    }
}