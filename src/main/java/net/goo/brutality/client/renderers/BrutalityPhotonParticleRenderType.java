package net.goo.brutality.client.renderers;


import com.lowdragmc.photon.Photon;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.goo.brutality.client.particle.base.trail.RendererSetting;
import net.goo.brutality.util.particle.BloomEffect;
import net.goo.brutality.util.particle.PositionedRect;
import net.goo.brutality.util.particle.Shaders;
import net.goo.brutality.util.render.IrisFramebufferUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public abstract class BrutalityPhotonParticleRenderType implements ParticleRenderType {
    @Nullable
    private static Frustum FRUSTUM;
    private static RendererSetting.Layer LAYER;
    public static boolean bloomMark;

    public static void renderBloom() {
        if (bloomMark) {
            PositionedRect lastViewport = new PositionedRect(GlStateManager.Viewport.x(), GlStateManager.Viewport.y(), GlStateManager.Viewport.width(), GlStateManager.Viewport.height());
            RenderTarget input = BloomEffect.getInput();
            RenderTarget output = BloomEffect.getOutput();
            RenderTarget background = Minecraft.getInstance().getMainRenderTarget();
            if (lastViewport.position.x != 0 || lastViewport.position.y != 0 || lastViewport.size.width != background.width || lastViewport.size.height != background.height) {
                RenderSystem.viewport(0, 0, background.width, background.height);
            }

            BloomEffect.renderBloom(background.width, background.height, LAYER == RendererSetting.Layer.OPAQUE ? Photon.getSolidTextureID() : Photon.getTranslucentTextureID(true), input.getColorTextureId(), output);
            input.bindWrite(false);
            GL20.glDrawBuffers(new int[]{36065});
            GlStateManager._clearColor(0.0F, 0.0F, 0.0F, 0.0F);
            int i = 16384;
            GlStateManager._clear(i, Minecraft.ON_OSX);
            GL20.glDrawBuffers(new int[]{36064, 36065});
            GlStateManager._colorMask(true, true, true, true);
            GlStateManager._disableDepthTest();
            GlStateManager._depthMask(false);
            GlStateManager._glBindFramebuffer(36160, LAYER == RendererSetting.Layer.OPAQUE ? Photon.getSolidFrameBufferID() : Photon.getTranslucentFrameBufferID());
            Shaders.getBlitShader().setSampler("DiffuseSampler", output.getColorTextureId());
            Shaders.getBlitShader().apply();
            GlStateManager._enableBlend();
            RenderSystem.defaultBlendFunc();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tesselator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
            bufferbuilder.vertex(-1.0F, 1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(-1.0F, -1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(1.0F, -1.0F, 0.0F).endVertex();
            bufferbuilder.vertex(1.0F, 1.0F, 0.0F).endVertex();
            BufferUploader.draw(bufferbuilder.end());
            Shaders.getBlitShader().clear();
            GlStateManager._depthMask(true);
            GlStateManager._colorMask(true, true, true, true);
            GlStateManager._enableDepthTest();
            if (lastViewport.position.x != 0 || lastViewport.position.y != 0 || lastViewport.size.width != background.width || lastViewport.size.height != background.height) {
                RenderSystem.viewport(lastViewport.position.x, lastViewport.position.y, lastViewport.size.width, lastViewport.size.height);
            }

            bloomMark = false;
        }

    }

    public static void prepareForParticleRendering(@Nullable Frustum cullingFrustum) {
        FRUSTUM = cullingFrustum;
        LAYER = RendererSetting.Layer.OPAQUE;
    }

    public static void finishRender() {
        if (LAYER == RendererSetting.Layer.OPAQUE || IrisFramebufferUtils.isRenderingGUIScreen()) {
            renderBloom();
        }

        if (LAYER == RendererSetting.Layer.OPAQUE) {
            LAYER = RendererSetting.Layer.TRANSLUCENT;
        }

    }

    public void beginBloom() {
        BloomEffect.bindBloomShader();
        bloomMark = true;
    }

    public void endParticle() {
        BloomEffect.setBloomColor(new Vector4f(0.0F));
        RenderTarget background = Minecraft.getInstance().getMainRenderTarget();
        background.bindWrite(false);
    }

    public boolean isParallel() {
        return false;
    }

    /** @deprecated */
    @Deprecated
    public final void begin(BufferBuilder builder, TextureManager textureManager) {
        this.prepareStatus();
        this.begin(builder);
    }

    /** @deprecated */
    @Deprecated
    public final void end(Tesselator tesselator) {
        this.end(tesselator.getBuilder());
        this.releaseStatus();
    }

    public void prepareStatus() {
    }

    public void begin(BufferBuilder builder) {
    }

    public void end(BufferBuilder builder) {
        BufferUploader.drawWithShader(builder.end());
    }

    public void releaseStatus() {
    }

    public static boolean checkLayer(RendererSetting.Layer layer) {
        return LAYER == layer;
    }

    public static boolean checkFrustum(AABB aabb) {
        return FRUSTUM == null || FRUSTUM.isVisible(aabb);
    }

    public static Comparator<ParticleRenderType> makeParticleRenderTypeComparator(List<ParticleRenderType> renderOrder) {
        Objects.requireNonNull(renderOrder);
        Comparator<ParticleRenderType> vanillaComparator = Comparator.comparingInt(renderOrder::indexOf);
        return (typeOne, typeTwo) -> {
            boolean vanillaOne = renderOrder.contains(typeOne);
            boolean vanillaTwo = renderOrder.contains(typeTwo);
            if (vanillaOne && vanillaTwo) {
                return vanillaComparator.compare(typeOne, typeTwo);
            } else if (!vanillaOne && !vanillaTwo) {
                return Integer.compare(System.identityHashCode(typeOne), System.identityHashCode(typeTwo));
            } else {
                return vanillaOne ? -1 : 1;
            }
        };
    }

    @Nullable
    public static Frustum getFRUSTUM() {
        return FRUSTUM;
    }

    public static RendererSetting.Layer getLAYER() {
        return LAYER;
    }

    static {
        LAYER = RendererSetting.Layer.TRANSLUCENT;
        bloomMark = false;
    }
}
