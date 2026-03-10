package net.goo.brutality.client.gui.misc_elements;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.components.ItemButton;
import net.goo.brutality.client.gui.screen.NameLoadoutScreen;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.loadouts.Loadout;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundSwitchLoadoutPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class LoadoutGuiHandler {

    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof InventoryScreen screen) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            int left = screen.getGuiLeft();
            int top = screen.getGuiTop();

            player.getCapability(BrutalityCapabilities.LOADOUTS).ifPresent(cap -> {
                List<Loadout> stack = cap.getStoredLoadouts();

                for (int i = 0; i < stack.size(); i++) {
                    final int index = i;
                    boolean isActive = (cap.getActiveLoadout() == index);

                    ItemButton button = new ItemButton(
                            left + (i * 20), top - 22, 18, 18,
                            stack.get(i).icon().getDefaultInstance(), isActive,
                            (btn) -> PacketHandler.sendToServer(new ServerboundSwitchLoadoutPacket(index))
                    );

                    event.addListener(button);
                }


                // 2. The "+" Button
                // Position it right below the last loadout
                if (stack.size() < 6) {
                    event.addListener(Button.builder(Component.literal("+"), (btn) -> {
                                // Open the naming sub-screen
                                Minecraft.getInstance().setScreen(new NameLoadoutScreen(screen));
                            })
                            .pos(left + 176, top + (stack.size() * 22))
                            .size(20, 20)
                            .tooltip(Tooltip.create(Component.literal("Create New Loadout")))
                            .build());
                }
            });
        }
    }
}