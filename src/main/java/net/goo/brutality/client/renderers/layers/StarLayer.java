package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.entity.projectile.generic.StarEntity;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModEntities;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StarLayer<T extends LivingEntity, M extends EntityModel<T>> extends LivingStuckInBodyLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public StarLayer(EntityRendererProvider.Context pContext, LivingEntityRenderer<T, M> pRenderer) {
        super(pRenderer);
        this.dispatcher = pContext.getEntityRenderDispatcher();
    }

    @Override
    protected int numStuck(T entity) {
        return entity.getCapability(BrutalityCapabilities.ENTITY_STAR_COUNT_CAP)
                .map(cap -> cap.getAllStarCounts().values().stream()
                        .mapToInt(entityMap -> entityMap.getOrDefault(entity.getId(), 0))
                        .sum())
                .orElse(0);
    }


    protected void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, Entity entity, float pX, float pY, float pZ, float pPartialTick) {
        float f = Mth.sqrt(pX * pX + pZ * pZ);
        StarEntity star = new StarEntity(BrutalityModEntities.STAR_ENTITY.get(), entity.level(), entity.getX(), entity.getY(), entity.getZ());
        star.setYRot((float) (Math.atan2(pX, pZ) * (double) (180F / (float) Math.PI)));
        star.setXRot((float) (Math.atan2(pY, f) * (double) (180F / (float) Math.PI)));
        star.inGround = true;
        star.renderForLayer = true;
        star.yRotO = star.getYRot();
        star.xRotO = star.getXRot();
        this.dispatcher.render(star, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTick, poseStack, multiBufferSource, pPackedLight);
    }
}