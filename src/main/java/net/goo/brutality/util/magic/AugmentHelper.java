package net.goo.brutality.util.magic;

import net.goo.brutality.common.item.base.BrutalityMagicItem;
import net.goo.brutality.common.item.generic.BrutalityAugmentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class AugmentHelper {
    private static final String AUGMENTS = "Augments";

    /**
     * Adds {@link BrutalityAugmentItem} instances to the provided {@link ItemStack} if the item supports augmentation.
     * The augmentations are stored in the item's tag under the "Augments" key.
     * Augment slots are determined by the {@code baseAugmentSlots()} method of {@link BrutalityMagicItem}.
     * Any augment exceeding the allowed slots will not be added.
     *
     * @param toAugment The {@link ItemStack} to which the augments are to be added. Must be {@link BrutalityMagicItem}.
     * @param augments  The {@link ItemStack}s representing the augments to be added
     * @return The {@link List} of {@link ItemStack}s that were successfully added
     */
    public static List<ItemStack> addAugments(ItemStack toAugment, ItemStack... augments) {
        CompoundTag tag = toAugment.getOrCreateTag();
        ListTag augmentList = tag.getList(AUGMENTS, Tag.TAG_STRING);
        List<ItemStack> successfullyAdded = new ArrayList<>();

        if (toAugment.getItem() instanceof BrutalityMagicItem magicItem) {
            for (ItemStack stack : augments) {
                if (stack.getItem() instanceof BrutalityAugmentItem brutalityAugmentItem) {
                    if (augmentList.size() >= magicItem.baseAugmentSlots) break;
                    boolean contains = false;
                    for (BrutalityMagicItem.MagicItemType type : brutalityAugmentItem.magicItemTypes) {
                        if (type == magicItem.type) {
                            contains = true;
                            break;
                        }
                    }
                    if (contains) {
                        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(brutalityAugmentItem);
                        if (registryName != null) {
                            augmentList.add(StringTag.valueOf(registryName.toString()));
                            successfullyAdded.add(stack);
                            SpellStorage.addSpellSlot(toAugment, brutalityAugmentItem.spellSlotBonus);
                            brutalityAugmentItem.onAddedToItem(toAugment);
                        }
                    }
                }
            }
        }

        tag.put(AUGMENTS, augmentList);
        return successfullyAdded;
    }

    public static boolean hasAugment(ItemStack augmentedItem, Item augmentItem) {
        CompoundTag tag = augmentedItem.getTag();

        if (tag != null && tag.contains(AUGMENTS, Tag.TAG_LIST)) {
            ListTag augmentList = tag.getList(AUGMENTS, Tag.TAG_STRING);
            for (int i = 0; i < augmentList.size(); i++) {
                String registryName = augmentList.getString(i);
                ResourceLocation id = ResourceLocation.tryParse(registryName);

                if (id != null) {
                    Item item = ForgeRegistries.ITEMS.getValue(id);
                    if (item != null && item.equals(augmentItem)) return true;
                }
            }
        }

        return false;
    }

    public static List<BrutalityAugmentItem> getAugmentsFromItem(ItemStack augmentedItem) {
        List<BrutalityAugmentItem> augments = new ArrayList<>();
        CompoundTag tag = augmentedItem.getTag();

        if (tag != null && tag.contains(AUGMENTS, Tag.TAG_LIST)) {
            ListTag augmentList = tag.getList(AUGMENTS, Tag.TAG_STRING);
            for (int i = 0; i < augmentList.size(); i++) {
                String registryName = augmentList.getString(i);
                ResourceLocation id = ResourceLocation.tryParse(registryName);

                if (id != null) {
                    Item item = ForgeRegistries.ITEMS.getValue(id);
                    if (item instanceof BrutalityAugmentItem brutalityAugmentItem) {
                        augments.add(brutalityAugmentItem);
                    }
                }
            }
        }

        return augments;
    }


}