package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(value = top.theillusivec4.curios.client.ClientEventHandler.class)
public class ClientEventHandlerMixinCuriosAPI {

    /**
     * Mimics logic in {@link ItemStackMixin#modifyOperationType(AttributeModifier.Operation, Map.Entry)} but for curio items
     */
    @WrapOperation(
            method = "onTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/ai/attributes/AttributeModifier;getOperation()Lnet/minecraft/world/entity/ai/attributes/AttributeModifier$Operation;"
            )
    )
    private AttributeModifier.Operation brutality$fakeOperationForCurios(
            AttributeModifier instance,
            Operation<AttributeModifier.Operation> originalCall,
            @Local Map.Entry<Attribute, AttributeModifier> entry
    ) {
        AttributeModifier.Operation original = originalCall.call(instance);

        // Safety check for null entry
        if (entry != null && entry.getKey() instanceof BrutalityAttributes.RangedPercentageAttribute) {
            if (original == AttributeModifier.Operation.ADDITION) {
                return AttributeModifier.Operation.MULTIPLY_BASE;
            }
        }

        return original;
    }
}