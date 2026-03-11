package net.goo.brutality.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.capabilities.PlayerLoadoutsCap;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundAddLoadoutPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class NameLoadoutScreen extends Screen {
    private final Screen lastScreen;
    private EditBox nameInput;

    // Default starting icon
    private ResourceLocation selectedIcon = PlayerLoadoutsCap.DEFAULT_LOADOUT_BUTTON;

    private final String[] types = {"arm_down", "arm_side", "arm_up", "shield", "sword", "pickaxe", "axe", "heart"};
    private final String[] colors = {"green", "red", "blue"};

    public NameLoadoutScreen(Screen lastScreen) {
        super(Component.literal("New Loadout"));
        this.lastScreen = lastScreen;
    }

    private static class IconButton extends ImageButton {

        public IconButton(int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, int pYDiffTex, ResourceLocation pResourceLocation, int pTextureWidth, int pTextureHeight, OnPress pOnPress) {
            super(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, pTextureWidth, pTextureHeight, pOnPress);
        }

        @Override
        public void renderTexture(GuiGraphics pGuiGraphics, ResourceLocation pTexture, int pX, int pY, int pUOffset, int pVOffset, int pTextureDifference, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
            int i = pVOffset + pTextureDifference * 2;

            RenderSystem.enableDepthTest();
            pGuiGraphics.blit(pTexture, pX, pY, (float)pUOffset, (float)i, pWidth, pHeight, pTextureWidth, pTextureHeight);
        }
    }

    @Override
    protected void init() {
        // 1. Name Input Box
        this.nameInput = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 60, 200, 20, Component.literal("Name"));
        this.nameInput.setMaxLength(32);
        this.nameInput.setFocused(true);
        this.setInitialFocus(this.nameInput);
        this.addWidget(this.nameInput);
        this.setFocused(this.nameInput);

        // 2. Icon Selection Grid
        int startX = this.width / 2 - (types.length * 18) / 2;
        int startY = this.height / 2 - 18;

        for (int c = 0; c < colors.length; c++) {
            for (int t = 0; t < types.length; t++) {
                String iconPath = "textures/gui/loadout/" + types[t] + "_" + colors[c] + ".png";
                ResourceLocation iconRes = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, iconPath);

                // Add an ImageButton for each icon
                this.addRenderableWidget(new IconButton(
                        startX + (t * 18), startY + (c * 18), 16, 16,
                        0, 0, 16, iconRes, 16, 48,
                        (btn) -> this.selectedIcon = iconRes
                ));
            }
        }

        // 3. Confirm Button
        this.addRenderableWidget(Button.builder(Component.literal("Create"), (btn) -> {
                    String name = nameInput.getValue().trim();
                    if (!name.isEmpty()) {
                        // Pass both the name and the selected icon to your packet
                        PacketHandler.sendToServer(new ServerboundAddLoadoutPacket(name, selectedIcon));
                        Minecraft.getInstance().setScreen(lastScreen);
                    }
                })
                .pos(this.width / 2 - 50, this.height / 2 + 50)
                .size(100, 20)
                .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);

        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 80, 0xFFFFFF);

        // Render a preview of the currently selected icon
        guiGraphics.blit(this.selectedIcon, this.width / 2 + 110, this.height / 2 - 60, 0, 32, 16, 16, 16, 48);

        this.nameInput.render(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}