package net.goo.brutality.util.helpers;

import net.minecraft.world.item.ItemStack;

public class NbtHelper {
    public static final int TAG_INT = net.minecraft.nbt.Tag.TAG_INT;
    public static final int TAG_BYTE = net.minecraft.nbt.Tag.TAG_BYTE;
    public static final int TAG_STRING = net.minecraft.nbt.Tag.TAG_STRING;

    public static boolean hasKey(ItemStack stack, String key, int type) {
        var tag = stack.getTag();
        return tag != null && tag.contains(key, type);
    }

    public static int getInt(ItemStack stack, String key, int defVal) {
        var tag = stack.getTag();
        return (tag != null && tag.contains(key, TAG_INT)) ? tag.getInt(key) : defVal;
    }

    public static boolean getBool(ItemStack stack, String key, boolean defVal) {
        var tag = stack.getTag();
        return (tag != null && tag.contains(key, TAG_BYTE)) ? tag.getBoolean(key) : defVal;
    }

    public static String getString(ItemStack stack, String key, String defVal) {
        var tag = stack.getTag();
        return (tag != null && tag.contains(key, TAG_STRING)) ? tag.getString(key) : defVal;
    }

    public static void setInt(ItemStack stack, String key, int value, int defVal) {
        if (value == defVal) {
            removeKeyAndMaybeClear(stack, key);
        } else {
            stack.getOrCreateTag().putInt(key, value);
        }
    }

    public static void setBool(ItemStack stack, String key, boolean value, boolean defVal) {
        if (value == defVal) {
            removeKeyAndMaybeClear(stack, key);
        } else {
            stack.getOrCreateTag().putBoolean(key, value);
        }
    }

    public static void setString(ItemStack stack, String key, String value, String defVal) {
        if (java.util.Objects.equals(value, defVal)) {
            removeKeyAndMaybeClear(stack, key);
        } else {
            stack.getOrCreateTag().putString(key, value);
        }
    }

    public static void removeKeyAndMaybeClear(ItemStack stack, String key) {
        var tag = stack.getTag();
        if (tag == null) return;
        tag.remove(key);
        if (tag.isEmpty()) {
            stack.setTag(null);
        }
    }

    public static void stripEmptyTag(ItemStack stack) {
        var tag = stack.getTag();
        if (tag != null && tag.isEmpty()) {
            stack.setTag(null);
        }
    }
}