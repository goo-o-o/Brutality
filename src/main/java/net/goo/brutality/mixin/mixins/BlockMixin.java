package net.goo.brutality.mixin.mixins;

import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.theillusivec4.curios.api.CuriosApi;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "updateEntityAfterFallOn", at = @At("HEAD"), cancellable = true)
    private void forceBouncy(BlockGetter pLevel, Entity pEntity, CallbackInfo ci) {
        if (pEntity instanceof LivingEntity livingEntity) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                if (handler.isEquipped(BrutalityModItems.PORTABLE_TRAMPOLINE.get())) {
                    // From SlimeBlock
                    Vec3 vec3 = pEntity.getDeltaMovement();
                    if (vec3.y < 0.0D) {
                        double d0 = pEntity instanceof LivingEntity ? 1.0D : 0.8D;
                        pEntity.setDeltaMovement(vec3.x, -vec3.y * d0, vec3.z);
                        ci.cancel();
                    }
                }
            });
        }
    }
}
