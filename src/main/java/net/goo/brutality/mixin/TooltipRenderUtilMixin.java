package net.goo.brutality.mixin;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.BrutalityShaders;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TooltipRenderUtil.class)
public abstract class TooltipRenderUtilMixin {
    @Unique
    private static final long brutality$startTime = System.currentTimeMillis();

    @Inject(method = "renderTooltipBackground(Lnet/minecraft/client/gui/GuiGraphics;IIIIIIIII)V",
            at = @At("HEAD"), cancellable = true, remap = false)
    private static void mymod$addCornerDecorations(
            GuiGraphics guiGraphics,
            int x, int y,
            int width, int height,
            int z,
            int bgStart, int bgEnd,
            int borderStart, int borderEnd,
            CallbackInfo ci) {
        int padding = 4;
        int i = x - 3;
        int j = y - 3;
        int k = width + 3 + 3;
        int l = height + 3 + 3;
        brutality$renderHorizontalLine(guiGraphics, i, j - 1, k, z, bgStart);
        brutality$renderHorizontalLine(guiGraphics, i, j + l, k, z, bgEnd);
        brutality$renderRectangle(guiGraphics, i, j, k, l, z, bgStart, bgEnd);
        brutality$renderVerticalLineGradient(guiGraphics, i - 1, j, l, z, bgStart, bgEnd);
        brutality$renderVerticalLineGradient(guiGraphics, i + k, j, l, z, bgStart, bgEnd);
        brutality$renderFrameGradient(guiGraphics, i, j + 1, k, l, z, borderStart, borderEnd);


        brutality$drawFancyBorder(guiGraphics, x, y, width, height, z + 1, 4);


        BrutalityShaders.applyFoilToTooltip(guiGraphics, x, y, z, width, height);
        ci.cancel();
    }

    @Unique
    private static void brutality$drawFancyBorder(GuiGraphics guiGraphics, int x, int y, int width, int height, int z, int padding) {
        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/tooltip/decoration/fabled.png");
        int cornerSize = 8;
        int textureWidth = 64;
        int textureHeight = 16;
        int middleTextureWidth = 48; // The middle section in your texture

        // Cap middle width at texture size or available space
        int middleWidth = Math.min(width - 2 * cornerSize, middleTextureWidth);

        // Draw corners
        guiGraphics.blit(texture, x - padding, y - padding, z, 0, 0, cornerSize, cornerSize, textureWidth, textureHeight);
        guiGraphics.blit(texture, x + width - cornerSize + padding, y - padding, z, 56, 0, cornerSize, cornerSize, textureWidth, textureHeight);
        guiGraphics.blit(texture, x - padding, y + height - cornerSize + padding, z, 0, 8, cornerSize, cornerSize, textureWidth, textureHeight);
        guiGraphics.blit(texture, x + width - cornerSize + padding, y + height - cornerSize + padding, z, 56, 8, cornerSize, cornerSize, textureWidth, textureHeight);

        // Draw middle sections (centered, blit once)
        if (middleWidth > 0) {
            int middleU = 8; // Start UV position
            int middleUSize = middleTextureWidth;

            // Center the source region if we're drawing less than full width
            if (middleWidth < middleTextureWidth) {
                middleU = 8 + (middleTextureWidth - middleWidth) / 2;
                middleUSize = middleWidth;
            }

            // Center horizontally
            int middleX = x + (width - middleWidth) / 2;

            // Top middle
            guiGraphics.blit(texture, middleX, y, z, middleU, 0, middleWidth, cornerSize, textureWidth, textureHeight);
            // Bottom middle
            guiGraphics.blit(texture, middleX, y + height - cornerSize, z, middleU, 8, middleWidth, cornerSize, textureWidth, textureHeight);
        }
    }

    @Unique
    private static void brutality$renderRectangle(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight, int pZ, int pColor, int colorTo) {
        pGuiGraphics.fillGradient(pX, pY, pX + pWidth, pY + pHeight, pZ, pColor, colorTo);
    }

    @Unique
    private static void brutality$renderFrameGradient(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight, int pZ, int pTopColor, int pBottomColor) {
        brutality$renderVerticalLineGradient(pGuiGraphics, pX, pY, pHeight - 2, pZ, pTopColor, pBottomColor);
        brutality$renderVerticalLineGradient(pGuiGraphics, pX + pWidth - 1, pY, pHeight - 2, pZ, pTopColor, pBottomColor);
        brutality$renderHorizontalLine(pGuiGraphics, pX, pY - 1, pWidth, pZ, pTopColor);
        brutality$renderHorizontalLine(pGuiGraphics, pX, pY - 1 + pHeight - 1, pWidth, pZ, pBottomColor);
    }

    private static void renderVerticalLine(GuiGraphics pGuiGraphics, int pX, int pY, int pLength, int pZ, int pColor) {
        pGuiGraphics.fill(pX, pY, pX + 1, pY + pLength, pZ, pColor);
    }

    @Unique
    private static void brutality$renderVerticalLineGradient(GuiGraphics pGuiGraphics, int pX, int pY, int pLength, int pZ, int pTopColor, int pBottomColor) {
        pGuiGraphics.fillGradient(pX, pY, pX + 1, pY + pLength, pZ, pTopColor, pBottomColor);
    }

    @Unique
    private static void brutality$renderHorizontalLine(GuiGraphics pGuiGraphics, int pX, int pY, int pLength, int pZ, int pColor) {
        pGuiGraphics.fill(pX, pY, pX + pLength, pY + 1, pZ, pColor);
    }

}