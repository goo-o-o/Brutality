package net.goo.brutality.client.renderers.textures;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.cache.texture.AutoGlowingTexture;

import java.util.function.Function;

import static com.mojang.blaze3d.platform.GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA;
import static com.mojang.blaze3d.platform.GlStateManager.SourceFactor.ONE;
import static com.mojang.blaze3d.platform.GlStateManager.SourceFactor.SRC_ALPHA;

public class BrutalityAutoFullbrightTexture extends AutoGlowingTexture {
    private static final RenderStateShard.ShaderStateShard SHADER_STATE = new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEyesShader);
    private static final RenderStateShard.TransparencyStateShard TRANSPARENCY_STATE = new RenderStateShard.TransparencyStateShard(
            "translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(SRC_ALPHA, ONE_MINUS_SRC_ALPHA, ONE, ONE_MINUS_SRC_ALPHA);

    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });
    private static final RenderStateShard.WriteMaskStateShard WRITE_MASK = new RenderStateShard.WriteMaskStateShard(true, true);
    protected static final RenderStateShard.CullStateShard CULL = new RenderStateShard.CullStateShard(true);

    public BrutalityAutoFullbrightTexture(ResourceLocation originalLocation, ResourceLocation location) {
        super(originalLocation, location);
    }

    private static final Function<ResourceLocation, RenderType> RENDER_TYPE_FUNCTION = Util.memoize((texture) -> {
        RenderStateShard.TextureStateShard textureState = new RenderStateShard.TextureStateShard(texture, false, false);
        return RenderType.create("arma_fullbright_layer",
                DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true,
                RenderType.CompositeState.builder().setShaderState(SHADER_STATE).setCullState(CULL).setTextureState(textureState).
                        setTransparencyState(TRANSPARENCY_STATE).setWriteMaskState(WRITE_MASK).createCompositeState(false));
    });

    public static RenderType getRenderType(ResourceLocation texture) {
        return RENDER_TYPE_FUNCTION.apply(getEmissiveResource(texture));
    }

    private static ResourceLocation getEmissiveResource(ResourceLocation baseResource) {
        ResourceLocation path = appendToPath(baseResource, "_fb");
        generateTexture(path, (textureManager) -> {
            textureManager.register(path, new AutoGlowingTexture(baseResource, path));
        });
        return path;
    }
}


