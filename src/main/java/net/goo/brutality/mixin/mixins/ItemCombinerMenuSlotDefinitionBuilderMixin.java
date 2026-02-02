package net.goo.brutality.mixin.mixins;

import net.goo.brutality.util.NBTUtils;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.function.Predicate;

@Mixin(ItemCombinerMenuSlotDefinition.Builder.class)
public class ItemCombinerMenuSlotDefinitionBuilderMixin {

    @Inject(method = "withSlot", at = @At("RETURN"))
    private void wrapMayPlacePredicate(
            int slotIndex, int x, int y, Predicate<ItemStack> pMayPlace, CallbackInfoReturnable<ItemCombinerMenuSlotDefinition.Builder> cir
    ) {
        Predicate<ItemStack> wrapped = stack -> {
            if (!pMayPlace.test(stack)) return false;
            return !NBTUtils.getBool(stack, "fromDoubleDown", false);
        };

        // Replace the slot with wrapped predicate
        // We can't modify the existing SlotDefinition, so we remove & re-add
        ItemCombinerMenuSlotDefinition.Builder builder = (ItemCombinerMenuSlotDefinition.Builder) (Object) this;
        List<ItemCombinerMenuSlotDefinition.SlotDefinition> slots = brutality$getSlotsField(builder);
        if (!slots.isEmpty() && slots.get(slots.size() - 1).slotIndex() == slotIndex) {
            slots.remove(slots.size() - 1);
            slots.add(new ItemCombinerMenuSlotDefinition.SlotDefinition(
                    slotIndex, x, y, wrapped
            ));
        }
    }

    @Final
    @Shadow
    private List<ItemCombinerMenuSlotDefinition.SlotDefinition> slots;

    @Unique
    private static List<ItemCombinerMenuSlotDefinition.SlotDefinition> brutality$getSlotsField(ItemCombinerMenuSlotDefinition.Builder builder) {
        return ((ItemCombinerMenuSlotDefinitionBuilderMixin) (Object) builder).slots;
    }
}