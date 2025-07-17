package net.goo.brutality.mixin;

import net.goo.brutality.item.BrutalityArmorMaterials;
import net.goo.brutality.item.weapon.custom.DarkinScythe;
import net.goo.brutality.util.ModUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.goo.brutality.util.ModResources.CUSTOM_MODEL_DATA;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "getVisibilityPercent", at = @At("TAIL"), cancellable = true)
    private void modifyVisibilityPercent(Entity pLookingEntity, CallbackInfoReturnable<Double> cir) {
        LivingEntity self = (LivingEntity)(Object)this;


        if (ModUtils.hasFullArmorSet(self, BrutalityArmorMaterials.NOIR)) {
            cir.setReturnValue(0D);
        }
    }

    @Inject(method = "isPushable", at = @At("HEAD"), cancellable = true)
    private void managePushable(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = (((LivingEntity) (Object) this));

        ItemStack mainHand = livingEntity.getMainHandItem();
        ItemStack offhand = livingEntity.getOffhandItem();


            if (mainHand.getItem() instanceof DarkinScythe)
                if (mainHand.getOrCreateTag().getInt(CUSTOM_MODEL_DATA) == 1)
                    cir.setReturnValue(false);

            if (offhand.getItem() instanceof DarkinScythe)
                if (offhand.getOrCreateTag().getInt(CUSTOM_MODEL_DATA) == 1)
                    cir.setReturnValue(false);
    }


}
