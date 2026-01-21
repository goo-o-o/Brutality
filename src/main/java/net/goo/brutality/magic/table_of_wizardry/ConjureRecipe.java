package net.goo.brutality.magic.table_of_wizardry;

import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.registry.BrutalityRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.List;
public record ConjureRecipe(
        ResourceLocation id, // Standardized as "brutality:school/spell_name"
        NonNullList<Ingredient> ingredients,
        List<EntityType<?>> requiredEntities,
        int mana,
        IBrutalitySpell spell
) implements Recipe<Container> {

    public ConjureRecipe {
        requiredEntities = List.copyOf(requiredEntities);
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    public boolean entityMatches(Level level, BlockPos pos) {
        if (this.requiredEntities == null || this.requiredEntities.isEmpty()) return true;

        AABB area = new AABB(pos).inflate(3.0);
        List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, area, Entity::isAlive);

        // Create a copy to track "consumed" entities in the check
        List<EntityType<?>> missing = new java.util.ArrayList<>(this.requiredEntities);

        for (LivingEntity found : nearbyEntities) {
            // If we found an entity type we need, remove it from the 'missing' list
            missing.remove(found.getType());
            if (missing.isEmpty()) return true; // Optimization: Exit early if all requirements met
        }

        return missing.isEmpty();
    }

    @Override
    public boolean matches(Container container, Level level) {
        // Optimization: Check container size once
        int containerSize = container.getContainerSize();
        int ingredientCount = this.ingredients.size();

        // Ensure we don't index out of bounds if the container is smaller than 8 slots
        int maxCheck = Math.min(containerSize, ingredientCount);

        for (int i = 0; i < maxCheck; i++) {
            Ingredient required = this.ingredients.get(i);
            ItemStack provided = container.getItem(i);

            // Ingredient.test() handles empty/Ingredient.EMPTY correctly,
            // but explicit empty checks are slightly faster for large lists.
            if (!required.test(provided)) {
                return false;
            }
        }



        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return BrutalityRecipes.CONJURE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return BrutalityRecipes.CONJURE_TYPE.get();
    }

    @Override
    public @NotNull ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

}