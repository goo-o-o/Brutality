package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

public class EyeOfViolenceLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public EyeOfViolenceLayer(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                       T entity, float limbSwing, float limbSwingAmount, float partialTicks,
                       float ageInTicks, float netHeadYaw, float headPitch) {

        if (!(entity instanceof Mob)) return;
        Player localPlayer = Minecraft.getInstance().player;

        if (localPlayer == null) return;
        if (!localPlayer.hasEffect(BrutalityModMobEffects.ENRAGED.get())) return;
        if (entity == localPlayer || entity.distanceTo(localPlayer) > 25) {
            return;
        }

        CuriosApi.getCuriosInventory(localPlayer).ifPresent(handler -> {
            handler.findFirstCurio(BrutalityModItems.EYE_FOR_VIOLENCE.get()).ifPresent(slot -> {

        VertexConsumer outlineBuffer = bufferSource.getBuffer(RenderType.outline(getTextureLocation(entity)));

                this.getParentModel().renderToBuffer(
                        poseStack, outlineBuffer, packedLight,
                        OverlayTexture.NO_OVERLAY,
                        1.0F, 0.0F, 0.0F, 1.0F // Red outline
                );
            });
        });
    }

}
