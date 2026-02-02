package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(value = top.theillusivec4.curios.client.ClientEventHandler.class, remap = false)
public class ClientEventHandler {

    /**
     * Mimics logic in {@link ItemStackMixin#modifyOperationType(AttributeModifier.Operation, Map.Entry)} but for curio items
     */
    @Redirect(
            method = "onTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;"
            )
    )
    private AttributeModifier.Operation brutality$fakeOperationForCurios(AttributeModifier instance, @Local(name = "entry") Map.Entry<Attribute, AttributeModifier> entry) {
        AttributeModifier.Operation original = instance.getOperation();
        if (entry.getKey() instanceof BrutalityAttributes.RangedPercentageAttribute) {
            if (original == AttributeModifier.Operation.ADDITION) {
                return AttributeModifier.Operation.MULTIPLY_BASE;
            }
        }

        return original;
    }
}