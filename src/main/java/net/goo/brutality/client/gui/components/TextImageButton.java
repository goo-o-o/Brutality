package net.goo.brutality.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;

public class TextImageButton extends ImageButton {
    private final Font font;
    private static final int RED = FastColor.ARGB32.color(255, 255, 0, 0);
    public int hoveredOrFocusedTextColor = RED;
    public int notHoveredOrFocusedTextColor = RED;
    public int inactiveTextColor = RED;
    public boolean dropShadow = true;
    public ResourceLocation icon = null;

    public TextImageButton(int x, int y, int w, int h, int xTex, int yTex, int yDiff, ResourceLocation tex, int texW, int texH, OnPress press, Component message,
                           int hoveredOrFocusedTextColor, int notHoveredOrFocusedTextColor, int inactiveTextColor, ResourceLocation icon) {
        this(x, y, w, h, xTex, yTex, yDiff, tex, texW, texH, press, message, Minecraft.getInstance().font);
        this.hoveredOrFocusedTextColor = hoveredOrFocusedTextColor;
        this.notHoveredOrFocusedTextColor = notHoveredOrFocusedTextColor;
        this.inactiveTextColor = inactiveTextColor;
        this.icon = icon;
    }

    public TextImageButton(int x, int y, int w, int h, int xTex, int yTex, int yDiff, ResourceLocation tex, int texW, int texH, OnPress press, Component message, Font font) {
        super(x, y, w, h, xTex, yTex, yDiff, tex, texW, texH, press);
        this.setMessage(message);
        this.font = font;
    }

    public TextImageButton(int x, int y, int w, int h, int xTex, int yTex, int yDiff, ResourceLocation tex, int texW, int texH, OnPress press, Component message) {
        this(x, y, w, h, xTex, yTex, yDiff, tex, texW, texH, press, message, Minecraft.getInstance().font);
    }


    @Override
    public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        int iconSize = this.height;
        int availableWidth = (this.icon != null) ? (this.width - iconSize - 4) : this.width;

        int textPadding = 4;
        int color = this.active ? (this.isHoveredOrFocused() ? hoveredOrFocusedTextColor : notHoveredOrFocusedTextColor) : inactiveTextColor;
        int textX = this.getX() + textPadding;
        int textY = this.getY() + (iconSize - font.lineHeight) / 2;

        Component originalMessage = getMessage();

        if (font.width(originalMessage) > availableWidth) {
            FormattedText truncated = font.substrByWidth(originalMessage, availableWidth - font.width("...") - textPadding);
            Component textToDraw = Component.empty().append(Component.literal(truncated.getString()).withStyle(originalMessage.getStyle())).append("...");
            guiGraphics.drawString(this.font, textToDraw, textX, textY, color, this.dropShadow);
        } else {
            guiGraphics.drawString(this.font, originalMessage, textX, textY, color, this.dropShadow);
        }

        if (this.icon != null) {
            int iconX = this.getX() + this.width - iconSize;
            int iconY = this.getY();
            guiGraphics.blit(this.icon, iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        }
    }
}