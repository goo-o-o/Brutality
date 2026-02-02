package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.entity.capabilities.EntityStickyBombCap;
import net.goo.brutality.common.entity.projectile.trident.physics_projectile.ThrownStickyBomb;
import net.goo.brutality.common.registry.BrutalityDamageTypes;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class StickyBombLayer<T extends LivingEntity, M extends EntityModel<T>> extends LivingStuckInBodyLayer<T, M> {
    private final EntityRenderDispatcher dispatcher;

    public StickyBombLayer(EntityRendererProvider.Context pContext, LivingEntityRenderer<T, M> pRenderer) {
        super(pRenderer);
        this.dispatcher = pContext.getEntityRenderDispatcher();
    }

    @Override
    protected int numStuck(T entity) {
        return entity.getCapability(BrutalityCapabilities.STICKY_BOMB)
                .map(EntityStickyBombCap::getTotalCount)
                .orElse(0);
    }


    protected void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int pPackedLight, Entity entity, float pX, float pY, float pZ, float pPartialTick) {
        float f = Mth.sqrt(pX * pX + pZ * pZ);
        ThrownStickyBomb thrownStickyBomb = new ThrownStickyBomb(BrutalityEntities.STICKY_BOMB.get(), entity.level(), BrutalityDamageTypes.THROWING_BLUNT);
        thrownStickyBomb.setPos(entity.getPosition(1));
        thrownStickyBomb.setYRot((float) (Math.atan2(pX, pZ) * (double) (180F / (float) Math.PI)));
        thrownStickyBomb.setXRot((float) (Math.atan2(pY, f) * (double) (180F / (float) Math.PI)));
        thrownStickyBomb.inGround = true;
        thrownStickyBomb.yRotO = thrownStickyBomb.getYRot();
        thrownStickyBomb.xRotO = thrownStickyBomb.getXRot();
        this.dispatcher.render(thrownStickyBomb, 0.0D, 0.0D, 0.0D, 0.0F, pPartialTick, poseStack, multiBufferSource, pPackedLight);
    }
}