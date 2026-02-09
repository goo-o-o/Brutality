package net.goo.brutality.client.gui.tooltip;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.item.StatTrakUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@Mod.EventBusSubscriber (value = Dist.CLIENT)
public class StatTrakGui {
    protected static ResourceLocation BASE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/stat_trak/");
    private static final int overlayPadding = 4, totalFrames = 17;
    private static final int screenTextureW = 41, screenTextureH = 15, overlayW = screenTextureW + (overlayPadding * 2), overlayH = screenTextureH + (overlayPadding * 2);
    private static final int totalHeight = totalFrames * overlayH;

    protected static int padding = 3; // tooltipHeight and tooltipWidth do not account for the extra borders rendered, so we manually add an offset, I will do it only for y values

    public enum Position {
        TOP_LEFT {
            @Override
            public int getX(int guiX, int tooltipWidth) {
                return guiX;
            }

            @Override
            public int getY(int guiY, int tooltipHeight) {
                return guiY - screenTextureH - 1 - padding;
            }
        },
        TOP_RIGHT {
            @Override
            public int getX(int guiX, int tooltipWidth) {
                return guiX + tooltipWidth - screenTextureW;
            }

            @Override
            public int getY(int guiY, int tooltipHeight) {
                return guiY - screenTextureH - 1 - padding;
            }
        },
        BOTTOM_LEFT {
            @Override
            public int getX(int guiX, int tooltipWidth) {
                return guiX;
            }

            @Override
            public int getY(int guiY, int tooltipHeight) {
                return guiY + tooltipHeight + 1 + padding;
            }
        },
        BOTTOM_RIGHT {
            @Override
            public int getX(int guiX, int tooltipWidth) {
                return guiX + tooltipWidth - screenTextureW;
            }

            @Override
            public int getY(int guiY, int tooltipHeight) {
                return guiY + tooltipHeight + 1 + padding;
            }
        };

        public abstract int getX(int guiX, int tooltipWidth);

        public abstract int getY(int guiY, int tooltipHeight);
    }

    private static final int TOTAL_FRAMES = 17;


    public static void render(ItemStack stack, GuiGraphics pGuiGraphics, int pX, int pY, int pWidth, int pHeight, int pZ) {
        Position position = BrutalityClientConfig.STAT_TRAK_POSITION.get();
        int renderX = position.getX(pX, pWidth), renderY = position.getY(pY, pHeight);
        StatTrakUtils.StatTrakVariant statTrakVariant = StatTrakUtils.getVariant(stack);
        if (statTrakVariant == null) return;
        ResourceLocation PARENT = BASE.withSuffix(statTrakVariant.name().toLowerCase(Locale.ROOT) + "/");

        if (statTrakVariant == StatTrakUtils.StatTrakVariant.PRISMATIC || statTrakVariant == StatTrakUtils.StatTrakVariant.GOLD) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            // 1. Define the cycle
            int cycleLength = 60;
            int currentTick = mc.player.tickCount;

            // 2. Determine where we are in the 40-tick loop (0 to 39)
            int phase = currentTick % cycleLength;

            // 3. Play animation only if we are in the first 17 ticks of that cycle
            if (phase < TOTAL_FRAMES) {
                // Since TICKS_PER_FRAME is 1, phase IS our frame index

                pGuiGraphics.blit(
                        PARENT.withSuffix("overlay.png"),
                        renderX - overlayPadding,
                        renderY - overlayPadding,
                        pZ + 200,
                        0,
                        phase * overlayH,
                        overlayW,
                        overlayH,
                        overlayW,
                        totalHeight
                );
            }
        }

        ResourceLocation SCREEN = PARENT.withSuffix("screen.png");
        pGuiGraphics.blit(SCREEN, renderX, renderY, pZ, 0, 0, screenTextureW, screenTextureH, screenTextureW, screenTextureH);
        ResourceLocation SUPPORT = PARENT.withSuffix("support.png");
        if (position == Position.TOP_LEFT || position == Position.TOP_RIGHT) {
            pGuiGraphics.blit(SUPPORT, renderX, renderY + screenTextureH, pZ, 0, 0, screenTextureW, 1, screenTextureW, 1);
        } else {
            pGuiGraphics.blit(SUPPORT, renderX, renderY - 1, pZ, 0, 0, screenTextureW, 1, screenTextureW, 1);
        }

        int textOffset = 3;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().translate(0, 0, pZ + 100);

        String value = String.valueOf(StatTrakUtils.getStatTrakValue(stack));
        value = StringUtils.repeat("0", 6 - value.length()) + value;

        pGuiGraphics.drawString(Minecraft.getInstance().font, Component.literal(value).withStyle(s -> s.withFont(ModResources.STAT_TRAK_FONT)), renderX + textOffset, renderY + textOffset, statTrakVariant.color, false);
        pGuiGraphics.pose().popPose();
    }
}
