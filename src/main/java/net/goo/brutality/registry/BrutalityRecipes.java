package net.goo.brutality.registry;

import net.goo.brutality.Brutality;
import net.goo.brutality.magic.table_of_wizardry.ConjureRecipe;
import net.goo.brutality.magic.table_of_wizardry.ConjureRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BrutalityRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
        DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Brutality.MOD_ID);

    public static final DeferredRegister<RecipeType<?>> TYPES =
        DeferredRegister.create(Registries.RECIPE_TYPE, Brutality.MOD_ID);

    // The Serializer: Handles JSON <-> Object conversion
    public static final RegistryObject<RecipeSerializer<ConjureRecipe>> CONJURE_SERIALIZER =
        SERIALIZERS.register("conjure", ConjureRecipeSerializer::new);

    // The Type: The "Category" for the recipe (like minecraft:crafting)
    public static final RegistryObject<RecipeType<ConjureRecipe>> CONJURE_TYPE =
        TYPES.register("conjure", () -> new RecipeType<>() {
            @Override
            public String toString() { return "conjure"; }
        });

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }
}