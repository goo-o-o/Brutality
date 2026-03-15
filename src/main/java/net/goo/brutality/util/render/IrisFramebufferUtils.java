//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.util.render;

import com.lowdragmc.lowdraglib.LDLib;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.irisshaders.iris.Iris;
import net.irisshaders.iris.api.v0.IrisApi;
import net.irisshaders.iris.gl.framebuffer.GlFramebuffer;
import net.irisshaders.iris.pipeline.IrisRenderingPipeline;
import net.irisshaders.iris.pipeline.SodiumTerrainPipeline;
import net.irisshaders.iris.pipeline.WorldRenderingPipeline;
import net.irisshaders.iris.shaderpack.properties.ParticleRenderingSettings;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL30C;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class IrisFramebufferUtils {
    private static boolean renderingGUIScreen = false;
    private static WorldRenderingPipeline lastPipeline = null;
    private static Field cachedField = null;
    private static int cachedSolidFramebufferId = -1;
    private static int cachedTranslucentFramebufferId = -1;
    private static int cachedDepthTextureId = -1;
    private static int cachedSolidTextureId = -1;
    private static int cachedTranslucentTextureId = -1;
    private static boolean depthTextureRefreshed = false;
    private static boolean solidTextureRefreshed = false;
    private static boolean translucentTextureRefreshed = false;

    public static int getIrisSolidFboId() {
        return getFramebufferID(true);
    }

    public static int getIrisTranslucentFboId() {
        return getFramebufferID(false);
    }

    private static int getFramebufferID(boolean solid) {
        if (renderingGUIScreen) {
            return Minecraft.getInstance().getMainRenderTarget().frameBufferId;
        } else {
            Optional<WorldRenderingPipeline> optional = Iris.getPipelineManager().getPipeline();
            if (optional.isEmpty()) {
                return Minecraft.getInstance().getMainRenderTarget().frameBufferId;
            } else {
                WorldRenderingPipeline pipeline = (WorldRenderingPipeline)optional.get();
                if (pipeline != lastPipeline) {
                    lastPipeline = pipeline;
                    cachedField = null;
                    cachedTranslucentFramebufferId = -1;
                    cachedSolidFramebufferId = -1;
                    translucentTextureRefreshed = false;
                    solidTextureRefreshed = false;
                    depthTextureRefreshed = false;
                }

                int cached = solid ? cachedSolidFramebufferId : cachedTranslucentFramebufferId;
                if (cached != -1) {
                    return cached;
                } else {
                    try {
                        if (pipeline instanceof IrisRenderingPipeline) {
                            if (cachedField == null) {
                                cachedField = IrisRenderingPipeline.class.getDeclaredField("sodiumTerrainPipeline");
                                cachedField.setAccessible(true);
                            }

                            SodiumTerrainPipeline stp = (SodiumTerrainPipeline)cachedField.get(pipeline);
                            GlFramebuffer solidBuffer = stp.getTerrainSolidFramebuffer();
                            GlFramebuffer translucentBuffer = stp.getTranslucentFramebuffer();
                            cachedSolidFramebufferId = solidBuffer.getId();
                            cachedTranslucentFramebufferId = translucentBuffer.getId();
                            return solid ? cachedSolidFramebufferId : cachedTranslucentFramebufferId;
                        }
                    } catch (Exception ignored) {
                    }

                    return Minecraft.getInstance().getMainRenderTarget().frameBufferId;
                }
            }
        }
    }

    public static int getIrisDepthTextureId() {
        return getTextureIdFromFramebuffer(getIrisSolidFboId(), 36096, () -> cachedDepthTextureId, (id) -> cachedDepthTextureId = id, () -> depthTextureRefreshed, (refreshed) -> depthTextureRefreshed = refreshed, () -> cachedSolidFramebufferId);
    }

    public static int getIrisSolidTextureId() {
        return getTextureIdFromFramebuffer(getIrisSolidFboId(), 36064, () -> cachedSolidTextureId, (id) -> cachedSolidTextureId = id, () -> solidTextureRefreshed, (refreshed) -> solidTextureRefreshed = refreshed, () -> cachedSolidFramebufferId);
    }

    public static int getIrisTranslucentTextureId(boolean writeBuffer) {
        return !writeBuffer && beforeEntitiesRendering() ? getIrisSolidTextureId() : getTextureIdFromFramebuffer(getIrisTranslucentFboId(), 36064, () -> cachedTranslucentTextureId, (id) -> cachedTranslucentTextureId = id, () -> translucentTextureRefreshed, (refreshed) -> translucentTextureRefreshed = refreshed, () -> cachedTranslucentFramebufferId);
    }

    private static int getTextureIdFromFramebuffer(int framebufferId, int attachment, IntSupplier cacheGetter, Consumer<Integer> cacheSetter, BooleanSupplier refreshedGetter, Consumer<Boolean> refreshedSetter, IntSupplier cachedFramebufferGetter) {
        if (renderingGUIScreen) {
            return attachment == 36096 ? Minecraft.getInstance().getMainRenderTarget().getDepthTextureId() : Minecraft.getInstance().getMainRenderTarget().getColorTextureId();
        } else {
            if (cachedFramebufferGetter.getAsInt() != framebufferId || !refreshedGetter.getAsBoolean()) {
                refreshedSetter.accept(true);
                cacheSetter.accept(attachment == 36096 ? Minecraft.getInstance().getMainRenderTarget().getDepthTextureId() : Minecraft.getInstance().getMainRenderTarget().getColorTextureId());
                RenderSystem.assertOnRenderThreadOrInit();
                GlStateManager._glBindFramebuffer(36160, framebufferId);
                int[] type = new int[1];
                GL30C.glGetFramebufferAttachmentParameteriv(36160, attachment, 36048, type);
                if (type[0] == 5890) {
                    int[] id = new int[1];
                    GL30C.glGetFramebufferAttachmentParameteriv(36160, attachment, 36049, id);
                    cacheSetter.accept(id[0]);
                }
            }

            return cacheGetter.getAsInt();
        }
    }

    public static Field getFboCachedField() {
        if (isUsingShaderPack()) {
            getIrisSolidFboId();
            getIrisTranslucentFboId();
        }

        return cachedField;
    }

    public static boolean isUsingShaderPack() {
        return (LDLib.isModLoaded("iris") || LDLib.isModLoaded("oculus")) && IrisApi.getInstance().isShaderPackInUse();
    }

    public static boolean isRenderingGUIScreen() {
        return renderingGUIScreen;
    }

    public static void setRenderingGUIScreen(boolean renderingGUIScreen) {
        IrisFramebufferUtils.renderingGUIScreen = renderingGUIScreen;
    }

    private static boolean beforeEntitiesRendering() {
        return Iris.getPipelineManager().getPipeline().map(WorldRenderingPipeline::getParticleRenderingSettings).orElse(ParticleRenderingSettings.MIXED) == ParticleRenderingSettings.BEFORE;
    }
}
