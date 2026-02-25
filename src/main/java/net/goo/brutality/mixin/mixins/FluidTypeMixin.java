package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.registry.BrutalityFluidTypes;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(FluidType.class)
public class FluidTypeMixin {


    @Inject(method = "canSwim", at = @At("RETURN"), cancellable = true, remap = false)
    public void modifyIfCanSwim(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity livingEntity) {
            FluidType fluidType = (((FluidType) (Object) this));
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                if (handler.isEquipped(BrutalityItems.SALAMANDER_BOOTS.get())) {
                    if (fluidType == Fluids.LAVA.getFluidType() || fluidType == Fluids.FLOWING_LAVA.getFluidType()) {
                        cir.setReturnValue(true);
                    }
                }
            });

        }

    }
}
