package net.goo.brutality.client.gui.misc_elements;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.weapon.sword.RoyalGuardianSword;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Brutality.MOD_ID)

public class RoyalGuardianSwordGui implements IGuiOverlay {

    private static final ResourceLocation BAR = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/royal_guardian_sword/bar.png");
    private static final int barWidth = 225, barHeight = 36;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack useItem = player.getUseItem();
        if (!useItem.is(BrutalityItems.ROYAL_GUARDIAN_SWORD.get())) return;

        float percent = RoyalGuardianSword.getChargePercent(player);

        if (percent < 1) {
            // need to render bg
            guiGraphics.setColor(0.5F, 0.5F, 0.5F, 1);
            guiGraphics.blit(BAR, (screenWidth - barWidth) / 2, screenHeight - 90, 10, 0, 0, barWidth, barHeight, barWidth, barHeight);
            guiGraphics.setColor(1F, 1F, 1F, 1);
        }

        guiGraphics.blit(BAR, (screenWidth - barWidth) / 2, screenHeight - 90, 10, 0, 0, (int) (barWidth * percent), barHeight, barWidth, barHeight);
    }
}
