//package net.goo.brutality.datagen;
//
//import net.goo.brutality.Brutality;
//import net.goo.brutality.registry.BrutalityModItems;
//import net.mcreator.terramity.init.TerramityModItems;
//import net.minecraft.data.PackOutput;
//import net.minecraft.data.recipes.*;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.crafting.AbstractCookingRecipe;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.item.crafting.RecipeSerializer;
//import net.minecraft.world.level.ItemLike;
//import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Iterator;
//import java.util.List;
//import java.util.function.Consumer;
//
//
//public class BrutalityRecipeProvider extends RecipeProvider implements IConditionBuilder {
//
//
//    public BrutalityRecipeProvider(PackOutput pOutput) {
//        super(pOutput);
//    }
//
//    @Override
//    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
//
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BrutalityModItems.POCKET_BLACK_HOLE.get())
//                .requires(TerramityModItems.ETHEREAL_ECLIPSE_EMBLEM.get())
//                .requires(TerramityModItems.NEUTRON_STAR.get())
//                .unlockedBy("has_neutron_star", has(TerramityModItems.NEUTRON_STAR.get()))
//                .save(consumer);
//
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BrutalityModItems.HIGH_FREQUENCY_ALLOY.get())
//                .requires(TerramityModItems.CONDUCTITE.get())
//                .requires(TerramityModItems.HELLSPEC_ALLOY.get())
//                .unlockedBy("has_hellspec_alloy", has(TerramityModItems.HELLSPEC_ALLOY.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DULL_KNIFE_DAGGER.get())
//                .pattern(" # ")
//                .pattern("$%@")
//                .pattern(" ^ ")
//                .define('#', TerramityModItems.SPECTRAL_SOUL.get())
//                .define('$', TerramityModItems.ACCURSED_SOUL.get())
//                .define('%', Items.NETHERITE_SWORD)
//                .define('@', TerramityModItems.LOST_SOUL.get())
//                .define('^', TerramityModItems.SPITEFUL_SOUL.get())
//                .unlockedBy("has_netherite_sword", has(Items.NETHERITE_SWORD))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CANOPY_OF_SHADOWS.get())
//                .pattern(" # ")
//                .pattern("$%#")
//                .pattern("@$ ")
//                .define('#', TerramityModItems.DECAYED_BEDROCK_DUST.get())
//                .define('$', TerramityModItems.BLACK_MATTER.get())
//                .define('%', TerramityModItems.BAND_OF_DRIFTING.get())
//                .define('@', Items.IRON_PICKAXE)
//                .unlockedBy("has_decayed_bedrock_dust", has(TerramityModItems.DECAYED_BEDROCK_DUST.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.JACKPOT_HAMMER.get())
//                .pattern(" #&")
//                .pattern("$%^")
//                .pattern("@$ ")
//                .define('#', Items.DIAMOND)
//                .define('&', TerramityModItems.SLOT_MACHINE.get())
//                .define('$', TerramityModItems.FATEFUL_COIN.get())
//                .define('%', TerramityModItems.POKER_CHIP_BRACELETS.get())
//                .define('^', Items.EMERALD)
//                .define('@', Items.GOLDEN_AXE)
//                .unlockedBy("has_slot_machine", has(TerramityModItems.SLOT_MACHINE.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TERRA_BLADE_SWORD.get())
//                .pattern(" # ")
//                .pattern("#@#")
//                .pattern(" # ")
//                .define('#', TerramityModItems.VIRENTIUM_ALLOY_INGOT.get())
//                .define('@', TerramityModItems.HERO_SWORD.get())
//                .unlockedBy("has_hero_sword", has(TerramityModItems.HERO_SWORD.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TERRATOMERE_SWORD.get())
//                .pattern("%#%")
//                .pattern("#@#")
//                .pattern("%#%")
//                .define('#', TerramityModItems.VIRENTIUM_ALLOY_INGOT.get())
//                .define('%', TerramityModItems.GAIANITE_CLUSTER.get())
//                .define('@', BrutalityModItems.TERRA_BLADE_SWORD.get())
//                .unlockedBy("has_terra_blade", has(BrutalityModItems.TERRA_BLADE_SWORD.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CREASE_OF_CREATION_ITEM.get())
//                .pattern(" # ")
//                .pattern("#@#")
//                .pattern(" # ")
//                .define('#', TerramityModItems.COSMILITE_INGOT.get())
//                .define('@', TerramityModItems.GRAVITY_GAUNTLET.get())
//                .unlockedBy("has_gravity_gauntlet", has(TerramityModItems.GRAVITY_GAUNTLET.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUPERNOVA_SWORD.get())
//                .pattern(" #%")
//                .pattern("#@#")
//                .pattern("$# ")
//                .define('#', Items.GOLD_BLOCK)
//                .define('%', Items.AMETHYST_SHARD)
//                .define('@', TerramityModItems.NEUTRON_STAR.get())
//                .define('$', TerramityModItems.COSMILITE_SWORD.get())
//                .unlockedBy("has_neutron_star", has(TerramityModItems.NEUTRON_STAR.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ATOMIC_JUDGEMENT_HAMMER.get())
//                .pattern(" #%")
//                .pattern("#@#")
//                .pattern("$# ")
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('@', Items.TNT)
//                .define('%', TerramityModItems.VIRENTIUM_ROCKET.get())
//                .define('$', TerramityModItems.HELLROK_GIGATON_HAMMER.get())
//                .unlockedBy("has_virentium_rocket", has(TerramityModItems.VIRENTIUM_ROCKET.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BLADE_OF_THE_RUINED_KING.get())
//                .pattern(" #%")
//                .pattern("!@#")
//                .pattern("$! ")
//                .define('#', Items.ECHO_SHARD)
//                .define('@', TerramityModItems.WARDEN_SOUL.get())
//                .define('%', TerramityModItems.DIMLITE_INGOT.get())
//                .define('$', TerramityModItems.DIMLITE_SWORD.get())
//                .define('!', TerramityModItems.LOST_SOUL.get())
//                .unlockedBy("has_warden_soul", has(TerramityModItems.WARDEN_SOUL.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DARKIN_BLADE_SWORD.get())
//                .pattern(" #@")
//                .pattern("!@#")
//                .pattern("$! ")
//                .define('#', TerramityModItems.CHTHONIAN_VOID.get())
//                .define('@', Items.BASALT)
//                .define('$', TerramityModItems.HELLSPEC_SWORD.get())
//                .define('!', TerramityModItems.SPITEFUL_SOUL.get())
//                .unlockedBy("has_chtonian_void", has(TerramityModItems.CHTHONIAN_VOID.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DARKIN_SCYTHE.get())
//                .pattern("#@@")
//                .pattern("!#!")
//                .pattern("  $")
//                .define('#', TerramityModItems.CHTHONIAN_VOID.get())
//                .define('@', Items.BASALT)
//                .define('$', TerramityModItems.HELLSPEC_HOE.get())
//                .define('!', TerramityModItems.SPITEFUL_SOUL.get())
//                .unlockedBy("has_chtonian_void", has(TerramityModItems.CHTHONIAN_VOID.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RHITTA_AXE.get())
//                .pattern(" #@")
//                .pattern("$@#")
//                .pattern("&$ ")
//                .define('#', Items.SUNFLOWER)
//                .define('@', TerramityModItems.PROFANUM.get())
//                .define('$', TerramityModItems.TOPAZ.get())
//                .define('&', TerramityModItems.TOPAZ_AXE.get())
//                .unlockedBy("has_profanum", has(TerramityModItems.PROFANUM.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THE_CLOUD_ITEM.get())
//                .pattern(" # ")
//                .pattern("@$@")
//                .pattern(" # ")
//                .define('#', TerramityModItems.PRISMATIC_JEWEL.get())
//                .define('@', TerramityModItems.IRIDESCENT_SHARD.get())
//                .define('$', Items.WHITE_WOOL)
//                .unlockedBy("has_prismatic_jewel", has(TerramityModItems.PRISMATIC_JEWEL.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EVENT_HORIZON_LANCE.get())
//                .pattern(" #!")
//                .pattern("@$#")
//                .pattern("%@ ")
//                .define('!', TerramityModItems.BLACK_MATTER.get())
//                .define('#', TerramityModItems.VOID_ALLOY.get())
//                .define('@', TerramityModItems.PROFANUM.get())
//                .define('$', BrutalityModItems.POCKET_BLACK_HOLE.get())
//                .define('%', Items.NETHERITE_SHOVEL)
//                .unlockedBy("has_void_alloy", has(TerramityModItems.VOID_ALLOY.get()))
//                .save(consumer);
//
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FROSTMOURNE_SWORD.get())
//                .pattern(" #!")
//                .pattern("@$#")
//                .pattern("%@ ")
//                .define('!', TerramityModItems.SAPPHIRE.get())
//                .define('#', TerramityModItems.DAEMONIUM.get())
//                .define('$', TerramityModItems.SPECTRAL_SOUL.get())
//                .define('@', Items.BLUE_ICE)
//                .define('%', Items.NETHERITE_SWORD)
//                .unlockedBy("has_blue_ice", has(Items.BLUE_ICE))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GUNGNIR_TRIDENT.get())
//                .pattern(" #!")
//                .pattern(" $#")
//                .pattern("%  ")
//                .define('!', TerramityModItems.UNHOLY_LANCE.get())
//                .define('#', TerramityModItems.HELLSPEC_ALLOY.get())
//                .define('$', TerramityModItems.ARCHANGEL_HALO.get())
//                .define('%', TerramityModItems.STRATUS_STORM_RULER.get())
//                .unlockedBy("has_stratus_storm_ruler", has(TerramityModItems.STRATUS_STORM_RULER.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MURASAMA_SWORD.get())
//                .pattern(" ##")
//                .pattern("#$#")
//                .pattern("%# ")
//                .define('#', BrutalityModItems.HIGH_FREQUENCY_ALLOY.get())
//                .define('$', TerramityModItems.HELLSPEC_SWORD.get())
//                .define('%', TerramityModItems.MURASAMA.get())
//                .unlockedBy("has_murasama", has(TerramityModItems.MURASAMA.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PROVIDENCE_BOW.get())
//                .pattern("%@#")
//                .pattern("@$!")
//                .pattern("#! ")
//                .define('#', TerramityModItems.ANGEL_FEATHER.get())
//                .define('!', TerramityModItems.GILDED_FEATHER.get())
//                .define('$', Items.BOW)
//                .define('@', TerramityModItems.TOPAZ.get())
//                .define('%', TerramityModItems.ARCHANGEL_HALO.get())
//                .unlockedBy("has_archangel_halo", has(TerramityModItems.ARCHANGEL_HALO.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SEVENTH_STAR_SWORD.get())
//                .pattern(" @#")
//                .pattern("@$!")
//                .pattern("%! ")
//                .define('#', TerramityModItems.COSMILITE_SWORD.get())
//                .define('!', Items.AMETHYST_SHARD)
//                .define('$', Items.NETHER_STAR)
//                .define('@', TerramityModItems.TOPAZ.get())
//                .define('%', TerramityModItems.GOLD_NECKLACE.get())
//                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DOUBLE_DOWN_SWORD.get())
//                .pattern(" @*")
//                .pattern("#$!")
//                .pattern("&% ")
//                .define('@', Items.DIAMOND_BLOCK)
//                .define('*', Items.GOLD_BLOCK)
//                .define('#', Items.EMERALD_BLOCK)
//                .define('$', TerramityModItems.SLOT_MACHINE.get())
//                .define('%', Items.REDSTONE_BLOCK)
//                .define('!', Items.LAPIS_BLOCK)
//                .define('&', TerramityModItems.STRATUS_STORM_RULER.get())
//                .unlockedBy("has_slot_machine", has(TerramityModItems.SLOT_MACHINE.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.OLD_GPU_AXE.get())
//                .pattern("!@#")
//                .pattern(" @#")
//                .pattern("$@#")
//                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('!', Items.COPPER_INGOT)
//                .define('$', Items.GOLD_INGOT)
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PAPER_CUT_SWORD.get())
//                .pattern(" # ")
//                .pattern(" # ")
//                .pattern(" # ")
//                .define('#', Items.PAPER)
//                .unlockedBy("has_paper", has(Items.PAPER))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WHISPERWALTZ_SWORD.get())
//                .pattern(" @#")
//                .pattern("@#@")
//                .pattern("$@ ")
//                .define('#', TerramityModItems.REVERIUM.get())
//                .define('@', TerramityModItems.IRIDESCENT_SHARD.get())
//                .define('$', TerramityModItems.REVERIUM_SWORD.get())
//                .unlockedBy("has_reverium", has(TerramityModItems.REVERIUM.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SPATULA_HAMMER.get())
//                .pattern(" ##")
//                .pattern(" ##")
//                .pattern("!  ")
//                .define('#', Items.IRON_INGOT)
//                .define('!', Items.STICK)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THE_GOLDEN_SPATULA_HAMMER.get())
//                .pattern(" #!")
//                .pattern("$@#")
//                .pattern("&$ ")
//                .define('#', TerramityModItems.TOPAZ_BLOCK.get())
//                .define('!', TerramityModItems.TOPAZ.get())
//                .define('@', TerramityModItems.PROFANUM.get())
//                .define('$', Items.GOLD_INGOT)
//                .define('&', TerramityModItems.TOPAZ_SHOVEL.get())
//                .unlockedBy("has_profanum", has(TerramityModItems.PROFANUM.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.IRON_KNIFE_SWORD.get())
//                .pattern("  #")
//                .pattern(" @ ")
//                .pattern("!  ")
//                .define('#', Items.IRON_INGOT)
//                .define('@', Items.IRON_BLOCK)
//                .define('!', Items.STICK)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GOLD_KNIFE_SWORD.get())
//                .pattern("  #")
//                .pattern(" @ ")
//                .pattern("!  ")
//                .define('#', Items.GOLD_INGOT)
//                .define('@', Items.GOLD_BLOCK)
//                .define('!', Items.STICK)
//                .unlockedBy("has_gold", has(Items.GOLD_INGOT))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DIAMOND_KNIFE_SWORD.get())
//                .pattern("  #")
//                .pattern(" @ ")
//                .pattern("!  ")
//                .define('#', Items.DIAMOND)
//                .define('@', Items.DIAMOND_BLOCK)
//                .define('!', Items.STICK)
//                .unlockedBy("has_diamonds", has(Items.DIAMOND))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.VOID_KNIFE_SWORD.get())
//                .pattern(" ##")
//                .pattern("###")
//                .pattern("!# ")
//                .define('#', TerramityModItems.VOID_ALLOY.get())
//                .define('!', BrutalityModItems.DIAMOND_KNIFE_SWORD.get())
//                .unlockedBy("has_void_alloy", has(TerramityModItems.VOID_ALLOY.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FRYING_PAN_HAMMER.get())
//                .pattern(" # ")
//                .pattern("#@#")
//                .pattern("!# ")
//                .define('#', Items.IRON_INGOT)
//                .define('@', Items.IRON_BLOCK)
//                .define('!', Items.STICK)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WHISK_HAMMER.get())
//                .pattern(" ##")
//                .pattern(" @#")
//                .pattern("!  ")
//                .define('#', Items.IRON_BARS)
//                .define('@', TerramityModItems.GRATE.get())
//                .define('!', Items.STICK)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.POTATO_MASHER_HAMMER.get())
//                .pattern(" #@")
//                .pattern(" ##")
//                .pattern("!  ")
//                .define('#', Items.IRON_BARS)
//                .define('@', Items.IRON_TRAPDOOR)
//                .define('!', Items.STICK)
//                .unlockedBy("has_iron", has(Items.IRON_INGOT))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SALT_SHAKER_CHARM.get())
//                .pattern(" # ")
//                .pattern("!@!")
//                .pattern("!!!")
//                .define('#', Items.IRON_TRAPDOOR)
//                .define('@', TerramityModItems.FAIRY_DUST.get())
//                .define('!', Items.GLASS)
//                .unlockedBy("has_fairy_dust", has(TerramityModItems.FAIRY_DUST.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PEPPER_SHAKER_CHARM.get())
//                .pattern(" # ")
//                .pattern("!@!")
//                .pattern("!!!")
//                .define('#', Items.IRON_TRAPDOOR)
//                .define('@', TerramityModItems.DECAYED_BEDROCK_DUST.get())
//                .define('!', Items.GLASS)
//                .unlockedBy("has_decayed_bedrock_dust", has(TerramityModItems.DECAYED_BEDROCK_DUST.get()))
//                .save(consumer);
//
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.SALT_AND_PEPPER_CHARM.get())
//                .requires(BrutalityModItems.SALT_SHAKER_CHARM.get())
//                .requires(BrutalityModItems.PEPPER_SHAKER_CHARM.get())
//                .unlockedBy("has_pepper_shaker", has(BrutalityModItems.PEPPER_SHAKER_CHARM.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MORTAR_AND_PESTLE_CHARM.get())
//                .pattern(" # ")
//                .pattern("!@!")
//                .pattern("!!!")
//                .define('#', Items.NETHERITE_SHOVEL)
//                .define('!', TerramityModItems.DAEMONIUM_BRICKS.get())
//                .define('@', Items.BOWL)
//                .unlockedBy("has_daemonium", has(TerramityModItems.DAEMONIUM.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BUTTER_GAUNTLETS_HANDS.get())
//                .pattern("###")
//                .pattern("#@#")
//                .pattern("!!!")
//                .define('#', Items.GOLD_INGOT)
//                .define('!', TerramityModItems.TOPAZ.get())
//                .define('@', TerramityModItems.HELLROK_GAUNTLET.get())
//                .unlockedBy("has_hellrok_gauntlet", has(TerramityModItems.HELLROK_GAUNTLET.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CHEESE_SAUCE_CHARM.get())
//                .pattern(" # ")
//                .pattern("!@!")
//                .pattern("!$!")
//                .define('#', Items.IRON_TRAPDOOR)
//                .define('!', Items.YELLOW_CONCRETE)
//                .define('$', Items.MILK_BUCKET)
//                .define('@', Items.HONEY_BOTTLE)
//                .unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.TOMATO_SAUCE_CHARM.get())
//                .pattern(" # ")
//                .pattern("!@!")
//                .pattern("!$!")
//                .define('#', Items.IRON_TRAPDOOR)
//                .define('!', Items.RED_CONCRETE)
//                .define('$', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
//                .define('@', Items.GLASS_BOTTLE)
//                .unlockedBy("has_stygian_blood_bucket", has(TerramityModItems.STYGIAN_BLOOD_BUCKET.get()))
//                .save(consumer);
//
//        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BrutalityModItems.PIZZA_SLOP_CHARM.get())
//                .requires(BrutalityModItems.CHEESE_SAUCE_CHARM.get())
//                .requires(BrutalityModItems.TOMATO_SAUCE_CHARM.get())
//                .unlockedBy("has_tomato_sauce", has(BrutalityModItems.TOMATO_SAUCE_CHARM.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HOT_SAUCE_CHARM.get())
//                .pattern(" # ")
//                .pattern("!@!")
//                .pattern("!$!")
//                .define('#', Items.IRON_TRAPDOOR)
//                .define('!', Items.ORANGE_CONCRETE)
//                .define('$', Items.LAVA_BUCKET)
//                .define('@', Items.GLASS_BOTTLE)
//                .unlockedBy("has_lava_bucket", has(Items.LAVA_BUCKET))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.OLIVE_OIL_CHARM.get())
//                .pattern(" # ")
//                .pattern("#@#")
//                .pattern(" # ")
//                .define('#', Items.ACACIA_SAPLING)
//                .define('@', TerramityModItems.EXTREMELY_THICK_OIL.get())
//                .unlockedBy("has_extremely_thick_oil", has(TerramityModItems.EXTREMELY_THICK_OIL.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EXTRA_VIRGIN_OLIVE_OIL_CHARM.get())
//                .pattern("$#$")
//                .pattern("#@#")
//                .pattern("$#$")
//                .define('#', Items.ACACIA_SAPLING)
//                .define('$', TerramityModItems.HERMIT_BEAN.get())
//                .define('@', BrutalityModItems.OLIVE_OIL_CHARM.get())
//                .unlockedBy("has_olive_oil", has(BrutalityModItems.OLIVE_OIL_CHARM.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FRIDGE_CHARM.get())
//                .pattern("##@")
//                .pattern("##@")
//                .pattern("##@")
//                .define('#', Items.IRON_BLOCK)
//                .define('@', Items.IRON_DOOR)
//                .unlockedBy("has_iron_block", has(Items.IRON_BLOCK))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SMART_FRIDGE_CHARM.get())
//                .pattern("#!^")
//                .pattern("#$ ")
//                .pattern("#!@")
//                .define('#', TerramityModItems.REINFORCED_BLACKSTONE_BRICKS.get())
//                .define('^', Items.LAPIS_LAZULI)
//                .define('@', Items.REDSTONE)
//                .define('!', Items.NETHERITE_INGOT)
//                .define('$', BrutalityModItems.FRIDGE_CHARM.get())
//                .unlockedBy("has_fridge", has(BrutalityModItems.FRIDGE_CHARM.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SCIENTIFIC_CALCULATOR_BELT.get())
//                .pattern("!#^")
//                .pattern("#$#")
//                .pattern("&#@")
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('^', Items.EMERALD)
//                .define('@', TerramityModItems.SAPPHIRE.get())
//                .define('!', TerramityModItems.TOPAZ.get())
//                .define('&', TerramityModItems.RUBY.get())
//                .define('$', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ADDITION_CHARM.get())
//                .pattern(" # ")
//                .pattern("#@#")
//                .pattern(" # ")
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SUBTRACTION_CHARM.get())
//                .pattern("   ")
//                .pattern("#@#")
//                .pattern("   ")
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MULTIPLICATION_CHARM.get())
//                .pattern("# #")
//                .pattern(" @ ")
//                .pattern("# #")
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DIVISION_CHARM.get())
//                .pattern(" @ ")
//                .pattern("###")
//                .pattern(" @ ")
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('@', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PI_CHARM.get())
//                .pattern("@@@")
//                .pattern("@#@")
//                .pattern("!!!")
//                .define('@', TerramityModItems.CONDUCTITE.get())
//                .define('!', Items.PUMPKIN_PIE)
//                .define('#', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EXPONENTIAL_CHARM.get())
//                .pattern(" !@")
//                .pattern("#  ")
//                .pattern("@  ")
//                .define('@', TerramityModItems.CONDUCTITE.get())
//                .define('!', TerramityModItems.TERAWATT_BRACERS.get())
//                .define('#', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SINE_CHARM.get())
//                .pattern(" !@")
//                .pattern("#  ")
//                .pattern("@  ")
//                .define('@', TerramityModItems.CONDUCTITE.get())
//                .define('#', TerramityModItems.SAPPHIRE_BLOCK.get())
//                .define('!', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.COSINE_CHARM.get())
//                .pattern("@! ")
//                .pattern("  #")
//                .pattern("  @")
//                .define('@', TerramityModItems.CONDUCTITE.get())
//                .define('#', TerramityModItems.RUBY_BLOCK.get())
//                .define('!', TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get())
//                .unlockedBy("has_conductite_upgrade_smithing_template", has(TerramityModItems.CONDUCTITE_UPGRADE_SMITHING_TEMPLATE.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WOODEN_RULER.get())
//                .pattern("  #")
//                .pattern(" ! ")
//                .pattern("@  ")
//                .define('@', Items.WOODEN_SWORD)
//                .define('#', TerramityModItems.CARDBOARD.get())
//                .define('!', Items.STRIPPED_OAK_LOG)
//                .unlockedBy("has_cardboard", has(TerramityModItems.CARDBOARD.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.METAL_RULER.get())
//                .pattern("  #")
//                .pattern(" ! ")
//                .pattern("@  ")
//                .define('@', Items.IRON_SWORD)
//                .define('#', TerramityModItems.DAEMONIUM.get())
//                .define('!', Items.IRON_INGOT)
//                .unlockedBy("has_daemonium", has(TerramityModItems.DAEMONIUM.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PORTABLE_MINING_RIG_CHARM.get())
//                .pattern("#$#")
//                .pattern("@!@")
//                .pattern("#$#")
//                .define('@', TerramityModItems.DAEMONIUM.get())
//                .define('#', Items.POLISHED_BLACKSTONE)
//                .define('$', TerramityModItems.CONDUCTITE.get())
//                .define('!', TerramityModItems.FATEFUL_COIN.get())
//                .unlockedBy("has_fateful_coin", has(TerramityModItems.FATEFUL_COIN.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CRYPTO_WALLET_CHARM.get())
//                .pattern("#@#")
//                .pattern("@!@")
//                .pattern("#@#")
//                .define('@', TerramityModItems.TOPAZ.get())
//                .define('#', Items.GOLD_BLOCK)
//                .define('!', TerramityModItems.FATEFUL_COIN.get())
//                .unlockedBy("has_fateful_coin", has(TerramityModItems.FATEFUL_COIN.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ROAD_RUNNERS_RING.get())
//                .pattern("#@#")
//                .pattern("@!@")
//                .pattern("#@#")
//                .define('@', TerramityModItems.RUSH_RING_PLUS.get())
//                .define('#', TerramityModItems.SAPPHIRE.get())
//                .define('!', TerramityModItems.GILDED_FEATHER.get())
//                .unlockedBy("has_rush_ring_plus", has(TerramityModItems.RUSH_RING_PLUS.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.RESPLENDENT_FEATHER_CHARM.get())
//                .pattern(" @ ")
//                .pattern("#!#")
//                .pattern(" @ ")
//                .define('@', TerramityModItems.PROFANUM.get())
//                .define('#', Items.LAVA_BUCKET)
//                .define('!', TerramityModItems.ANGEL_FEATHER.get())
//                .unlockedBy("has_profanum", has(TerramityModItems.PROFANUM.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NANOMACHINES_HANDS.get())
//                .pattern("#@#")
//                .pattern("@!@")
//                .pattern("#@#")
//                .define('@', TerramityModItems.BLACK_MATTER.get())
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('!', Items.BLACK_DYE)
//                .unlockedBy("has_black_matter", has(TerramityModItems.BLACK_MATTER.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ABYSSAL_NECKLACE.get())
//                .pattern("#@#")
//                .pattern("@!@")
//                .pattern(" % ")
//                .define('#', TerramityModItems.ONYX.get())
//                .define('@', Items.KELP)
//                .define('!', TerramityModItems.GOLD_NECKLACE.get())
//                .define('%', Items.HEART_OF_THE_SEA)
//                .unlockedBy("has_heart_of_the_sea", has(Items.HEART_OF_THE_SEA))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.LAST_PRISM_ITEM.get())
//                .pattern(" @ ")
//                .pattern("#!#")
//                .pattern("#%#")
//                .define('#', TerramityModItems.NYXIUM_GLASS.get())
//                .define('@', TerramityModItems.PRISMATIC_CRYSTAL_CLUSTER.get())
//                .define('!', TerramityModItems.IRIDESCENT_SHARD.get())
//                .define('%', TerramityModItems.PRISMATIC_JEWEL.get())
//                .unlockedBy("has_nyxium_glass", has(TerramityModItems.NYXIUM_GLASS.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.EXOBLADE_SWORD.get())
//                .pattern(" @@")
//                .pattern("#!@")
//                .pattern("^# ")
//                .define('#', TerramityModItems.EXODIUM_SUPERALLOY.get())
//                .define('@', TerramityModItems.PRISMATIC_JEWEL.get())
//                .define('!', BrutalityModItems.TERRATOMERE_SWORD.get())
//                .define('^', TerramityModItems.NYXIUM_GREATSWORD.get())
//                .unlockedBy("has_terratomere", has(BrutalityModItems.TERRATOMERE_SWORD.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BIOMECH_REACTOR_TRIDENT.get())
//                .pattern("#@#")
//                .pattern("@%@")
//                .pattern("#@#")
//                .define('@', TerramityModItems.NYXIUM.get())
//                .define('#', TerramityModItems.VIRENTIUM_ALLOY_INGOT.get())
//                .define('%', TerramityModItems.PRISMATIC_JEWEL.get())
//                .unlockedBy("has_nyxium", has(TerramityModItems.NYXIUM.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.THUNDERBOLT_TRIDENT.get())
//                .pattern("#@#")
//                .pattern("@%@")
//                .pattern("#@#")
//                .define('@', TerramityModItems.TOPAZ.get())
//                .define('#', TerramityModItems.CONDUCTITE.get())
//                .define('%', Items.TRIDENT)
//                .unlockedBy("has_conductite", has(TerramityModItems.CONDUCTITE.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get())
//                .pattern(" ^#")
//                .pattern("%@^")
//                .pattern("!% ")
//                .define('%', Items.KELP)
//                .define('@', Items.TRIDENT)
//                .define('#', Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE)
//                .define('!', Items.NETHERITE_AXE)
//                .define('^', Items.ROTTEN_FLESH)
//                .unlockedBy("has_tide_armor_trim_smithing_template", has(Items.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SECOND_HEART.get())
//                .pattern("   ")
//                .pattern("%@^")
//                .pattern("   ")
//                .define('%', TerramityModItems.ANALEPTIC_AMPHORA.get())
//                .define('@', TerramityModItems.CRYSTAL_HEART.get())
//                .define('^', TerramityModItems.STYGIAN_BLOOD_BUCKET.get())
//                .unlockedBy("has_crystal_heart", has(TerramityModItems.CRYSTAL_HEART.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.HEART_OF_GOLD.get())
//                .pattern("###")
//                .pattern("#@#")
//                .pattern("###")
//                .define('@', TerramityModItems.CRYSTAL_HEART.get())
//                .define('#', Items.GOLD_BLOCK)
//                .unlockedBy("has_second_heart", has(BrutalityModItems.SECOND_HEART.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.FROZEN_HEART.get())
//                .pattern("###")
//                .pattern("#@#")
//                .pattern("###")
//                .define('@', TerramityModItems.CRYSTAL_HEART.get())
//                .define('#', Items.BLUE_ICE)
//                .unlockedBy("has_second_heart", has(BrutalityModItems.SECOND_HEART.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.ROYAL_GUARDIAN_SWORD.get())
//                .pattern("  #")
//                .pattern("^@ ")
//                .pattern("!^ ")
//                .define('@', TerramityModItems.EXODIUM_SWORD.get())
//                .define('#', TerramityModItems.NYXIUM_GREATSWORD.get())
//                .define('!', TerramityModItems.REVERIUM_SWORD.get())
//                .define('^', TerramityModItems.DECAYED_BEDROCK.get())
//                .unlockedBy("has_exodium_sword", has(TerramityModItems.EXODIUM_SWORD.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_HELMET.get())
//                .pattern("###")
//                .pattern("#@#")
//                .pattern("$$$")
//                .define('@', TerramityModItems.BLACK_MATTER.get())
//                .define('#', Items.LEATHER_HELMET)
//                .define('$', Items.BLACK_DYE)
//                .unlockedBy("has_black_matter", has(TerramityModItems.BLACK_MATTER.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_CHESTPLATE.get())
//                .pattern("###")
//                .pattern("#@#")
//                .pattern("$$$")
//                .define('@', TerramityModItems.BLACK_MATTER.get())
//                .define('#', Items.LEATHER_CHESTPLATE)
//                .define('$', Items.BLACK_DYE)
//                .unlockedBy("has_black_matter", has(TerramityModItems.BLACK_MATTER.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_LEGGINGS.get())
//                .pattern("#!#")
//                .pattern("#@#")
//                .pattern("$$$")
//                .define('@', TerramityModItems.BLACK_MATTER.get())
//                .define('#', Items.LEATHER_HELMET)
//                .define('$', Items.BLACK_DYE)
//                .define('!', TerramityModItems.SHINOBI_SASH.get())
//                .unlockedBy("has_black_matter", has(TerramityModItems.BLACK_MATTER.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.NOIR_BOOTS.get())
//                .pattern("###")
//                .pattern("%@%")
//                .pattern("$$$")
//                .define('@', TerramityModItems.BLACK_MATTER.get())
//                .define('#', Items.LEATHER_HELMET)
//                .define('%', Items.BLACK_DYE)
//                .define('$', Items.BLACK_WOOL)
//                .unlockedBy("has_black_matter", has(TerramityModItems.BLACK_MATTER.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SHADOWSTEP_SWORD.get())
//                .pattern("#@#")
//                .pattern("@$@")
//                .pattern("#@#")
//                .define('@', TerramityModItems.BLACK_MATTER.get())
//                .define('#', Items.ENDER_PEARL)
//                .define('$', Items.NETHERITE_SWORD)
//                .unlockedBy("has_black_matter", has(TerramityModItems.BLACK_MATTER.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.APPLE_CORE_LANCE.get())
//                .pattern(" @#")
//                .pattern("##@")
//                .pattern("!# ")
//                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
//                .define('#', Items.APPLE)
//                .define('!', Items.STICK)
//                .unlockedBy("has_daemonium_chunk", has(TerramityModItems.DAEMONIUM_CHUNK.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MELONCHOLY_SWORD.get())
//                .pattern(" @#")
//                .pattern("&#@")
//                .pattern("!& ")
//                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
//                .define('#', Items.MELON)
//                .define('&', Items.MELON_SLICE)
//                .define('!', Items.STICK)
//                .unlockedBy("has_daemonium_chunk", has(TerramityModItems.DAEMONIUM_CHUNK.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.PLUNDER_CHEST_CHARM.get())
//                .pattern("#@#")
//                .pattern("&%&")
//                .pattern("!!!")
//                .define('@', TerramityModItems.GILDED_FEATHER.get())
//                .define('#', TerramityModItems.TOPAZ.get())
//                .define('%', Items.CHEST)
//                .define('&', Items.GOLD_INGOT)
//                .define('!', Items.GOLD_BLOCK)
//                .unlockedBy("has_gilded_feather", has(TerramityModItems.GILDED_FEATHER.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CHOPSTICK_STAFF.get())
//                .pattern("# #")
//                .pattern(" @ ")
//                .pattern("# #")
//                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
//                .define('#', Items.STICK)
//                .unlockedBy("has_daemonium_chunk", has(TerramityModItems.DAEMONIUM_CHUNK.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.BAMBOO_STAFF.get())
//                .pattern(" @#")
//                .pattern("@#@")
//                .pattern("#@ ")
//                .define('@', TerramityModItems.DAEMONIUM_CHUNK.get())
//                .define('#', Items.BAMBOO)
//                .unlockedBy("has_daemonium_chunk", has(TerramityModItems.DAEMONIUM_CHUNK.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.SERAPHIM_HALO.get())
//                .pattern("#@#")
//                .pattern("@$@")
//                .pattern("#@#")
//                .define('#', Items.GLOWSTONE)
//                .define('@', TerramityModItems.TOPAZ.get())
//                .define('$', TerramityModItems.ARCHANGEL_HALO.get())
//                .unlockedBy("has_archangel_halo", has(TerramityModItems.ARCHANGEL_HALO.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BrutalityModItems.CELESTIAL_STARBOARD.get())
//                .pattern("#@#")
//                .pattern("@$@")
//                .pattern("#@#")
//                .define('@', TerramityModItems.REVERIUM.get())
//                .define('#', TerramityModItems.TOPAZ.get())
//                .define('$', Items.NETHER_STAR)
//                .unlockedBy("has_nether_star", has(Items.NETHER_STAR))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.WOOLY_BLINDFOLD.get())
//                .pattern("   ")
//                .pattern("###")
//                .pattern("   ")
//                .define('#', Items.BLACK_WOOL)
//                .unlockedBy("has_black_wool", has(Items.BLACK_WOOL))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.GOLDEN_HEADBAND.get())
//                .pattern("   ")
//                .pattern("#@#")
//                .pattern(" % ")
//                .define('#', TerramityModItems.TOPAZ.get())
//                .define('@', TerramityModItems.ARCHANGEL_HALO.get())
//                .define('%', Items.GOLDEN_HELMET)
//                .unlockedBy("has_archangel_halo", has(TerramityModItems.ARCHANGEL_HALO.get()))
//                .save(consumer);
//
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.MARIANAS_TRENCH_SWORD.get())
//                .pattern("  #")
//                .pattern("!@#")
//                .pattern("$  ")
//                .define('#', TerramityModItems.DIMLITE_INGOT.get())
//                .define('@', Items.HEART_OF_THE_SEA)
//                .define('!', Items.NAUTILUS_SHELL)
//                .define('$', BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get())
//                .unlockedBy("has_depth_crusher", has(BrutalityModItems.DEPTH_CRUSHER_TRIDENT.get()))
//                .save(consumer);
//        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BrutalityModItems.CHALLENGER_DEEP_SWORD.get())
//                .pattern("  #")
//                .pattern("%# ")
//                .pattern("$% ")
//                .define('#', TerramityModItems.EXODIUM_SUPERALLOY.get())
//                .define('%', Items.ENDER_EYE)
//                .define('$', BrutalityModItems.MARIANAS_TRENCH_SWORD.get())
//                .unlockedBy("has_marianas_trench", has(BrutalityModItems.MARIANAS_TRENCH_SWORD.get()))
//                .save(consumer);
//    }
//
//    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
//        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
//    }
//
//    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
//        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
//    }
//
//    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
//        Iterator var9 = pIngredients.iterator();
//
//        while(var9.hasNext()) {
//            ItemLike itemlike = (ItemLike)var9.next();
//            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
//                    .group(pGroup)
//                    .unlockedBy(getHasName(itemlike), has(itemlike))
//                    .save(pFinishedRecipeConsumer, Brutality.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
//        }
//
//    }
//}
