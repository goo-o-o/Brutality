package net.goo.brutality.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnhancedStringWidget extends AbstractStringWidget {
    private float alignX = 0.5F;
    private final boolean wordWrap;

    public EnhancedStringWidget(Component pMessage, Font pFont, boolean wordWrap) {
        this(0, 0, pFont.width(pMessage.getVisualOrderText()), 9, pMessage, pFont, wordWrap);
    }

    public EnhancedStringWidget(int pWidth, Component pMessage, Font pFont, boolean wordWrap) {
        this(0, 0, pWidth, pFont.wordWrapHeight(pMessage, pWidth), pMessage, pFont, wordWrap);
    }

    public EnhancedStringWidget(int pX, int pY, int pWidth, int pHeight, Component pMessage, Font pFont, boolean wordWrap) {
        super(pX, pY, pWidth, pHeight, pMessage, pFont);
        this.active = false;
        this.wordWrap = wordWrap;
    }

    public float getAlignX() {
        return alignX;
    }

    public EnhancedStringWidget setColor(int pColor) {
        super.setColor(pColor);
        return this;
    }

    private EnhancedStringWidget horizontalAlignment(float pHorizontalAlignment) {
        this.alignX = pHorizontalAlignment;
        return this;
    }

    public EnhancedStringWidget alignLeft() {
        return this.horizontalAlignment(0.0F);
    }

    public EnhancedStringWidget alignCenter() {
        return this.horizontalAlignment(0.5F);
    }

    public EnhancedStringWidget alignRight() {
        return this.horizontalAlignment(1.0F);
    }

    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Component component = this.getMessage();
        Font font = this.getFont();
        int i = this.getX() + Math.round(this.alignX * (float) (this.getWidth() - font.width(component)));
        int j = this.getY() + (this.getHeight() - 9) / 2;
        if (this.wordWrap) {
            pGuiGraphics.drawWordWrap(font, component, getX(), getY(), getWidth(), getColor()); // alignment is ignored if word wrap is enabled
        } else {
            pGuiGraphics.drawString(font, component, i, j, this.getColor());
        }
    }

    @Override
    public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        int hoverOffset = pVOffset;
        if (!this.isActive()) {
            hoverOffset = pVOffset + pTextureDifference * 2;
        } else if (this.isHoveredOrFocused()) {
            hoverOffset = pVOffset + pTextureDifference;
        }

        RenderSystem.enableDepthTest();
        pGuiGraphics.blitNineSlicedSized(pTexture, pX, pY, pWidth, pHeight, 5, 5, pWidth, pHeight, pUOffset, hoverOffset, pTextureWidth, pTextureHeight);

    }


}
