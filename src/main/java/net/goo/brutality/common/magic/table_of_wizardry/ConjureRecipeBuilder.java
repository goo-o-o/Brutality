package net.goo.brutality.common.magic.table_of_wizardry;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.goo.brutality.common.registry.BrutalityRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConjureRecipeBuilder {
    private final ResourceLocation spellId;
    private final NonNullList<Ingredient> ingredients = NonNullList.withSize(8, Ingredient.EMPTY);
    private List<EntityType<?>> entities = null;
    private int mana = 0;

    public ConjureRecipeBuilder(ResourceLocation spellId) {
        this.spellId = spellId;
    }

    public static ConjureRecipeBuilder conjure(ResourceLocation spellId) {
        return new ConjureRecipeBuilder(spellId);
    }



    public ConjureRecipeBuilder requires(int slot, ItemLike item) {
        this.ingredients.set(slot, Ingredient.of(item));
        return this;
    }

    public ConjureRecipeBuilder withEntity(EntityType<?> entity) {
        if (entities == null) this.entities = new ArrayList<>();
        entities.add(entity);
        return this;
    }

    public ConjureRecipeBuilder mana(int mana) {
        this.mana = mana;
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        // The spellId passed here is already in the format "brutality:school/name"
        consumer.accept(new Result(this.spellId, ingredients, entities, mana));
    }

    // Internal Result class that handles the actual JSON generation
    private record Result(ResourceLocation id, NonNullList<Ingredient> ingredients,
                          List<EntityType<?>> entityTypes, int mana) implements FinishedRecipe {
        @Override
        public void serializeRecipeData(JsonObject json) {
            JsonArray ingredientArray = new JsonArray();
            for (int i = 0; i < ingredients.size(); i++) {
                if (!ingredients.get(i).isEmpty()) {
                    JsonObject obj = new JsonObject();
                    obj.addProperty("slot", i);
                    obj.add("ingredient", ingredients.get(i).toJson());
                    ingredientArray.add(obj);
                }
            }
            json.add("ingredients", ingredientArray);

            if (entityTypes != null && !entityTypes.isEmpty()) {
                JsonArray entitiesArray = new JsonArray();
                for (EntityType<?> type : entityTypes) {
                    ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(type);
                    if (entityId != null) entitiesArray.add(entityId.toString());
                }
                json.add("entities", entitiesArray);
            }

            json.addProperty("mana", mana);

            JsonObject output = new JsonObject();
            output.addProperty("spell", id.toString());
            json.add("output", output);
        }

        @Override public @NotNull ResourceLocation getId() { return id; }
        @Override public @NotNull RecipeSerializer<?> getType() { return BrutalityRecipes.CONJURE_SERIALIZER.get(); }
        @Nullable
        @Override public JsonObject serializeAdvancement() { return null; }
        @Nullable @Override public ResourceLocation getAdvancementId() { return null; }
    }
}