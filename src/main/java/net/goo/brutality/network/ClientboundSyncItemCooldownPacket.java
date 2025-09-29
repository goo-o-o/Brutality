package net.goo.brutality.network;

import net.goo.brutality.client.ClientAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ClientboundSyncItemCooldownPacket {
    private final Map<Item, ItemCooldowns.CooldownInstance> cooldowns;
    private final int tickCount;

    public ClientboundSyncItemCooldownPacket(Map<Item, ItemCooldowns.CooldownInstance> cooldowns, int tickCount) {
        this.cooldowns = new HashMap<>(cooldowns);
        this.tickCount = tickCount;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(cooldowns.size());
        buf.writeVarInt(tickCount);
        for (Map.Entry<Item, ItemCooldowns.CooldownInstance> entry : cooldowns.entrySet()) {
            buf.writeItem(entry.getKey().getDefaultInstance());
            buf.writeVarInt(entry.getValue().startTime);
            buf.writeVarInt(entry.getValue().endTime);
        }
    }

    public static ClientboundSyncItemCooldownPacket decode(FriendlyByteBuf buf) {
        Map<Item, ItemCooldowns.CooldownInstance> cooldowns = new HashMap<>();
        int size = buf.readVarInt();
        int tickCount = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            ItemStack stack = buf.readItem();
            Item item = stack.getItem();
            int startTime = buf.readVarInt();
            int endTime = buf.readVarInt();
            cooldowns.put(item, new ItemCooldowns.CooldownInstance(startTime, endTime));
        }
        return new ClientboundSyncItemCooldownPacket(cooldowns, tickCount);
    }


    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientAccess.syncItemCooldowns(cooldowns, tickCount));
        ctx.get().setPacketHandled(true);
    }
}