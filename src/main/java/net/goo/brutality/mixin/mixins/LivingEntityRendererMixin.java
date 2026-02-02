package net.goo.brutality.mixin.mixins;

import net.goo.brutality.client.renderers.layers.*;
import net.goo.brutality.common.item.BrutalityArmorMaterials;
import net.goo.brutality.util.ModUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {

    @Shadow
    public abstract boolean addLayer(RenderLayer<T, M> pLayer);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, EntityModel<T> model, float shadowRadius, CallbackInfo ci) {
        LivingEntityRenderer<T, M> renderer = (LivingEntityRenderer<T, M>) (Object) this;

        this.addLayer(new StickyBombLayer<>(context, renderer));
        this.addLayer(new LightBoundLayer<>(context, renderer));
        this.addLayer(new EyeOfViolenceLayer<>(renderer));

        if (((Object) this) instanceof PlayerRenderer) {
            this.addLayer(new EnragedOverlayLayer<>(renderer));
        }
    }

    @Inject(method = "isBodyVisible", at = @At("HEAD"), cancellable = true)
    private void modifyIsBodyVisible(T pLivingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (ModUtils.hasFullArmorSet(pLivingEntity, BrutalityArmorMaterials.NOIR)) {
            cir.setReturnValue(false);
        }
    }
}