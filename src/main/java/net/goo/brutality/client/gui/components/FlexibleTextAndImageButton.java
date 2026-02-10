package net.goo.brutality.client.gui.components;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class FlexibleTextAndImageButton extends Button {
    protected final ResourceLocation resourceLocation;
    protected final int xTexStart;
    protected final int yTexStart;
    protected final int yDiffTex;
    protected final int textureWidth;
    protected final int textureHeight;
    private final int xOffset;
    private final int yOffset;
    private final int xTextOffset;
    private final int yTextOffset;
    private final int usedTextureWidth;
    private final int usedTextureHeight;
    private final boolean dropShadow;
    private final float alignX;
    private final Integer activeColor;   // null = use fallback 16777215 (white)
    private final Integer hoverColor;    // null = use fallback 10526880
    private final Integer defaultColor;  // null = use fallback -1
    private final Integer xIconOffset;
    private final Integer yIconOffset;
    private final Integer iconWidth;
    private final Integer iconHeight;
    private final ResourceLocation icon;

    public FlexibleTextAndImageButton(
            Component pMessage, int pWidth, int pHeight,
            int pXTexStart, int pYTexStart,
            int pXOffset, int pYOffset,
            int xTextOffset, int yTextOffset,
            int pYDiffTex,
            int pUsedTextureWidth, int pUsedTextureHeight,
            int pTextureWidth, int pTextureHeight,
            float alignX, boolean dropShadow,
            ResourceLocation pResourceLocation,
            Button.OnPress pOnPress,
            Integer hoverColor, Integer activeColor, Integer defaultColor,
            Integer xIconOffset, Integer yIconOffset, ResourceLocation icon,
            Integer iconWidth, Integer iconHeight
    ) {
        super(0, 0, pWidth, pHeight, pMessage, pOnPress, DEFAULT_NARRATION);
        this.textureWidth = pTextureWidth;
        this.textureHeight = pTextureHeight;
        this.xTexStart = pXTexStart;
        this.yTexStart = pYTexStart;
        this.yDiffTex = pYDiffTex;
        this.resourceLocation = pResourceLocation;
        this.xOffset = pXOffset;
        this.yOffset = pYOffset;
        this.xTextOffset = xTextOffset;
        this.yTextOffset = yTextOffset;
        this.usedTextureWidth = pUsedTextureWidth;
        this.usedTextureHeight = pUsedTextureHeight;
        this.dropShadow = dropShadow;
        this.alignX = alignX;
        this.hoverColor = hoverColor;
        this.activeColor = activeColor;
        this.defaultColor = defaultColor;
        this.xIconOffset = xIconOffset;
        this.yIconOffset = yIconOffset;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.icon = icon;
    }


    public void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderTexture(pGuiGraphics, this.resourceLocation, this.getXOffset(), this.getYOffset(), this.xTexStart, this.yTexStart, this.yDiffTex, this.usedTextureWidth, this.usedTextureHeight, this.textureWidth, this.textureHeight);
        Minecraft minecraft = Minecraft.getInstance();
        int i = getFGColor();
        this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);

        if (this.icon != null && this.xIconOffset != null && this.yIconOffset != null) {
            int actualXOffset = xIconOffset > 0 ? xIconOffset : this.width - this.iconWidth + xIconOffset;
            int actualYOffset = yIconOffset > 0 ? yIconOffset : this.height - this.iconHeight + yIconOffset;
            pGuiGraphics.blit(icon, this.getX() + actualXOffset, this.getY() + actualYOffset, 0,0, iconWidth, iconHeight, iconWidth, iconHeight);
        }

    }

    @Override
    public int getFGColor() {
        int colorToUse;
        if (!this.active) {
            colorToUse = Objects.requireNonNullElse(this.defaultColor, -1);
        } else if (this.isHoveredOrFocused()) {
            colorToUse = Objects.requireNonNullElse(this.hoverColor, 10526880);
        } else {
            colorToUse = Objects.requireNonNullElse(this.activeColor, 16777215);
        }
        return colorToUse;
    }

    public void renderString(GuiGraphics pGuiGraphics, Font pFont, int pColor) {
        renderScrollingString(pGuiGraphics, pFont, this.getMessage(), this.getX() + xTextOffset, this.getY() + yTextOffset, xTextOffset + this.getX() + this.getWidth(), yTextOffset + this.getY() + this.getHeight(), pColor, this.dropShadow);
    }

    @Override
    protected void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, int pWidth, int pColor) {
        int i = this.getX() + pWidth;
        int j = this.getX() + this.getWidth() - pWidth;
        renderScrollingString(pGuiGraphics, pFont, this.getMessage(), i, this.getY(), j, this.getY() + this.getHeight(), pColor, this.dropShadow);
    }

    protected void renderScrollingString(GuiGraphics pGuiGraphics, Font pFont, Component pText, int pMinX, int pMinY, int pMaxX, int pMaxY, int pColor, boolean dropShadow) {
        int i = pFont.width(pText);
        int j = (pMinY + pMaxY - pFont.lineHeight) / 2;
        int k = pMaxX - pMinX;
        if (i > k) {
            int l = i - k;
            double d0 = (double) Util.getMillis() / 1000.0D;
            double d1 = Math.max((double) l * 0.5D, 3.0D);
            double d2 = Math.sin((Math.PI / 2D) * Math.cos((Math.PI * 2D) * d0 / d1)) / 2.0D + 0.5D;
            double d3 = Mth.lerp(d2, 0.0D, l);
            pGuiGraphics.enableScissor(pMinX, pMinY, pMaxX, pMaxY);
            pGuiGraphics.drawString(pFont, pText, pMinX - (int) d3, j, pColor, dropShadow);
            pGuiGraphics.disableScissor();
        } else {
            int availableWidth = pMaxX - pMinX;
            int textWidth = pFont.width(pText);

            // 2. Calculate the starting X based on alignX
            // If alignX is 0.0 (Left), start at pMinX
            // If alignX is 0.5 (Center), start at pMinX + (half of remaining space)
            // If alignX is 1.0 (Right), start at pMinX + (all remaining space)
            int xPos = pMinX + Math.round(this.alignX * (float) (availableWidth - textWidth));

            pGuiGraphics.drawString(pFont, pText, xPos, j, pColor, dropShadow);
        }
    }

    private int getXOffset() {
        return this.getX() + (this.width / 2 - this.usedTextureWidth / 2) + this.xOffset;
    }

    private int getYOffset() {
        return this.getY() + this.yOffset;
    }

    public static FlexibleTextAndImageButton.Builder builder(Component pMessage, ResourceLocation pResourceLocation, Button.OnPress pOnPress) {
        return new FlexibleTextAndImageButton.Builder(pMessage, pResourceLocation, pOnPress);
    }

    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final Component message;
        private final ResourceLocation resourceLocation;
        private final Button.OnPress onPress;
        private int xTexStart;
        private int yTexStart;
        private int yDiffTex;
        private int usedTextureWidth;
        private int usedTextureHeight;
        private int textureWidth;
        private int textureHeight;
        private int xOffset;
        private int yOffset;
        private int xTextOffset;
        private int yTextOffset;
        private int width;
        private int height;
        private float alignX;
        private boolean dropShadow;
        private Integer activeColor = null;   // null = use fallback 16777215 (white)
        private Integer hoverColor = null;    // null = use fallback 10526880
        private Integer defaultColor = null;  // null = use fallback -1
        private ResourceLocation icon;
        private Integer xIconOffset;
        private Integer yIconOffset;
        private Integer iconWidth;
        private Integer iconHeight;

        public Builder(Component pMessage, ResourceLocation pResourceLocation, Button.OnPress pOnPress) {
            this.message = pMessage;
            this.resourceLocation = pResourceLocation;
            this.onPress = pOnPress;
        }

        public FlexibleTextAndImageButton.Builder withIcon(ResourceLocation icon, int xOffset, int yOffset, int iconWidth, int iconHeight) {
            this.xIconOffset = xOffset;
            this.yIconOffset = yOffset;
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
            this.icon = icon;
            return this;
        }


        public FlexibleTextAndImageButton.Builder texStart(int pX, int pY) {
            this.xTexStart = pX;
            this.yTexStart = pY;
            return this;
        }

        public FlexibleTextAndImageButton.Builder offset(int pX, int pY) {
            this.xOffset = pX;
            this.yOffset = pY;
            return this;
        }

        public FlexibleTextAndImageButton.Builder size(int pWidth, int pHeight) {
            this.width = pWidth;
            this.height = pHeight;
            return this;
        }

        public FlexibleTextAndImageButton.Builder yDiffTex(int pYDiffTex) {
            this.yDiffTex = pYDiffTex;
            return this;
        }

        public FlexibleTextAndImageButton.Builder dropShadow(boolean dropShadow) {
            this.dropShadow = dropShadow;
            return this;
        }

        public FlexibleTextAndImageButton.Builder usedTextureSize(int pWidth, int pHeight) {
            this.usedTextureWidth = pWidth;
            this.usedTextureHeight = pHeight;
            return this;
        }

        public FlexibleTextAndImageButton.Builder textureSize(int pWidth, int pHeight) {
            this.textureWidth = pWidth;
            this.textureHeight = pHeight;
            return this;
        }

        public FlexibleTextAndImageButton.Builder horizontalAlignment(float alignX) {
            this.alignX = alignX;
            return this;
        }

        public FlexibleTextAndImageButton.Builder alignLeft() {
            this.alignX = 0;
            return this;
        }

        public FlexibleTextAndImageButton.Builder alignCenter() {
            this.alignX = 0.5F;
            return this;
        }

        public FlexibleTextAndImageButton.Builder alignRight() {
            this.alignX = 1F;
            return this;
        }

        public Builder activeColor(int color) {
            this.activeColor = color;
            return this;
        }

        public Builder hoverColor(int color) {
            this.hoverColor = color;
            return this;
        }

        public Builder defaultColor(int color) {
            this.defaultColor = color;
            return this;
        }

        public Builder textOffset(int x, int y) {
            this.xTextOffset = x;
            this.yTextOffset = y;
            return this;
        }

        public FlexibleTextAndImageButton build() {
            return new FlexibleTextAndImageButton(
                    this.message, this.width, this.height,
                    this.xTexStart, this.yTexStart,
                    this.xOffset, this.yOffset,
                    this.xTextOffset, this.yTextOffset,
                    this.yDiffTex,
                    this.usedTextureWidth, this.usedTextureHeight,
                    this.textureWidth, this.textureHeight,
                    this.alignX, this.dropShadow,
                    this.resourceLocation, this.onPress,
                    this.hoverColor, this.activeColor, this.defaultColor,   // pass the Integer fields
                    this.xIconOffset, this.yIconOffset, this.icon,
                    this.iconWidth, this.iconHeight
            );
        }

    }
}