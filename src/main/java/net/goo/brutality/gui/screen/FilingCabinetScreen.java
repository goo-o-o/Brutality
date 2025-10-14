package net.goo.brutality.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.gui.menu.FilingCabinetMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.goo.brutality.block.block_entity.FilingCabinetBlockEntity;

public class FilingCabinetScreen extends AbstractContainerScreen<FilingCabinetMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.parse("textures/gui/container/generic_54.png");

    public FilingCabinetScreen(FilingCabinetMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        // Adjust imageHeight for 2 rows (18 slots) instead of 3 rows
        this.imageHeight = 133; // 54 (3 rows) = 166, 36 (2 rows) = 133
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        // Render background for 2 rows (18 slots)
        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}