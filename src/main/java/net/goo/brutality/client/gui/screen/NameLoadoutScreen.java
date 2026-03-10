package net.goo.brutality.client.gui.screen;

import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundAddLoadoutPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

public class NameLoadoutScreen extends Screen {
    private final Screen lastScreen;
    private EditBox nameInput;

    public NameLoadoutScreen(Screen lastScreen) {
        super(Component.literal("New Loadout"));
        this.lastScreen = lastScreen;
    }

    @Override
    protected void init() {
        // Center the input box
        this.nameInput = new EditBox(this.font, this.width / 2 - 100, this.height / 2 - 10, 200, 20, Component.literal("Name"));
        this.nameInput.setMaxLength(16); // Keep it short for the GUI buttons
        this.nameInput.setFocused(true);
        this.setInitialFocus(this.nameInput);
        this.addWidget(this.nameInput);

        // Confirm Button
        this.addRenderableWidget(Button.builder(Component.literal("Create"), (btn) -> {
            String name = nameInput.getValue().trim();
            if (!name.isEmpty()) {
                PacketHandler.sendToServer(new ServerboundAddLoadoutPacket(name));
                // Go back to the inventory
                Minecraft.getInstance().setScreen(lastScreen);
            }
        })
        .pos(this.width / 2 - 50, this.height / 2 + 20)
        .size(100, 20)
        .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics); // Gray out the world
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 30, 0xFFFFFF);
        this.nameInput.render(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            // Trigger the same logic as the 'Create' button
            this.children().stream()
                .filter(c -> c instanceof Button)
                .map(c -> (Button) c)
                .findFirst()
                .ifPresent(Button::onPress);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
}