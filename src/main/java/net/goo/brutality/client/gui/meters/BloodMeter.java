package net.goo.brutality.client.gui.meters;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.BrutalityArmorMaterials;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.build_archetypes.BloodHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Brutality.MOD_ID)

public class BloodMeter {
    @SubscribeEvent
    public static void renderBloodMeter(RenderGuiOverlayEvent.Post e) {
        Player player = Minecraft.getInstance().player;
        if (shouldRender(player)) {
            BloodMeterRenderer.render(e.getGuiGraphics(), player);
        }
    }

    public static class BloodMeterRenderer {
        private static final ResourceLocation BG = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/blood_meter/bg.png");
        private static final ResourceLocation DIVIDER = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/blood_meter/divider.png");
        private static final ResourceLocation FG = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/blood_meter/fg.png");
        private static final ResourceLocation ICON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/blood_meter/icon.png");

        public static void render(GuiGraphics gui, Player player) {
            float percent = BloodHelper.getCurrentBloodPercentage(player);

            int fgW = 75;
            int visibleWidth = (int) (fgW * percent);

            int bgW = 81;
            int bgH = 11;
            int bgX = gui.guiWidth() / 2 - bgW / 2;
            int bgY = (int) (gui.guiHeight() * 0.75F);

            int fgX = bgX + 3;
            int fgY = bgY + 3;
            int iconX = bgX + 25;
            int iconY = bgY - 3;

            gui.blit(BG, bgX, bgY, 0, 0, bgW, bgH, bgW, bgH);
            int fgH = 5;
            gui.blit(FG, fgX, fgY, 0, 0, visibleWidth, fgH, fgW, fgH);
            int iconW = 31;
            int iconH = 10;
            gui.blit(DIVIDER, bgX, bgY, 0, 0, bgW, bgH, bgW, bgH);
            gui.blit(ICON, iconX, iconY, 0, 0, iconW, iconH, iconW, iconH);
        }
    }

    public static boolean shouldRender(Player player) {
        if (player == null) return false;
        return ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.VAMPIRE_LORD);
    }

}
