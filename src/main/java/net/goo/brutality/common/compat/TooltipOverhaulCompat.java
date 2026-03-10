package net.goo.brutality.common.compat;

import dev.xylonity.tooltipoverhaul.client.TooltipContext;
import net.goo.brutality.client.gui.tooltip.StatTrakGui;
import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

public class TooltipOverhaulCompat {
    public static void handleStatTrak(TooltipContext ctx, Rectangle rect) {
        ItemStack stack = ctx.stack();
        if (StatTrakUtils.hasStatTrak(stack)) {
            StatTrakGui.render(stack, ctx.graphics(), rect.x, rect.y, rect.width, rect.height, 1000);
        }
    }
}
