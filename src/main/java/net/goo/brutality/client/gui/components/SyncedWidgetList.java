package net.goo.brutality.client.gui.components;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SyncedWidgetList extends AbstractWidgetList {
    private final List<AbstractWidgetList> partners = new ArrayList<>();

    public SyncedWidgetList(Minecraft client, int width, int height, int top, int left) {
        super(client, width, height, top, left);
    }

    public SyncedWidgetList(Minecraft client, int width, int height, int top, int left, int margin) {
        super(client, width, height, top, left, margin);
    }

    public SyncedWidgetList(Minecraft client, int width, int height, int top, int left, int margin, int spacing, int barWidth) {
        super(client, width, height, top, left, margin, spacing, barWidth);
    }


    public void setPartners(AbstractWidgetList... partners) {
        this.partners.addAll(Arrays.asList(partners));
    }

    @Override
    public void setScrollDistance(double scrollDistance) {
        this.scrollDistance = scrollDistance; // Directly update field
        for (AbstractWidgetList partner : partners) {
            // Prevent infinite recursion by checking the current value
            if (Math.abs(partner.getScrollDistance() - scrollDistance) > 0.001f) {
                partner.setScrollDistance(scrollDistance);
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        boolean handled = super.mouseScrolled(mouseX, mouseY, scroll);
        if (handled) {
            this.setScrollDistance(this.scrollDistance); // Trigger the sync
        }
        return handled;
    }
}
