package net.goo.armament.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.armament.Armament;
import net.goo.armament.entity.custom.ThrownZeusThunderbolt;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ThrownZeusThunderboltRenderer extends GeoEntityRenderer<ThrownZeusThunderbolt> {

    public ThrownZeusThunderboltRenderer(EntityRendererProvider.Context context) { // Correct constructor!
        super(context, new ThrownZeusThunderboltModel());
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }


    @Override
    public void render(ThrownZeusThunderbolt entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(ThrownZeusThunderbolt animatable) {
        return new ResourceLocation(Armament.MOD_ID, "textures/entity/thrown_zeus_thunderbolt.png");
    }
}