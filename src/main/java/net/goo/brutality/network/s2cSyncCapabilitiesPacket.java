package net.goo.brutality.network;

import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class s2cSyncEntityEffectsPacket {
    private final int entityId;
    private final CompoundTag effectsData;

    public s2cSyncEntityEffectsPacket(int entityId, EntityCapabilities.EntityEffectCap effects) {
        this.entityId = entityId;
        this.effectsData = effects.serializeNBT();
//        System.out.println("Creating packet for entity " + entityId + " with data: " + effectsData); // Debug

    }

    public s2cSyncEntityEffectsPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.effectsData = buf.readNbt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeNbt(effectsData);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().level;
            if (level != null) {
                Entity entity = level.getEntity(entityId);
                if (entity != null) {
//                    System.out.println("Received packet for entity " + entityId + " with data: " + effectsData); // Debug
                    entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP)
                            .ifPresent(cap -> cap.deserializeNBT(effectsData));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}