package net.goo.brutality.mixin;

import net.goo.brutality.util.helpers.NbtHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(ExperienceOrb.class)
public abstract class ExperienceOrbMixin {

    @Redirect(method = "repairPlayerItems", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getRandomItemWith(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Predicate;)Ljava/util/Map$Entry;"))
    private Map.Entry<EquipmentSlot, ItemStack> modifyMendingSelection(Enchantment enchantment, LivingEntity entity, Predicate<ItemStack> originalPredicate) {
        Predicate<ItemStack> combinedPredicate = stack -> originalPredicate.test(stack) && !NbtHelper.getBool(stack, "fromDoubleDown", false);

        return EnchantmentHelper.getRandomItemWith(Enchantments.MENDING, entity, combinedPredicate);
    }

}