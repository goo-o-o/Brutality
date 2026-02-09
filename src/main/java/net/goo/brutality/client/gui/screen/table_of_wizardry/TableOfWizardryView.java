package net.goo.brutality.client.gui.screen.table_of_wizardry;

import net.goo.brutality.client.gui.screen.TableOfWizardryScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;

public abstract class TableOfWizardryView implements Renderable, GuiEventListener {
    protected final TableOfWizardryScreen screen;


    public TableOfWizardryView(TableOfWizardryScreen screen) {
        this.screen = screen;
    }

    public abstract void init();

    @Override
    public void render(GuiGraphics gui, int mx, int my, float partial) {}
}
