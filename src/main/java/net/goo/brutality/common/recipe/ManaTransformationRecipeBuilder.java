package net.goo.brutality.common.recipe;

import com.google.gson.JsonObject;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ManaTransformationRecipeBuilder {
    private final Ingredient input;
    private final ItemStack result;

    public ManaTransformationRecipeBuilder(Ingredient input, ItemLike result, int count) {
        this.input = input;
        this.result = new ItemStack(result, count);
    }

    public static ManaTransformationRecipeBuilder transform(Ingredient input, ItemLike result) {
        return new ManaTransformationRecipeBuilder(input, result, 1);
    }

    public static ManaTransformationRecipeBuilder transform(Ingredient input, ItemLike result, int count) {
        return new ManaTransformationRecipeBuilder(input, result, count);
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        consumer.accept(new Result(id, input, result));
    }
    public void save(Consumer<FinishedRecipe> consumer) {
        String inputName = input.getItems().length > 0
                ? ForgeRegistries.ITEMS.getKey(input.getItems()[0].getItem()).getPath()
                : "unknown";

        String resultName = ForgeRegistries.ITEMS.getKey(result.getItem()).getPath();

        ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(
                Brutality.MOD_ID,
                "mana_transform/" + inputName + "_to_" + resultName
        );

        consumer.accept(new Result(recipeId, input, result));
    }
    private record Result(ResourceLocation id, Ingredient input, ItemStack result) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            json.add("input", input.toJson());

            JsonObject resultObj = new JsonObject();
            resultObj.addProperty("item", net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(result.getItem()).toString());
            if (result.getCount() > 1) {
                resultObj.addProperty("count", result.getCount());
            }
            json.add("output", resultObj);
        }

        @Override public @NotNull ResourceLocation getId() { return id; }
        @Override public @NotNull RecipeSerializer<?> getType() { return BrutalityRecipes.MANA_TRANSFORMATION_SERIALIZER.get(); }
        @Nullable @Override public JsonObject serializeAdvancement() { return null; }
        @Nullable @Override public ResourceLocation getAdvancementId() { return null; }
    }
}