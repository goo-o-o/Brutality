package net.goo.brutality.network;

import net.goo.brutality.client.ClientAccess;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class s2cSyncCapabilitiesPacket {
    private final int entityId;
    private final Map<String, CompoundTag> data;

    public s2cSyncCapabilitiesPacket(int entityId, Entity entity) {
        this.entityId = entityId;
        this.data = new HashMap<>();

        for (Map.Entry<String, Capability<? extends INBTSerializable<CompoundTag>>> entry : BrutalityCapabilities.CapabilitySyncRegistry.getAll().entrySet()) {
            String key = entry.getKey();
            Capability<?> cap = entry.getValue();

            entity.getCapability(cap).ifPresent(inst -> {
                CompoundTag tag = ((INBTSerializable<CompoundTag>) inst).serializeNBT();
                data.put(key, tag);
            });
        }
    }


    public s2cSyncCapabilitiesPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        int count = buf.readInt();
        this.data = new HashMap<>();

        for (int i = 0; i < count; i++) {
            String key = buf.readUtf();
            CompoundTag tag = buf.readNbt();
            data.put(key, tag);
        }
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(data.size());
        data.forEach((key, tag) -> {
                    buf.writeUtf(key);
                    buf.writeNbt(tag);

                }

        );

    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                ClientAccess.syncCapabilities(entityId, data);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}