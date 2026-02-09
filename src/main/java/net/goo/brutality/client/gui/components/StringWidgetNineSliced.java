package net.goo.brutality.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class StringWidgetNineSliced extends StringWidget {
    public StringWidgetNineSliced(Component pMessage, Font pFont) {
        super(pMessage, pFont);
    }

    public StringWidgetNineSliced(int pWidth, int pHeight, Component pMessage, Font pFont) {
        super(pWidth, pHeight, pMessage, pFont);
    }

    public StringWidgetNineSliced(int pX, int pY, int pWidth, int pHeight, Component pMessage, Font pFont) {
        super(pX, pY, pWidth, pHeight, pMessage, pFont);
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
        pGuiGraphics.blitNineSlicedSized(pTexture, pX , pY, pWidth, pHeight, 5, 5, pWidth, pHeight, pUOffset, hoverOffset, pTextureWidth, pTextureHeight);

    }
}
