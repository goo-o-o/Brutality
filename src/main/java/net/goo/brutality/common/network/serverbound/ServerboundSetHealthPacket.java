package net.goo.brutality.common.network.serverbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundSetHealthPacket {
    private final int entityId;
    private final float health;

    public ServerboundSetHealthPacket(int entityId, float health) {
        this.entityId = entityId;
        this.health = health;
    }

    public ServerboundSetHealthPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.health = buf.readFloat();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeFloat(health);
    }

    public static void handle(ServerboundSetHealthPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            ServerLevel level = sender.serverLevel();
            Entity target = level.getEntity(packet.entityId);

            if (target instanceof LivingEntity livingTarget && target.isAlive()) {
                livingTarget.setHealth(packet.health);
            }

        });
        context.setPacketHandled(true);
    }
}