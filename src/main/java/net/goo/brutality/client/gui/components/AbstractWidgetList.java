package net.goo.brutality.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static net.goo.brutality.client.gui.screen.TableOfWizardryScreen.*;

public class AbstractWidgetList extends ScrollPanel {
    private final List<AbstractWidget> widgets = new ArrayList<>();
    Minecraft client;
    private int barWidth = 6;

    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left) {
        super(client, width, height, top, left);
        this.client = client;
    }

    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left, int border) {
        super(client, width, height, top, left, border);
        this.client = client;
    }

    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left, int border, int barWidth) {
        super(client, width, height, top, left, border, barWidth);
        this.client = client;
        this.barWidth = barWidth;
    }

    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left, int border, int barWidth, int bgColor) {
        super(client, width, height, top, left, border, barWidth, bgColor);
        this.client = client;
        this.barWidth = barWidth;

    }

    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left, int border, int barWidth, int bgColorFrom, int bgColorTo) {
        super(client, width, height, top, left, border, barWidth, bgColorFrom, bgColorTo);
        this.client = client;
        this.barWidth = barWidth;
    }

    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left, int border, int barWidth, int bgColorFrom, int bgColorTo, int barBgColor, int barColor, int barBorderColor) {
        super(client, width, height, top, left, border, barWidth, bgColorFrom, bgColorTo, barBgColor, barColor, barBorderColor);
        this.client = client;
        this.barWidth = barWidth;
    }

    public void add(AbstractWidget widget) {
        widgets.add(widget);
    }

    @Override
    protected int getContentHeight() {
        if (widgets.isEmpty()) return 0;

        return widgets.stream().mapToInt(AbstractWidget::getHeight).sum()
                + Math.max(0, widgets.size() - 1) * border;
    }

    @Override
    protected void drawBackground(GuiGraphics guiGraphics, Tesselator tess, float partialTick) {

    }

    @Override
    protected void drawPanel(GuiGraphics gui, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        int cy = relativeY;
        for (AbstractWidget w : widgets) {
            w.setX(left);
            w.setY(cy);
            if (w.visible) {
                w.render(gui, mouseX, mouseY, 0);
            }
            cy += w.getHeight() + border;
        }
    }

    @Override
    public @NotNull NarrationPriority narrationPriority() {
        return NarrationPriority.HOVERED;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }

    @Override
    public List<? extends GuiEventListener> children() {
        return widgets;
    }


    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTick) {
        // Keep scissor + background + panel drawing from parent
        double scale = client.getWindow().getGuiScale();
        RenderSystem.enableScissor(
                (int) (left * scale),
                (int) (client.getWindow().getHeight() - (bottom * scale)),
                (int) (width * scale),
                (int) (height * scale)
        );

        drawBackground(gui, Tesselator.getInstance(), partialTick);

        int baseY = top + border - (int) scrollDistance;
        drawPanel(gui, right, baseY, Tesselator.getInstance(), mouseX, mouseY);

        RenderSystem.disableDepthTest();

        // ── Your custom scrollbar ──
        int contentH = getContentHeight();
        int maxScroll = Math.max(0, contentH - height);

        if (maxScroll > 0) {
            int sbX = left + width - 6;
            int sbEndX = sbX + barWidth;

            int thumbH = Mth.clamp((height * height) / contentH, 12, height);
            int thumbY = top + (int) (scrollDistance * (height - thumbH) / maxScroll);

            // Track background
            gui.fill(sbX, top, sbEndX, top + height, GRAY);
            gui.renderOutline(sbX, top, barWidth, height, DARK_GRAY);

            // Thumb
            gui.fill(sbX, thumbY, sbEndX, thumbY + thumbH, LIGHTER_GRAY);
            gui.fill(sbX + 1, thumbY + 1, sbEndX - 1, thumbY + thumbH - 1, LIGHT_GRAY);
            gui.renderOutline(sbX + 1, thumbY + 1, barWidth - 2, thumbH - 2, WHITE);
            gui.fill(sbX + 2, thumbY + 2, sbEndX - 2, thumbY + thumbH - 2, DARK_WHITE);
        }

        RenderSystem.disableBlend();
        RenderSystem.disableScissor();
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY);
    }
}
