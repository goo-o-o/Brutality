package net.goo.brutality.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.util.magic.SpellCastingHandler;
import net.goo.brutality.util.magic.SpellCooldownTracker;
import net.goo.brutality.util.magic.SpellStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE, modid = Brutality.MOD_ID)

public class SpellSelectionGui implements IGuiOverlay {

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = Minecraft.getInstance().player;
        if (player == null) return;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        if (mainHand.getItem() instanceof BaseMagicTome) {
            renderSpellSelection(guiGraphics, player, mainHand);
        } else if (offHand.getItem() instanceof BaseMagicTome) {
            renderSpellSelection(guiGraphics, player, offHand);
        }
    }

    private static final ResourceLocation SELECTED_SLOT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spell_selection/selected_slot.png");
    private static final ResourceLocation SLOT = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/spell_selection/slot.png");
    private static final int WHITE = FastColor.ARGB32.color(255, 255, 255, 255);
    private static final int ICON_BG = FastColor.ARGB32.color(191, 18, 18, 7);

    private static final int ICON_SIZE = 16, SLOT_SIZE = 20, SELECTED_SLOT_SIZE = 22;

    public static void renderSpellSelection(GuiGraphics gui, Player player, ItemStack tome) {
        List<SpellStorage.SpellEntry> spells = SpellStorage.getSpells(tome);
        SpellStorage.SpellEntry current = SpellStorage.getCurrentSpellEntry(tome);
        if (spells.isEmpty() || current == null) return;

        int index = spells.indexOf(current);
        int size = spells.size();

        int slotsToRender = size == 1 ? 1 : (size <= 3 ? 3 : 5);
        int half = slotsToRender / 2;

        int screenW = gui.guiWidth();
        int screenH = gui.guiHeight();



        final int GAP = 1;
        final int CENTER_GAP = 2;

        int baseY = screenH - 90;        // Fixed just above vanilla overlay message
        int centerX = screenW / 2;


        for (int i = -half; i <= half; i++) {
            int slotIdx = (index + i + size) % size;
            SpellStorage.SpellEntry entry = spells.get(slotIdx);

            boolean isSelected = entry.equals(current);

            int slotSize = isSelected ? SELECTED_SLOT_SIZE : SLOT_SIZE;
            ResourceLocation tex = isSelected ? SELECTED_SLOT : SLOT;

            float alpha = 1.0f;
            int dist = Math.abs(i);
            if (dist == 1) alpha = 0.9f;      // adjacent to center
            if (dist == 2) alpha = 0.6f;      // edges

            int offset = -i * (SLOT_SIZE + GAP);  // invert direction
            if (Math.abs(i) >= 1) {
                // Keep same gap logic but flip sign direction
                offset += i < 0 ? CENTER_GAP / 2 : -CENTER_GAP / 2;
            }
            int slotX = centerX + offset - slotSize / 2;
            int iconOffset = (slotSize - ICON_SIZE) / 2;

            int slotY = baseY - slotSize / 2;
            int iconX = slotX + iconOffset;
            int iconY = slotY + iconOffset;
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            gui.setColor(1f, 1f, 1f, 0.75F);
            gui.fill(iconX, iconY, iconX + ICON_SIZE, iconY + ICON_SIZE, ICON_BG);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            gui.setColor(1, 1, 1, alpha);
            gui.blit(tex, slotX, slotY, 0, 0, slotSize, slotSize, slotSize, slotSize);
            gui.blit(entry.spell().getIcon(), iconX, iconY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            gui.setColor(1, 1, 1, 0.5F);

            if (SpellCooldownTracker.isOnCooldown(player, entry.spell())) {
            float progress = SpellCooldownTracker.getRemainingCooldownPercentage(player, entry.spell()); // 0 = just started, 1 = finished
            int filledHeight = Math.round(ICON_SIZE * (1.0f - progress));  // invert: full at 0, empty at 1
            int fillY = iconY + (ICON_SIZE - filledHeight);
            gui.fill(iconX, fillY, iconX + ICON_SIZE, iconY + ICON_SIZE, WHITE);  // or use a dark texture/tint
            } else if (isSelected && SpellCastingHandler.currentlyChannellingSpell(player, tome)) {
                float progress = SpellCastingHandler.getChannellingProgress(player, tome);
                int filledHeight = Math.round(ICON_SIZE * (progress));
                int fillY = iconY + (ICON_SIZE - filledHeight);
                gui.fill(iconX, fillY, iconX + ICON_SIZE, iconY + ICON_SIZE, WHITE);  // or use a dark texture/tint
            }


        }

        gui.setColor(1f, 1f, 1f, 1f); // reset color
        RenderSystem.disableBlend();
    }


}
