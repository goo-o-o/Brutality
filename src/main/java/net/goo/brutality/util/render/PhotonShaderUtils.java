package net.goo.brutality.util.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModList;

public class PhotonShaderUtils {
    public static boolean isStencilEnabled(RenderTarget target) {
        return !isUsingShaderPack();   // disable stencil effects under shaders
    }

    public static boolean useCombinedDepthStencilAttachment() {
        return !isUsingShaderPack();
    }

    public static boolean isShaderModInstalled() {
        return ModList.get().isLoaded("iris") || ModList.get().isLoaded("oculus");
    }

    public static boolean isUsingShaderPack() {
        return isShaderModInstalled() && IrisFramebufferUtils.isUsingShaderPack();
    }

    public static int getSolidFrameBufferID() {
        if (isUsingShaderPack()) {
            return IrisFramebufferUtils.getIrisSolidFboId();
        }

        return Minecraft.getInstance().getMainRenderTarget().frameBufferId;
    }

    public static int getTranslucentFrameBufferID() {
        if (isUsingShaderPack()) {
            return IrisFramebufferUtils.getIrisTranslucentFboId();
        }

        return Minecraft.getInstance().getMainRenderTarget().frameBufferId;
    }

    public static int getDepthTextureID() {
        if (isUsingShaderPack()) {
            return IrisFramebufferUtils.getIrisDepthTextureId();
        }

        return Minecraft.getInstance().getMainRenderTarget().getDepthTextureId();
    }

    public static int getSolidTextureID() {
        if (isUsingShaderPack()) {
            return IrisFramebufferUtils.getIrisSolidTextureId();
        }

        return Minecraft.getInstance().getMainRenderTarget().getColorTextureId();
    }

    public static int getTranslucentTextureID(boolean writeBuffer) {
        if (isUsingShaderPack()) {
            return IrisFramebufferUtils.getIrisTranslucentTextureId(writeBuffer);
        }

        return Minecraft.getInstance().getMainRenderTarget().getColorTextureId();
    }
}
