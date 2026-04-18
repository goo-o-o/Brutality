package net.goo.brutality.util.render;

import com.mojang.blaze3d.pipeline.RenderTarget;
import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL33;

import javax.annotation.Nullable;

public class ShaderHelper {
    private static Player pendingPlayer = null;
    private static ItemStack pendingStack = null;

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


    public static void push(Player player, ItemStack stack) {
        pendingPlayer = player;
        pendingStack = stack;
    }

    public static void clear() {
        pendingPlayer = null;
        pendingStack = null;
    }

    @Nullable
    public static Player getPlayer() {
        return pendingPlayer;
    }

    public static ItemStack getStack() {
        return pendingStack;
    }
}
