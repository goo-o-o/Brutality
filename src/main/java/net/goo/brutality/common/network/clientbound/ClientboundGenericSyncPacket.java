package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.ClientAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundGenericSyncPacket {
    private final int entityId; // New field
    private final String key;
    private final CompoundTag data;

    public ClientboundGenericSyncPacket(int entityId, String key, CompoundTag data) {
        this.entityId = entityId;
        this.key = key;
        this.data = data;
    }

    public ClientboundGenericSyncPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.key = buf.readUtf();
        this.data = buf.readNbt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeUtf(key);
        buf.writeNbt(data);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientAccess.handleSync(this.entityId, this.key, this.data));
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getEntity(entityId) != null) {
            System.out.println("entity: " + Minecraft.getInstance().level.getEntity(entityId).getName() + " | key: " + key + " | data: " + data);
        }
        ctx.get().setPacketHandled(true);
    }
}