package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.network.IBrutalityPacket;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundStartPlayerAnimationPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * Triggers an animation from client-side to be synchronized across other clients.
 */
public class ServerboundStartPlayerAnimationPacket implements IBrutalityPacket<ServerboundStartPlayerAnimationPacket> {
    UUID playerId;
    ResourceLocation animation;
    boolean mirrored;
    float speed;
    int fadeTicks;

    public ServerboundStartPlayerAnimationPacket(UUID playerId, ResourceLocation animation, boolean mirrored,
                                                 float speed, int fadeTicks) {
        this.playerId = playerId;
        this.animation = animation;
        this.mirrored = mirrored;
        this.speed = speed;
        this.fadeTicks = fadeTicks;
    }

    public ServerboundStartPlayerAnimationPacket(UUID playerId, ResourceLocation animation, boolean mirrored,
                                                 float speed) {
        this(playerId, animation, mirrored, speed, 2);
    }

    public ServerboundStartPlayerAnimationPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.animation = buf.readResourceLocation();
        this.mirrored = buf.readBoolean();
        this.speed = buf.readFloat();
        this.fadeTicks = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeResourceLocation(this.animation);
        buf.writeBoolean(this.mirrored);
        buf.writeFloat(this.speed);
        buf.writeInt(this.fadeTicks);
    }

    public void handle(ServerboundStartPlayerAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            ServerLevel level = sender.serverLevel();
            for (ServerPlayer player : level.players()) {
                if (player != ctx.get().getSender()) {
                    PacketHandler.sendToPlayerClient(new ClientboundStartPlayerAnimationPacket(packet.playerId, packet.animation, packet.mirrored, packet.speed, packet.fadeTicks), player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}