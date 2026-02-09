package net.goo.brutality.common.fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.Brutality;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class LiquifiedManaFluidType extends FluidType {
    /**
     * Default constructor.
     *
     * @param properties the general properties of the fluid type
     */
    public LiquifiedManaFluidType(Properties properties) {
        super(properties);
    }


    private static final ResourceLocation STILL = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/liquified_mana_still");
    private static final ResourceLocation FLOWING = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/liquified_mana_flow");
    private static final ResourceLocation OVERLAY = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/overlay/liquified_mana.png");
    private static final Vector3f FOG_COLOR = new Vector3f(0.075F, 0.322F, 0.557F);

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getStillTexture() {
                return STILL;
            }

            @Override
            public ResourceLocation getFlowingTexture() {
                return FLOWING;
            }

            @Override
            public @NotNull ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                return OVERLAY;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return FOG_COLOR;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance,
                                        float partialTick, float nearDistance, float farDistance, FogShape shape) {
                // Controls how thick the fog is.
                // nearDistance: where the fog starts (0 = right in your face)
                // farDistance: where you can't see anything anymore (measured in blocks)
                RenderSystem.setShaderFogShape(FogShape.SPHERE);
                RenderSystem.setShaderFogStart(0f);
                RenderSystem.setShaderFogEnd(4f); // Very thick, magical feel
            }
        });
    }
}
