package net.goo.brutality.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.OptionalDouble;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityRenderTypes extends RenderType {
    public BrutalityRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean b, boolean b1, Runnable runnable, Runnable runnable1) {
        super(string, vertexFormat, mode, i, b, b1, runnable, runnable1);
    }


    public static RenderType getfullBright(ResourceLocation locationIn) {
        TextureStateShard textureStateShard = new TextureStateShard(locationIn, false, false);
        return create("full_bright", DefaultVertexFormat.NEW_ENTITY, QUADS, 256, false, true, CompositeState
                .builder()
                .setTextureState(textureStateShard)
                .setShaderState(RENDERTYPE_EYES_SHADER)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(CULL)
                .createCompositeState(false));
    }

    public static RenderType getfullBrightNoDepth(ResourceLocation locationIn) {
        TextureStateShard textureStateShard = new TextureStateShard(locationIn, false, false);
        return create("fullbright_no_depth", DefaultVertexFormat.NEW_ENTITY, QUADS, 256, false, true,
                CompositeState
                        .builder()
                        .setTextureState(textureStateShard)
                        .setShaderState(RENDERTYPE_EYES_SHADER)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(CULL)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false));
    }
    public static final RenderType GLOW_NO_TEXTURE = RenderType.create(
            "brutality:glow_no_texture",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,  // no crumbling
            true,   // sort transparency
            CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)  // or RENDERTYPE_ENTITY_TRANSLUCENT_SHADER
                    .setTextureState(TextureStateShard.NO_TEXTURE)   // ← no texture
                    .setTransparencyState(new TransparencyStateShard(
                            "additive",
                            () -> {
                                RenderSystem.enableBlend();
                                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
                            },
                            () -> {
                                RenderSystem.disableBlend();
                                RenderSystem.defaultBlendFunc();
                            }
                    ))
                    .setLightmapState(LightmapStateShard.LIGHTMAP)
                    .setCullState(CullStateShard.NO_CULL)             // ← no culling
                    .setWriteMaskState(COLOR_WRITE)                   // optional, helps with overlays
                    .createCompositeState(false)
    );

    public static final RenderType NO_CULL_NO_DEPTH = create(
            "no_cull_no_depth", // Unique name
            DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, // Use a standard format matching your vertices
            VertexFormat.Mode.QUADS, // Drawing quads
            256, // Buffer size
            false, // affectsCrumbling
            true, // sortOnClient (good for transparent blending)
            RenderType.CompositeState.builder()
                    .setShaderState(RENDERTYPE_TRANSLUCENT_SHADER) // Use the translucent shader for transparency/blending
                    .setLayeringState(NO_LAYERING)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY) // Standard translucent blending
                    .setDepthTestState(NO_DEPTH_TEST) // CRITICAL: Skip depth test (draw regardless of what's already drawn)
                    .setCullState(NO_CULL)          // CRITICAL: Disable backface culling
                    .setLightmapState(LIGHTMAP)
                    .setWriteMaskState(COLOR_WRITE) // CRITICAL: Write to color buffer (A, R, G, B)
                    .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
                    .createCompositeState(false)
    );

    public static RenderType noCullTextured(ResourceLocation texture) {
        return RenderType.create(
                "brutality:no_cull_textured",
                DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP,
                VertexFormat.Mode.QUADS,
                256,
                false,  // affects crumbling
                true,   // transparency sorting
                CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_SHADER)
                        .setTextureState(new TextureStateShard(texture, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)   // or ADDITIVE_TRANSPARENCY
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .setCullState(CullStateShard.NO_CULL)             // ← no culling
                        .createCompositeState(false)
        );
    }

    public static ParticleRenderType PARTICLE_ADDITIVE = new ParticleRenderType() {
        public void begin(BufferBuilder builder, TextureManager manager) {
            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        public void end(Tesselator tesselator) {
            tesselator.end();
        }

        public String toString() {
            return Brutality.MOD_ID + ":additive";
        }
    };


}