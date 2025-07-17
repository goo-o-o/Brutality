package net.goo.brutality.datagen;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.ModItems;
import net.mcreator.terramity.init.TerramityModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {


    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DULL_KNIFE_DAGGER.get())
                .pattern(" # ")
                .pattern("$%@")
                .pattern(" ^ ")
                .define('#', TerramityModItems.SPECTRAL_SOUL.get())
                .define('$', TerramityModItems.ACCURSED_SOUL.get())
                .define('%', Items.NETHERITE_SWORD)
                .define('@', TerramityModItems.LOST_SOUL.get())
                .define('^', TerramityModItems.SPITEFUL_SOUL.get())
                .unlockedBy("has_netherite_sword", has(Items.NETHERITE_SWORD))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DULL_KNIFE_DAGGER.get())
                .pattern(" # ")
                .pattern("$%#")
                .pattern("@$ ")
                .define('#', TerramityModItems.DECAYED_BEDROCK_DUST.get())
                .define('$', TerramityModItems.BLACK_MATTER.get())
                .define('%', TerramityModItems.BAND_OF_DRIFTING.get())
                .define('@', Items.IRON_PICKAXE)
                .unlockedBy("has_netherite_sword", has(TerramityModItems.DECAYED_BEDROCK_DUST.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.DULL_KNIFE_DAGGER.get())
                .pattern(" # ")
                .pattern("$%#")
                .pattern("@$ ")
                .define('#', TerramityModItems.DECAYED_BEDROCK_DUST.get())
                .define('$', TerramityModItems.BLACK_MATTER.get())
                .define('%', TerramityModItems.BAND_OF_DRIFTING.get())
                .define('@', Items.IRON_PICKAXE)
                .unlockedBy("has_netherite_sword", has(TerramityModItems.DECAYED_BEDROCK_DUST.get()))
                .save(consumer);



    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        Iterator var9 = pIngredients.iterator();

        while(var9.hasNext()) {
            ItemLike itemlike = (ItemLike)var9.next();
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer)
                    .group(pGroup)
                    .unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, Brutality.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }
}
