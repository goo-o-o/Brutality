package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.goo.brutality.client.renderers.layers.EnragedOverlayLayer;
import net.goo.brutality.client.renderers.layers.EyeOfViolenceLayer;
import net.goo.brutality.client.renderers.layers.LightBoundLayer;
import net.goo.brutality.client.renderers.layers.StickyBombLayer;
import net.goo.brutality.common.item.armor.BrutalityArmorMaterials;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.Iterator;
import java.util.List;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {


    protected LivingEntityRendererMixin(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

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

    @Shadow
    protected M model;

    @Shadow
    protected abstract float getAttackAnim(T pLivingEntity, float pPartialTick);

    @Shadow
    protected abstract void setupRotations(T pEntity, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTick);

    @Shadow
    protected abstract void scale(T pLivingEntity, PoseStack pPoseStack, float pPartialTick);

    @Shadow
    protected abstract boolean isBodyVisible(T pLivingEntity);

    @Shadow
    protected abstract float getWhiteOverlayProgress(T pLivingEntity, float pPartialTick);

    @Shadow
    protected abstract RenderType getRenderType(T pEntity, boolean pBodyVisible, boolean pTranslucent, boolean pGlowing);

    @Shadow
    protected abstract float getBob(T pLivingEntity, float pPartialTick);

    @Shadow
    @Final
    protected List<RenderLayer<T, M>> layers;

    //
//    @Inject(
//            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
//            at = @At("HEAD")
//    )
//    private void wrapBufferForPixelation(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource originalBuffer, int pPackedLight, CallbackInfo ci) {
//        if (CuriosApi.getCuriosInventory(pEntity)
//                .map(h -> h.isEquipped(BrutalityItems.CENSORSHIP.get()))
//                .orElse(false)) {
//            PostEffectRegistry.renderEffectForNextTick(BrutalityShaders.PIXELATED_SHADER);
//            MultiBufferSource pBuffer = new PixelatedBufferSourceWrapper(originalBuffer);
//            pPoseStack.pushPose();
//            this.model.attackTime = this.getAttackAnim(pEntity, pPartialTicks);
//
//            boolean shouldSit = pEntity.isPassenger() && (pEntity.getVehicle() != null && pEntity.getVehicle().shouldRiderSit());
//            this.model.riding = shouldSit;
//            this.model.young = pEntity.isBaby();
//            float f = Mth.rotLerp(pPartialTicks, pEntity.yBodyRotO, pEntity.yBodyRot);
//            float f1 = Mth.rotLerp(pPartialTicks, pEntity.yHeadRotO, pEntity.yHeadRot);
//            float f2 = f1 - f;
//            if (shouldSit && pEntity.getVehicle() instanceof LivingEntity livingentity) {
//                f = Mth.rotLerp(pPartialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
//                f2 = f1 - f;
//                float f3 = Mth.wrapDegrees(f2);
//                if (f3 < -85.0F) {
//                    f3 = -85.0F;
//                }
//
//                if (f3 >= 85.0F) {
//                    f3 = 85.0F;
//                }
//
//                f = f1 - f3;
//                if (f3 * f3 > 2500.0F) {
//                    f += f3 * 0.2F;
//                }
//
//                f2 = f1 - f;
//            }
//
//            float f6 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
//            if (isEntityUpsideDown(pEntity)) {
//                f6 *= -1.0F;
//                f2 *= -1.0F;
//            }
//
//            if (pEntity.hasPose(Pose.SLEEPING)) {
//                Direction direction = pEntity.getBedOrientation();
//                if (direction != null) {
//                    float f4 = pEntity.getEyeHeight(Pose.STANDING) - 0.1F;
//                    pPoseStack.translate((float) (-direction.getStepX()) * f4, 0.0F, (float) (-direction.getStepZ()) * f4);
//                }
//            }
//
//            float f7 = this.getBob(pEntity, pPartialTicks);
//            this.setupRotations(pEntity, pPoseStack, f7, f, pPartialTicks);
//            pPoseStack.scale(-1.0F, -1.0F, 1.0F);
//            this.scale(pEntity, pPoseStack, pPartialTicks);
//            pPoseStack.translate(0.0F, -1.501F, 0.0F);
//            float f8 = 0.0F;
//            float f5 = 0.0F;
//            if (!shouldSit && pEntity.isAlive()) {
//                f8 = pEntity.walkAnimation.speed(pPartialTicks);
//                f5 = pEntity.walkAnimation.position(pPartialTicks);
//                if (pEntity.isBaby()) {
//                    f5 *= 3.0F;
//                }
//
//                if (f8 > 1.0F) {
//                    f8 = 1.0F;
//                }
//            }
//
//            this.model.prepareMobModel(pEntity, f5, f8, pPartialTicks);
//            this.model.setupAnim(pEntity, f5, f8, f7, f2, f6);
//            Minecraft minecraft = Minecraft.getInstance();
//            boolean flag = this.isBodyVisible(pEntity);
//            boolean flag1 = !flag && !pEntity.isInvisibleTo(minecraft.player);
//            boolean flag2 = minecraft.shouldEntityAppearGlowing(pEntity);
//            RenderType rendertype = this.getRenderType(pEntity, flag, flag1, flag2);
//            if (rendertype != null) {
//                VertexConsumer vertexconsumer = pBuffer.getBuffer(rendertype);
//                int i = getOverlayCoords(pEntity, this.getWhiteOverlayProgress(pEntity, pPartialTicks));
//                this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
//            }
//
//            if (!pEntity.isSpectator()) {
//                for (RenderLayer<T, M> renderlayer : this.layers) {
//                    renderlayer.render(pPoseStack, pBuffer, pPackedLight, pEntity, f5, f8, pPartialTicks, f7, f2, f6);
//                }
//            }
//
//            pPoseStack.popPose();
//            super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
//        }
//    }
//
    @Inject(
            method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/entity/layers/RenderLayer;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/Entity;FFFFFF)V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT // This is the key
    )
    private void onRenderLayerRender(T pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci, boolean shouldSit, float f, float f1, float f2, float f6, float f7, float f8, float f5, Minecraft minecraft, boolean flag, boolean flag1, boolean flag2, RenderType rendertype, Iterator var20, RenderLayer<T, M> renderlayer) {

        if (CuriosApi.getCuriosInventory(pEntity)
                .map(h -> h.isEquipped(BrutalityItems.CENSORSHIP.get()))
                .orElse(false)) {

            // Use your stencil buffer
            VertexConsumer stencilConsumer = pBuffer.getBuffer(BrutalityRenderTypes.PIXELATE_ENTITY);
            MultiBufferSource stencilBuffer = (renderType) -> stencilConsumer;

            // Re-render the layer with the stencil buffer
            renderlayer.render(pPoseStack, stencilBuffer, pPackedLight, pEntity, f5, f8, pPartialTicks, f7, f2, f6);
        }
    }


    @Shadow
    public abstract M getModel();

    @Inject(method = "isBodyVisible", at = @At("HEAD"), cancellable = true)
    private void modifyIsBodyVisible(T pLivingEntity, CallbackInfoReturnable<Boolean> cir) {
        if (ModUtils.hasFullArmorSet(pLivingEntity, BrutalityArmorMaterials.NOIR)) {
            cir.setReturnValue(false);
        }
    }
}