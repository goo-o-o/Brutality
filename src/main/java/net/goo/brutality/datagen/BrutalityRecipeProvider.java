package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.goo.brutality.registry.BrutalityModItems;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.PartialNBTIngredient;
import net.minecraftforge.common.crafting.StrictNBTIngredient;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;


public class BrutalityRecipeProvider extends RecipeProvider implements IConditionBuilder {


    public BrutalityRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BrutalityModItems.POCKET_BLACK_HOLE.get())
                .requires(TerramityModItems.ETHEREAL_ECLIPSE_EMBLEM.get())
                .requires(TerramityModItems.NEUTRON_STAR.get())
                .unlockedBy(getHasName(TerramityModItems.NEUTRON_STAR.get()), has(TerramityModItems.NEUTRON_STAR.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BrutalityModItems.HIGH_FREQUENCY_ALLOY.get())
                .requires(TerramityModItems.CONDUCTITE.get())
                .requires(TerramityModItems.HELLSPEC_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DULL_KNIFE_DAGGER.get())
                .pattern(" # ")
                .pattern("$%@")
                .pattern(" ^ ")
                .define('#', TerramityModItems.SPECTRAL_SOUL.get())
                .define('$', TerramityModItems.ACCURSED_SOUL.get())
                .define('%', Items.NETHERITE_SWORD)
                .define('@', TerramityModItems.LOST_SOUL.get())
                .define('^', TerramityModItems.SPITEFUL_SOUL.get())
                .unlockedBy(getHasName(Items.NETHERITE_SWORD), has(Items.NETHERITE_SWORD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CANOPY_OF_SHADOWS.get())
                .pattern(" # ")
                .pattern("$%#")
                .pattern("@$ ")
                .define('#', TerramityModItems.DECAYED_BEDROCK_DUST.get())
                .define('$', TerramityModItems.BLACK_MATTER.get())
                .define('%', TerramityModItems.BAND_OF_DRIFTING.get())
                .define('@', Items.IRON_PICKAXE)
                .unlockedBy(getHasName(TerramityModItems.DECAYED_BEDROCK_DUST.get()), has(TerramityModItems.DECAYED_BEDROCK_DUST.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.JACKPOT_HAMMER.get())
                .pattern(" #&")
                .pattern("$%^")
                .pattern("@$ ")
                .define('#', Items.DIAMOND)
                .define('&', TerramityModItems.SLOT_MACHINE.get())
                .define('$', TerramityModItems.FATEFUL_COIN.get())
                .define('%', TerramityModItems.POKER_CHIP_BRACELETS.get())
                .define('^', Items.EMERALD)
                .define('@', Items.GOLDEN_AXE)
                .unlockedBy(getHasName(TerramityModItems.SLOT_MACHINE.get()), has(TerramityModItems.SLOT_MACHINE.get()))
                .save(consumer);

//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TERRA_BLADE_SWORD.get())
//                .pattern(" # ")
//                .pattern("#@#")
//                .pattern(" # ")
//                .define('#', TerramityModItems.VIRENTIUM_ALLOY_INGOT.get())
//                .define('@', TerramityModItems.HERO_SWORD.get())
//                .unlockedBy(getHasName(hero_sword), has(TerramityModItems.HERO_SWORD.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TERRATOMERE_SWORD.get())
//                .pattern("%#%")
//                .pattern("#@#")
//                .pattern("%#%")
//                .define('#', TerramityModItems.VIRENTIUM_ALLOY_INGOT.get())
//                .define('%', TerramityModItems.GAIANITE_CLUSTER.get())
//                .define('@', BrutalityModItems.TERRA_BLADE_SWORD.get())
//                .unlockedBy(getHasName(terra_blade), has(BrutalityModItems.TERRA_BLADE_SWORD.get()))
//                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CREASE_OF_CREATION.get())
                .pattern(" # ")
                .pattern("#@#")
                .pattern(" # ")
                .define('#', TerramityModItems.COSMILITE_INGOT.get())
                .define('@', TerramityModItems.GRAVITY_GAUNTLET.get())
                .unlockedBy(getHasName(TerramityModItems.GRAVITY_GAUNTLET.get()), has(TerramityModItems.GRAVITY_GAUNTLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUPERNOVA.get())
                .pattern(" #%")
                .pattern("#@#")
                .pattern("$# ")
                .define('#', Items.GOLD_BLOCK)
                .define('%', Items.AMETHYST_SHARD)
                .define('@', TerramityModItems.NEUTRON_STAR.get())
                .define('$', TerramityModItems.COSMILITE_SWORD.get())
                .unlockedBy(getHasName(TerramityModItems.NEUTRON_STAR.get()), has(TerramityModItems.NEUTRON_STAR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get())
                .pattern(" #%")
                .pattern("#@#")
                .pattern("$# ")
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('@', Items.TNT)
                .define('%', TerramityModItems.VIRENTIUM_ROCKET.get())
                .define('$', TerramityModItems.HELLROK_GIGATON_HAMMER.get())
                .unlockedBy(getHasName(TerramityModItems.VIRENTIUM_ROCKET.get()), has(TerramityModItems.VIRENTIUM_ROCKET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLADE_OF_THE_RUINED_KING.get())
                .pattern(" #%")
                .pattern("!@#")
                .pattern("$! ")
                .define('#', Items.ECHO_SHARD)
                .define('@', TerramityModItems.WARDEN_SOUL.get())
                .define('%', TerramityModItems.DIMLITE_INGOT.get())
                .define('$', TerramityModItems.DIMLITE_SWORD.get())
                .define('!', TerramityModItems.LOST_SOUL.get())
                .unlockedBy(getHasName(TerramityModItems.WARDEN_SOUL.get()), has(TerramityModItems.WARDEN_SOUL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DARKIN_BLADE_SWORD.get())
                .pattern(" #@")
                .pattern("!@#")
                .pattern("$! ")
                .define('#', TerramityModItems.CHTHONIAN_VOID.get())
                .define('@', Items.BASALT)
                .define('$', TerramityModItems.HELLSPEC_SWORD.get())
                .define('!', TerramityModItems.SPITEFUL_SOUL.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIAN_VOID.get()), has(TerramityModItems.CHTHONIAN_VOID.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DARKIN_SCYTHE.get())
                .pattern("#@@")
                .pattern("!#!")
                .pattern("  $")
                .define('#', TerramityModItems.CHTHONIAN_VOID.get())
                .define('@', Items.BASALT)
                .define('$', TerramityModItems.HELLSPEC_HOE.get())
                .define('!', TerramityModItems.SPITEFUL_SOUL.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIAN_VOID.get()), has(TerramityModItems.CHTHONIAN_VOID.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RHITTA_AXE.get())
                .pattern(" #@")
                .pattern("$@#")
                .pattern("&$ ")
                .define('#', Items.SUNFLOWER)
                .define('@', TerramityModItems.PROFANUM.get())
                .define('$', TerramityModItems.TOPAZ.get())
                .define('&', TerramityModItems.TOPAZ_AXE.get())
                .unlockedBy(getHasName(TerramityModItems.PROFANUM.get()), has(TerramityModItems.PROFANUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THE_CLOUD.get())
                .pattern(" # ")
                .pattern("@$@")
                .pattern(" # ")
                .define('#', TerramityModItems.PRISMATIC_JEWEL.get())
                .define('@', TerramityModItems.IRIDESCENT_SHARD_BLOCK.get())
                .define('$', Items.WHITE_WOOL)
                .unlockedBy(getHasName(TerramityModItems.PRISMATIC_JEWEL.get()), has(TerramityModItems.PRISMATIC_JEWEL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EVENT_HORIZON.get())
                .pattern(" #!")
                .pattern("@$#")
                .pattern("%@ ")
                .define('!', TerramityModItems.BLACK_MATTER.get())
                .define('#', TerramityModItems.VOID_ALLOY.get())
                .define('@', TerramityModItems.PROFANUM.get())
                .define('$', BrutalityModItems.POCKET_BLACK_HOLE.get())
                .define('%', Items.NETHERITE_SHOVEL)
                .unlockedBy(getHasName(TerramityModItems.VOID_ALLOY.get()), has(TerramityModItems.VOID_ALLOY.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FROSTMOURNE_SWORD.get())
                .pattern(" #!")
                .pattern("@$#")
                .pattern("%@ ")
                .define('!', TerramityModItems.SAPPHIRE.get())
                .define('#', TerramityModItems.DAEMONIUM.get())
                .define('$', TerramityModItems.SPECTRAL_SOUL.get())
                .define('@', Items.BLUE_ICE)
                .define('%', Items.NETHERITE_SWORD)
                .unlockedBy(getHasName(Items.BLUE_ICE), has(Items.BLUE_ICE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GUNGNIR_TRIDENT.get())
                .pattern(" #!")
                .pattern(" $#")
                .pattern("%  ")
                .define('!', TerramityModItems.UNHOLY_LANCE.get())
                .define('#', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('$', TerramityModItems.ARCHANGEL_HALO.get())
                .define('%', TerramityModItems.STRATUS_STORM_RULER.get())
                .unlockedBy(getHasName(TerramityModItems.STRATUS_STORM_RULER.get()), has(TerramityModItems.STRATUS_STORM_RULER.get()))
                .save(consumer);

//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MURASAMA_SWORD.get())
//                .pattern(" ##")
//                .pattern("#$#")
//                .pattern("%# ")
//                .define('#', BrutalityModItems.HIGH_FREQUENCY_ALLOY.get())
//                .define('$', TerramityModItems.HELLSPEC_SWORD.get())
//                .define('%', TerramityModItems.MURASAMA.get())
//                .unlockedBy(getHasName(murasama), has(TerramityModItems.MURASAMA.get()))
//                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PROVIDENCE.get())
                .pattern("%@#")
                .pattern("@$!")
                .pattern("#! ")
                .define('#', TerramityModItems.ANGEL_FEATHER.get())
                .define('!', TerramityModItems.GILDED_FEATHER.get())
                .define('$', Items.BOW)
                .define('@', TerramityModItems.TOPAZ.get())
                .define('%', TerramityModItems.ARCHANGEL_HALO.get())
                .unlockedBy(getHasName(TerramityModItems.ARCHANGEL_HALO.get()), has(TerramityModItems.ARCHANGEL_HALO.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SEVENTH_STAR.get())
                .pattern(" @#")
                .pattern("@$!")
                .pattern("%! ")
                .define('#', TerramityModItems.COSMILITE_SWORD.get())
                .define('!', Items.AMETHYST_SHARD)
                .define('$', Items.NETHER_STAR)
                .define('@', TerramityModItems.TOPAZ.get())
                .define('%', TerramityModItems.GOLD_NECKLACE.get())
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DOUBLE_DOWN.get())
                .pattern(" @*")
                .pattern("#$!")
                .pattern("&% ")
                .define('@', Items.DIAMOND_BLOCK)
                .define('*', Items.GOLD_BLOCK)
                .define('#', Items.EMERALD_BLOCK)
                .define('$', TerramityModItems.SLOT_MACHINE.get())
                .define('%', Items.REDSTONE_BLOCK)
                .define('!', Items.LAPIS_BLOCK)
                .define('&', TerramityModItems.STRATUS_STORM_RULER.get())
                .unlockedBy(getHasName(TerramityModItems.SLOT_MACHINE.get()), has(TerramityModItems.SLOT_MACHINE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.OLD_GPU.get())
                .pattern("!@#")
                .pattern(" @#")
                .pattern("$@#")
                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('!', Items.COPPER_INGOT)
                .define('$', Items.GOLD_INGOT)
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PAPER_CUT.get())
                .pattern("#")
                .pattern("#")
                .pattern("#")
                .define('#', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WHISPERWALTZ.get())
                .pattern(" @#")
                .pattern("@#@")
                .pattern("$@ ")
                .define('#', TerramityModItems.REVERIUM.get())
                .define('@', TerramityModItems.IRIDESCENT_SHARD.get())
                .define('$', TerramityModItems.REVERIUM_SWORD.get())
                .unlockedBy(getHasName(TerramityModItems.REVERIUM.get()), has(TerramityModItems.REVERIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SPATULA_HAMMER.get())
                .pattern(" ##")
                .pattern(" ##")
                .pattern("!  ")
                .define('#', Items.IRON_INGOT)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THE_GOLDEN_SPATULA_HAMMER.get())
                .pattern(" #!")
                .pattern("$@#")
                .pattern("&$ ")
                .define('#', TerramityModItems.TOPAZ_BLOCK.get())
                .define('!', TerramityModItems.TOPAZ.get())
                .define('@', TerramityModItems.PROFANUM.get())
                .define('$', Items.GOLD_INGOT)
                .define('&', TerramityModItems.TOPAZ_SHOVEL.get())
                .unlockedBy(getHasName(TerramityModItems.PROFANUM.get()), has(TerramityModItems.PROFANUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.IRON_KNIFE.get())
                .pattern("  #")
                .pattern(" @ ")
                .pattern("!  ")
                .define('#', Items.IRON_INGOT)
                .define('@', Items.IRON_BLOCK)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GOLD_KNIFE.get())
                .pattern("  #")
                .pattern(" @ ")
                .pattern("!  ")
                .define('#', Items.GOLD_INGOT)
                .define('@', Items.GOLD_BLOCK)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DIAMOND_KNIFE.get())
                .pattern("  #")
                .pattern(" @ ")
                .pattern("!  ")
                .define('#', Items.DIAMOND)
                .define('@', Items.DIAMOND_BLOCK)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VOID_KNIFE.get())
                .pattern(" ##")
                .pattern("###")
                .pattern("!# ")
                .define('#', TerramityModItems.VOID_ALLOY.get())
                .define('!', BrutalityModItems.DIAMOND_KNIFE.get())
                .unlockedBy(getHasName(TerramityModItems.VOID_ALLOY.get()), has(TerramityModItems.VOID_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FRYING_PAN.get())
                .pattern(" # ")
                .pattern("#@#")
                .pattern("!# ")
                .define('#', Items.IRON_INGOT)
                .define('@', Items.IRON_BLOCK)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WHISK_HAMMER.get())
                .pattern(" ##")
                .pattern(" @#")
                .pattern("!  ")
                .define('#', Items.IRON_BARS)
                .define('@', TerramityModItems.GRATE.get())
                .define('!', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.POTATO_MASHER_HAMMER.get())
                .pattern(" #@")
                .pattern(" ##")
                .pattern("!  ")
                .define('#', Items.IRON_BARS)
                .define('@', Items.IRON_TRAPDOOR)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SALT_SHAKER_CHARM.get())
                .pattern(" # ")
                .pattern("!@!")
                .pattern("!!!")
                .define('#', Items.IRON_TRAPDOOR)
                .define('@', TerramityModItems.FAIRY_DUST.get())
                .define('!', Items.GLASS)
                .unlockedBy(getHasName(TerramityModItems.FAIRY_DUST.get()), has(TerramityModItems.FAIRY_DUST.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PEPPER_SHAKER_CHARM.get())
                .pattern(" # ")
                .pattern("!@!")
                .pattern("!!!")
                .define('#', Items.IRON_TRAPDOOR)
                .define('@', TerramityModItems.DECAYED_BEDROCK_DUST.get())
                .define('!', Items.GLASS)
                .unlockedBy(getHasName(TerramityModItems.DECAYED_BEDROCK_DUST.get()), has(TerramityModItems.DECAYED_BEDROCK_DUST.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.SALT_AND_PEPPER_CHARM.get())
                .requires(BrutalityModItems.SALT_SHAKER_CHARM.get())
                .requires(BrutalityModItems.PEPPER_SHAKER_CHARM.get())
                .unlockedBy(getHasName(BrutalityModItems.PEPPER_SHAKER_CHARM.get()), has(BrutalityModItems.PEPPER_SHAKER_CHARM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MORTAR_AND_PESTLE_CHARM.get())
                .pattern(" # ")
                .pattern("!@!")
                .pattern("!!!")
                .define('#', Items.NETHERITE_SHOVEL)
                .define('!', TerramityModItems.DAEMONIUM_BRICKS.get())
                .define('@', Items.BOWL)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BUTTER_GAUNTLETS.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("!!!")
                .define('#', Items.GOLD_INGOT)
                .define('!', TerramityModItems.TOPAZ.get())
                .define('@', TerramityModItems.HELLROK_GAUNTLET.get())
                .unlockedBy(getHasName(TerramityModItems.HELLROK_GAUNTLET.get()), has(TerramityModItems.HELLROK_GAUNTLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CHEESE_SAUCE_CHARM.get())
                .pattern(" # ")
                .pattern("!@!")
                .pattern("!$!")
                .define('#', Items.IRON_TRAPDOOR)
                .define('!', Items.YELLOW_CONCRETE)
                .define('$', Items.MILK_BUCKET)
                .define('@', Items.HONEY_BOTTLE)
                .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TOMATO_SAUCE_CHARM.get())
                .pattern(" # ")
                .pattern("!@!")
                .pattern("!$!")
                .define('#', Items.IRON_TRAPDOOR)
                .define('!', Items.RED_CONCRETE)
                .define('$', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
                .define('@', Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(TerramityModItems.STYGIAN_BLOOD_BUCKET.get()), has(TerramityModItems.STYGIAN_BLOOD_BUCKET.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.PIZZA_SLOP_CHARM.get())
                .requires(BrutalityModItems.CHEESE_SAUCE_CHARM.get())
                .requires(BrutalityModItems.TOMATO_SAUCE_CHARM.get())
                .unlockedBy(getHasName(BrutalityModItems.TOMATO_SAUCE_CHARM.get()), has(BrutalityModItems.TOMATO_SAUCE_CHARM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HOT_SAUCE_CHARM.get())
                .pattern(" # ")
                .pattern("!@!")
                .pattern("!$!")
                .define('#', Items.IRON_TRAPDOOR)
                .define('!', Items.ORANGE_CONCRETE)
                .define('$', Items.LAVA_BUCKET)
                .define('@', Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(Items.LAVA_BUCKET), has(Items.LAVA_BUCKET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.OLIVE_OIL_CHARM.get())
                .pattern(" # ")
                .pattern("#@#")
                .pattern(" # ")
                .define('#', Items.ACACIA_SAPLING)
                .define('@', TerramityModItems.EXTREMELY_THICK_OIL.get())
                .unlockedBy(getHasName(TerramityModItems.EXTREMELY_THICK_OIL.get()), has(TerramityModItems.EXTREMELY_THICK_OIL.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EXTRA_VIRGIN_OLIVE_OIL_CHARM.get())
                .pattern("$#$")
                .pattern("#@#")
                .pattern("$#$")
                .define('#', Items.ACACIA_SAPLING)
                .define('$', TerramityModItems.HERMIT_BEAN.get())
                .define('@', BrutalityModItems.OLIVE_OIL_CHARM.get())
                .unlockedBy(getHasName(BrutalityModItems.OLIVE_OIL_CHARM.get()), has(BrutalityModItems.OLIVE_OIL_CHARM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FRIDGE_CHARM.get())
                .pattern("##@")
                .pattern("##@")
                .pattern("##@")
                .define('#', Items.IRON_BLOCK)
                .define('@', Items.IRON_DOOR)
                .unlockedBy(getHasName(Items.IRON_BLOCK), has(Items.IRON_BLOCK))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SMART_FRIDGE_CHARM.get())
                .pattern("#!^")
                .pattern("#$ ")
                .pattern("#!@")
                .define('#', TerramityModItems.REINFORCED_BLACKSTONE_BRICKS.get())
                .define('^', Items.LAPIS_LAZULI)
                .define('@', Items.REDSTONE)
                .define('!', Items.NETHERITE_INGOT)
                .define('$', BrutalityModItems.FRIDGE_CHARM.get())
                .unlockedBy(getHasName(BrutalityModItems.FRIDGE_CHARM.get()), has(BrutalityModItems.FRIDGE_CHARM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SCIENTIFIC_CALCULATOR.get())
                .pattern("!#^")
                .pattern("#$#")
                .pattern("&#@")
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('^', Items.EMERALD)
                .define('@', TerramityModItems.SAPPHIRE.get())
                .define('!', TerramityModItems.TOPAZ.get())
                .define('&', TerramityModItems.RUBY.get())
                .define('$', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ADDITION_CHARM.get())
                .pattern(" # ")
                .pattern("#@#")
                .pattern(" # ")
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUBTRACTION_CHARM.get())
                .pattern("#@#")
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MULTIPLICATION_CHARM.get())
                .pattern("# #")
                .pattern(" @ ")
                .pattern("# #")
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DIVISION_CHARM.get())
                .pattern(" @ ")
                .pattern("###")
                .pattern(" @ ")
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PI_CHARM.get())
                .pattern("@@@")
                .pattern("@#@")
                .pattern("!!!")
                .define('@', TerramityModItems.CONDUCTITE.get())
                .define('!', Items.PUMPKIN_PIE)
                .define('#', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EXPONENTIAL_CHARM.get())
                .pattern(" !@")
                .pattern("#  ")
                .pattern("@  ")
                .define('@', TerramityModItems.CONDUCTITE.get())
                .define('!', TerramityModItems.TERAWATT_BRACERS.get())
                .define('#', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SINE_CHARM.get())
                .pattern(" !@")
                .pattern("#  ")
                .pattern("@  ")
                .define('@', TerramityModItems.CONDUCTITE.get())
                .define('#', TerramityModItems.SAPPHIRE_BLOCK.get())
                .define('!', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.COSINE_CHARM.get())
                .pattern("@! ")
                .pattern("  #")
                .pattern("  @")
                .define('@', TerramityModItems.CONDUCTITE.get())
                .define('#', TerramityModItems.RUBY_BLOCK.get())
                .define('!', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WOODEN_RULER.get())
                .pattern("  #")
                .pattern(" ! ")
                .pattern("@  ")
                .define('@', Items.WOODEN_SWORD)
                .define('#', TerramityModItems.CARDBOARD.get())
                .define('!', Items.STRIPPED_OAK_LOG)
                .unlockedBy(getHasName(TerramityModItems.CARDBOARD.get()), has(TerramityModItems.CARDBOARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.METAL_RULER.get())
                .pattern("  #")
                .pattern(" ! ")
                .pattern("@  ")
                .define('@', Items.IRON_SWORD)
                .define('#', TerramityModItems.DAEMONIUM.get())
                .define('!', Items.IRON_INGOT)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PORTABLE_MINING_RIG.get())
                .pattern("#$#")
                .pattern("@!@")
                .pattern("#$#")
                .define('@', TerramityModItems.DAEMONIUM.get())
                .define('#', Items.POLISHED_BLACKSTONE)
                .define('$', TerramityModItems.CONDUCTITE.get())
                .define('!', TerramityModItems.FATEFUL_COIN.get())
                .unlockedBy(getHasName(TerramityModItems.FATEFUL_COIN.get()), has(TerramityModItems.FATEFUL_COIN.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CRYPTO_WALLET_CHARM.get())
                .pattern("#@#")
                .pattern("@!@")
                .pattern("#@#")
                .define('@', TerramityModItems.TOPAZ.get())
                .define('#', Items.GOLD_BLOCK)
                .define('!', TerramityModItems.FATEFUL_COIN.get())
                .unlockedBy(getHasName(TerramityModItems.FATEFUL_COIN.get()), has(TerramityModItems.FATEFUL_COIN.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ROAD_RUNNERS_RING.get())
                .pattern("#@#")
                .pattern("@!@")
                .pattern("#@#")
                .define('@', TerramityModItems.RUSH_RING_PLUS.get())
                .define('#', TerramityModItems.SAPPHIRE.get())
                .define('!', TerramityModItems.GILDED_FEATHER.get())
                .unlockedBy(getHasName(TerramityModItems.RUSH_RING_PLUS.get()), has(TerramityModItems.RUSH_RING_PLUS.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RESPLENDENT_FEATHER_CHARM.get())
                .pattern(" @ ")
                .pattern("#!#")
                .pattern(" @ ")
                .define('@', TerramityModItems.PROFANUM.get())
                .define('#', Items.LAVA_BUCKET)
                .define('!', TerramityModItems.ANGEL_FEATHER.get())
                .unlockedBy(getHasName(TerramityModItems.PROFANUM.get()), has(TerramityModItems.PROFANUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NANOMACHINES.get())
                .pattern("#@#")
                .pattern("@!@")
                .pattern("#@#")
                .define('@', TerramityModItems.BLACK_MATTER.get())
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('!', Items.BLACK_DYE)
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ABYSSAL_NECKLACE.get())
                .pattern("#@#")
                .pattern("@!@")
                .pattern(" % ")
                .define('#', TerramityModItems.ONYX.get())
                .define('@', Items.KELP)
                .define('!', TerramityModItems.GOLD_NECKLACE.get())
                .define('%', Items.HEART_OF_THE_SEA)
                .unlockedBy(getHasName(Items.HEART_OF_THE_SEA), has(Items.HEART_OF_THE_SEA))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.LAST_PRISM_ITEM.get())
                .pattern(" @ ")
                .pattern("#!#")
                .pattern("#%#")
                .define('#', TerramityModItems.NYXIUM_GLASS.get())
                .define('@', TerramityModItems.PRISMATIC_CRYSTAL_CLUSTER.get())
                .define('!', TerramityModItems.IRIDESCENT_SHARD.get())
                .define('%', TerramityModItems.PRISMATIC_JEWEL.get())
                .unlockedBy(getHasName(TerramityModItems.NYXIUM_GLASS.get()), has(TerramityModItems.NYXIUM_GLASS.get()))
                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EXOBLADE_SWORD.get())
//                .pattern(" @@")
//                .pattern("#!@")
//                .pattern("^# ")
//                .define('#', TerramityModItems.EXODIUM_SUPERALLOY.get())
//                .define('@', TerramityModItems.PRISMATIC_JEWEL.get())
//                .define('!', BrutalityModItems.TERRATOMERE_SWORD.get())
//                .define('^', TerramityModItems.NYXIUM_GREATSWORD.get())
//                .unlockedBy(getHasName(terratomere), has(BrutalityModItems.TERRATOMERE_SWORD.get()))
//                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BIOMECH_REACTOR.get())
                .pattern("#@#")
                .pattern("@%@")
                .pattern("#@#")
                .define('@', TerramityModItems.NYXIUM.get())
                .define('#', TerramityModItems.VIRENTIUM_ALLOY_INGOT.get())
                .define('%', TerramityModItems.PRISMATIC_JEWEL.get())
                .unlockedBy(getHasName(TerramityModItems.NYXIUM.get()), has(TerramityModItems.NYXIUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THUNDERBOLT_TRIDENT.get())
                .pattern("#@#")
                .pattern("@%@")
                .pattern("#@#")
                .define('@', TerramityModItems.TOPAZ.get())
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('%', Items.TRIDENT)
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE.get()), has(TerramityModItems.CONDUCTITE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get())
                .pattern(" ^#")
                .pattern("%@^")
                .pattern("!% ")
                .define('%', Items.KELP)
                .define('@', Items.TRIDENT)
                .define('#', Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)
                .define('!', Items.NETHERITE_AXE)
                .define('^', Items.ROTTEN_FLESH)
                .unlockedBy(getHasName(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE), has(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SECOND_HEART.get())
                .pattern("%@^")
                .define('%', TerramityModItems.ANALEPTIC_AMPHORA.get())
                .define('@', TerramityModItems.CRYSTAL_HEART.get())
                .define('^', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
                .unlockedBy(getHasName(TerramityModItems.CRYSTAL_HEART.get()), has(TerramityModItems.CRYSTAL_HEART.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HEART_OF_GOLD.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("###")
                .define('@', TerramityModItems.CRYSTAL_HEART.get())
                .define('#', Items.GOLD_BLOCK)
                .unlockedBy(getHasName(BrutalityModItems.SECOND_HEART.get()), has(BrutalityModItems.SECOND_HEART.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FROZEN_HEART.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("###")
                .define('@', TerramityModItems.CRYSTAL_HEART.get())
                .define('#', Items.BLUE_ICE)
                .unlockedBy(getHasName(BrutalityModItems.SECOND_HEART.get()), has(BrutalityModItems.SECOND_HEART.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ROYAL_GUARDIAN_SWORD.get())
                .pattern("  #")
                .pattern("^@ ")
                .pattern("!^ ")
                .define('@', TerramityModItems.EXODIUM_SWORD.get())
                .define('#', TerramityModItems.NYXIUM_GREATSWORD.get())
                .define('!', TerramityModItems.REVERIUM_SWORD.get())
                .define('^', TerramityModItems.DECAYED_BEDROCK.get())
                .unlockedBy(getHasName(TerramityModItems.EXODIUM_SWORD.get()), has(TerramityModItems.EXODIUM_SWORD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_HELMET.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("$$$")
                .define('@', TerramityModItems.BLACK_MATTER.get())
                .define('#', Items.LEATHER_HELMET)
                .define('$', Items.BLACK_DYE)
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_CHESTPLATE.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("$$$")
                .define('@', TerramityModItems.BLACK_MATTER.get())
                .define('#', Items.LEATHER_CHESTPLATE)
                .define('$', Items.BLACK_DYE)
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_LEGGINGS.get())
                .pattern("#!#")
                .pattern("#@#")
                .pattern("$$$")
                .define('@', TerramityModItems.BLACK_MATTER.get())
                .define('#', Items.LEATHER_LEGGINGS)
                .define('$', Items.BLACK_DYE)
                .define('!', TerramityModItems.SHINOBI_SASH.get())
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_BOOTS.get())
                .pattern("###")
                .pattern("%@%")
                .pattern("$$$")
                .define('@', TerramityModItems.BLACK_MATTER.get())
                .define('#', Items.LEATHER_BOOTS)
                .define('%', Items.BLACK_DYE)
                .define('$', Items.BLACK_WOOL)
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SHADOWSTEP.get())
                .pattern("#@#")
                .pattern("@$@")
                .pattern("#@#")
                .define('@', TerramityModItems.BLACK_MATTER.get())
                .define('#', Items.ENDER_PEARL)
                .define('$', Items.NETHERITE_SWORD)
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.APPLE_CORE_LANCE.get())
                .pattern(" @#")
                .pattern("##@")
                .pattern("!# ")
                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('#', Items.APPLE)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MELONCHOLY_SWORD.get())
                .pattern(" @#")
                .pattern("&#@")
                .pattern("!& ")
                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('#', Items.MELON)
                .define('&', Items.MELON_SLICE)
                .define('!', Items.STICK)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PLUNDER_CHEST_CHARM.get())
                .pattern("#@#")
                .pattern("&%&")
                .pattern("!!!")
                .define('@', TerramityModItems.GILDED_FEATHER.get())
                .define('#', TerramityModItems.TOPAZ.get())
                .define('%', Items.CHEST)
                .define('&', Items.GOLD_INGOT)
                .define('!', Items.GOLD_BLOCK)
                .unlockedBy(getHasName(TerramityModItems.GILDED_FEATHER.get()), has(TerramityModItems.GILDED_FEATHER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CHOPSTICK_STAFF.get())
                .pattern("# #")
                .pattern(" @ ")
                .pattern("# #")
                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('#', Items.STICK)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BAMBOO_STAFF.get())
                .pattern(" @#")
                .pattern("@#@")
                .pattern("#@ ")
                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('#', Items.BAMBOO)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SERAPHIM_HALO.get())
                .pattern("#@#")
                .pattern("@$@")
                .pattern("#@#")
                .define('#', Items.GLOWSTONE)
                .define('@', TerramityModItems.TOPAZ.get())
                .define('$', TerramityModItems.ARCHANGEL_HALO.get())
                .unlockedBy(getHasName(TerramityModItems.ARCHANGEL_HALO.get()), has(TerramityModItems.ARCHANGEL_HALO.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BrutalityModItems.CELESTIAL_STARBOARD.get())
                .pattern("#@#")
                .pattern("@$@")
                .pattern("#@#")
                .define('@', TerramityModItems.REVERIUM.get())
                .define('#', TerramityModItems.TOPAZ.get())
                .define('$', Items.NETHER_STAR)
                .unlockedBy(getHasName(Items.NETHER_STAR), has(Items.NETHER_STAR))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WOOLY_BLINDFOLD.get())
                .pattern("###")
                .define('#', Items.BLACK_WOOL)
                .unlockedBy(getHasName(Items.BLACK_WOOL), has(Items.BLACK_WOOL))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GOLDEN_HEADBAND.get())
                .pattern("#@#")
                .pattern(" % ")
                .define('#', TerramityModItems.TOPAZ.get())
                .define('@', TerramityModItems.ARCHANGEL_HALO.get())
                .define('%', Items.GOLDEN_HELMET)
                .unlockedBy(getHasName(TerramityModItems.ARCHANGEL_HALO.get()), has(TerramityModItems.ARCHANGEL_HALO.get()))
                .save(consumer);

//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MARIANAS_TRENCH.get())
//                .pattern("  #")
//                .pattern("!@#")
//                .pattern("$  ")
//                .define('#', TerramityModItems.DIMLITE_INGOT.get())
//                .define('@', Items.HEART_OF_THE_SEA)
//                .define('!', Items.NAUTILUS_SHELL)
//                .define('$', BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get())
//                .unlockedBy(getHasName(depth_crusher), has(BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CHALLENGER_DEEP.get())
//                .pattern("  #")
//                .pattern("%# ")
//                .pattern("$% ")
//                .define('#', TerramityModItems.EXODIUM_SUPERALLOY.get())
//                .define('%', Items.ENDER_EYE)
//                .define('$', BrutalityModItems.MARIANAS_TRENCH.get())
//                .unlockedBy(getHasName(marianas_trench), has(BrutalityModItems.MARIANAS_TRENCH.get()))
//                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PRIDE_CHARM.get())
                .pattern("#@#")
                .pattern("@%@")
                .pattern("#@#")
                .define('#', TerramityModItems.SAPPHIRE_BLOCK.get())
                .define('%', Items.ENDER_EYE)
                .define('@', Items.NETHERITE_SCRAP)
                .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SLOTH_CHARM.get())
                .pattern("###")
                .pattern("#%#")
                .pattern("###")
                .define('#', Items.BLACKSTONE)
                .define('%', TerramityModItems.DAEMONIUM_BLOCK.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ENVY_CHARM.get())
                .pattern("#@#")
                .pattern("@ @")
                .pattern("#@#")
                .define('#', Items.BLACKSTONE)
                .define('@', Items.CRYING_OBSIDIAN)
                .unlockedBy(getHasName(Items.CRYING_OBSIDIAN), has(Items.CRYING_OBSIDIAN))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.LUST_CHARM.get())
                .pattern("#@#")
                .pattern("@$@")
                .pattern("#@#")
                .define('#', Items.BLACKSTONE)
                .define('@', TerramityModItems.RUBY.get())
                .define('$', TerramityModItems.CRYSTAL_HEART.get())
                .unlockedBy(getHasName(TerramityModItems.CRYSTAL_HEART.get()), has(TerramityModItems.CRYSTAL_HEART.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GLUTTONY_CHARM.get())
                .pattern("#@#")
                .pattern("@$@")
                .pattern("#@#")
                .define('#', Items.BLACKSTONE)
                .define('@', Items.EMERALD)
                .define('$', TerramityModItems.VIRENTIUM_ALLOY_INGOT.get())
                .unlockedBy(getHasName(TerramityModItems.VIRENTIUM_ALLOY_INGOT.get()), has(TerramityModItems.VIRENTIUM_ALLOY_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GREED_CHARM.get())
                .pattern("#@#")
                .pattern("@$@")
                .pattern("#@#")
                .define('#', Items.BLACKSTONE)
                .define('@', Items.NETHERITE_SCRAP)
                .define('$', TerramityModItems.FATEFUL_COIN.get())
                .unlockedBy(getHasName(TerramityModItems.FATEFUL_COIN.get()), has(TerramityModItems.FATEFUL_COIN.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BRAIN_ROT.get())
                .pattern("#@#")
                .pattern("@$@")
                .pattern("#@#")
                .define('#', Items.ROTTEN_FLESH)
                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('$', Items.ZOMBIE_HEAD)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GOOD_BOOK.get())
                .pattern(" @ ")
                .pattern("@$@")
                .pattern(" @ ")
                .define('@', TerramityModItems.REVERIUM.get())
                .define('$', TerramityModItems.TERRAMITY_GUIDEBOOK_ICON.get())
                .unlockedBy(getHasName(TerramityModItems.REVERIUM.get()), has(TerramityModItems.REVERIUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.YATA_NO_KAGAMI.get())
                .pattern(" @!")
                .pattern("@$@")
                .pattern("!@ ")
                .define('@', Items.OXIDIZED_COPPER)
                .define('$', TerramityModItems.DAEMONIUM_GLASS.get())
                .define('!', TerramityModItems.GAIANITE_CLUSTER.get())
                .unlockedBy(getHasName(TerramityModItems.GAIANITE_CLUSTER.get()), has(TerramityModItems.GAIANITE_CLUSTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DUELING_GLOVE.get())
                .pattern(" @ ")
                .pattern("!$!")
                .pattern(" # ")
                .define('@', TerramityModItems.SAPPHIRE.get())
                .define('#', TerramityModItems.DIAMOND_GAUNTLET.get())
                .define('$', TerramityModItems.DIMLITE_INGOT.get())
                .define('!', Items.REDSTONE)
                .unlockedBy(getHasName(TerramityModItems.DIMLITE_INGOT.get()), has(TerramityModItems.DIMLITE_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CARTON_OF_PRISM_SOLUTION_MILK.get())
                .pattern(" @ ")
                .pattern("@$@")
                .pattern(" @ ")
                .define('@', TerramityModItems.CARDBOARD.get())
                .define('$', TerramityModItems.PRISM_SOLUTION_BUCKET.get())
                .unlockedBy(getHasName(TerramityModItems.PRISM_SOLUTION_BUCKET.get()), has(TerramityModItems.PRISM_SOLUTION_BUCKET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SOLAR_SYSTEM.get())
                .pattern("123")
                .pattern("456")
                .pattern("789")
                .define('1', Items.GRANITE)
                .define('2', Items.DIORITE)
                .define('3', Items.SAND)
                .define('4', Items.MAGMA_BLOCK)
                .define('5', Items.STONE)
                .define('6', Items.MOSS_BLOCK)
                .define('7', Items.RED_SAND)
                .define('8', TerramityModItems.IGNEOSTONE.get())
                .define('9', Items.BLUE_ICE)
                .unlockedBy(getHasName(TerramityModItems.IGNEOSTONE.get()), has(TerramityModItems.IGNEOSTONE.get()))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BrutalityModItems.UNBRIDLED_RAGE.get(), 2)
                .requires(TerramityModItems.BLACK_MATTER.get())
                .requires(TerramityModItems.SPITEFUL_SOUL.get())
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MECHANICAL_AORTA.get())
                .pattern("#@#")
                .pattern("123")
                .pattern("$$$")
                .define('1', Items.REPEATER)
                .define('2', Items.PISTON)
                .define('3', TerramityModItems.BATTERY.get())
                .define('#', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('@', TerramityModItems.RED_WARP_PIPE.get())
                .define('$', BrutalityModItems.UNBRIDLED_RAGE.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOOD_PULSE_GAUNTLETS.get())
                .pattern("#@#")
                .pattern("!%!")
                .pattern(" @ ")
                .define('#', TerramityModItems.SPITEFUL_SOUL.get())
                .define('@', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('%', TerramityModItems.DIAMOND_GAUNTLET.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FURY_BAND.get())
                .pattern(" @ ")
                .pattern("!%!")
                .pattern(" @ ")
                .define('@', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('%', TerramityModItems.EMPTY_RING.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GRUDGE_TOTEM.get())
                .pattern(" ! ")
                .pattern("!%!")
                .pattern(" ! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('%', Items.TOTEM_OF_UNDYING)
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOOD_STONE.get())
                .pattern("@!@")
                .pattern("!%!")
                .pattern("@!@")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('@', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('%', TerramityModItems.MAGMA_STONE.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WRATH_CHARM.get())
                .pattern("!!!")
                .pattern("!%!")
                .pattern("!!!")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('%', TerramityModItems.CHTHONIC_CURSEMARK.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ANGER_MANAGEMENT.get())
                .pattern(" @ ")
                .pattern("!%!")
                .pattern("!!!")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('%', TerramityModItems.TERRAMITY_GUIDEBOOK_ICON.get())
                .define('@', Items.SUNFLOWER)
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BATTLE_SCARS.get())
                .pattern(" !@")
                .pattern("!@!")
                .pattern("@! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('@', Items.IRON_SWORD)
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RAGE_STONE.get())
                .pattern("@#@")
                .pattern("!%!")
                .pattern("@#@")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('@', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('%', TerramityModItems.MAGMA_STONE.get())
                .define('#', TerramityModItems.BLACK_MATTER.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PAIN_CATALYST.get())
                .pattern(" ! ")
                .pattern("!@!")
                .pattern(" ! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('@', Items.SCULK_CATALYST)
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RAMPAGE_CLOCK.get())
                .pattern(" ! ")
                .pattern("!@!")
                .pattern(" ! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('@', Items.CLOCK)
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOOD_HOWL_PENDANT.get())
                .pattern(" ! ")
                .pattern("!@!")
                .pattern(" ! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('@', TerramityModItems.GOLD_MEDAL.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SPITE_SHARD.get())
                .pattern("!@!")
                .pattern("@#@")
                .pattern("!@!")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('@', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('#', TerramityModItems.PRISMATIC_CRYSTAL_CLUSTER.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HATE_SIGIL.get())
                .pattern(" ! ")
                .pattern("!#!")
                .pattern(" ! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('#', TerramityModItems.CHTHONIC_CURSEMARK.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HEART_OF_WRATH.get())
                .pattern(" ! ")
                .pattern("!#!")
                .pattern(" ! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('#', BrutalityModItems.SECOND_HEART.get())
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EYE_FOR_VIOLENCE.get())
                .pattern(" ! ")
                .pattern("!#!")
                .pattern(" ! ")
                .define('!', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('#', Items.ENDER_EYE)
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUGAR_GLAZE.get())
                .pattern("###")
                .pattern("#@#")
                .pattern("###")
                .define('#', Items.SUGAR)
                .define('@', Items.CAKE)
                .unlockedBy(getHasName(Items.CAKE), has(Items.CAKE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RAINBOW_SPRINKLES.get())
                .pattern("123")
                .pattern("456")
                .pattern(" @ ")
                .define('1', TerramityModItems.ONYX.get())
                .define('2', Items.AMETHYST_SHARD)
                .define('3', TerramityModItems.TOPAZ.get())
                .define('4', TerramityModItems.RUBY.get())
                .define('5', Items.EMERALD)
                .define('6', TerramityModItems.SAPPHIRE.get())
                .define('@', Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(Items.GLASS_BOTTLE), has(Items.GLASS_BOTTLE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DUNKED_DONUT.get())
                .pattern(" ! ")
                .pattern("1!3")
                .pattern(" ! ")
                .define('1', BrutalityModItems.SUGAR_GLAZE.get())
                .define('!', TerramityModItems.DAEMONIUM.get())
                .define('3', BrutalityModItems.RAINBOW_SPRINKLES.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ROCK_CANDY_RING.get())
                .pattern("1#2")
                .pattern("@!@")
                .pattern("3#4")
                .define('1', TerramityModItems.RUBY.get())
                .define('2', TerramityModItems.SAPPHIRE.get())
                .define('3', TerramityModItems.TOPAZ.get())
                .define('4', TerramityModItems.ONYX.get())
                .define('#', TerramityModItems.PRISMATIC_CRYSTAL_CLUSTER.get())
                .define('@', Items.AMETHYST_CLUSTER)
                .define('!', TerramityModItems.EMPTY_RING.get())
                .unlockedBy(getHasName(TerramityModItems.PRISMATIC_CRYSTAL_CLUSTER.get()), has(TerramityModItems.PRISMATIC_CRYSTAL_CLUSTER.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SEARED_SUGAR_BROOCH.get())
                .pattern("@#@")
                .pattern("#!#")
                .pattern("@#@")
                .define('#', Items.MAGMA_BLOCK)
                .define('@', Items.SUGAR)
                .define('!', TerramityModItems.TOPAZ_PICKAXE.get())
                .unlockedBy(getHasName(TerramityModItems.TOPAZ_PICKAXE.get()), has(TerramityModItems.TOPAZ_PICKAXE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CARAMEL_CRUNCH_MEDALLION.get())
                .pattern(" ! ")
                .pattern("1!3")
                .pattern(" ! ")
                .define('1', BrutalityModItems.SEARED_SUGAR_BROOCH.get())
                .define('!', TerramityModItems.DAEMONIUM.get())
                .define('3', BrutalityModItems.ROCK_CANDY_RING.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.LOLLIPOP_OF_ETERNITY.get())
                .pattern(" ! ")
                .pattern("1!3")
                .pattern(" ! ")
                .define('1', BrutalityModItems.DUNKED_DONUT.get())
                .define('!', TerramityModItems.IRIDESCENT_SHARD.get())
                .define('3', BrutalityModItems.CARAMEL_CRUNCH_MEDALLION.get())
                .unlockedBy(getHasName(TerramityModItems.IRIDESCENT_SHARD.get()), has(TerramityModItems.IRIDESCENT_SHARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SMOKE_STONE.get())
                .pattern(" @ ")
                .pattern("$!$")
                .pattern(" # ")
                .define('@', Items.SMOKER)
                .define('$', Items.FIRE_CHARGE)
                .define('!', TerramityModItems.SHARPENING_STONE.get())
                .define('#', Items.MAGMA_BLOCK)
                .unlockedBy(getHasName(TerramityModItems.SHARPENING_STONE.get()), has(TerramityModItems.SHARPENING_STONE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THE_SMOKEHOUSE.get())
                .pattern("#@#")
                .pattern("#!#")
                .pattern("$$$")
                .define('@', TerramityModItems.PROFANUM.get())
                .define('$', Items.COBBLESTONE)
                .define('!', BrutalityModItems.SMOKE_STONE.get())
                .define('#', Items.OAK_LOG)
                .unlockedBy(getHasName(TerramityModItems.PROFANUM.get()), has(TerramityModItems.PROFANUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BAMBOO_STEAMER.get())
                .pattern("###")
                .pattern("#!#")
                .pattern("#$#")
                .define('$', Items.MAGMA_BLOCK)
                .define('!', Items.WATER_BUCKET)
                .define('#', Items.STRIPPED_BAMBOO_BLOCK)
                .unlockedBy(getHasName(Items.STRIPPED_BAMBOO_BLOCK), has(Items.STRIPPED_BAMBOO_BLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUM_CHARM.get())
                .pattern("###")
                .pattern("@  ")
                .pattern("###")
                .define('#', TerramityModItems.CONDUCTITE.get())
                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.STYGIAN_CHAIN.get())
                .pattern("A")
                .pattern("B")
                .pattern("C")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', TerramityModItems.GOLD_NECKLACE.get())
                .define('C', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PORTABLE_QUANTUM_THINGAMABOB.get())
                .pattern("A")
                .pattern("B")
                .pattern("C")
                .define('A', BrutalityModItems.COSINE_CHARM.get())
                .define('B', TerramityModItems.EMPTY_ESSENCE.get())
                .define('C', BrutalityModItems.SINE_CHARM.get())
                .unlockedBy(getHasName(TerramityModItems.EMPTY_ESSENCE.get()), has(TerramityModItems.EMPTY_SPELL_TOME.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MIRACLE_CURE.get())
                .pattern("A")
                .pattern("B")
                .pattern("C")
                .define('A', Items.MILK_BUCKET)
                .define('B', TerramityModItems.MUSIC_SHEET_OF_CLEANSING.get())
                .define('C', Items.GLASS_BOTTLE)
                .unlockedBy(getHasName(TerramityModItems.MUSIC_SHEET_OF_CLEANSING.get()), has(TerramityModItems.MUSIC_SHEET_OF_CLEANSING.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MICROBLADE_BAND.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('B', TerramityModItems.EMPTY_RING.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.LIGHT_SWITCH.get())
                .pattern("AAB")
                .pattern("A  ")
                .pattern("AAC")
                .define('A', Items.REDSTONE)
                .define('B', Items.REDSTONE_LAMP)
                .define('C', Items.LEVER)
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BrutalityModItems.HYPERBOLIC_FEATHER.get())
                .pattern("A B")
                .pattern(" C ")
                .define('A', TerramityModItems.HELLSPEC_UPGRADE_SMITHING_TEMPLATE.get())
                .define('B', TerramityModItems.HELLSPEC_SHOTSHELLS.get())
                .define('C', Items.FEATHER)
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_SHOTSHELLS.get()), has(TerramityModItems.HELLSPEC_SHOTSHELLS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HELL_SPECS.get())
                .pattern("A A")
                .pattern("BCB")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', TerramityModItems.CONDUCTITE_SCOUTER_HELMET.get())
                .define('C', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GLASS_HEART.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.GLASS)
                .define('B', Items.GLASS_PANE)
                .define('C', TerramityModItems.CRYSTAL_HEART.get())
                .unlockedBy(getHasName(TerramityModItems.CRYSTAL_HEART.get()), has(TerramityModItems.CRYSTAL_HEART.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FUZZY_DICE.get())
                .pattern(" AB")
                .pattern("AAA")
                .pattern("BA ")
                .define('A', Items.RED_WOOL)
                .define('B', TerramityModItems.LUCKY_DICE.get())
                .unlockedBy(getHasName(TerramityModItems.LUCKY_DICE.get()), has(TerramityModItems.LUCKY_DICE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FORBIDDEN_ORB.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', TerramityModItems.VOID_GLASS.get())
                .define('B', TerramityModItems.FORBIDDEN_FRUIT.get())
                .unlockedBy(getHasName(TerramityModItems.VOID_GLASS.get()), has(TerramityModItems.VOID_GLASS.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ESSENTIAL_OILS.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.CHAINMAIL_HELMET)
                .define('B', Items.GLASS_PANE)
                .define('C', TerramityModItems.EXTREMELY_THICK_OIL.get())
                .unlockedBy(getHasName(TerramityModItems.EXTREMELY_THICK_OIL.get()), has(TerramityModItems.EXTREMELY_THICK_OIL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ESCAPE_KEY.get())
                .pattern("A")
                .pattern("B")
                .define('A', Items.QUARTZ)
                .define('B', TerramityModItems.ELECTRONIC_SCRAP.get())
                .unlockedBy(getHasName(TerramityModItems.ELECTRONIC_SCRAP.get()), has(TerramityModItems.ELECTRONIC_SCRAP.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DUMBBELL.get())
                .pattern("A A")
                .pattern("BAB")
                .pattern("A A")
                .define('A', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('B', TerramityModItems.DAEMONIUM.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DIVINE_IMMOLATION.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', TerramityModItems.RUBY.get())
                .define('C', TerramityModItems.REVERIUM.get())
                .unlockedBy(getHasName(TerramityModItems.REVERIUM.get()), has(TerramityModItems.REVERIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CROWN_OF_TYRANNY.get())
                .pattern("AAA")
                .pattern(" B ")
                .define('A', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('B', Items.GOLDEN_HELMET)
                .unlockedBy(getHasName(BrutalityModItems.UNBRIDLED_RAGE.get()), has(BrutalityModItems.UNBRIDLED_RAGE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CHOCOLATE_BAR.get())
                .pattern("A")
                .pattern("B")
                .define('A', TerramityModItems.CARTON_OF_CHOCOLATE_MILK.get())
                .define('B', Tags.Items.INGOTS)
                .unlockedBy(getHasName(TerramityModItems.CARTON_OF_CHOCOLATE_MILK.get()), has(TerramityModItems.CARTON_OF_CHOCOLATE_MILK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BROKEN_HEART.get())
                .pattern("A")
                .pattern("B")
                .define('A', BrutalityModItems.SECOND_HEART.get())
                .define('B', BrutalityModItems.IRON_KNIFE.get())
                .unlockedBy(getHasName(Items.REDSTONE), has(Items.REDSTONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BOX_OF_CHOCOLATES.get())
                .pattern(" A")
                .pattern("AB")
                .define('A', BrutalityModItems.CHOCOLATE_BAR.get())
                .define('B', TerramityModItems.CARDBOARD_BOX.get())
                .unlockedBy(getHasName(BrutalityModItems.CHOCOLATE_BAR.get()), has(BrutalityModItems.CHOCOLATE_BAR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOOD_ORB.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('B', BrutalityModItems.UNBRIDLED_RAGE.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_CRYSTAL.get()), has(TerramityModItems.CHTHONIC_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLACK_HOLE_ORB.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', TerramityModItems.NYXIUM_GLASS.get())
                .define('B', BrutalityModItems.POCKET_BLACK_HOLE.get())
                .unlockedBy(getHasName(BrutalityModItems.POCKET_BLACK_HOLE.get()), has(BrutalityModItems.POCKET_BLACK_HOLE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ARCHMAGES_TRICK.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABD")
                .define('A', TerramityModItems.DIMLITE_INGOT.get())
                .define('B', TerramityModItems.ANTIPRISM.get())
                .define('C', TerramityModItems.FORTUNES_FAVOR.get())
                .define('D', TerramityModItems.DIMLITE_BLOCK.get())
                .unlockedBy(getHasName(TerramityModItems.ANTIPRISM.get()), has(TerramityModItems.ANTIPRISM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BRUTESKIN_BELT.get())
                .pattern(" A ")
                .pattern(" B ")
                .define('A', BrutalityModItems.UVOGRE_HEART.get())
                .define('B', TerramityModItems.SLAM_BELT.get())
                .unlockedBy(getHasName(BrutalityModItems.UVOGRE_HEART.get()), has(BrutalityModItems.UVOGRE_HEART.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MINIATURE_ANCHOR.get())
                .pattern("A B")
                .pattern(" AB")
                .pattern("BBB")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.IRON_BLOCK)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WARPSLICE_SCABBARD.get())
                .pattern("AB ")
                .pattern("BAB")
                .pattern(" BA")
                .define('A', TerramityModItems.ANTIPRISM.get())
                .define('B', BrutalityModItems.QUANTITE_INGOT.get())
                .unlockedBy(getHasName(BrutalityModItems.QUANTITE_INGOT.get()), has(TerramityModItems.DIMLITE_INGOT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLACK_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.BLACK_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLUE_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.BLUE_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GREEN_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.GREEN_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ORANGE_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.ORANGE_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PINK_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.PINK_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PURPLE_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.PURPLE_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RED_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.RED_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CYAN_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.CYAN_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.YELLOW_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.YELLOW_DYE)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BOMB_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', TerramityModItems.THROWING_BOMB.get())
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.COSMIC_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', TerramityModItems.COSMILITE_INGOT.get())
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GLASS_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.GLASS)
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.QUANTITE_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', BrutalityModItems.QUANTITE_INGOT.get())
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BrutalityModItems.QUANTITE_INGOT.get(), 2)
                .requires(TerramityModItems.DIMLITE_INGOT.get())
                .requires(BrutalityModItems.HIGH_FREQUENCY_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.DIMLITE_INGOT.get()), has(TerramityModItems.DIMLITE_INGOT.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VOID_SEAL.get())
                .pattern(" A ")
                .pattern("BCB")
                .pattern(" B ")
                .define('A', Items.HONEYCOMB)
                .define('B', TerramityModItems.VOID_ALLOY.get())
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BROKEN_CLOCK.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("DDD")
                .define('A', TerramityModItems.DIMLITE_NUGGET.get())
                .define('B', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('C', Items.CLOCK)
                .define('D', Items.IRON_INGOT)
                .unlockedBy(getHasName(TerramityModItems.DIMLITE_NUGGET.get()), has(TerramityModItems.DIMLITE_NUGGET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SHATTERED_CLOCK.get())
                .pattern("ABA")
                .pattern("ECE")
                .pattern("DDD")
                .define('A', Items.DIAMOND)
                .define('E', Items.DIAMOND_BLOCK)
                .define('B', TerramityModItems.DIMLITE_INGOT.get())
                .define('C', BrutalityModItems.BROKEN_CLOCK.get())
                .define('D', TerramityModItems.PRISMATIC_JEWEL.get())
                .unlockedBy(getHasName(BrutalityModItems.BROKEN_CLOCK.get()), has(BrutalityModItems.BROKEN_CLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUNDERED_CLOCK.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("CEC")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', BrutalityModItems.RAMPAGE_CLOCK.get())
                .define('C', Items.LAPIS_BLOCK) // TODO: Replace with Cobalt in 1.0
                .define('D', BrutalityModItems.SHATTERED_CLOCK.get())
                .define('E', TerramityModItems.ICEBRAND.get())
                .unlockedBy(getHasName(BrutalityModItems.SHATTERED_CLOCK.get()), has(BrutalityModItems.SHATTERED_CLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THE_CLOCK_OF_FROZEN_TIME.get()) // TODO: Replace with 1.0 Materials
                .pattern("ABC")
                .pattern("DEF")
                .pattern("CBA")
                .define('A', TerramityModItems.REVERIUM_BLOCK.get())
                .define('B', TerramityModItems.EXODIUM_BLOCK.get())
                .define('C', TerramityModItems.NYXIUM_BLOCK.get())
                .define('D', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
                .define('E', BrutalityModItems.TIMEKEEPERS_CLOCK.get())
                .define('F', BrutalityModItems.FORBIDDEN_ORB.get())
                .unlockedBy(getHasName(BrutalityModItems.TIMEKEEPERS_CLOCK.get()), has(BrutalityModItems.TIMEKEEPERS_CLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TIMEKEEPERS_CLOCK.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ADA")
                .define('A', TerramityModItems.REVERIUM.get())
                .define('B', TerramityModItems.ANTIPRISM.get())
                .define('C', BrutalityModItems.SUNDERED_CLOCK.get())
                .define('D', TerramityModItems.GOLD_MEDAL.get())
                .unlockedBy(getHasName(BrutalityModItems.SUNDERED_CLOCK.get()), has(BrutalityModItems.SUNDERED_CLOCK.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.EMPTY_ANKLET.get())
                .requires(TerramityModItems.EMPTY_RING.get())
                .unlockedBy(getHasName(TerramityModItems.EMPTY_RING.get()), has(TerramityModItems.EMPTY_RING.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ANKLE_MONITOR.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern("CDE")
                .define('A', Items.BLACK_CONCRETE)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', Items.REDSTONE)
                .define('D', TerramityModItems.BALL_N_CHAIN.get())
                .define('E', BrutalityModItems.SCIENTIFIC_CALCULATOR.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ANKLENT.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.BLANK_VOID.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ANKLET_OF_THE_IMPRISONED.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.SWORD_OF_THE_IMPRISONED.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BASKETBALL_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', Items.LEATHER)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', Items.SLIME_BALL)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOOD_CLOT_ANKLET.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('B', BrutalityModItems.UNBRIDLED_RAGE.get())
                .define('C', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CONDUCTITE_ANKLET.get())
                .pattern(" AC")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.CONDUCTITE.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.CIRCUIT_BOARD.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.COSMIC_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', TerramityModItems.RAW_COSMILITE.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', Items.GOLD_BLOCK)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DAVYS_ANKLET.get())
                .pattern("CA ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.KELP)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.DECAYED_BEDROCK_DUST.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DEBUG_ANKLET.get())
                .pattern(" C ")
                .pattern("ABA")
                .pattern(" D ")
                .define('C', Items.BLACK_CONCRETE)
                .define('A', Items.MAGENTA_CONCRETE)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('D', TerramityModItems.NULL_SCARF.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DEVILS_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.ANXIETY_AMULET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EMERALD_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.EMERALD)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EXODIUM_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.EXODIUM_SUPERALLOY.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FIERY_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', TerramityModItems.ARTIFICIAL_LAVA_DISPLAY.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.POT_OF_HOT_COALS.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GLADIATORS_ANKLET.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern(" D ")
                .define('A', Items.GOLD_INGOT)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', Items.COPPER_INGOT)
                .define('D', Items.ENCHANTED_GOLDEN_APPLE)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GNOME_KINGS_ANKLET.get())
                .pattern(" A ")
                .pattern("DBD")
                .pattern(" C ")
                .define('A', TerramityModItems.GOBS_GILDED_HAT_HELMET.get())
                .define('B', TerramityModItems.BELT_OF_THE_GNOME_KING.get())
                .define('C', TerramityModItems.GOBS_CLAYMORE.get())
                .define('D', Items.CYAN_CARPET)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GUNDALFS_ANKLET.get())
                .pattern(" C ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.OCCULT_FABRIC.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.GUNDALFS_HAT_HELMET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.IRONCLAD_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', TerramityModItems.IRIDIUM_CHUNK.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', Items.NETHERITE_INGOT)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NYXIUM_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.NYXIUM.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ONYX_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.ONYX.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RUBY_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.RUBY.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SAPPHIRE_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.SAPPHIRE.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TOPAZ_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.TOPAZ.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.REDSTONE_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', Items.REDSTONE)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.SHARPENING_STONE.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.SACRED_SPEED_ANKLET.get())
                .requires(TerramityModItems.SACRED_SPEED_BRACELETS.get())
                .requires(BrutalityModItems.EMPTY_ANKLET.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        CompoundTag enchantment = EnchantmentHelper.storeEnchantment(ResourceLocation.parse("sharpness"), 1);
        enchantment.remove("lvl");


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SHARPNESS_ANKLET.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern("DAD")
                .define('A', createSharpnessBookIngredient())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', Items.IRON_INGOT)
                .define('D', Items.GOLD_INGOT)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUPER_DODGE_ANKLET.get())
                .pattern(" D ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', Items.TORCHFLOWER)
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.GIANT_SNIFFERS_HOOF.get())
                .define('D', TerramityModItems.SUPER_SNIFFERS_PELT.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TRIAL_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern("CDC")
                .define('A', TerramityModItems.STONE_TILES.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.DIMLITE_NUGGET.get())
                .define('D', TerramityModItems.ENERGIZED_CORE.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ULTRA_DODGE_ANKLET.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', Items.DIAMOND_BLOCK)
                .define('B', TerramityModItems.DRAGON_BAND.get())
                .define('C', TerramityModItems.ULTRA_SNIFFER_FUR.get())
                .unlockedBy(getHasName(TerramityModItems.DRAGON_BAND.get()), has(TerramityModItems.DRAGON_BAND.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VIRENTIUM_ANKLET.get())
                .pattern(" C ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', TerramityModItems.GAIANITE_CLUSTER.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', Items.FLOWERING_AZALEA_LEAVES)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VOID_ANKLET.get())
                .pattern(" C ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', TerramityModItems.VOID_ALLOY.get())
                .define('B', BrutalityModItems.EMPTY_ANKLET.get())
                .define('C', TerramityModItems.CHISELED_VOID_BLOCK.get())
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WINDSWEPT_ANKLET.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.BLAZE_POWDER)
                .define('C', BrutalityModItems.EMPTY_ANKLET.get())
                .define('B', Items.WATER_BUCKET)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SAPPHIRE_PHASESABER.get())
                .pattern("A")
                .pattern("A")
                .pattern("B")
                .define('A', TerramityModItems.SAPPHIRE_BLOCK.get())
                .define('B', Items.STICK)
                .unlockedBy(getHasName(TerramityModItems.SAPPHIRE.get()), has(TerramityModItems.SAPPHIRE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TOPAZ_PHASESABER.get())
                .pattern("A")
                .pattern("A")
                .pattern("B")
                .define('A', TerramityModItems.TOPAZ_BLOCK.get())
                .define('B', Items.STICK)
                .unlockedBy(getHasName(TerramityModItems.TOPAZ.get()), has(TerramityModItems.TOPAZ.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RUBY_PHASESABER.get())
                .pattern("A")
                .pattern("A")
                .pattern("B")
                .define('A', TerramityModItems.RUBY_BLOCK.get())
                .define('B', Items.STICK)
                .unlockedBy(getHasName(TerramityModItems.RUBY.get()), has(TerramityModItems.RUBY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ONYX_PHASESABER.get())
                .pattern("A")
                .pattern("A")
                .pattern("B")
                .define('A', TerramityModItems.ONYX_BLOCK.get())
                .define('B', Items.STICK)
                .unlockedBy(getHasName(TerramityModItems.ONYX.get()), has(TerramityModItems.ONYX.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VAMPIRE_KNIVES.get())
                .pattern("CAA")
                .pattern("CBA")
                .pattern("CCC")
                .define('A', BrutalityModItems.IRON_KNIFE.get())
                .define('B', TerramityModItems.CHTHONIC_NECTAR.get())
                .define('C', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_NECTAR.get()), has(TerramityModItems.CHTHONIC_NECTAR.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DYNAMITE.get())
                .pattern("  A")
                .pattern(" B ")
                .pattern("C  ")
                .define('A', Items.STRING)
                .define('B', Items.PAPER)
                .define('C', Items.TNT)
                .unlockedBy(getHasName(Items.TNT), has(Items.TNT))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.STICKY_DYNAMITE.get())
                .requires(BrutalityModItems.BLUE_SLIME_BALL.get(), 8)
                .requires(BrutalityModItems.DYNAMITE.get())
                .unlockedBy(getHasName(BrutalityModItems.BLUE_SLIME_BALL.get()), has(BrutalityModItems.BLUE_SLIME_BALL.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.BOUNCY_DYNAMITE.get())
                .requires(BrutalityModItems.PINK_SLIME_BALL.get(), 8)
                .requires(BrutalityModItems.DYNAMITE.get())
                .unlockedBy(getHasName(BrutalityModItems.PINK_SLIME_BALL.get()), has(BrutalityModItems.PINK_SLIME_BALL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HOLY_HAND_GRENADE.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.REVERIUM.get())
                .define('B', Items.TNT)
                .unlockedBy(getHasName(Items.TNT), has(Items.TNT))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.OVERCLOCKED_TOASTER.get())
                .pattern(" T ")
                .pattern("CBD")
                .pattern("AAA")
                .define('A', Items.IRON_BLOCK)
                .define('B', Items.BREAD)
                .define('C', Items.LEVER)
                .define('D', TerramityModItems.CIRCUIT_BOARD.get())
                .define('T', Items.IRON_TRAPDOOR)
                .unlockedBy(getHasName(TerramityModItems.CIRCUIT_BOARD.get()), has(TerramityModItems.CIRCUIT_BOARD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ICE_CUBE.get()) // TODO: Make Intermediary version for Permafrost cube
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.ICE)
                .define('B', TerramityModItems.SAPPHIRE_BLOCK.get())
                .unlockedBy(getHasName(TerramityModItems.SAPPHIRE.get()), has(TerramityModItems.SAPPHIRE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PERMAFROST_CUBE.get()) // TODO: Make Intermediary version for Permafrost cube
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.BLUE_ICE)
                .define('B', BrutalityModItems.ICE_CUBE.get())
                .unlockedBy(getHasName(BrutalityModItems.ICE_CUBE.get()), has(BrutalityModItems.ICE_CUBE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WINTER_MELON.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.PACKED_ICE)
                .define('B', Items.MELON)
                .unlockedBy(getHasName(Items.PACKED_ICE), has(Items.PACKED_ICE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GOLDEN_PHOENIX.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', TerramityModItems.SPIKES.get())
                .define('B', Items.MELON)
                .unlockedBy(getHasName(TerramityModItems.SPIKES.get()), has(TerramityModItems.SPIKES.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.STICK_OF_BUTTER.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.HONEYCOMB)
                .define('B', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.HONEYCOMB), has(Items.HONEYCOMB))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CAVENDISH.get())
                .pattern("CCA")
                .pattern("CBC")
                .pattern("CCC")
                .define('A', Items.GOLDEN_PICKAXE)
                .define('B', Items.SHEARS)
                .define('C', TerramityModItems.TOPAZ.get())
                .unlockedBy(getHasName(TerramityModItems.TOPAZ.get()), has(TerramityModItems.TOPAZ.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CANNONBALL_CABBAGE.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ItemTags.LEAVES)
                .define('B', TerramityModItems.BALL_N_CHAIN.get())
                .unlockedBy(getHasName(TerramityModItems.BALL_N_CHAIN.get()), has(TerramityModItems.BALL_N_CHAIN.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CRIMSON_DELIGHT.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.RUBY.get())
                .define('B', Items.APPLE)
                .unlockedBy(getHasName(TerramityModItems.RUBY.get()), has(TerramityModItems.RUBY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ABSOLUTE_ZERO.get())
                .pattern("CAC")
                .pattern("ABA")
                .pattern("CAC")
                .define('A', Items.BLUE_ICE)
                .define('B', Items.GLASS_BOTTLE)
                .define('C', TerramityModItems.SAPPHIRE.get())
                .unlockedBy(getHasName(TerramityModItems.SAPPHIRE.get()), has(TerramityModItems.SAPPHIRE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PERFUME_BOTTLE.get())
                .pattern("CAC")
                .pattern("ABA")
                .pattern("CAC")
                .define('A', Items.POPPY)
                .define('B', Items.GLASS_BOTTLE)
                .define('C', TerramityModItems.RUBY.get())
                .unlockedBy(getHasName(TerramityModItems.RUBY.get()), has(TerramityModItems.RUBY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DECK_OF_FATE.get())
                .pattern("AGB")
                .pattern("FCF")
                .pattern("DGE")
                .define('A', TerramityModItems.SAPPHIRE.get())
                .define('B', TerramityModItems.RUBY.get())
                .define('C', TerramityModItems.TOPAZ.get())
                .define('D', TerramityModItems.ONYX.get())
                .define('E', TerramityModItems.GAIANITE_CLUSTER.get())
                .define('F', TerramityModItems.CARDBOARD.get())
                .define('G', TerramityModItems.DAEMONIUM_CHUNK.get())
                .unlockedBy(getHasName(TerramityModItems.CARDBOARD.get()), has(TerramityModItems.CARDBOARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLAST_BARREL.get())
                .pattern("DAD")
                .pattern("DBD")
                .pattern("DCD")
                .define('A', Items.NETHERITE_SCRAP)
                .define('B', Items.TNT)
                .define('C', Items.BARREL)
                .define('D', TerramityModItems.DAEMONIUM_CHUNK.get())
                .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BEACH_BALL.get())
                .pattern("AD ")
                .pattern("DBD")
                .pattern(" DC")
                .define('A', Items.LIGHT_BLUE_DYE)
                .define('B', Items.ORANGE_DYE)
                .define('C', Items.PINK_DYE)
                .define('D', Items.WHITE_WOOL)
                .unlockedBy(getHasName(Items.WHITE_WOOL), has(Items.WHITE_WOOL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.STICKY_BOMB.get())
                .pattern(" A ")
                .pattern("BDB")
                .pattern("ECE")
                .define('A', Items.REDSTONE_TORCH)
                .define('B', TerramityModItems.CIRCUIT_BOARD.get())
                .define('C', BrutalityModItems.BLUE_SLIME_BALL.get())
                .define('D', Items.TNT)
                .define('E', Items.CLAY_BALL)
                .unlockedBy(getHasName(TerramityModItems.CIRCUIT_BOARD.get()), has(TerramityModItems.CIRCUIT_BOARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SCULK_GRENADE.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', TerramityModItems.THROWING_BOMB.get())
                .define('B', TerramityModItems.DIMLITE_INGOT.get())
                .define('C', Items.ECHO_SHARD)
                .unlockedBy(getHasName(TerramityModItems.DIMLITE_INGOT.get()), has(TerramityModItems.DIMLITE_INGOT.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.CINDER_BLOCK.get())
                .requires(Items.STONE)
                .requires(Items.SMOOTH_STONE)
                .requires(Items.ANDESITE)
                .requires(Items.POLISHED_ANDESITE)
                .requires(Items.CLAY_BALL)
                .requires(Items.LIGHT_GRAY_CONCRETE_POWDER)
                .unlockedBy(getHasName(Items.STONE), has(Items.STONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DECK_OF_CARDS.get())
                .pattern(" A ")
                .pattern("BAB")
                .pattern(" A ")
                .define('A', TerramityModItems.CARDBOARD.get())
                .define('B', TerramityModItems.VOID_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.VOID_ALLOY.get()), has(TerramityModItems.VOID_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DAEMONIUM_WHETSTONE.get())
                .pattern(" C ")
                .pattern("CAC")
                .pattern(" C ")
                .define('A', TerramityModItems.SHARPENING_STONE.get())
                .define('C', TerramityModItems.DAEMONIUM_CHUNK.get())
                .unlockedBy(getHasName(TerramityModItems.SHARPENING_STONE.get()), has(TerramityModItems.SHARPENING_STONE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.KNIGHTS_PENDANT.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', TerramityModItems.DIAMOND_PENDANT.get())
                .define('B', Items.IRON_SWORD)
                .unlockedBy(getHasName(TerramityModItems.DIAMOND_PENDANT.get()), has(TerramityModItems.DIAMOND_PENDANT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THE_OATH.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', TerramityModItems.TERRAMITY_GUIDEBOOK_ICON.get())
                .define('B', TerramityModItems.REVERIUM.get())
                .unlockedBy(getHasName(TerramityModItems.REVERIUM.get()), has(TerramityModItems.REVERIUM.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.LUCKY_INSOLES.get())
                .requires(TerramityModItems.LUCKY_DICE.get(), 2)
                .requires(Tags.Items.ARMORS_BOOTS)
                .unlockedBy(getHasName(TerramityModItems.LUCKY_DICE.get()), has(TerramityModItems.LUCKY_DICE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.OLD_GUILLOTINE.get())
                .pattern("A")
                .pattern("A")
                .pattern("B")
                .define('A', Items.NETHERITE_SCRAP)
                .define('B', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.NETHERITE_SCRAP), has(Items.NETHERITE_SCRAP))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.GAMBLERS_CHAIN.get())
                .requires(Items.RABBIT_FOOT, 2)
                .requires(TerramityModItems.LUCKY_DICE.get())
                .unlockedBy(getHasName(TerramityModItems.LUCKY_DICE.get()), has(TerramityModItems.LUCKY_DICE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DAEMONIUM_SEWING_KIT.get())
                .pattern("A A")
                .pattern("B B")
                .pattern("B B")
                .define('A', Items.IRON_NUGGET)
                .define('B', TerramityModItems.DAEMONIUM_CHUNK.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PINCUSHION.get())
                .pattern("A")
                .pattern("B")
                .define('A', BrutalityModItems.DAEMONIUM_SEWING_KIT.get())
                .define('B', ItemTags.WOOL)
                .unlockedBy(getHasName(BrutalityModItems.DAEMONIUM_SEWING_KIT.get()), has(BrutalityModItems.DAEMONIUM_SEWING_KIT.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SOLDIERS_SYRINGE.get())
                .pattern("ECD")
                .pattern("EAC")
                .pattern("BEE")
                .define('A', Items.GLASS_PANE)
                .define('B', Items.IRON_BARS)
                .define('C', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('D', TerramityModItems.DAEMONIUM.get())
                .define('E', Items.SUGAR)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TARGET_CUBE.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', TerramityModItems.LUCKY_DICE.get())
                .define('B', Items.TARGET)
                .unlockedBy(getHasName(TerramityModItems.LUCKY_DICE.get()), has(TerramityModItems.LUCKY_DICE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ENERGY_FOCUSER.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('B', Items.LAPIS_LAZULI)
                .define('C', Items.GOLD_INGOT)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CRITICAL_THINKING.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', Items.GLASS_BOTTLE)
                .define('B', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_CRYSTAL.get()), has(TerramityModItems.CHTHONIC_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DEADSHOT_BROOCH.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', TerramityModItems.EYEPATCH.get())
                .define('B', Items.GOLD_INGOT)
                .define('C', TerramityModItems.DAEMONIUM_CHUNK.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM_CHUNK.get()), has(TerramityModItems.DAEMONIUM_CHUNK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VINDICATOR_STEROIDS.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.EMERALD)
                .define('B', TerramityModItems.MORPHINE_PILLS.get())
                .unlockedBy(getHasName(TerramityModItems.MORPHINE_PILLS.get()), has(TerramityModItems.MORPHINE_PILLS.get()))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.BLUE_SLIME_BALL.get())
                .requires(Tags.Items.SLIMEBALLS)
                .requires(Items.BLUE_DYE)
                .unlockedBy(getHasName(Items.SLIME_BALL), has(Items.SLIME_BALL))
                .save(consumer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.PINK_SLIME_BALL.get())
                .requires(Tags.Items.SLIMEBALLS)
                .requires(Items.PINK_DYE)
                .unlockedBy(getHasName(Items.SLIME_BALL), has(Items.SLIME_BALL))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SILVER_RESPAWN_CARD.get())
                .pattern("CAC")
                .pattern("CBC")
                .pattern("CAC")
                .define('A', Items.IRON_BLOCK)
                .define('B', Items.TOTEM_OF_UNDYING)
                .define('C', TerramityModItems.CARDBOARD.get())
                .unlockedBy(getHasName(Items.TOTEM_OF_UNDYING), has(Items.TOTEM_OF_UNDYING))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DIAMOND_RESPAWN_CARD.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.DIAMOND)
                .define('B', BrutalityModItems.SILVER_RESPAWN_CARD.get())
                .unlockedBy(getHasName(BrutalityModItems.SILVER_RESPAWN_CARD.get()), has(BrutalityModItems.SILVER_RESPAWN_CARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EVIL_KING_RESPAWN_CARD.get())
                .pattern(" B ")
                .pattern("EAC")
                .pattern(" D ")
                .define('A', BrutalityModItems.DIAMOND_RESPAWN_CARD.get())
                .define('B', TerramityModItems.EVIL_KING_ARMOR_HELMET.get())
                .define('C', TerramityModItems.EVIL_KING_ARMOR_CHESTPLATE.get())
                .define('D', TerramityModItems.EVIL_KING_ARMOR_LEGGINGS.get())
                .define('E', TerramityModItems.EVIL_KING_ARMOR_BOOTS.get())
                .unlockedBy(getHasName(BrutalityModItems.DIAMOND_RESPAWN_CARD.get()), has(BrutalityModItems.DIAMOND_RESPAWN_CARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SILVER_BOOSTER_PACK.get())
                .pattern("BDB")
                .pattern("EAC")
                .pattern("BBB")
                .define('A', TerramityModItems.CARDBOARD_BOX.get())
                .define('B', TerramityModItems.CARDBOARD.get())
                .define('E', Items.GOLDEN_CARROT)
                .define('C', StrictNBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.SWIFTNESS)))
                .define('D', TerramityModItems.AMP_SHIELD.get())
                .unlockedBy(getHasName(TerramityModItems.CARDBOARD_BOX.get()), has(TerramityModItems.CARDBOARD_BOX.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DIAMOND_BOOSTER_PACK.get())
                .pattern("ACA")
                .pattern("DBD")
                .pattern("ACA")
                .define('A', Items.DIAMOND)
                .define('B', BrutalityModItems.SILVER_BOOSTER_PACK.get())
                .define('C', StrictNBTIngredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.LEAPING)))
                .define('D', Items.GOLDEN_APPLE)
                .unlockedBy(getHasName(BrutalityModItems.SILVER_BOOSTER_PACK.get()), has(BrutalityModItems.SILVER_BOOSTER_PACK.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EVIL_KING_BOOSTER_PACK.get())
                .pattern("B C")
                .pattern(" A ")
                .pattern("EFD")
                .define('A', BrutalityModItems.DIAMOND_BOOSTER_PACK.get())
                .define('B', TerramityModItems.EVIL_KING_ARMOR_HELMET.get())
                .define('C', TerramityModItems.EVIL_KING_ARMOR_CHESTPLATE.get())
                .define('D', TerramityModItems.EVIL_KING_ARMOR_LEGGINGS.get())
                .define('E', TerramityModItems.EVIL_KING_ARMOR_BOOTS.get())
                .define('F', TerramityModItems.IBUPROFEN_CAPSULES.get())
                .unlockedBy(getHasName(BrutalityModItems.DIAMOND_BOOSTER_PACK.get()), has(BrutalityModItems.DIAMOND_BOOSTER_PACK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CROWBAR.get())
                .pattern(" BB")
                .pattern("  A")
                .pattern("  A")
                .define('A', TerramityModItems.DAEMONIUM.get())
                .define('B', TerramityModItems.DAEMONIUM_CHUNK.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WIRE_CUTTERS.get())
                .pattern("B B")
                .pattern(" A ")
                .pattern("C C")
                .define('A', Items.SHEARS)
                .define('B', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('C', Items.RED_CONCRETE)
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.AIR_JORDAN_EARRINGS.get())
                .pattern("BAB")
                .pattern("DCD")
                .define('A', TerramityModItems.DIAMOND_EARRINGS.get())
                .define('B', Items.IRON_NUGGET)
                .define('C', Items.PHANTOM_MEMBRANE)
                .define('D', Items.RED_CONCRETE)
                .unlockedBy(getHasName(TerramityModItems.RAW_COSMILITE.get()), has(TerramityModItems.RAW_COSMILITE.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.JURY_NULLIFIER.get())
                .pattern("CAC")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', TerramityModItems.HELLROK_GIGATON_HAMMER.get())
                .define('B', Items.STICK)
                .define('C', BrutalityModItems.RED_SEAL.get())
                .unlockedBy(getHasName(TerramityModItems.HELLROK_GIGATON_HAMMER.get()), has(TerramityModItems.HELLROK_GIGATON_HAMMER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EYE_OF_THE_DRAGON.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', TerramityModItems.SUPER_SNIFFERS_PELT.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.LENS_MAKERS_GLASSES.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', TerramityModItems.EYEGLASSES.get())
                .define('B', Items.RED_STAINED_GLASS)
                .define('C', TerramityModItems.HELLSPEC_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BIG_STEPPA.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', BrutalityModItems.EMPTY_ANKLET.get())
                .define('B', Items.GOLD_BLOCK)
                .unlockedBy(getHasName(BrutalityModItems.EMPTY_ANKLET.get()), has(BrutalityModItems.EMPTY_ANKLET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SCOPE_GOGGLES.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', TerramityModItems.EYEGLASSES.get())
                .define('B', Items.BLUE_STAINED_GLASS)
                .define('C', TerramityModItems.DAEMONIUM.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLACK_MATTER_NECKLACE.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', TerramityModItems.GOLD_NECKLACE.get())
                .define('B', TerramityModItems.BLACK_MATTER.get())
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PHANTOM_FINGER.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', TerramityModItems.DIAMOND_GAUNTLET.get())
                .define('B', TerramityModItems.SPECTRAL_SOUL.get())
                .define('C', TerramityModItems.LOST_SOUL.get())
                .unlockedBy(getHasName(TerramityModItems.LOST_SOUL.get()), has(TerramityModItems.LOST_SOUL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BRUTAL_HEART.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', BrutalityModItems.SECOND_HEART.get())
                .define('B', TerramityModItems.SPITEFUL_SOUL.get())
                .define('C', TerramityModItems.FORBIDDEN_FRUIT.get())
                .unlockedBy(getHasName(TerramityModItems.SPITEFUL_SOUL.get()), has(TerramityModItems.SPITEFUL_SOUL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NINJA_HEART.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', BrutalityModItems.SECOND_HEART.get())
                .define('B', Items.BLACK_WOOL)
                .define('C', TerramityModItems.BLACK_MATTER.get())
                .unlockedBy(getHasName(BrutalityModItems.SECOND_HEART.get()), has(BrutalityModItems.SECOND_HEART.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOOD_CHALICE.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', TerramityModItems.HOLY_CHALICE.get())
                .define('B', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('C', TerramityModItems.HELLSPEC_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_CRYSTAL.get()), has(TerramityModItems.CHTHONIC_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HEMOMATIC_LOCKET.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', TerramityModItems.GOLD_MEDAL.get())
                .define('B', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('C', Items.GOLD_INGOT)
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_CRYSTAL.get()), has(TerramityModItems.CHTHONIC_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SANGUINE_SIGNET.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', TerramityModItems.LIFE_RING.get())
                .define('B', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('C', Items.GOLD_INGOT)
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_CRYSTAL.get()), has(TerramityModItems.CHTHONIC_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SELF_REPAIR_NEXUS.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', Items.NETHER_STAR)
                .define('B', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .define('C', TerramityModItems.DAEMONIUM_CHUNK.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_CRYSTAL.get()), has(TerramityModItems.CHTHONIC_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HEMOGRAFT_NEEDLE.get())
                .pattern("  B")
                .pattern(" A ")
                .pattern("A  ")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .unlockedBy(getHasName(TerramityModItems.CHTHONIC_CRYSTAL.get()), has(TerramityModItems.CHTHONIC_CRYSTAL.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VAMPIRE_FANG.get())
                .pattern(" B ")
                .pattern("CAC")
                .pattern(" B ")
                .define('A', Items.BONE_BLOCK)
                .define('B', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('C', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);


        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SANGUINE_SPECTACLES.get())
                .pattern("DCD")
                .pattern("BAB")
                .pattern("DCD")
                .define('A', BrutalityModItems.LENS_MAKERS_GLASSES.get())
                .define('B', Items.RED_STAINED_GLASS)
                .define('C', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('D', Items.GOLD_INGOT)
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PROGENITORS_EARRINGS.get())
                .pattern("CBC")
                .pattern("BAB")
                .pattern("CBC")
                .define('A', TerramityModItems.DIAMOND_EARRINGS.get())
                .define('B', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('C', TerramityModItems.CHTHONIC_CRYSTAL.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOODSTAINED_MIRROR.get())
                .pattern(" CD")
                .pattern("BAB")
                .pattern("DC ")
                .define('A', TerramityModItems.DAEMONIUM_GLASS.get())
                .define('B', TerramityModItems.CHTHONIAN_VOID.get())
                .define('C', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('D', Items.GOLD_INGOT)
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.INCOGNITO_MODE.get())
                .pattern(" B ")
                .pattern("CAC")
                .pattern(" C ")
                .define('A', TerramityModItems.EYEGLASSES.get())
                .define('B', TerramityModItems.CONJUROR_HELMET.get())
                .define('C', TerramityModItems.BLACK_MATTER.get())
                .unlockedBy(getHasName(TerramityModItems.BLACK_MATTER.get()), has(TerramityModItems.BLACK_MATTER.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PAPER_AIRPLANE.get())
                .pattern("  A")
                .pattern("AA ")
                .pattern("AA ")
                .define('A', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FIRE_EXTINGUISHER.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', Items.PACKED_ICE)
                .define('B', Items.RED_CONCRETE)
                .define('C', Items.LEVER)
                .unlockedBy(getHasName(Items.LEVER), has(Items.LEVER))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EMERGENCY_MEETING.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', TerramityModItems.CIRCUIT_BOARD.get())
                .define('B', Items.CYAN_TERRACOTTA)
                .define('C', Items.MANGROVE_BUTTON)
                .unlockedBy(getHasName(TerramityModItems.CIRCUIT_BOARD.get()), has(TerramityModItems.CIRCUIT_BOARD.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PENCIL_SHARPENER.get())
                .pattern("CCC")
                .pattern("BAB")
                .pattern("CCC")
                .define('A', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('B', Items.IRON_SWORD)
                .define('C', Items.LIGHT_BLUE_CONCRETE)
                .unlockedBy(getHasName(Items.IRON_SWORD), has(Items.IRON_SWORD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VAMPIRIC_TALISMAN.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern("CAC")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', Items.ENDER_EYE)
                .define('C', Items.BLACK_WOOL)
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FALLEN_ANGELS_HALO.get())
                .pattern(" B ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', TerramityModItems.ARCHANGEL_HALO.get())
                .define('B', TerramityModItems.HELLSPEC_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.ARCHANGEL_HALO.get()), has(TerramityModItems.ARCHANGEL_HALO.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HELLSPEC_TIE.get())
                .pattern(" A ")
                .pattern("BAB")
                .pattern(" B ")
                .define('A', Items.RED_WOOL)
                .define('B', TerramityModItems.HELLSPEC_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLOOD_PACK.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
                .define('B', Items.GLASS)
                .define('C', Items.NETHERITE_INGOT)
                .unlockedBy(getHasName(TerramityModItems.STYGIAN_BLOOD_BUCKET.get()), has(TerramityModItems.STYGIAN_BLOOD_BUCKET.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SOUL_STONE.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', TerramityModItems.MAGMA_STONE.get())
                .define('B', TerramityModItems.SPITEFUL_SOUL.get())
                .unlockedBy(getHasName(TerramityModItems.MAGMA_STONE.get()), has(TerramityModItems.MAGMA_STONE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ONYX_IDOL.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', Items.TOTEM_OF_UNDYING)
                .define('B', TerramityModItems.ONYX.get())
                .unlockedBy(getHasName(TerramityModItems.ONYX.get()), has(TerramityModItems.ONYX.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SCRIBES_INDEX.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', BrutalityModItems.RED_SEAL.get())
                .define('B', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.OCCULT_FABRIC.get()), has(TerramityModItems.OCCULT_FABRIC.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PROFANUM_REACTOR.get())
                .pattern(" C ")
                .pattern("BAB")
                .pattern(" C ")
                .define('A', TerramityModItems.ENERGIZED_CORE.get())
                .define('B', TerramityModItems.PROFANED_ORE.get())
                .define('C', Items.MAGMA_BLOCK)
                .unlockedBy(getHasName(TerramityModItems.PROFANED_ORE.get()), has(TerramityModItems.PROFANED_ORE.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MANA_SYRINGE.get())
                .pattern("ECD")
                .pattern("EAC")
                .pattern("BEE")
                .define('A', Items.GLASS_PANE)
                .define('B', Items.IRON_BARS)
                .define('C', TerramityModItems.DAEMONIUM_CHUNK.get())
                .define('D', TerramityModItems.DAEMONIUM.get())
                .define('E', TerramityModItems.ONYX_BLOCK.get())
                .unlockedBy(getHasName(TerramityModItems.DAEMONIUM.get()), has(TerramityModItems.DAEMONIUM.get()))
                .save(consumer);

        for (DyeColor dyeColor : DyeColor.values()) {
            Block baseConcrete = BrutalityModBlocks.CONCRETE_BLOCKS.get(dyeColor.ordinal());
            // Stairs
            RegistryObject<Block> stair = BrutalityModBlocks.CONCRETE_STAIRS.get(dyeColor.ordinal());
            stair(consumer, stair.get(), baseConcrete);

            // Slabs
            RegistryObject<Block> slab = BrutalityModBlocks.CONCRETE_SLABS.get(dyeColor.ordinal());
            slab(consumer, RecipeCategory.BUILDING_BLOCKS, slab.get(), baseConcrete);
        }

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModItems.WATER_COOLER_ITEM.get())
                .pattern(" A ")
                .pattern("DBD")
                .pattern("DCD")
                .define('A', Items.BUCKET)
                .define('B', Items.TRIPWIRE_HOOK)
                .define('C', Items.IRON_BLOCK)
                .define('D', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.BUCKET), has(Items.BUCKET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.PLASTERBOARD.get())
                .pattern("BBB")
                .pattern("BAB")
                .pattern("BBB")
                .define('A', Items.WHITE_DYE)
                .define('B', Items.STRIPPED_BAMBOO_BLOCK)
                .unlockedBy(getHasName(Items.STRIPPED_BAMBOO_BLOCK), has(Items.STRIPPED_BAMBOO_BLOCK))
                .save(consumer);


        twoByTwoPackerWithCount(consumer, RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.LIGHT_GRAY_OFFICE_RUG.get(), 4, Items.LIGHT_GRAY_WOOL);
        twoByTwoPackerWithCount(consumer, RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.GRAY_OFFICE_RUG.get(), 4, Items.GRAY_WOOL);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.OLD_SERVER_CASING.get(), 9)
                .pattern("ACA")
                .pattern("ABA")
                .pattern("ACA")
                .define('A', Items.IRON_INGOT)
                .define('B', TerramityModItems.CONDUCTITE.get())
                .define('C', Items.GRAY_DYE)
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE.get()), has(TerramityModItems.CONDUCTITE.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.OLD_SERVER_PANEL.get(), 9)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', BrutalityModBlocks.OLD_SERVER_CASING.get())
                .define('B', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
                .unlockedBy(getHasName(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()), has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(consumer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.PUDDLE.get(), 16)
                .requires(Items.WATER_BUCKET)
                .unlockedBy(getHasName(Items.WATER_BUCKET), has(Items.WATER_BUCKET))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.EXIT_SIGN.get(), 2)
                .pattern("AAA")
                .pattern("CBC")
                .pattern("AAA")
                .define('A', Items.WHITE_CONCRETE)
                .define('B', ItemTags.SIGNS)
                .define('C', Items.GLOW_INK_SAC)
                .unlockedBy(getHasName(Items.WHITE_CONCRETE), has(Items.WHITE_CONCRETE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.TOILET.get())
                .pattern("A  ")
                .pattern("CBC")
                .pattern("CDC")
                .define('A', Items.STONE_BUTTON)
                .define('B', Items.WATER_BUCKET)
                .define('C', Items.QUARTZ_BLOCK)
                .define('D', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.QUARTZ_BLOCK), has(Items.QUARTZ_BLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.URINAL.get())
                .pattern("CAC")
                .pattern("CBC")
                .pattern("CDC")
                .define('A', Items.STONE_BUTTON)
                .define('B', Items.WATER_BUCKET)
                .define('C', Items.QUARTZ_BLOCK)
                .define('D', Items.IRON_BARS)
                .unlockedBy(getHasName(Items.QUARTZ_BLOCK), has(Items.QUARTZ_BLOCK))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.UPPER_HVAC.get())
                .pattern(" B ")
                .pattern("CAC")
                .pattern(" C ")
                .define('A', TerramityModItems.BATTERY.get())
                .define('B', Items.IRON_BARS)
                .define('C', Items.IRON_INGOT)
                .unlockedBy(getHasName(TerramityModItems.BATTERY.get()), has(TerramityModItems.BATTERY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.LOWER_HVAC.get())
                .pattern(" C ")
                .pattern("CAC")
                .pattern(" C ")
                .define('A', TerramityModItems.BATTERY.get())
                .define('C', Items.IRON_INGOT)
                .unlockedBy(getHasName(TerramityModItems.BATTERY.get()), has(TerramityModItems.BATTERY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.OLD_AIR_CONDITIONER.get())
                .pattern("BCB")
                .pattern("CAC")
                .pattern("BCB")
                .define('A', TerramityModItems.BATTERY.get())
                .define('B', Items.IRON_NUGGET)
                .define('C', Items.IRON_INGOT)
                .unlockedBy(getHasName(TerramityModItems.BATTERY.get()), has(TerramityModItems.BATTERY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.BLUE_CUBICLE_PANEL.get())
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Items.BLUE_CARPET)
                .unlockedBy(getHasName(Items.BLUE_CARPET), has(Items.BLUE_CARPET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.GRAY_CUBICLE_PANEL.get())
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Items.GRAY_CARPET)
                .unlockedBy(getHasName(Items.GRAY_CARPET), has(Items.GRAY_CARPET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.GREEN_CUBICLE_PANEL.get())
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Items.GREEN_CARPET)
                .unlockedBy(getHasName(Items.GREEN_CARPET), has(Items.GREEN_CARPET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.LIGHT_GRAY_CUBICLE_PANEL.get())
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Items.LIGHT_GRAY_CARPET)
                .unlockedBy(getHasName(Items.LIGHT_GRAY_CARPET), has(Items.LIGHT_GRAY_CARPET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.RED_CUBICLE_PANEL.get())
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Items.RED_CARPET)
                .unlockedBy(getHasName(Items.RED_CARPET), has(Items.RED_CARPET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.WHITE_CUBICLE_PANEL.get())
                .pattern("AAA")
                .pattern("AAA")
                .define('A', Items.WHITE_CARPET)
                .unlockedBy(getHasName(Items.WHITE_CARPET), has(Items.WHITE_CARPET))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.CRT_MONITOR.get())
                .pattern(" A ")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.WHITE_CONCRETE)
                .define('B', Items.REDSTONE_LAMP)
                .unlockedBy(getHasName(Items.REDSTONE_LAMP), has(Items.REDSTONE_LAMP))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.LCD_MONITOR.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern(" A ")
                .define('A', Items.BLACK_CONCRETE)
                .define('B', Items.REDSTONE_LAMP)
                .unlockedBy(getHasName(Items.REDSTONE_LAMP), has(Items.REDSTONE_LAMP))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.DUSTBIN.get())
                .pattern("A A")
                .pattern("A A")
                .pattern("ABA")
                .define('A', Items.CHAIN)
                .define('B', Items.POLISHED_BLACKSTONE_PRESSURE_PLATE)
                .unlockedBy(getHasName(Items.CHAIN), has(Items.CHAIN))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.WET_FLOOR_SIGN.get(), 2)
                .pattern(" A ")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.YELLOW_CONCRETE)
                .define('B', ItemTags.SIGNS)
                .unlockedBy(getHasName(Items.YELLOW_CONCRETE), has(Items.YELLOW_CONCRETE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.WHITE_OFFICE_CHAIR.get(), 2)
                .pattern("A  ")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.WHITE_CONCRETE)
                .define('B', Items.BLACK_CARPET)
                .unlockedBy(getHasName(Items.WHITE_CONCRETE), has(Items.WHITE_CONCRETE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.BLACK_OFFICE_CHAIR.get(), 2)
                .pattern("A  ")
                .pattern("ABA")
                .pattern("A A")
                .define('A', Items.BLACK_CONCRETE)
                .define('B', Items.BLACK_CARPET)
                .unlockedBy(getHasName(Items.BLACK_CONCRETE), has(Items.BLACK_CONCRETE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.WHITE_FILING_CABINET.get())
                .pattern("ABA")
                .pattern("A A")
                .pattern("ABA")
                .define('A', Items.WHITE_CONCRETE)
                .define('B', Tags.Items.CHESTS)
                .unlockedBy(getHasName(Items.WHITE_CONCRETE), has(Items.WHITE_CONCRETE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.GRAY_FILING_CABINET.get())
                .pattern("ABA")
                .pattern("A A")
                .pattern("ABA")
                .define('A', Items.GRAY_CONCRETE)
                .define('B', Tags.Items.CHESTS)
                .unlockedBy(getHasName(Items.GRAY_CONCRETE), has(Items.GRAY_CONCRETE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.LIGHT_GRAY_FILING_CABINET.get())
                .pattern("ABA")
                .pattern("A A")
                .pattern("ABA")
                .define('A', Items.LIGHT_GRAY_CONCRETE)
                .define('B', Tags.Items.CHESTS)
                .unlockedBy(getHasName(Items.LIGHT_GRAY_CONCRETE), has(Items.LIGHT_GRAY_CONCRETE))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.OFFICE_LIGHT.get(), 2)
                .pattern(" C ")
                .pattern("ABA")
                .pattern(" C ")
                .define('A', Items.WHITE_DYE)
                .define('B', Items.GLOWSTONE)
                .define('C', Items.BLACK_DYE)
                .unlockedBy(getHasName(Items.GLOWSTONE), has(Items.GLOWSTONE))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PRISMATIC_GREATSWORD.get())
                .pattern("CBC")
                .pattern("CBC")
                .pattern(" A ")
                .define('A', Items.STICK)
                .define('B', TerramityModItems.PRISMATIC_JEWEL.get())
                .define('C', TerramityModItems.IRIDESCENT_SHARD.get())
                .unlockedBy(getHasName(TerramityModItems.PRISMATIC_JEWEL.get()), has(TerramityModItems.PRISMATIC_JEWEL.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SHADOWFLAME_SCISSOR_BLADE.get())
                .pattern(" AB")
                .pattern(" B ")
                .pattern("B  ")
                .define('A', TerramityModItems.SHARPENING_STONE.get())
                .define('B', TerramityModItems.NYXIUM.get())
                .unlockedBy(getHasName(TerramityModItems.NYXIUM.get()), has(TerramityModItems.NYXIUM.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CRIMSON_SCISSOR_BLADE.get())
                .pattern(" AB")
                .pattern(" B ")
                .pattern("B  ")
                .define('A', TerramityModItems.SHARPENING_STONE.get())
                .define('B', TerramityModItems.HELLSPEC_ALLOY.get())
                .unlockedBy(getHasName(TerramityModItems.HELLSPEC_ALLOY.get()), has(TerramityModItems.HELLSPEC_ALLOY.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RHONGOMYNIAD.get())
                .pattern("  A")
                .pattern(" B ")
                .pattern("B  ")
                .define('A', TerramityModItems.EXCALIBUR.get())
                .define('B', Items.STICK)
                .unlockedBy(getHasName(BrutalityModItems.RHONGOMYNIAD.get()), has(BrutalityModItems.RHONGOMYNIAD.get()))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModItems.IMPORTANT_DOCUMENTS.get(), 8)
                .pattern("CAC")
                .pattern("CBC")
                .pattern("CCC")
                .define('A', Items.FEATHER)
                .define('B', Items.INK_SAC)
                .define('C', Items.PAPER)
                .unlockedBy(getHasName(Items.PAPER), has(Items.PAPER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, BrutalityModItems.COFFEE_MACHINE_ITEM.get())
                .pattern("CCB")
                .pattern("C  ")
                .pattern("CCA")
                .define('A', Items.IRON_BARS)
                .define('B', Items.LEVER)
                .define('C', Items.IRON_BLOCK)
                .unlockedBy(getHasName(Items.LEVER), has(Items.LEVER))
                .save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.KNUCKLE_WRAPS.get())
                .pattern(" A ")
                .pattern("CBC")
                .pattern(" A ")
                .define('A', TerramityModItems.HELLSPEC_ALLOY.get())
                .define('B', TerramityModItems.DIAMOND_GAUNTLET.get())
                .define('C', TerramityModItems.OCCULT_FABRIC.get())
                .unlockedBy(getHasName(TerramityModItems.DIAMOND_GAUNTLET.get()), has(TerramityModItems.DIAMOND_GAUNTLET.get()))
                .save(consumer);


        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, BrutalityModBlocks.SMALL_OFFICE_LIGHT.get(), 2)
                .requires(BrutalityModBlocks.OFFICE_LIGHT.get())
                .unlockedBy(getHasName(BrutalityModBlocks.OFFICE_LIGHT.get()), has(BrutalityModBlocks.OFFICE_LIGHT.get()))
                .save(consumer);

        carpet(consumer, BrutalityModBlocks.GRAY_OFFICE_CARPET.get(), Items.GRAY_CARPET);
        carpet(consumer, BrutalityModBlocks.LIGHT_GRAY_OFFICE_CARPET.get(), Items.LIGHT_GRAY_CARPET);
    }

    private static Ingredient createSharpnessBookIngredient() {
        // Display book: Sharpness I
        ItemStack displayStack = EnchantedBookItem.createForEnchantment(
                new EnchantmentInstance(Enchantments.SHARPNESS, 1)
        );

        // Base NBT that matches any Sharpness level (no "lvl" tag required)
        CompoundTag anySharpness = new CompoundTag();
        anySharpness.putString("id", "minecraft:sharpness"); // only id matters
        ListTag list = new ListTag();
        list.add(anySharpness);
        CompoundTag nbt = new CompoundTag();
        nbt.put("StoredEnchantments", list);

        return new PartialNBTIngredient(Set.of(Items.ENCHANTED_BOOK), nbt) {
            private final ItemStack[] display = { displayStack };

            @Override
            public boolean test(ItemStack stack) {
                if (!stack.is(Items.ENCHANTED_BOOK)) return false;
                return EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SHARPNESS);
            }

            @Override
            public ItemStack @NotNull [] getItems() {
                return display; // JEI sees Sharpness I
            }

        };
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void stair(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pPressurePlate, ItemLike pMaterial) {
        stairBuilder(pPressurePlate, Ingredient.of(pMaterial)).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static void twoByTwoPackerWithCount(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pPacked, int count, ItemLike pUnpacked) {
        ShapedRecipeBuilder.shaped(pCategory, pPacked, count).define('#', pUnpacked).pattern("##").pattern("##").unlockedBy(getHasName(pUnpacked), has(pUnpacked)).save(pFinishedRecipeConsumer);
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {

        for (ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, Brutality.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
