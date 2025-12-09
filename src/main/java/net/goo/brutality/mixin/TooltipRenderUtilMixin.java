package net.goo.brutality.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.BrutalityShaders;
import net.goo.brutality.registry.ModRarities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TooltipRenderUtil.class)
public abstract class TooltipRenderUtilMixin {
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


        brutality$drawFancyBorder(guiGraphics, x, y, z + 1, width, height, 4, ModRarities.RarityData.FABLED.default_sprite);


        BrutalityShaders.applyFoilToTooltip(guiGraphics, x, y, z, width, height);
        ci.cancel();
    }

    @Unique
    private static void brutality$drawFancyBorder(GuiGraphics guiGraphics,
                                                  int x, int y, int z, int width, int height, int padding,
                                                  TextureAtlasSprite sprite) {  // <-- pass the sprite (animated!)


        PoseStack pose = guiGraphics.pose();
        pose.pushPose();

        guiGraphics.blit(x, y, z + 1, 64, 16, sprite);

        pose.popPose();
    }


    @Unique
    private static void brutality$blitSpriteSection(GuiGraphics graphics, int x, int y, int w, int h, float u, float v, int uW, int vH, TextureAtlasSprite sprite) {
        SpriteContents c = sprite.contents();
        int width = (int) ((float) c.width() / (sprite.getU1() - sprite.getU0()));
        int height = (int) ((float) c.height() / (sprite.getV1() - sprite.getV0()));
        graphics.blit(sprite.atlasLocation(), x, y, w, h, sprite.getU(u) * (float) width, (float) height * sprite.getV(v), uW, vH, width, height);
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