package net.goo.brutality.event.forge.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber (value = Dist.CLIENT)
public class ClientTickHandler {
    private static long clientTick = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        clientTick++;
    }

    public static long getClientTick() {
        return clientTick;
    }

    public static void setClientTick(long tick) {
        clientTick = tick;
    }
}
