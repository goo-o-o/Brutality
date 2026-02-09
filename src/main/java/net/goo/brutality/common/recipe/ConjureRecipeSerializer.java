package net.goo.brutality.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalitySpells;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConjureRecipeSerializer implements RecipeSerializer<ConjureRecipe> {
    @Override
    public @NotNull ConjureRecipe fromJson(ResourceLocation id, JsonObject json) {
        // Standard ingredient parsing
        NonNullList<Ingredient> ingredients = NonNullList.withSize(8, Ingredient.EMPTY);
        JsonArray ingredientsArray = GsonHelper.getAsJsonArray(json, "ingredients");
        for (int i = 0; i < ingredientsArray.size(); i++) {
            JsonObject obj = ingredientsArray.get(i).getAsJsonObject();
            int slot = GsonHelper.getAsInt(obj, "slot");
            ingredients.set(slot, Ingredient.fromJson(obj.get("ingredient")));
        }

        // Entity parsing
        List<EntityType<?>> entities = new java.util.ArrayList<>();
        if (json.has("entities")) {
            JsonArray entityArray = GsonHelper.getAsJsonArray(json, "entities");
            for (int i = 0; i < entityArray.size(); i++) {
                String entityId = entityArray.get(i).getAsString();
                EntityType<?> type = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.parse(entityId));
                if (type != null) entities.add(type);
            }
        }

        int mana = GsonHelper.getAsInt(json, "mana", 0);

        BrutalitySpell spell = BrutalitySpells.getSpell(id);

        if (spell == null) {
            // Fallback: Check if there's an explicit "output" field if ID lookup fails
            if (json.has("output")) {
                ResourceLocation altId = ResourceLocation.parse(json.getAsJsonObject("output").get("spell").getAsString());
                spell = BrutalitySpells.getSpell(altId);
            }
        }

        if (spell == null) {
            throw new JsonSyntaxException("No spell found for recipe: " + id);
        }

        int count = GsonHelper.getAsInt(json, "count", 0);
        return new ConjureRecipe(id, ingredients, entities, mana, count, spell);
    }
    @Override
    public void toNetwork(FriendlyByteBuf buf, ConjureRecipe recipe) {
        // Start directly with ingredients to match fromNetwork
        for (Ingredient ing : recipe.ingredients()) {
            ing.toNetwork(buf);
        }
        buf.writeCollection(recipe.requiredEntities(), (b, type) ->
                b.writeResourceLocation(ForgeRegistries.ENTITY_TYPES.getKey(type)));
        buf.writeInt(recipe.mana());
    }

    @Override
    public ConjureRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        // id is provided by Minecraft's internal recipe syncing
        NonNullList<Ingredient> ingredients = NonNullList.withSize(8, Ingredient.EMPTY);
        for (int i = 0; i < 8; i++) {
            ingredients.set(i, Ingredient.fromNetwork(buf));
        }

        List<EntityType<?>> entities = buf.readList(b -> ForgeRegistries.ENTITY_TYPES.getValue(b.readResourceLocation()));
        int mana = buf.readInt();

        // Client-side lookup
        BrutalitySpell spell = BrutalitySpells.getSpell(id);
        int count = buf.readInt();

        return new ConjureRecipe(id, ingredients, entities, mana, count, spell);
    }


}