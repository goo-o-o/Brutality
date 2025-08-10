package net.goo.brutality.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.goo.brutality.Brutality;
import net.goo.brutality.magic.IBrutalitySpell;
import net.goo.brutality.magic.SpellStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class RadialProgressBarRenderer {
    private static final double RADIUS = 48;

    /**
     * Draw a circular radial progress bar at (x, y) with given progress (0.0 to 1.0) and ARGB color.
     * Credit to Paraglider mod
     */
    public static void renderProgressBar(ItemStack tome, @NotNull GuiGraphics graphics, double x, double y, double z, float progress) {
        SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(tome);
        if (spellEntry == null) return;
        IBrutalitySpell.MagicSchool school = spellEntry.spell().getSchool();
        String schoolStr = school.toString().toLowerCase(Locale.ROOT);
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderTexture(0,
                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spells/" + schoolStr + "/spell_container/cast_ring.png"));

//        float red = red(color) / 255.0f;
//        float green = green(color) / 255.0f;
//        float blue = blue(color) / 255.0f;
//        float alpha = alpha(color) / 255.0f;
        RenderSystem.setShaderColor(1, 1, 1, 1);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(x, y, z).uv(0.5f, 0.5f).endVertex();

        double end = Mth.clamp(progress, 0F, 1.0F);
        int pointCount = 180;
        for (float startPoint = 0; startPoint <= 1.0f; startPoint += 1.0f / pointCount) {
            if (startPoint >= end) break;
            double endPoint = Math.min(startPoint + 1.0f / pointCount, end);
            if (endPoint >= end) {
                addVertex(buffer, x, y, z, end, RADIUS);
                break;
            }
            addVertex(buffer, x, y, z, endPoint, RADIUS);
        }
        tesselator.end();

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableDepthTest();
    }

    private static void addVertex(BufferBuilder buffer, double x, double y, double z, double point, double radius) {
        double angle = point * 2 * Math.PI + (Math.PI / 2);
        double vx = Math.cos(angle);
        double vy = -Math.sin(angle); // Negative to match Minecraft's coordinate system (positive Y is down)
        buffer.vertex(x + vx * radius, y + vy * radius, z).uv((float) (vx / 2 + 0.5), (float) (vy / 2 + 0.5)).endVertex();
    }
}