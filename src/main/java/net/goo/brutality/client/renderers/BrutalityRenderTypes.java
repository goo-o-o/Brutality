package net.goo.brutality.client.renderers;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.shaders.BrutalityShaders;
import net.goo.brutality.client.renderers.shaders.PostEffectRegistry;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BrutalityRenderTypes extends RenderType {


    public BrutalityRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean b, boolean b1, Runnable runnable, Runnable runnable1) {
        super(string, vertexFormat, mode, i, b, b1, runnable, runnable1);
    }

    protected static final RenderStateShard.OutputStateShard PIXELATED_OUTPUT = new RenderStateShard.OutputStateShard("pixelated_target", () -> {
        RenderTarget target = PostEffectRegistry.getRenderTargetFor(BrutalityShaders.PIXELATED_SHADER);
        if (target != null) {
            target.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            target.bindWrite(false);
        }
    }, () -> Minecraft.getInstance().getMainRenderTarget().bindWrite(false));

    // For items (uses block atlas)
    public static final RenderType PIXELATE_ITEM = RenderType.create(
            Brutality.MOD_ID + ":pixelate_item",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_CUTOUT_SHADER)
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            InventoryMenu.BLOCK_ATLAS,
                            false,
                            false
                    ))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(PIXELATED_OUTPUT)
                    .createCompositeState(false)
    );

    public static final RenderType PIXELATE_ENTITY = RenderType.create(
            Brutality.MOD_ID + ":pixelate_entity",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_ENTITY_CUTOUT_SHADER)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setOutputState(PIXELATED_OUTPUT)

                    // --- ADD THESE TWO ---
                    // This ensures the stencil renders even if armor is "blocking" it
                    .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
                    // This ensures back-faces of the player (inside of arms) also trigger pixelation
                    .setCullState(RenderStateShard.NO_CULL)
                    // ---------------------

                    .createCompositeState(true)
    );
    public static final RenderType LIGHTNING = create(Brutality.MOD_ID + ":lightning",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            RenderType.CompositeState.builder()
                    .setShaderState(POSITION_COLOR_SHADER)
                    .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                    .createCompositeState(false));

    public static Function<ResourceLocation, RenderType> FULLBRIGHT = Util.memoize((texture) ->
        create(Brutality.MOD_ID + ":full_bright", DefaultVertexFormat.NEW_ENTITY, QUADS, 256, false, true, CompositeState
                .builder()
                .setTextureState(new TextureStateShard(texture, false, false))
                .setShaderState(RENDERTYPE_EYES_SHADER)
                .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                .setCullState(CULL)
                .createCompositeState(false)));

    public static Function<ResourceLocation, RenderType> FULLBRIGHT_NO_DEPTH = Util.memoize((texture) ->
            create(Brutality.MOD_ID + ":fullbright_no_depth", DefaultVertexFormat.NEW_ENTITY, QUADS, 256, false, true,
            CompositeState
                    .builder()
                    .setTextureState(new TextureStateShard(texture, false, false))
                    .setShaderState(RENDERTYPE_EYES_SHADER)
                    .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                    .setCullState(CULL)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false)));

    public static final RenderType GLOW_NO_TEXTURE = create(
            "brutality:glow_no_texture",
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256,
            false,
            true,
            CompositeState.builder()
                    .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                    .setTextureState(TextureStateShard.NO_TEXTURE)
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
                    .setCullState(CullStateShard.NO_CULL)
                    .setWriteMaskState(COLOR_WRITE)
                    .createCompositeState(false)
    );

    public static final RenderType ITEM_OUTLINE = create(Brutality.MOD_ID + ":item_outline",
            DefaultVertexFormat.BLOCK,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new ShaderStateShard(() -> BrutalityShaders.itemOutlineCoreShader))
                    .setTextureState(new RenderStateShard.TextureStateShard(
                            InventoryMenu.BLOCK_ATLAS, false, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );
    public static final RenderType PARTICLE_OUTLINE = create(Brutality.MOD_ID + ":particle_outline",
            DefaultVertexFormat.PARTICLE,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            RenderType.CompositeState.builder()
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .setTextureState(RenderStateShard.BLOCK_SHEET)
                    .setShaderState(new ShaderStateShard(() -> BrutalityShaders.particleOutlineCoreShader))
                    .createCompositeState(false));


    public static final Function<ResourceLocation, RenderType> TRAIL_RENDER_TYPE = Util.memoize((texture) ->
            create(Brutality.MOD_ID + ":trail",
            DefaultVertexFormat.NEW_ENTITY,
            VertexFormat.Mode.QUADS,
            256,
            true,
            true,
            CompositeState.builder()
                    .setShaderState(RENDERTYPE_ITEM_ENTITY_TRANSLUCENT_CULL_SHADER)
                    .setTextureState(new TextureStateShard(texture, false, false))
                    .setTransparencyState(RenderStateShard.ADDITIVE_TRANSPARENCY)
                    .setOutputState(RenderStateShard.TRANSLUCENT_TARGET)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setOverlayState(RenderStateShard.OVERLAY)
                    .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                    .createCompositeState(true)));

}