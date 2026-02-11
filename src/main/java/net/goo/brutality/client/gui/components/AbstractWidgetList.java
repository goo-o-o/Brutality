package net.goo.brutality.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.ArrayList;
import java.util.List;

import static net.goo.brutality.client.gui.screen.TableOfWizardryScreen.*;

public class AbstractWidgetList extends AbstractContainerEventHandler implements Renderable, NarratableEntry {
    private final List<AbstractWidget> widgets = new ArrayList<>();
    private final Minecraft client;
    protected final int width;
    protected final int height;
    protected final int top;
    protected final int bottom;
    protected final int right;
    protected final int left;
    private boolean scrolling;
    protected float scrollDistance;
    protected boolean captureMouse = true;
    protected final int border;

    private final int barWidth;
    private final int barLeft;

    /**
     * @param client the minecraft instance this AbstractWidgetList should use
     * @param width  the width
     * @param height the height
     * @param top    the offset from the top (y coord)
     * @param left   the offset from the left (x coord)
     */
    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left) {
        this(client, width, height, top, left, 4);
    }

    /**
     * @param client the minecraft instance this AbstractWidgetList should use
     * @param width  the width
     * @param height the height
     * @param top    the offset from the top (y coord)
     * @param left   the offset from the left (x coord)
     * @param border the size of the border
     */
    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left, int border) {
        this(client, width, height, top, left, border, 6);
    }

    /**
     * Base constructor
     *
     * @param client   the minecraft instance this AbstractWidgetList should use
     * @param width    the width
     * @param height   the height
     * @param top      the offset from the top (y coord)
     * @param left     the offset from the left (x coord)
     * @param border   the size of the border
     * @param barWidth the width of the scroll bar
     */
    public AbstractWidgetList(Minecraft client, int width, int height, int top, int left, int border, int barWidth) {
        this.client = client;
        this.width = width;
        this.height = height;
        this.top = top;
        this.left = left;
        this.bottom = height + this.top;
        this.right = width + this.left;
        this.barLeft = this.left + this.width - barWidth;
        this.border = border;
        this.barWidth = barWidth;
    }

    protected int getContentHeight() {
        if (widgets.isEmpty()) return 0;

        return widgets.stream().mapToInt(AbstractWidget::getHeight).sum()
//                + Math.max(0, widgets.size() - 2) * margin
                + Math.max(0, widgets.size() - 1) * border;
    }

    /**
     * Draws the background of the scroll panel. This runs AFTER Scissors are enabled.
     */
    protected void drawBackground(GuiGraphics guiGraphics, Tesselator tess, float partialTick) {
//        BufferBuilder worldr = tess.getBuilder();
//
//        if (this.client.level != null) {
//            this.drawGradientRect(guiGraphics, this.left, this.top, this.right, this.bottom, bgColorFrom, bgColorTo);
//        } else // Draw dark dirt background
//        {
//            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
//            RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
//            final float texScale = 32.0F;
//            worldr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
//            worldr.vertex(this.left, this.bottom, 0.0D).uv(this.left / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.vertex(this.right, this.bottom, 0.0D).uv(this.right / texScale, (this.bottom + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.vertex(this.right, this.top, 0.0D).uv(this.right / texScale, (this.top + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.vertex(this.left, this.top, 0.0D).uv(this.left / texScale, (this.top + (int) this.scrollDistance) / texScale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            tess.end();
//        }
    }

    protected void drawPanel(GuiGraphics gui, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
        int cy = relativeY;
        for (int i = 0; i < widgets.size(); i++) {
            AbstractWidget w = widgets.get(i);
            w.setX(left);
            w.setY(cy);

            if (w.visible) {
                w.render(gui, mouseX, mouseY, 0);
            }
            cy += w.getHeight() + border;
        }
    }

    protected boolean clickPanel(double mouseX, double mouseY, int button) {
        return false;
    }

    private int getMaxScroll() {
        return this.getContentHeight() - (this.height - this.border);
    }

    private void applyScrollLimits() {
        // Math.max(0, ...) ensures that if the content is short,
        // the max allowed scroll is just 0.
        int max = Math.max(0, getMaxScroll());

        if (this.scrollDistance < 0.0F) {
            this.scrollDistance = 0.0F;
        }

        if (this.scrollDistance > max) {
            this.scrollDistance = max;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (scroll != 0) {
            this.scrollDistance += (float) (-scroll * getScrollAmount());
            applyScrollLimits();
            return true;
        }
        return false;
    }

    protected int getScrollAmount() {
        return 20;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= this.left && mouseX <= this.left + this.width &&
                mouseY >= this.top && mouseY <= this.bottom;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button))
            return true;

        this.scrolling = button == 0 && mouseX >= barLeft && mouseX < barLeft + barWidth;
        if (this.scrolling) {
            return true;
        }
        int mouseListY = ((int) mouseY) - this.top - this.getContentHeight() + (int) this.scrollDistance - border;
        if (mouseX >= left && mouseX <= right && mouseListY < 0) {
            return this.clickPanel(mouseX - left, mouseY - this.top + (int) this.scrollDistance - border, button);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.mouseReleased(mouseX, mouseY, button))
            return true;
        boolean ret = this.scrolling;
        this.scrolling = false;
        return ret;
    }

    private int getBarHeight() {
        int barHeight = (height * height) / this.getContentHeight();

        if (barHeight < 32) barHeight = 32;

        if (barHeight > height - border * 2)
            barHeight = height - border * 2;

        return barHeight;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.scrolling) {
            int maxScroll = height - getBarHeight();
            double moved = deltaY / maxScroll;
            this.scrollDistance += (float) (getMaxScroll() * moved);
            applyScrollLimits();
            return true;
        }
        return false;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Tesselator tess = Tesselator.getInstance();

        double scale = client.getWindow().getGuiScale();
        RenderSystem.enableScissor((int) (left * scale), (int) (client.getWindow().getHeight() - (bottom * scale)),
                (int) (width * scale), (int) (height * scale));

        this.drawBackground(guiGraphics, tess, partialTick);

        int baseY = this.top + border - (int) this.scrollDistance;
        this.drawPanel(guiGraphics, right, baseY, tess, mouseX, mouseY);

        RenderSystem.disableDepthTest();

        int contentH = getContentHeight();
        int maxScroll = Math.max(0, contentH - height);
        if (maxScroll > 0) {
            int barHeight = getBarHeight();

            int barTop = (int) this.scrollDistance * (height - barHeight) / maxScroll + this.top;
            if (barTop < this.top) {
                barTop = this.top;
            }

            int sbX = left + width - barWidth;
            int sbEndX = sbX + barWidth;


            // Track background
            guiGraphics.fill(sbX, top, sbEndX, top + height, GRAY);
            guiGraphics.renderOutline(sbX, top, barWidth, height, DARK_GRAY);

            // Thumb
            guiGraphics.fill(sbX, barTop, sbEndX, barTop + barHeight, LIGHTER_GRAY);
            guiGraphics.fill(sbX + 1, barTop + 1, sbEndX - 1, barTop + barHeight - 1, LIGHT_GRAY);
            guiGraphics.renderOutline(sbX + 1, barTop + 1, barWidth - 2, barHeight - 2, WHITE);
            guiGraphics.fill(sbX + 2, barTop + 2, sbEndX - 2, barTop + barHeight - 2, DARK_WHITE);
        }

        RenderSystem.disableBlend();
        RenderSystem.disableScissor();
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return widgets;
    }

    public void add(AbstractWidget widget) {
        widgets.add(widget);
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput) {

    }
}
