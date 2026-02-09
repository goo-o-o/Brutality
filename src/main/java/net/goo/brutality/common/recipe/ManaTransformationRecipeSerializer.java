package net.goo.brutality.common.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class ManaTransformationRecipeSerializer implements RecipeSerializer<ManaTransformationRecipe> {

    @Override
    public ManaTransformationRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
        Ingredient input = Ingredient.fromJson(pSerializedRecipe.get("input"));
        ItemStack output = ShapedRecipe.itemStackFromJson(pSerializedRecipe.getAsJsonObject("output"));

        return new ManaTransformationRecipe(pRecipeId, input, output);
    }

    @Override
    public @Nullable ManaTransformationRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        Ingredient input = Ingredient.fromNetwork(pBuffer);
        ItemStack output = pBuffer.readItem();
        return new ManaTransformationRecipe(pRecipeId, input, output);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, ManaTransformationRecipe pRecipe) {
        pRecipe.input().toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.output());
    }
}