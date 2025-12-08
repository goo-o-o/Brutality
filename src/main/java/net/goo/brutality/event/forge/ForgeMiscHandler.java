package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.NbtHelper;
import net.minecraft.server.TickTask;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeMiscHandler {

    @SubscribeEvent
    public static void onItemBreak(PlayerDestroyItemEvent event) {
        boolean fromDoubleDown = NbtHelper.getBool(event.getOriginal(), "fromDoubleDown", false);
        if (fromDoubleDown) {
            Player player = event.getEntity();
            if (!player.level().isClientSide()) {
                player.level().getServer().tell(new TickTask(1, () -> player.getInventory().setItem(player.getInventory().selected, BrutalityModItems.DOUBLE_DOWN.get().getDefaultInstance())));
            }
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        boolean fromDoubleDown = NbtHelper.getBool(event.getEntity().getItem(), "fromDoubleDown", false);
        if (fromDoubleDown) {
            event.getEntity().setItem(BrutalityModItems.DOUBLE_DOWN.get().getDefaultInstance());
        }
    }
}