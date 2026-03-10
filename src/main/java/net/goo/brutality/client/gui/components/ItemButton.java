package net.goo.brutality.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ItemButton extends Button {
    private final ItemStack icon;
    private final boolean isActive;

    public ItemButton(int x, int y, int width, int height, ItemStack icon, boolean isActive, OnPress onPress) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);
        this.icon = icon;
        this.isActive = isActive;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        int offsetX = (this.width - 16) / 2;
        int offsetY = (this.height - 16) / 2;
        guiGraphics.renderFakeItem(icon, this.getX() + offsetX, this.getY() + offsetY);
    }
}