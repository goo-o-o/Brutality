package net.goo.brutality.client.gui.misc_elements;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.gui.screen.NameLoadoutScreen;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.loadouts.Loadout;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundSwitchLoadoutPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.client.gui.CuriosScreenV2;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LoadoutGuiHandler {
    @SubscribeEvent
    public static void onScreenInit(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof CuriosScreenV2 || event.getScreen() instanceof InventoryScreen) {
            AbstractContainerScreen screen = (AbstractContainerScreen) event.getScreen();
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) return;

            int left = screen.getGuiLeft();
            int top = screen.getGuiTop();

            player.getCapability(BrutalityCapabilities.LOADOUTS).ifPresent(cap -> {
                List<Loadout> stack = cap.getStoredLoadouts();

                // 1. Render existing Loadout Buttons
                for (int i = 0; i < stack.size(); i++) {
                    final int index = i;
                    boolean selected = (cap.getActiveLoadout() == index);

                    ImageButton button = new ImageButton(
                            left + (i * 18), top - 18, 16, 16, 0, 0, 16,
                            stack.get(i).icon(), 16, 48, (btn) ->
                            PacketHandler.sendToServer(new ServerboundSwitchLoadoutPacket(index)),
                            Component.literal(stack.get(index).name()
                            ));
                    button.active = !selected;
                    button.setTooltip(Tooltip.create(Component.literal(stack.get(index).name())));
                    event.addListener(button);
                }

                ResourceLocation addTexture = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/loadout/add_button.png");

                ImageButton addButton = new ImageButton(
                        left + (stack.size() * 18), top - 18, 17, 17,
                        0, 0, 16,
                        addTexture, 18, 48,
                        (btn) -> Minecraft.getInstance().setScreen(new NameLoadoutScreen(screen))
                );

                addButton.setTooltip(Tooltip.create(Component.literal("Create New Loadout")));
                event.addListener(addButton);
            });
        }
    }
}