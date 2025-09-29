package net.goo.brutality.network;

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
public class ServerboundPlayerAnimationPacket {
    UUID playerId;
    ResourceLocation animation;
    boolean mirrored;
    float speed;

    public ServerboundPlayerAnimationPacket(UUID playerId, ResourceLocation animation, boolean mirrored,
                                            float speed) {
        this.playerId = playerId;
        this.animation = animation;
        this.mirrored = mirrored;
        this.speed = speed;
    }

    public ServerboundPlayerAnimationPacket(FriendlyByteBuf buf) {
        this.playerId = buf.readUUID();
        this.animation = buf.readResourceLocation();
        this.mirrored = buf.readBoolean();
        this.speed = buf.readFloat();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.playerId);
        buf.writeResourceLocation(this.animation);
        buf.writeBoolean(this.mirrored);
        buf.writeFloat(this.speed);
    }

    public static void handle(ServerboundPlayerAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            ServerLevel level = sender.serverLevel();
            for (ServerPlayer player : level.players()) {
                if (player != ctx.get().getSender()) {
                    PacketHandler.sendToPlayer(new ClientboundPlayerAnimationPacket(packet.playerId, packet.animation, packet.mirrored, packet.speed), player);
                }

            }
        });
        ctx.get().setPacketHandled(true);
    }
}