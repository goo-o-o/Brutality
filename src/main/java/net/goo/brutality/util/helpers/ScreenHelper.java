package net.goo.brutality.util.helpers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScreenHelper {
    public static class BlackScreen extends Screen {
        public BlackScreen() {
            super(Component.literal(""));
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);

            pGuiGraphics.fill(0,0,1080, 1920, BrutalityTooltipHelper.rgbToInt(0,0,0));

        }

        @Override
        public boolean shouldCloseOnEsc() {
            return false;
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }

}
