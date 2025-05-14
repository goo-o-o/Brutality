package net.goo.armament.mixin;

import net.goo.armament.item.weapon.base.ArmaBowItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(ItemProperties.class)
public abstract class MixinItemProperties {
    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void registerCustomItemProperties(CallbackInfo ci) {
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof ArmaBowItem) {
                ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    }
                    return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
                });

                ItemProperties.register(item, new ResourceLocation("pulling"), (stack, level, entity, seed) -> {
                    return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
                });
            }
        }
    }

}
