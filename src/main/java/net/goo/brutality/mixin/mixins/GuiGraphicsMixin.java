package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.client.gui.tooltip.StatTrakGui;
import net.goo.brutality.common.registry.BrutalityRarities;
import net.goo.brutality.util.ColorUtils;
import net.goo.brutality.util.RarityBorderManager;
import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
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
    public ItemStack tooltipStack;


    @Inject(method = "lambda$renderTooltipInternal$5", at = @At("HEAD"))
    private void renderTooltip(int x, int y, RenderTooltipEvent.Pre preEvent, List<ClientTooltipComponent> pComponents, int width, int height, CallbackInfo ci) {
        GuiGraphics guiGraphics = (((GuiGraphics) (Object) this));


        RenderTooltipEvent.Color colorEvent = ForgeHooksClient.onRenderTooltipColor(tooltipStack, guiGraphics, x, y, preEvent.getFont(), pComponents);


        ItemStack current = this.tooltipStack;
        if (current != null && !current.isEmpty()) {
            brutality$updateTimerValue(current);
            brutality$cachedMainStack = current.copy();
            brutality$renderTooltipBackground(guiGraphics, x, y, width, height, 400, colorEvent.getBackgroundStart(), colorEvent.getBackgroundEnd(), colorEvent.getBorderStart(), colorEvent.getBorderEnd());
            if (StatTrakUtils.hasStatTrak(current))
                StatTrakGui.render(current, guiGraphics, x, y, width, height, 401);
        }
    }


    @Unique
    private static void brutality$renderTooltipBackground(GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight, int pZ, int backgroundTop, int backgroundBottom, int borderTop, int borderBottom) {
        int i = pX - 3;
        int j = pY - 3;
        int k = pWidth + 3 + 3;
        int l = pHeight + 3 + 3;


        ColorUtils.ColorData data = BrutalityRarities.from(brutality$cachedMainStack);
        if (data == null) return;

        RarityBorderManager.BorderData borderToRender = data.idle;
        if (data.open != null && brutality$hoverTime < data.open.getAnimationDurationMs()) {
            borderToRender = data.open;
        }
        // Determine which border to render based on hover time
        long timeMs = brutality$hoverTime;

        borderTop = data.colors[0];
        borderBottom = data.colors[1];
        // Render the border
        if (borderToRender != null) {
            brutality$renderBorder(pGuiGraphics, pX, pY, pZ + 10, pWidth, pHeight, borderToRender, timeMs);
        }
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
        int frameH = border.frameHeight();   // 32
        int frameW = border.frameWidth();    // 128
        int corner = border.cornerSize();    // 16 or 32
        int textureH = frameH * border.frameCount();

        int frameIndex = border.hasAnimation() ? border.getFrameForTime(timeMs) : 0;
        int vOffset = frameIndex * frameH;

        int rightU = frameW - corner;
        int bottomV = frameH - corner;

        // Remove pad completely – corners are already centered in texture
        int leftX = x - corner / 2 - 4;
        int rightX = x + w - corner / 2 + 4;
        int topY = y - corner / 2 - 4;
        int bottomY = y + h - corner / 2 + 4;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // Corners
        gg.blit(tex, leftX, topY, z, 0, vOffset, corner, corner, frameW, textureH); // top-left
        gg.blit(tex, rightX, topY, z, rightU, vOffset, corner, corner, frameW, textureH); // top-right
        gg.blit(tex, leftX, bottomY, z, 0, vOffset + bottomV, corner, corner, frameW, textureH); // bottom-left
        gg.blit(tex, rightX, bottomY, z, rightU, vOffset + bottomV, corner, corner, frameW, textureH); // bottom-right

        // Top & bottom bars (centered)
        int midWidth = frameW - 2 * corner;
        int midScreenX = x + w / 2 - midWidth / 2;

        gg.blit(tex, midScreenX, topY, z, corner, vOffset, midWidth, corner, frameW, textureH);
        gg.blit(tex, midScreenX, bottomY, z, corner, vOffset + corner, midWidth, corner, frameW, textureH);
        RenderSystem.disableBlend();
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
