package net.goo.brutality.mixin;

import net.goo.brutality.item.weapon.trident.ThunderboltTrident;
import net.goo.brutality.util.helpers.EnchantmentHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ArrowInfiniteEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.MendingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowInfiniteEnchantment.class)
public abstract class InfinityEnchantmentMixin extends Enchantment {

    // Constructor must match the original class
    protected InfinityEnchantmentMixin(Rarity rarity, EnchantmentCategory category, EquipmentSlot[] slots) {
        super(rarity, category, slots);
    }

    /**
     * Modify the compatibility check to allow Infinity and Mending on specific items.
     */
    @Inject(
            method = "checkCompatibility",
            at = @At("HEAD"),
            cancellable = true
    )
    public void checkCompatibility(Enchantment ench, CallbackInfoReturnable<Boolean> cir) {
        // Check if the other enchantment is Mending
        if (ench instanceof MendingEnchantment) {
            // Get the item stack being enchanted (if available)
            ItemStack stack = EnchantmentHelper.getCurrentAnvilStack();
            if (stack != null && stack.getItem() instanceof ThunderboltTrident) {
                // Allow Infinity and Mending to be compatible for your custom trident
                cir.setReturnValue(true);
                return;
            }
        }

        // Default behavior for all other cases
        cir.setReturnValue(super.checkCompatibility(ench));
    }
}