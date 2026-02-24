package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.goo.brutality.util.ModResources;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Style;

public abstract class TableOfWizardryView implements Renderable, GuiEventListener {
    protected final TableOfWizardryScreen screen;
    protected static final Minecraft mc = Minecraft.getInstance();
    protected static Style style = Style.EMPTY.withFont(ModResources.TABLE_OF_WIZARDRY_FONT).withColor(ChatFormatting.DARK_GRAY);

    public TableOfWizardryView(TableOfWizardryScreen screen) {
        this.screen = screen;
    }

    public abstract void init();

    @Override
    public void render(GuiGraphics gui, int mx, int my, float partial) {}
}
