package net.goo.brutality.util.item;

import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.util.NBTUtils;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.nbt.Tag.TAG_INT;

public class StatTrakUtils {
    public enum StatTrakVariant {
        BASIC(FastColor.ARGB32.color(255, 255, 116, 0)),
        GOLD(FastColor.ARGB32.color(255, 253, 245, 95)),
        PRISMATIC(FastColor.ARGB32.color(255, 179, 95,233));

        public final int color;

        StatTrakVariant(int color) {
            this.color = color;
        }
    }

    public static String STAT_TRAK = "stat_trak";
    public static String STAT_TRAK_VARIANT = "st_variant";

    public static void incrementStatTrakIfPossible(ItemStack stack) {
        if (hasStatTrak(stack)) {
            stack.getOrCreateTag().putInt(STAT_TRAK, getStatTrakValue(stack) + 1);
        }
    }

    public static void decrementStatTrakIfPossible(ItemStack stack) {
        if (hasStatTrak(stack)) {
            stack.getOrCreateTag().putInt(STAT_TRAK, getStatTrakValue(stack) - 1);
        }
    }

    public static StatTrakVariant getVariant(ItemStack stack) {
        if (hasStatTrak(stack) && NBTUtils.hasKey(stack, STAT_TRAK_VARIANT, TAG_INT)) {
            return StatTrakVariant.values()[NBTUtils.getInt(stack, STAT_TRAK_VARIANT, 0)];
        }
        return null;
    }

    public static ItemStack rollAndAddStatTrak(Player player, ItemStack stack) {
        if (ItemCategoryUtils.isTool(stack) || ItemCategoryUtils.isWeapon(stack)) {
            if (StatTrakUtils.getVariant(stack) != null) return stack;
            float roll = player.getRandom().nextFloat();
            if (roll < BrutalityCommonConfig.PRISMATIC_STAT_TRAK_CHANCE.get() * 0.01) {
                StatTrakUtils.addStatTrak(stack, StatTrakUtils.StatTrakVariant.PRISMATIC);
            } else if (roll < BrutalityCommonConfig.GOLDEN_STAT_TRAK_CHANCE.get() * 0.01) {
                StatTrakUtils.addStatTrak(stack, StatTrakUtils.StatTrakVariant.GOLD);
            } else if (roll < BrutalityCommonConfig.BASIC_STAT_TRAK_CHANCE.get() * 0.01) {
                StatTrakUtils.addStatTrak(stack, StatTrakUtils.StatTrakVariant.BASIC);
            }
        }
        return stack;
    }

    public static void addStatTrak(ItemStack stack, StatTrakVariant statTrakVariant) {
        stack.getOrCreateTag().putInt(STAT_TRAK, 0);
        stack.getOrCreateTag().putInt(STAT_TRAK_VARIANT, statTrakVariant.ordinal());
    }

    public static int getStatTrakValue(ItemStack stack) {
        return NBTUtils.getInt(stack, STAT_TRAK, 0);
    }

    public static boolean hasStatTrak(ItemStack stack) {
        return NBTUtils.hasKey(stack, STAT_TRAK, TAG_INT);
    }

    public static void removeStatTrak(ItemStack stack) {
        stack.removeTagKey(STAT_TRAK);
    }
}
