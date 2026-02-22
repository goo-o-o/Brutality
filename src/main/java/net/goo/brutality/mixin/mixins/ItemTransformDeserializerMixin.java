package net.goo.brutality.mixin.mixins;

import net.minecraft.client.renderer.block.model.ItemTransform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemTransform.Deserializer.class)
public class ItemTransformDeserializerMixin {
    @Redirect(
            method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/ItemTransform;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;clamp(FFF)F")
    )
    private float removeScaleLimit(float pValue, float pMin, float pMax) {
        return pValue;
    }
}
