package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.player_animation.AnimationHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundPlayerAnimationPacket {
    UUID playerId;
    ResourceLocation animation;
    boolean mirrored;
    float speed;

    public ClientboundPlayerAnimationPacket(UUID playerId, ResourceLocation animation, boolean mirrored,
                                            float speed) {
        this.playerId = playerId;
        this.animation = animation;
        this.mirrored = mirrored;
        this.speed = speed;
    }

    public ClientboundPlayerAnimationPacket(FriendlyByteBuf buf) {
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


    public static void handle(ClientboundPlayerAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> AnimationHelper.playAnimation(packet.playerId, packet.animation, packet.mirrored, packet.speed));
        ctx.get().setPacketHandled(true);
    }
}