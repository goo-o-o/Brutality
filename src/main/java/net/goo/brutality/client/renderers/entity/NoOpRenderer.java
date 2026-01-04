package net.goo.brutality.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.Brutality;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class NoOpRenderer<T extends Entity> extends EntityRenderer<T> {
    public NoOpRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(T entity, float yaw, float tick, PoseStack pose, MultiBufferSource buffer, int light) {
        // Do nothing
    }

    private static final ResourceLocation EMPTY_TEXTURE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/entity/empty.png");

    @Override
    public @NotNull ResourceLocation getTextureLocation(T entity) {
        return EMPTY_TEXTURE; // Not used
    }
}