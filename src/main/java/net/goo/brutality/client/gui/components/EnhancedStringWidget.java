package net.goo.brutality.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.util.RenderUtils;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class EnhancedStringWidget extends AbstractStringWidget {
    private final int lineHeight;
    private final float alignX;
    private final boolean wordWrap;
    private final ResourceLocation texture;
    private final boolean dropShadow;
    private final int xTexStart;
    private final int yTexStart;
    private final int yDiffTex;
    private final int textureWidth;
    private final int textureHeight;
    private final Integer sliceWidth;
    private final Integer sliceHeight;
    private final int xOffset;
    private final int yOffset;
    private final int xTextOffset;
    private final int yTextOffset;
    private final int margin;

    EnhancedStringWidget(int x, int y, int pWidth, int pHeight, MutableComponent pMessage, Font pFont, int margin, int xOffset, int yOffset, int xTextOffset, int yTextOffset,
                         boolean wordWrap, float alignX, ResourceLocation texture, boolean dropShadow,
                         int xTexStart, int yTexStart, int yDiffTex,
                         int textureWidth, int textureHeight, int lineHeight,
                         Integer sliceWidth, Integer sliceHeight) {
        super(x, y, pWidth, pHeight, pMessage, pFont);
        this.wordWrap = wordWrap;
        this.margin = margin;
        this.alignX = alignX;
        this.texture = texture;
        this.dropShadow = dropShadow;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yDiffTex = yDiffTex;
        this.lineHeight = lineHeight;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.sliceWidth = sliceWidth;
        this.sliceHeight = sliceHeight;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xTextOffset = xTextOffset;
        this.yTextOffset = yTextOffset;
    }

    protected boolean clicked(double pMouseX, double pMouseY) {
        return this.active && isMouseOver(pMouseX, pMouseY);
    }


    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.visible) {
            this.isHovered = isMouseOver(pMouseX, pMouseY);
            this.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
            this.updateTooltip();
        }
    }

    @Override
    public boolean isMouseOver(double pMouseX, double pMouseY) {
        return pMouseX >= (double) this.getX() - margin && pMouseY >= (double) this.getY() - margin
                && pMouseX < (double) (this.getX() + this.width + margin) && pMouseY < (double) (this.getY() + this.height + margin);
    }

    @Override
    public int getY() {
        return super.getY() + yOffset;
    }

    @Override
    public int getX() {
        return super.getX() + xOffset;
    }

    @Override
    public int getHeight() {
        return this.height + this.margin * 2;
    }

    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        // Render texture EXPANDED by margin
        if (this.texture != null)
            this.renderTexture(pGuiGraphics, this.texture,
                    this.getX() - margin, this.getY() - margin,
                    this.xTexStart, this.yTexStart, this.yDiffTex,
                    this.width + margin * 2, this.height + margin * 2,
                    this.textureWidth, this.textureHeight);


        int x = getX() + xTextOffset;
        int y = getY() + yTextOffset;
        int right = x + width;
        int bottom = y + height;

        if (this.wordWrap) {
            RenderUtils.drawWordWrapWithAlignment(pGuiGraphics, getFont(), getMessage(),
                    x, y - 1, this.width, getColor(), lineHeight, dropShadow, alignX);
        } else {
            // Pass the actual calculated boundaries
            renderScrollingString(pGuiGraphics, getFont(), getMessage(), pMouseX, pMouseY, x, y, right, bottom, getColor(), alignX);
        }
    }

    protected void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, Component pText, int pMouseX, int pMouseY, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor, float alignX) {
        int textWidth = pFont.width(pText);
        int centerY = (pMinY + pMaxY - 9) / 2 + 1;
        int availableWidth = pMaxX - pMinX;
        boolean isHovering = pMouseX >= pMinX && pMouseX <= pMaxX && pMouseY >= pMinY && pMouseY <= pMaxY;
        if (textWidth > availableWidth && isHovering) {
//            pGuiGraphics.enableScissor(pMinX, pMinY, pMaxX + (dropShadow ? 1 : 0), pMaxY);
            // SCROLLING LOGIC
            double offset;

            int scrollDelta = textWidth - availableWidth;
            double time = (double) Util.getMillis() / 1000.0D;
            double speed = Math.max((double) scrollDelta * 0.5D, 3.0D);
            double sineWave = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * time / speed)) / 2.0D + 0.5D;
            offset = Mth.lerp(sineWave, 0.0D, scrollDelta);


            // Use pMinX as base, then subtract the scroll offset
            pGuiGraphics.drawString(pFont, pText, pMinX - (int) offset, centerY, pColor, dropShadow);
//            pGuiGraphics.disableScissor();
        } else {
            int alignedX = pMinX + (int) ((availableWidth - textWidth) * alignX);
            pGuiGraphics.drawString(pFont, pText, alignedX, centerY, pColor, dropShadow);
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
        if (sliceWidth != null && sliceHeight != null) {
            pGuiGraphics.blitNineSlicedSized(pTexture, pX, pY, pWidth, pHeight,
                    this.sliceWidth, this.sliceHeight,
                    this.textureWidth, this.textureHeight,
                    pUOffset, hoverOffset,
                    this.textureWidth, this.textureHeight);
        } else {
            pGuiGraphics.blit(pTexture, pX, pY, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
        }
    }

    public static class Builder {
        private final MutableComponent message;
        private ResourceLocation texture;
        private int xTexStart;
        private int yTexStart;
        private int yDiffTex;
        private Integer width;
        private Integer height;
        private int textureWidth;
        private int textureHeight;
        private final int margin;
        private int lineHeight = 9;
        private Integer sliceWidth = null;
        private Integer sliceHeight = null;
        private boolean wordWrap = false;
        private boolean dropShadow = true;
        private float alignX = 0.5F;
        private final Font font;
        private int xOffset;
        private int yOffset;
        private int xTextOffset;
        private int yTextOffset;
        private int x, y;

        public Builder(Integer width, Integer height, MutableComponent message, Font font, int margin) {
            this.message = message;
            this.font = font;
            this.width = width;
            this.height = height;
            this.margin = margin;
        }

        public Builder dropShadow(boolean dropShadow) {
            this.dropShadow = dropShadow;
            return this;
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

        public Builder lineHeight(int lineHeight) {
            this.lineHeight = lineHeight;
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

        public Builder relPos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder offset(int x, int y) {
            this.xOffset = x;
            this.yOffset = y;
            return this;
        }

        public Builder textOffset(int x, int y) {
            this.xTextOffset = x;
            this.yTextOffset = y;
            return this;
        }

        public EnhancedStringWidget build() {
            if (this.width == null) {
                if (this.wordWrap) {
                    this.width = 100;
                } else {
                    this.width = font.width(message);
                }
            }
            if (this.height == null) {
                if (this.wordWrap) {
                    this.height = RenderUtils.wordWrapHeight(font, message, this.width, this.lineHeight);
                } else {
                    this.height = this.lineHeight;
                }
            }


            return new EnhancedStringWidget(this.x, this.y, this.width, this.height, this.message, this.font, this.margin, this.xOffset, this.yOffset,
                    this.xTextOffset, this.yTextOffset, this.wordWrap, this.alignX, this.texture, this.dropShadow,
                    this.xTexStart, this.yTexStart, this.yDiffTex,
                    this.textureWidth, this.textureHeight, this.lineHeight, this.sliceWidth, this.sliceHeight);
        }


    }

}
