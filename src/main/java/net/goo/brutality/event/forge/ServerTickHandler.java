package net.goo.brutality.event.forge;

import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ClientboundSyncTickPacket;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ServerTickHandler {
    private static long serverTick = 0;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        serverTick++;
        if (serverTick % 20 == 0) {
            PacketHandler.sendToAllClients(new ClientboundSyncTickPacket(serverTick));
        }
    }

    public static long getServerTick() {
        return serverTick;
    }


}
