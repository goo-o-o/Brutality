package net.goo.armament.mixin;

import net.goo.armament.item.ModArmorMaterials;
import net.goo.armament.util.ModUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "getVisibilityPercent", at = @At("TAIL"), cancellable = true)
    private void modifyVisibilityPercent(Entity pLookingEntity, CallbackInfoReturnable<Double> cir) {
        LivingEntity self = (LivingEntity)(Object)this;


        if (ModUtils.hasFullArmorSet(self, ModArmorMaterials.NOIR)) {
            cir.setReturnValue(0D);
        }

    }
}
