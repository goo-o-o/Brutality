package net.goo.brutality.util;

import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.generic.augments.BrutalityAugmentItem;
import net.goo.brutality.common.item.generic.augments.BrutalityAugmentationDevice;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class AugmentHelper {
    private static final String AUGMENTS = "Augments", AUGMENT_SLOTS = "AugmentSlots", AUGMENT_ITEM_TYPES = "AugmentItemTypes";

    /**
     * Attempts to add the specified augment items to the given item, provided they meet compatibility
     * criteria such as augment type and available slots on the item.
     *
     * @param toAugment The {@link ItemStack} to which the augments should be applied. This item determines
     *                  the augment type compatibility and slot capacity.
     * @param augments  The array of {@link ItemStack}s representing potential augments
     *                  to be added to {@code toAugment}.
     * @return A {@link List} of {@link ItemStack} objects containing the augments that were successfully added
     * to the {@code toAugment} item. Only augments that passed compatibility checks and slot availability
     * constraints are included.
     */
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
            // or if there is no augment type specified
            if (isAugmentCompatible(augmentStack, targetType) || !NBTUtils.hasKey(augmentStack, AUGMENT_ITEM_TYPES, Tag.TAG_STRING)) {
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

    public static Map<BrutalityAugmentItem, Integer> getAugmentCounts(ItemStack augmentedItem) {
        CompoundTag tag = augmentedItem.getTag();
        if (tag == null || !tag.contains(AUGMENTS, Tag.TAG_LIST)) {
            return Collections.emptyMap();
        }

        ListTag augmentList = tag.getList(AUGMENTS, Tag.TAG_STRING);
        Map<BrutalityAugmentItem, Integer> counts = new HashMap<>(augmentList.size());

        for (int i = 0; i < augmentList.size(); i++) {
            String registryName = augmentList.getString(i);
            ResourceLocation id = ResourceLocation.tryParse(registryName);

            if (id != null) {
                Item item = ForgeRegistries.ITEMS.getValue(id);
                if (item instanceof BrutalityAugmentItem augment) {
                    // merge is the most concise way to increment
                    counts.merge(augment, 1, Integer::sum);
                }
            }
        }

        return counts;
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
            String name = ((Enum<?>) type).name();
            StringTag typeTag = StringTag.valueOf(name);

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

    public static boolean addAugmentSlot(ItemStack stack, BrutalityAugmentationDevice item) {
        int currentAmount = getAugmentSlots(stack);
        if (currentAmount < item.maxSlots) {
            forceAddAugmentSlot(stack, 1);
            return true;
        }
        return false;
    }

    public static void forceAddAugmentSlot(ItemStack stack, int amount) {
        stack.getOrCreateTag().putInt(AUGMENT_SLOTS, getAugmentSlots(stack) + amount);
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

    public static void addAugmentsToProjectile(ItemStack stack, Entity projectile) {
        AugmentHelper.getAugmentsFromItem(stack).forEach(brutalityAugmentItem -> {
            projectile.getCapability(BrutalityCapabilities.AUGMENT).ifPresent(cap -> {
                cap.addAugment(brutalityAugmentItem);
            });
        });
    }

    public static void addAugmentsToProjectile(Entity from, Entity to) {
        from.getCapability(BrutalityCapabilities.AUGMENT).ifPresent(fromAugmentCap -> {
            to.getCapability(BrutalityCapabilities.AUGMENT).ifPresent(toAugmentCap -> {
                fromAugmentCap.getAugments().forEach(toAugmentCap::addAugment);
            });
        });
    }
}