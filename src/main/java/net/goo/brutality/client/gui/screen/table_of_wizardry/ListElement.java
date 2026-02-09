package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface ListElement {
    int getHeight();
    void render(GuiGraphics gui, int x, int y, int width, int mouseX, int mouseY, float partialTicks);
    default boolean mouseClicked(double mouseX, double mouseY, int button) { return false; }
    default Component getNarration() { return Component.empty(); }
}
