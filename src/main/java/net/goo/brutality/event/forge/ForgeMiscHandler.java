package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeMiscHandler {

    @SubscribeEvent
    public static void onItemBreak(PlayerDestroyItemEvent event) {
        if (event.getOriginal().getOrCreateTag().getBoolean("fromDoubleDown")) {
            Player player = event.getEntity();
            if (player.level() instanceof ServerLevel serverLevel) {
                serverLevel.getServer().executeIfPossible(new TickTask(1, () ->
                        player.getInventory().setItem(player.getInventory().selected, BrutalityModItems.DOUBLE_DOWN.get().getDefaultInstance())));
            }
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        if (event.getEntity().getItem().getOrCreateTag().getBoolean("fromDoubleDown")) {
            event.getEntity().setItem(BrutalityModItems.DOUBLE_DOWN.get().getDefaultInstance());
        }
    }


}
