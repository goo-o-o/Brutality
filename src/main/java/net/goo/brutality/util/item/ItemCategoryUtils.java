package net.goo.brutality.util.item;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.base.*;
import net.goo.brutality.util.BrutalityTags;
import net.mcreator.terramity.init.TerramityModItems;
import net.mcreator.terramity.item.CosmiliteArmorItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import top.theillusivec4.curios.Curios;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

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

    public static BrutalityCategories getCategory(ItemStack stack) {
        // 1. Check Magic Items First (Specialized classes)
        if (stack.getItem() instanceof BrutalityMagicItem magicItem) {
            return magicItem.type; // Returns MagicItemType.STAFF, TOME, etc.
        }

        if (isSword(stack)) return BrutalityCategories.ItemType.SWORD;
        if (isAxe(stack)) return BrutalityCategories.ItemType.AXE;
        if (isHammer(stack)) return BrutalityCategories.ItemType.HAMMER;
        if (isScythe(stack)) return BrutalityCategories.ItemType.SCYTHE;
        if (isSpear(stack)) return BrutalityCategories.ItemType.SPEAR;
        if (isTrident(stack)) return BrutalityCategories.ItemType.TRIDENT;
        if (isBow(stack)) return BrutalityCategories.ItemType.BOW;
        if (isThrowing(stack)) return BrutalityCategories.ItemType.THROWING;
        if (isPickaxe(stack)) return BrutalityCategories.ItemType.PICKAXE;

        // 3. Handle Curios and Armor (if applicable)
        if (isArmor(stack)) return BrutalityCategories.ItemType.ARMOR;
        if (stack.getItem() instanceof BlockItem) return BrutalityCategories.ItemType.BLOCK;
        if (isCurio(stack)) return BrutalityCategories.ItemType.CURIO;
        // 4. Default Fallback
        return BrutalityCategories.ItemType.GENERIC;
    }

    public static boolean isCurio(ItemStack stack) {
        return stack.getItem() instanceof ICurioItem || stack.is(BrutalityTags.Items.ANKLET) || stack.is(BrutalityTags.Items.BELT) ||
                stack.is(BrutalityTags.Items.BELT) || stack.is(BrutalityTags.Items.CHARM) || stack.is(BrutalityTags.Items.FEET) ||
                stack.is(BrutalityTags.Items.HANDS) || stack.is(BrutalityTags.Items.HEAD) || stack.is(BrutalityTags.Items.HEART) ||
                stack.is(BrutalityTags.Items.NECKLACE) || stack.is(BrutalityTags.Items.RING);
    }

    public static boolean isArmor(ItemStack stack) {
        return stack.getItem() instanceof ArmorItem && stack.is(Tags.Items.ARMORS) || stack.is(ItemTags.TRIMMABLE_ARMOR) ||
                isHelmet(stack) || isChestplate(stack) || isLeggings(stack) || isBoots(stack);
    }

    public static boolean isHelmet(ItemStack stack) {
        return stack.is(Tags.Items.ARMORS_HELMETS) ||
                stack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.HELMET;
    }

    public static boolean isChestplate(ItemStack stack) {
        return stack.is(Tags.Items.ARMORS_CHESTPLATES) ||
                stack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.CHESTPLATE;
    }

    public static boolean isLeggings(ItemStack stack) {
        return stack.is(Tags.Items.ARMORS_LEGGINGS) ||
                stack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.LEGGINGS;
    }

    public static boolean isBoots(ItemStack stack) {
        return stack.is(Tags.Items.ARMORS_BOOTS) ||
                stack.getItem() instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.BOOTS;
    }

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
