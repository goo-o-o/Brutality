package net.goo.brutality.client.renderers.shaders;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.client.renderers.BrutalityRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class PixelatedBufferSourceWrapper implements MultiBufferSource {
    private final MultiBufferSource delegate;

    public PixelatedBufferSourceWrapper(MultiBufferSource delegate) {
        this.delegate = delegate;
    }

    @Override
    public VertexConsumer getBuffer(RenderType renderType) {
        return new DualVertexConsumer(delegate.getBuffer(renderType), delegate.getBuffer(BrutalityRenderTypes.PIXELATE_ENTITY));
    }

}