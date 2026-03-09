package net.goo.brutality.util.item;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.item.base.*;
import net.goo.brutality.util.BrutalityTags;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
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
        return getCategory(stack.getItem());
    }
    public static BrutalityCategories getCategory(Item item) {
        // 1. Check Magic Items First (Specialized classes)
        if (item instanceof BrutalityMagicItem magicItem) {
            return magicItem.type; // Returns MagicItemType.STAFF, TOME, etc.
        }

        if (isSword(item)) return BrutalityCategories.ItemType.SWORD;
        if (isAxe(item)) return BrutalityCategories.ItemType.AXE;
        if (isHammer(item)) return BrutalityCategories.ItemType.HAMMER;
        if (isScythe(item)) return BrutalityCategories.ItemType.SCYTHE;
        if (isSpear(item)) return BrutalityCategories.ItemType.SPEAR;
        if (isTrident(item)) return BrutalityCategories.ItemType.TRIDENT;
        if (isBow(item)) return BrutalityCategories.ItemType.BOW;
        if (isThrowing(item)) return BrutalityCategories.ItemType.THROWING;
        if (isPickaxe(item)) return BrutalityCategories.ItemType.PICKAXE;

        // 3. Handle Curios and Armor (if applicable)
        if (isArmor(item)) return BrutalityCategories.ItemType.ARMOR;
        if (item instanceof BlockItem) return BrutalityCategories.ItemType.BLOCK;
        if (isCurio(item)) return BrutalityCategories.ItemType.CURIO;
        // 4. Default Fallback
        return BrutalityCategories.ItemType.GENERIC;
    }

    public static boolean isCurio(Item item) {
        return item instanceof ICurioItem || item.builtInRegistryHolder().is(BrutalityTags.Items.ANKLET) || item.builtInRegistryHolder().is(BrutalityTags.Items.BELT) ||
                item.builtInRegistryHolder().is(BrutalityTags.Items.BELT) || item.builtInRegistryHolder().is(BrutalityTags.Items.CHARM) || item.builtInRegistryHolder().is(BrutalityTags.Items.FEET) ||
                item.builtInRegistryHolder().is(BrutalityTags.Items.HANDS) || item.builtInRegistryHolder().is(BrutalityTags.Items.HEAD) || item.builtInRegistryHolder().is(BrutalityTags.Items.HEART) ||
                item.builtInRegistryHolder().is(BrutalityTags.Items.NECKLACE) || item.builtInRegistryHolder().is(BrutalityTags.Items.RING);
    }

    public static boolean isArmor(Item item) {
        return item instanceof ArmorItem && item.builtInRegistryHolder().is(Tags.Items.ARMORS) || item.builtInRegistryHolder().is(ItemTags.TRIMMABLE_ARMOR) ||
                isHelmet(item) || isChestplate(item) || isLeggings(item) || isBoots(item);
    }
    public static boolean isArmor(ItemStack stack) {
        return isArmor(stack.getItem());
    }

    public static boolean isHelmet(Item item) {
        return item.builtInRegistryHolder().is(Tags.Items.ARMORS_HELMETS) ||
                item instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.HELMET;
    }

    public static boolean isChestplate(Item item) {
        return item.builtInRegistryHolder().is(Tags.Items.ARMORS_CHESTPLATES) ||
                item instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.CHESTPLATE;
    }

    public static boolean isLeggings(Item item) {
        return item.builtInRegistryHolder().is(Tags.Items.ARMORS_LEGGINGS) ||
                item instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.LEGGINGS;
    }

    public static boolean isBoots(Item item) {
        return item.builtInRegistryHolder().is(Tags.Items.ARMORS_BOOTS) ||
                item instanceof ArmorItem armorItem && armorItem.getType() == ArmorItem.Type.BOOTS;
    }

    public static boolean isHelmet(ItemStack stack) {
        return isHelmet(stack.getItem());
    }

    public static boolean isChestplate(ItemStack stack) {
        return isChestplate(stack.getItem());
    }

    public static boolean isLeggings(ItemStack stack) {
        return isLeggings(stack.getItem());
    }

    public static boolean isBoots(ItemStack stack) {
        return isBoots(stack.getItem());
    }

    public static boolean isWeapon(Item item) {
        return isAxe(item) || isSword(item) || isScythe(item) || isHammer(item) || isRangedWeapon(item);
    }
    public static boolean isWeapon(ItemStack stack) {
        return isWeapon(stack.getItem());
    }

    public static boolean isRangedWeapon(Item item) {
        return isGun(item) || isBow(item) || isThrowing(item) || isTrident(item);
    }
    public static boolean isRangedWeapon(ItemStack stack) {
        return isRangedWeapon(stack.getItem());
    }

    public static boolean isTool(Item item) {
        return isShovel(item) || isPickaxe(item) || isAxe(item) || isHoe(item) || item.builtInRegistryHolder().is(Tags.Items.TOOLS) || isShear(item);
    }

    public static boolean isTool(ItemStack stack) {
        return isTool(stack.getItem());
    }

    public static boolean isGun(Item item) {
        return GUNS.contains(item);
    }
    public static boolean isGun(ItemStack stack) {
        return isGun(stack.getItem());
    }


    public static boolean isSpearOrTrident(Item item) {
        return isSpear(item) || isTrident(item);
    }
    public static boolean isSpearOrTrident(ItemStack stack) {
        return isSpear(stack.getItem()) || isTrident(stack.getItem());
    }

    public static boolean isSpear(Item item) {
        return item instanceof BrutalitySpearItem;
    }

    public static boolean isTrident(Item item) {
        return item instanceof TridentItem || item.builtInRegistryHolder().is(Tags.Items.TOOLS_TRIDENTS) || item instanceof BrutalityTridentItem || nameHas(item, "trident");
    }
    public static boolean isTrident(ItemStack stack) {
        return isTrident(stack.getItem());
    }

    public static boolean isBow(Item item) {
        return item instanceof BowItem || nameHas(item, "bow") || item.builtInRegistryHolder().is(Tags.Items.TOOLS_BOWS);
    }
    public static boolean isBow(ItemStack stack) {
        return isBow(stack.getItem());
    }

    public static boolean isThrowing(Item item) {
        return item instanceof BrutalityThrowingItem;
    }

    public static boolean isThrowing(ItemStack stack) {
        return isThrowing(stack.getItem());
    }

    public static boolean isScythe(Item item) {
        return item instanceof BrutalityScytheItem || nameHas(item, "scythe");
    }

    public static boolean isScythe(ItemStack stack) {
        return isScythe(stack.getItem());
    }

    public static boolean isHammer(Item item) {
        if (item instanceof BrutalityHammerItem) return true;

        if (item == TerramityModItems.HELLROK_GIGATON_HAMMER.get()) return true;

        return item.builtInRegistryHolder().is(BrutalityTags.Items.HAMMER);
    }

    public static boolean isHammer(ItemStack stack) {
        return isHammer(stack.getItem());
    }

    public static boolean isSword(Item item) {
        return item instanceof SwordItem || item.builtInRegistryHolder().is(ItemTags.SWORDS);
    }

    public static boolean isSword(ItemStack stack) {
        if (isSword(stack.getItem())) return true;
        for (ToolAction DEFAULT_SWORD_ACTION : ToolActions.DEFAULT_SWORD_ACTIONS) {
            if (stack.canPerformAction(DEFAULT_SWORD_ACTION)) return true;
        }
        return false;
    }

    public static boolean isAxe(Item item) {
        return item instanceof AxeItem || item.builtInRegistryHolder().is(ItemTags.AXES);
    }

    public static boolean isAxe(ItemStack stack) {
        if (isAxe(stack.getItem())) return true;

        for (ToolAction toolAction : ToolActions.DEFAULT_AXE_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isPickaxe(Item item) {
        return item instanceof PickaxeItem || item.builtInRegistryHolder().is(ItemTags.PICKAXES);
    }

    public static boolean isPickaxe(ItemStack stack) {
        if (isPickaxe(stack.getItem())) return true;

        for (ToolAction toolAction : ToolActions.DEFAULT_PICKAXE_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isHoe(Item item) {
        return item instanceof HoeItem || item.builtInRegistryHolder().is(ItemTags.HOES);
    }

    public static boolean isHoe(ItemStack stack) {
        if (isHoe(stack.getItem())) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_HOE_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isShovel(Item item) {
        return item instanceof ShovelItem || item.builtInRegistryHolder().is(ItemTags.SHOVELS);
    }

    public static boolean isShovel(ItemStack stack) {
        if (isShovel(stack.getItem())) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_SHOVEL_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isShield(Item item) {
        return item instanceof ShieldItem || item.builtInRegistryHolder().is(Tags.Items.TOOLS_SHIELDS);
    }

    public static boolean isShield(ItemStack stack) {
        if (isShield(stack.getItem())) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_SHIELD_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean isShear(Item item) {
        return item instanceof ShearsItem || item.builtInRegistryHolder().is(Tags.Items.SHEARS);
    }

    public static boolean isShear(ItemStack stack) {
        if (isShear(stack.getItem())) return true;
        for (ToolAction toolAction : ToolActions.DEFAULT_SHEARS_ACTIONS) {
            if (stack.canPerformAction(toolAction)) return true;
        }
        return false;
    }

    public static boolean nameHas(Item item, String text) {
        return item.getDescriptionId().toLowerCase(Locale.ROOT).contains(text);
    }


}
