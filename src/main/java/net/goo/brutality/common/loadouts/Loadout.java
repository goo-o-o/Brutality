package net.goo.brutality.common.loadouts;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

public record Loadout(ListTag data, String name, ResourceLocation icon) {

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.put("Data", this.data);
        tag.putString("Name", this.name);
        tag.putString("Icon", this.icon.toString());

        return tag;
    }

    public static Loadout deserialize(CompoundTag tag) {
        ListTag items = tag.getList("Data", Tag.TAG_COMPOUND);
        String name = tag.getString("Name");
        String icon = tag.getString("Icon");
        return new Loadout(items, name, ResourceLocation.tryParse(icon));
    }
}