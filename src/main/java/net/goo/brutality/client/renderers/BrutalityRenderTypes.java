package net.goo.brutality.client.renderers;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.shaders.BrutalityShaders;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityRenderTypes extends RenderType {
    public BrutalityRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean b, boolean b1, Runnable runnable, Runnable runnable1) {
        super(string, vertexFormat, mode, i, b, b1, runnable, runnable1);
    }

    public static List<RenderType> renderTypes = new ArrayList<>();
    public static List<RenderType> normalRenderTypes = new ArrayList<>();
    public static List<RenderType> particleRenderTypes = new ArrayList<>();

    public static RenderType registerRenderType(RenderType type, boolean isParticle) {
        renderTypes.add(type);
        if (isParticle) {
            particleRenderTypes.add(type);
        } else {
            normalRenderTypes.add(type);
        }
        return type;
    }


    public static final RenderType LIGHTNING = RenderType.create(Brutality.MOD_ID + ":lightning", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder()
            .setShaderState(POSITION_COLOR_SHADER)
            .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
            .createCompositeState(false));

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

    public static final ShaderStateShard ITEM_OUTLINE_SHADER = new ShaderStateShard(BrutalityShaders::getItemOutlineCoreShader);
    public static final RenderType ITEM_OUTLINE = create(Brutality.MOD_ID + ":item_outline",
            DefaultVertexFormat.BLOCK,  // was POSITION
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(ITEM_OUTLINE_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            InventoryMenu.BLOCK_ATLAS, false, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );
    public static final ShaderStateShard PARTICLE_OUTLINE_SHADER = new ShaderStateShard(BrutalityShaders::getParticleOutlineCoreShader);
    public static final RenderType PARTICLE_OUTLINE = registerRenderType(create(Brutality.MOD_ID + ":particle_outline",
            DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            RenderType.CompositeState.builder().setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE).setLightmapState(RenderStateShard.LIGHTMAP).setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setTextureState(RenderStateShard.BLOCK_SHEET).setShaderState(PARTICLE_OUTLINE_SHADER).createCompositeState(false)), true);


    public static final ParticleRenderType TRAIL_RENDER_TYPE = new ParticleRenderType() {
        @Override
        public void begin(BufferBuilder builder, TextureManager textureManager) {
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderSystem.setShader(GameRenderer::getParticleShader);
            builder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public void end(Tesselator tesselator) {
            tesselator.end();
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
        }

        @Override
        public String toString() { return "brutality:trail"; }
    };

    public static RenderType createDepthClearRenderType() {
        return RenderType.create(
                "entity_depth_clear",
                DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS,
                256,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                        .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.NO_DEPTH_TEST) // ALWAYS pass (ignore existing depth)
                        .setWriteMaskState(RenderStateShard.DEPTH_WRITE) // Only write depth, not color
                        .setCullState(RenderStateShard.NO_CULL)
                        .createCompositeState(false)
        );
    }

    // The actual visible black quad
    public static RenderType createOverlayRenderType() {
        return RenderType.create(
                "entity_redacted_overlay",
                DefaultVertexFormat.POSITION_COLOR,
                VertexFormat.Mode.QUADS,
                256,
                false,
                false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
                        .setTransparencyState(RenderStateShard.NO_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST) // Now test against the cleared depth
                        .setWriteMaskState(RenderStateShard.COLOR_WRITE) // Only write color
                        .setCullState(RenderStateShard.NO_CULL)
                        .createCompositeState(false)
        );
    }
}