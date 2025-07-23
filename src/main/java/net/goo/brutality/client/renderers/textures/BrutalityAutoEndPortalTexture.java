package net.goo.brutality.client.renderers.textures;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;

import java.util.function.Function;

public class BrutalityAutoEndPortalTexture extends AutoGlowingTexture {

    private static final RenderType PORTAL_TYPE = RenderType.create(
            "brutality_end_portal",
            DefaultVertexFormat.POSITION_COLOR, // Important for portal effect
            VertexFormat.Mode.QUADS,
            256,
            false,
            false, // Needs depth sorting
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEndGatewayShader))
                    .setTextureState(new RenderStateShard.TextureStateShard(TheEndPortalRenderer.END_SKY_LOCATION, false, false))
                    .setTransparencyState(new RenderStateShard.TransparencyStateShard(
                            "additive_transparency",
                            () -> {
                                RenderSystem.enableBlend();
                                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                                        GlStateManager.DestFactor.ONE); // Additive blending
                            },
                            () -> {
                                RenderSystem.disableBlend();
                                RenderSystem.defaultBlendFunc();
                            }
                    ))
                    .setCullState(new RenderStateShard.CullStateShard(false))
                    .setWriteMaskState(new RenderStateShard.WriteMaskStateShard(true, true))
                    .createCompositeState(false)
    );

    private static final RenderType END_PORTAL = RenderType.create("end_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEndPortalShader)).setTextureState(RenderStateShard.MultiTextureStateShard.builder().add(TheEndPortalRenderer.END_SKY_LOCATION, false, false).add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false).build()).createCompositeState(false));


    public BrutalityAutoEndPortalTexture(ResourceLocation originalLocation, ResourceLocation location) {
        super(originalLocation, location);
    }

    private static final Function<ResourceLocation, RenderType> RENDER_TYPE_FUNCTION = Util.memoize((texture) -> END_PORTAL);

    public static RenderType getRenderType(ResourceLocation texture) {
        return RENDER_TYPE_FUNCTION.apply(getEmissiveResource(texture));
    }

    private static ResourceLocation getEmissiveResource(ResourceLocation baseResource) {
        ResourceLocation path = appendToPath(baseResource, "_endportal");
        generateTexture(path, (textureManager) -> {
            textureManager.register(path, new AutoGlowingTexture(baseResource, path));
        });
        return path;
    }
}


