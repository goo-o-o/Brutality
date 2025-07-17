package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.entity.custom.projectile.generic.StarEntity;
import net.goo.brutality.registry.ModEntities;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.StuckInBodyLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StarLayer<T extends LivingEntity, M extends EntityModel<T>> extends StuckInBodyLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public StarLayer(EntityRendererProvider.Context pContext, LivingEntityRenderer<T, M> pRenderer) {
        super(pRenderer);
        this.dispatcher = pContext.getEntityRenderDispatcher();
    }

    protected int numStuck(T pEntity) {
        return pEntity.getArrowCount();
    }

    protected void renderStuckItem(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Entity pEntity, float pX, float pY, float pZ, float pPartialTick) {
        float f = Mth.sqrt(pX * pX + pZ * pZ);
        StarEntity arrow = new StarEntity(ModEntities.STAR_ENTITY.get(), pEntity.level(), pEntity.getX(), pEntity.getY(), pEntity.getZ());
        arrow.setYRot((float)(Math.atan2(pX, pZ) * (double)(180F / (float)Math.PI)));
        arrow.setXRot((float)(Math.atan2(pY, f) * (double)(180F / (float)Math.PI)));
        arrow.yRotO = arrow.getYRot();
        arrow.xRotO = arrow.getXRot();
        this.dispatcher.render(arrow, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }
}