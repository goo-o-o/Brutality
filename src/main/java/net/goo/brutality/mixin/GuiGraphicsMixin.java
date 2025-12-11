package net.goo.brutality.mixin;

import net.goo.brutality.registry.ModRarities;
import net.goo.brutality.util.RarityBorderManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderTooltipEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class GuiGraphicsMixin {
    @Unique
    private static ItemStack brutality$cachedMainStack = ItemStack.EMPTY;
    @Unique
    private static long brutality$hoverStartTime = 0;
    @Unique
    private static long brutality$hoverTime = 0;

    @Shadow
    private ItemStack tooltipStack;


    @Inject(method = "lambda$renderTooltipInternal$5", at = @At("HEAD"))
    private void renderTooltip(int x, int y, RenderTooltipEvent.Pre preEvent, List<ClientTooltipComponent> pComponents, int width, int height, CallbackInfo ci) {
        GuiGraphics guiGraphics = (((GuiGraphics) (Object) this));
        ItemStack current = this.tooltipStack;

        brutality$updateTimerValue(current);

        brutality$cachedMainStack = current.copy();

        RenderTooltipEvent.Color colorEvent = ForgeHooksClient.onRenderTooltipColor(tooltipStack, guiGraphics, x, y, preEvent.getFont(), pComponents);
        brutality$renderTooltipBackground(guiGraphics, x, y, width, height, 400, colorEvent.getBackgroundStart(), colorEvent.getBackgroundEnd(), colorEvent.getBorderStart(), colorEvent.getBorderEnd());

    }

    @Unique
    private static void brutality$renderTooltipBackground(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight, int pZ, int backgroundTop, int backgroundBottom, int borderTop, int borderBottom) {
        int i = pX - 3;
        int j = pY - 3;
        int k = pWidth + 3 + 3;
        int l = pHeight + 3 + 3;


        ModRarities.RarityData data = ModRarities.from(brutality$cachedMainStack);
        if (data == null) return;
        Rarity rarity = data.rarity;
        RarityBorderManager manager = RarityBorderManager.getInstance();

        // Get borders
        RarityBorderManager.BorderData idleBorder = manager.getIdleBorder(rarity);
        RarityBorderManager.BorderData openBorder = manager.getOpenBorder(rarity);

        // Determine which border to render based on hover time
        long timeMs = brutality$hoverTime;
        RarityBorderManager.BorderData borderToRender = idleBorder; // Default to idle


        if (openBorder != null && timeMs < openBorder.getAnimationDurationMs()) {
            borderToRender = openBorder;
        }

        borderTop = data.colors[0];
        borderBottom = data.colors[1];
        // Render the border
        if (borderToRender != null) {
            brutality$renderBorder(pGuiGraphics, pX, pY, pZ + 10, pWidth, pHeight, borderToRender, timeMs);
        }

        System.out.println("TOP: " + borderTop);
        System.out.println("BOTTOM: " + borderBottom);


        brutality$renderHorizontalLine(pGuiGraphics, i, j - 1, k, pZ, backgroundTop);
        brutality$renderHorizontalLine(pGuiGraphics, i, j + l, k, pZ, backgroundBottom);
        brutality$renderRectangle(pGuiGraphics, i, j, k, l, pZ, backgroundTop, backgroundBottom);
        brutality$renderVerticalLineGradient(pGuiGraphics, i - 1, j, l, pZ, backgroundTop, backgroundBottom);
        brutality$renderVerticalLineGradient(pGuiGraphics, i + k, j, l, pZ, backgroundTop, backgroundBottom);
        brutality$renderFrameGradient(pGuiGraphics, i, j + 1, k, l, pZ, borderTop, borderBottom);
    }

    @Unique
    private static void brutality$renderBorder(GuiGraphics gg, int x, int y, int z, int w, int h,
                                               RarityBorderManager.BorderData border, long timeMs) {
        ResourceLocation tex = border.texture();
        int frameH = border.frameHeight();  // Should be 32
        int frameW = border.frameWidth();   // Should be 128
        int corner = border.cornerSize();   // Should be 16
        int pad = corner / 4;

        // Get frame index based on time
        int frameIndex = border.hasAnimation() ? border.getFrameForTime(timeMs) : 0;
        int vOffset = frameIndex * frameH;  // Each frame is 32 pixels tall

        int rightU = frameW - corner;       // 128 - 16 = 112
        int bottomV = frameH - corner;      // 32 - 16 = 16
        int textureH = frameH * border.frameCount();
        // Positions for corners
        int leftCornerX = x - corner / 2;
        int rightCornerX = x + w - corner / 2;
        int topCornerY = y - corner / 2;
        int bottomCornerY = y + h - corner / 2;

        // === Render CORNERS ===
        // Top-left corner
        gg.blit(tex, leftCornerX - pad, topCornerY - pad, z,
                0, vOffset, corner, corner, frameW, textureH);

        // Top-right corner
        gg.blit(tex, rightCornerX + pad, topCornerY - pad, z,
                rightU, vOffset, corner, corner, frameW, textureH);

        // Bottom-left corner
        gg.blit(tex, leftCornerX - pad, bottomCornerY + pad, z,
                0, vOffset + bottomV, corner, corner, frameW, textureH);

        // Bottom-right corner
        gg.blit(tex, rightCornerX + pad, bottomCornerY + pad, z,
                rightU, vOffset + bottomV, corner, corner, frameW, textureH);

        int screenMiddleStartX = x + w / 2;
        int textureMiddleWidth = frameW - corner - corner;

        gg.blit(tex, screenMiddleStartX - textureMiddleWidth / 2, topCornerY - pad, z,
                corner, vOffset,
                textureMiddleWidth, corner,  // Use up to 64px from texture
                frameW, textureH);
        gg.blit(tex, screenMiddleStartX - textureMiddleWidth / 2, bottomCornerY + pad, z,
                corner, vOffset + corner,
                textureMiddleWidth, corner,
                frameW, textureH);
    }

    @Unique
    private void brutality$updateTimerValue(ItemStack of) {
        if (ItemStack.isSameItemSameTags(brutality$cachedMainStack, of)) {
            brutality$hoverTime = System.currentTimeMillis() - brutality$hoverStartTime;
        } else {
            brutality$hoverStartTime = System.currentTimeMillis();
            brutality$hoverTime = 0;
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
