package net.goo.brutality.common.recipe;

import net.goo.brutality.common.registry.BrutalityRecipes;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public record ManaTransformationRecipe(ResourceLocation id, Ingredient input, ItemStack output) implements Recipe<Container> {
    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return this.input.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return this.output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return this.output;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BrutalityRecipes.MANA_TRANSFORMATION_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return BrutalityRecipes.MANA_TRANSFORMATION_TYPE.get();
    }


}
