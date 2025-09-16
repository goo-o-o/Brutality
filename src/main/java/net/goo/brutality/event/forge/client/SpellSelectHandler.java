package net.goo.brutality.event.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.magic.SpellCastingHandler;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ServerboundChangeSpellPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT)
public class SpellSelectHandler {

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            boolean isShiftDown = InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue());
            int scrollDirection = (int) Math.signum(event.getScrollDelta());
            if (isShiftDown) {
                if (mc.player.getMainHandItem().getItem() instanceof BaseMagicTome) {
                    if (SpellCastingHandler.currentlyChannellingSpell(mc.player, mc.player.getMainHandItem())) return;
                    PacketHandler.sendToServer(new ServerboundChangeSpellPacket(scrollDirection, mc.player.getInventory().selected));
                    event.setCanceled(true);
                } else if (mc.player.getOffhandItem().getItem() instanceof BaseMagicTome) {
                    if (SpellCastingHandler.currentlyChannellingSpell(mc.player, mc.player.getOffhandItem())) return;
                    PacketHandler.sendToServer(new ServerboundChangeSpellPacket(scrollDirection, 40));
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(ScreenEvent.MouseScrolled event) {
        if (event.getScreen() instanceof AbstractContainerScreen<?> screen) {
            Slot hoveredSlot = screen.getSlotUnderMouse();
            int scrollDirection = (int) Math.signum(event.getScrollDelta());

            if (hoveredSlot != null && hoveredSlot.getItem().getItem() instanceof BaseMagicTome) {
                PacketHandler.sendToServer(new ServerboundChangeSpellPacket(scrollDirection, hoveredSlot.getSlotIndex()));
                event.setCanceled(true);
            }
        }
    }

}
