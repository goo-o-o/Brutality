package net.goo.armament.entity.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.armament.Armament;
import net.goo.armament.entity.client.ModModelLayers;
import net.goo.armament.entity.client.model.CruelSun2Model;
import net.goo.armament.entity.custom.CruelSun2Entity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CruelSun2Renderer extends EntityRenderer<CruelSun2Entity> {
    private static final ResourceLocation CRUEL_SUN_2_LOCATION = new ResourceLocation(Armament.MOD_ID, "cruel_sun");
    private final CruelSun2Model model;

    public CruelSun2Renderer (EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CruelSun2Model(pContext.bakeLayer(ModModelLayers.THROWN_ZEUS_THUNDERBOLT_ENTITY_LAYER));

    }

    public void render(CruelSun2Entity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }


    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getTextureLocation(CruelSun2Entity pEntity) {
        return CRUEL_SUN_2_LOCATION;
    }

    public boolean shouldRender(CruelSun2Entity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return super.shouldRender(pLivingEntity, pCamera, pCamX, pCamY, pCamZ);
    }
}