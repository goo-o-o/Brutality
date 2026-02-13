package net.goo.brutality.client.datagen.ingredients;

import com.google.gson.JsonObject;
import net.goo.brutality.Brutality;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class AnySharpnessBookIngredient extends Ingredient {

    public static final AnySharpnessBookIngredient INSTANCE = new AnySharpnessBookIngredient();

    private final ItemStack displayStack;

    private AnySharpnessBookIngredient() {
        super(Stream.empty()); // we override getItems() anyway
        this.displayStack = EnchantedBookItem.createForEnchantment(
                new EnchantmentInstance(Enchantments.SHARPNESS, 1));
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        return stack != null
                && stack.is(Items.ENCHANTED_BOOK)
                && EnchantmentHelper.getEnchantments(stack).containsKey(Enchantments.SHARPNESS);
    }

    @Override
    public ItemStack @NotNull [] getItems() {
        return new ItemStack[]{ displayStack.copy() };
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<AnySharpnessBookIngredient> {

        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "any_sharpness_book");

        private Serializer() {}

        @Override
        public AnySharpnessBookIngredient parse(FriendlyByteBuf buffer) {
            return AnySharpnessBookIngredient.INSTANCE;
        }

        @Override
        public AnySharpnessBookIngredient parse(JsonObject json) {
            return AnySharpnessBookIngredient.INSTANCE;
        }

        @Override
        public void write(FriendlyByteBuf buffer, AnySharpnessBookIngredient ingredient) {
            // singleton – nothing to write
        }
    }
}