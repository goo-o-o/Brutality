package net.goo.brutality.util.magic;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.base.BrutalityMagicItem;
import net.goo.brutality.common.item.generic.BrutalityAugmentItem;
import net.goo.brutality.common.item.generic.BrutalityMagicAugmentItem;
import net.goo.brutality.common.item.generic.BrutalityMagicAugmentItem;
import net.goo.brutality.util.NBTUtils;
import net.goo.brutality.util.item.ItemCategoryUtils;
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
    private static final String AUGMENTS = "Augments", AUGMENT_SLOTS = "AugmentSlots", AUGMENT_ITEM_TYPES = "AugmentItemTypes";

    public static List<ItemStack> addAugments(ItemStack toAugment, ItemStack... augments) {
        CompoundTag tag = toAugment.getOrCreateTag();
        ListTag augmentList = tag.getList(AUGMENTS, Tag.TAG_STRING);
        List<ItemStack> successfullyAdded = new ArrayList<>();

        BrutalityCategories targetType = ItemCategoryUtils.getCategory(toAugment);

        for (ItemStack augmentStack : augments) {
            if (!(augmentStack.getItem() instanceof BrutalityAugmentItem augmentItem)) continue;

            // 2. Check slots (based on the base item's capacity)
            if (augmentList.size() >= getAugmentSlots(toAugment)) break;

            // 3. Check if this augment is allowed on this specific item type
            if (isAugmentCompatible(augmentStack, targetType)) {
                ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(augmentItem);
                if (registryName != null) {
                    augmentList.add(StringTag.valueOf(registryName.toString()));
                    successfullyAdded.add(augmentStack);
                    // 5. Trigger lifecycle hook
                    augmentItem.onAddedToItem(toAugment);
                }
            }
        }

        tag.put(AUGMENTS, augmentList);
        return successfullyAdded;
    }

    public static boolean hasAugment(ItemStack augmentedItem, Item augmentItem) {
        if (!(augmentItem instanceof BrutalityAugmentItem brutalityAugmentItem)) return false;
        return getAugmentsFromItem(augmentedItem).contains(brutalityAugmentItem);
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

    public static boolean isAugmentCompatible(ItemStack stack, BrutalityCategories testType) {
        return getAugmentItemTypes(stack).contains(testType);
    }

    public static void addAugmentItemTypes(ItemStack stack, BrutalityCategories... itemTypes) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag augmentItemTypes = tag.getList(AUGMENT_ITEM_TYPES, Tag.TAG_STRING);

        for (BrutalityCategories type : itemTypes) {
            // Convert enum to String and wrap it in a StringTag
            StringTag typeTag = StringTag.valueOf(type.toString());

            if (!augmentItemTypes.contains(typeTag)) {
                augmentItemTypes.add(typeTag);
            }
        }
        tag.put(AUGMENT_ITEM_TYPES, augmentItemTypes);
    }

    public static List<BrutalityCategories> getAugmentItemTypes(ItemStack stack) {
        List<BrutalityCategories> itemTypes = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(AUGMENT_ITEM_TYPES, Tag.TAG_LIST)) {
            ListTag augmentItemTypes = tag.getList(AUGMENT_ITEM_TYPES, Tag.TAG_STRING);
            for (int i = 0; i < augmentItemTypes.size(); i++) {
                String itemTypesTag = augmentItemTypes.getString(i);
                BrutalityCategories itemType = BrutalityCategories.findByName(itemTypesTag);

                if (itemType != null) {
                    itemTypes.add(itemType);
                }
            }
        }
        return itemTypes;
    }

    public static int getAugmentSlots(ItemStack stack) {
        return NBTUtils.getInt(stack, AUGMENT_SLOTS, 0);
    }

    public static void addAugmentSlot(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt(AUGMENT_SLOTS, stack.getOrCreateTag().getInt(AUGMENT_SLOTS) + amount);
    }

    public static void removeAugmentSlot(ItemStack stack, int amount) {
        int finalSlots = Math.max(0, stack.getOrCreateTag().getInt(AUGMENT_SLOTS) - amount);
        if (finalSlots == 0) {
            stack.removeTagKey(AUGMENT_SLOTS);
            return;
        }
        stack.getOrCreateTag().putInt(AUGMENT_SLOTS, finalSlots);
    }

    public static void setAugmentSlots(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt(AUGMENT_SLOTS, amount);
    }


}