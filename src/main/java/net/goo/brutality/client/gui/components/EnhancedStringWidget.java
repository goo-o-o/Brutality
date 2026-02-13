package net.goo.brutality.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class EnhancedStringWidget extends AbstractStringWidget {
    private float alignX;
    private final boolean wordWrap;
    private final ResourceLocation texture;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;
    private int xOffset;
    private int yOffset;
    private final Integer sliceWidth;
    private final Integer sliceHeight;
    private final int margin;

    EnhancedStringWidget(int pWidth, int pHeight, Component pMessage, Font pFont, int margin,
                         boolean wordWrap, float alignX, ResourceLocation texture,
                         int xTexStart, int yTexStart, int yDiffTex,
                         int textureWidth, int textureHeight, int xOffset, int yOffset,
                         Integer sliceWidth, Integer sliceHeight) {
        super(0, 0, pWidth, pHeight, pMessage, pFont);
        this.wordWrap = wordWrap;
        this.margin = margin;
        this.alignX = alignX;
        this.texture = texture;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sliceWidth = sliceWidth;
        this.sliceHeight = sliceHeight;
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
        this.renderTexture(pGuiGraphics, this.texture, this.getX() + this.xOffset, this.getY() + yOffset, this.xTexStart, this.yTexStart,
                this.yDiffTex, this.width, this.height, this.textureWidth, this.textureHeight);


        Component component = this.getMessage();
        Font font = this.getFont();
        if (this.wordWrap) {
            pGuiGraphics.drawWordWrap(font, component, getX() + margin, getY() + margin, getWidth() - margin * 2, getColor()); // alignment is ignored if word wrap is enabled
        } else {
            int i = this.getX() + Math.round(this.alignX * (float) (this.getWidth() - font.width(component) - margin));
            int j = this.getY() + this.margin + (this.getHeight() - 9) / 2;
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
        if (sliceWidth != null && sliceHeight != null)
            pGuiGraphics.blitNineSlicedSized(pTexture, pX, pY, pWidth, pHeight,
                    this.sliceWidth, this.sliceHeight,
                    this.textureWidth, this.textureHeight + margin * 2, // The width/height of the slice in the UV map
                    pUOffset, hoverOffset,
                    this.textureWidth, this.textureHeight);
        else
            pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
    }

    public static class Builder {
        private final Component message;
        private ResourceLocation texture;
        private int xTexStart;
        private int yTexStart;
        private int yDiffTex;
        private final int width;
        private final int height;
        private int textureWidth;
        private int textureHeight;
        private int xOffset;
        private int yOffset;
        private int margin;
        private Integer sliceWidth = null;
        private Integer sliceHeight = null;
        private boolean wordWrap = false;
        private float alignX = 0.5F;
        private final Font font;

        public Builder(Component message, Font font, int xOffset, int yOffset, int width, int height, int margin) {
            this.message = message;
            this.font = font;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.width = width;
            this.height = height;
            this.margin = margin;
        }

        public Builder alignLeft() {
            this.alignX = 0;
            return this;
        }

        public Builder alignCenter() {
            this.alignX = 0.5F;
            return this;
        }

        public Builder alignRight() {
            this.alignX = 1;
            return this;
        }

        public Builder wordWrap() {
            this.wordWrap = true;
            return this;
        }

        public Builder withOffset(int xOffset, int yOffset) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        public Builder withTexture(ResourceLocation texture,
                                   int xTexStart, int yTexStart, int yDiffTex) {
            this.texture = texture;
            this.xTexStart = xTexStart;
            this.yTexStart = yTexStart;
            this.yDiffTex = yDiffTex;
            this.textureWidth = width;
            this.textureHeight = height;
            return this;
        }

        public Builder withTextureNineSliced(ResourceLocation texture,
                                             int xTexStart, int yTexStart, int yDiffTex, int textureWidth, int textureHeight,
                                             int sliceWidth, int sliceHeight) {
            this.texture = texture;
            this.xTexStart = xTexStart;
            this.yTexStart = yTexStart;
            this.yDiffTex = yDiffTex;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
            this.sliceWidth = sliceWidth;
            this.sliceHeight = sliceHeight;
            return this;
        }

        public EnhancedStringWidget build() {
            return new EnhancedStringWidget(this.width, this.height, this.message, this.font, this.margin,
                    this.wordWrap, this.alignX, this.texture, this.xTexStart, this.yTexStart, this.yDiffTex,
                    this.textureWidth, this.textureHeight, this.xOffset, this.yOffset,
                    this.sliceWidth, this.sliceHeight);
        }
    }

}
