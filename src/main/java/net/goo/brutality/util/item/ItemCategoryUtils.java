package net.goo.brutality.util.item;

import net.goo.brutality.common.item.base.*;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.Locale;
import java.util.Set;

public class ItemCategoryUtils {
    static Set<Item> GUNS = Set.of(
            TerramityModItems.HANDCANNON.get(),
            TerramityModItems.FLINTLOCK_PISTOL.get(),
            TerramityModItems.BASIC_PISTOL.get(),
            TerramityModItems.ADVANCED_PISTOL.get(),
            TerramityModItems.SUPPRESSED_ADVANCED_PISTOL.get(),
            TerramityModItems.PLAGUE_PISTOL.get(),
            TerramityModItems.REQUIEM.get(),
            TerramityModItems.VULCAN.get(),
            TerramityModItems.BIG_IRON.get(),
            TerramityModItems.SIX_SHOOTER.get(),
            TerramityModItems.CELESTIAL_SIXTY.get(),
            TerramityModItems.FIVE_THOUSAND_MAGNUM.get(),
            TerramityModItems.BASIC_RIFLE.get(),
            TerramityModItems.ADVANCED_BURST_RIFLE.get(),
            TerramityModItems.ADVANCED_AUTOMATIC_RIFLE.get(),
            TerramityModItems.GAIAS_TEMPEST.get(),
            TerramityModItems.NIHILITY.get(),
            TerramityModItems.TITANOMACHY.get(),
            TerramityModItems.STAIRWAY_TO_HEAVEN.get(),
            TerramityModItems.ELITE_RIFLE.get(),
            TerramityModItems.ANTI_MATERIAL_RIFLE.get(),
            TerramityModItems.ANTIMATTER_RIFLE.get(),
            TerramityModItems.DAVY_JONES.get(),
            TerramityModItems.DIVINE_INTERVENTION.get(),
            TerramityModItems.FORTUNES_FAVOR.get(),
            TerramityModItems.BLASPHEMIC_RAPTURE.get(),
            TerramityModItems.BLUNDERBUSS.get(),
            TerramityModItems.SAWED_OFF_SHOTGUN.get(),
            TerramityModItems.ONYX_STORM.get(),
            TerramityModItems.PUMP_ACTION_SHOTGUN.get(),
            TerramityModItems.PLAGUEBRINGER.get(),
            TerramityModItems.COSMIC_STORM.get(),
            TerramityModItems.HELLFIRE_FLURRY.get(),
            TerramityModItems.ASPHODEL.get(),
            TerramityModItems.OLYMPUS.get(),
            TerramityModItems.FLARE_GUN.get(),
            TerramityModItems.METEOR_CANNON.get(),
            TerramityModItems.ROCKET_LAUNCHER.get(),
//            TerramityModItems.OVERCLOCKED_MICROWAVE.get(),
            TerramityModItems.RAILGUN.get(),
            TerramityModItems.PLANET_BUSTER.get(),
            TerramityModItems.CONDUCTITE_LASER_RIFLE.get()
    );

    public static boolean isWeapon(ItemStack stack) {
        return isAxe(stack) || isSword(stack) || isScythe(stack) || isHammer(stack) || isRangedWeapon(stack);
    }

    public static boolean isRangedWeapon(ItemStack stack) {
        return isGun(stack) || isBow(stack) || isThrowing(stack) || isTrident(stack);
    }

    public static boolean isTool(ItemStack stack) {
        return isShovel(stack) || isPickaxe(stack) || isAxe(stack) || isHoe(stack) || stack.is(Tags.Items.TOOLS) || isShear(stack);
    }

    public static boolean isGun(ItemStack stack) {
        return GUNS.contains(stack.getItem());
    }

    public static boolean isSword(ItemStack stack) {
        if (stack.getItem() instanceof SwordItem || stack.is(ItemTags.SWORDS)) return true;
        for (ToolAction DEFAULT_SWORD_ACTION : ToolActions.DEFAULT_SWORD_ACTIONS) {
            if (stack.canPerformAction(DEFAULT_SWORD_ACTION)) return true;
        }
        return false;
    }

    public static boolean isSpearOrTrident(ItemStack stack) {
        return isSpear(stack) || isTrident(stack);
    }

    public static boolean isSpear(ItemStack stack) {
        return stack.getItem() instanceof BrutalitySpearItem;
    }

    public static boolean isTrident(ItemStack stack) {
        return stack.getItem() instanceof TridentItem || stack.is(Tags.Items.TOOLS_TRIDENTS) || stack.getItem() instanceof BrutalityTridentItem || nameHas(stack, "trident");
    }

    public static boolean isBow(ItemStack stack) {
        return stack.getItem() instanceof BowItem || nameHas(stack, "bow") || stack.is(Tags.Items.TOOLS_BOWS);
    }

    public static boolean isThrowing(ItemStack stack) {
        return stack.getItem() instanceof BrutalityThrowingItem;
    }

    public static boolean isScythe(ItemStack stack) {
        return stack.getItem() instanceof BrutalityScytheItem || nameHas(stack, "scythe");
    }

    public static boolean isHammer(ItemStack stack) {
        return stack.getItem() instanceof BrutalityHammerItem || stack.is(TerramityModItems.HELLROK_GIGATON_HAMMER.get());
    }

    public static boolean isAxe(ItemStack stack) {
        if (stack.getItem() instanceof AxeItem || stack.is(ItemTags.AXES)) return true;

        for (ToolAction toolAction : ToolActions.DEFAULT_AXE_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isPickaxe(ItemStack stack) {
        if (stack.getItem() instanceof PickaxeItem || stack.is(ItemTags.PICKAXES)) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_PICKAXE_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isHoe(ItemStack stack) {
        if (stack.getItem() instanceof HoeItem || stack.is(ItemTags.HOES)) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_HOE_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isShovel(ItemStack stack) {
        if (stack.getItem() instanceof ShovelItem || stack.is(ItemTags.SHOVELS)) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_SHOVEL_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isShield(ItemStack stack) {
        if (stack.getItem() instanceof ShieldItem || stack.is(Tags.Items.TOOLS_SHIELDS)) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_SHIELD_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isShear(ItemStack stack) {
        if (stack.getItem() instanceof ShearsItem || stack.is(Tags.Items.SHEARS)) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_SHEARS_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean nameHas(ItemStack stack, String text) {
        return stack.getDescriptionId().toLowerCase(Locale.ROOT).contains(text);
    }


}
