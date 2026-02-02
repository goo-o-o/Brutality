package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.mixin.accessors.IBrutalityAttribute;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A mixin class for extending the behavior of {@link AttributeInstance}.
 * This class modifies the dynamic attribute value retrieval mechanism
 * and implements {@link IBrutalityAttribute} to provide additional functionality
 * for handling a related {@link LivingEntity} owner.
 */
@Mixin(AttributeInstance.class)
public abstract class AttributeInstanceMixin implements IBrutalityAttribute {
    @Shadow public abstract Attribute getAttribute();

    @Unique
    private LivingEntity brutality$owner;

    @Inject(method = "getValue", at = @At("RETURN"), cancellable = true)
    private void brutality$directInstanceAccess(CallbackInfoReturnable<Double> cir) {
        // If we are already inside a Map.getValue call, skip this to prevent double-dipping

        if (this.brutality$owner != null) {
            double original = cir.getReturnValue();
            double modified = BrutalityCurioItem.Hooks.modifyDynamicAttributeValues(
                    this.brutality$owner, this.getAttribute(), original
            );
            cir.setReturnValue(modified);
        }
    }


    @Override
    public void brutality$setOwner(LivingEntity entity) {
        this.brutality$owner = entity;
    }

    @Override
    public LivingEntity brutality$getOwner() {
        return brutality$owner;
    }
}
