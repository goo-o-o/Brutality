package net.goo.brutality.util.tooltip;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.generic.BrutalityAugmentItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.List;

public class MagicItemAugmentComponent implements ClientTooltipComponent {
    // Roughly adapted from https://github.com/Shadows-of-Fire/Apotheosis/blob/1.21/src/main/java/dev/shadowsoffire/apotheosis/client/SocketTooltipRenderer.java
    private final int spacing = 12;
    private static final ResourceLocation AUGMENT_SLOT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/augment_slot.png");
    private final AugmentComponent component;

    public MagicItemAugmentComponent(AugmentComponent component) {
        this.component = component;
    }

    @Override
    public int getHeight() {
        return this.spacing * component.maxSlots;
    }

    @Override
    public int getWidth(Font pFont) {
        int maxWidth = 12;
        for (BrutalityAugmentItem augmentItem : component.augments) {
            maxWidth = Math.max(maxWidth, pFont.width(augmentItem.getDescription()) + 12);
        }
        return maxWidth;
    }

    @Override
    public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
        // 1. Render Background Slots
        for (int i = 0; i < component.maxSlots(); i++) {
            pGuiGraphics.blit(AUGMENT_SLOT, pX, pY + (this.spacing * i), 0, 0, 10, 10, 10, 10);
        }

        int currentY = pY;
        for (BrutalityAugmentItem augmentItem : this.component.augments()) {
            TextureAtlasSprite model = Minecraft.getInstance().getItemRenderer().getModel(augmentItem.getDefaultInstance(), null, null, 0).getParticleIcon();

            pGuiGraphics.blit(pX + 1, currentY + 1, 0, 8, 8, model);

            currentY += spacing;
        }
    }

    private static final int DARK_GRAY = ChatFormatting.DARK_GRAY.getColor();
    @Override
    public void renderText(Font pFont, int pX, int pY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
        for (int i = 0; i < component.maxSlots; i++) {
            int color;
            Component text;
            if (i < component.augments.size()) {
                text = component.augments.get(i).getDescription();
                color = 0xAABBCC;
            } else {
                text = Component.translatable("message.brutality.empty_slot");
                color = DARK_GRAY;
            }
            pFont.drawInBatch(text, pX + 12, pY + 1 + this.spacing * i, color, true, pMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);

        }
    }

    public record AugmentComponent(ItemStack parent, List<BrutalityAugmentItem> augments,
                                   int maxSlots) implements TooltipComponent {
    }

}
