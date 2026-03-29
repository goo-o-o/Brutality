package net.goo.brutality.util.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.ModList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

public class ShaderHelper {
    public static boolean shouldUseAlternateRendering() {
        if (ModList.get().isLoaded("iris")) {
            return IrisApi.getInstance().isShaderPackInUse() || Minecraft.useShaderTransparency();
        }
        return Minecraft.useShaderTransparency();

    }

    public static boolean isDrawRenderTarget(RenderTarget target) {
        return target.frameBufferId == getDrawFrameBufferId();
    }

    public static int getDrawFrameBufferId() {
        return GL11.glGetInteger(GL33.GL_DRAW_FRAMEBUFFER_BINDING);
    }
}
