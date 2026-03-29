package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientboundStartPlayerAnimationPacket implements IBrutalityPacket<ClientboundStartPlayerAnimationPacket> {
    UUID playerId;
    ResourceLocation animation;
    boolean mirrored;
    float speed;
    int fadeTicks;

    public ClientboundStartPlayerAnimationPacket(UUID playerId, ResourceLocation animation, boolean mirrored,
                                                 float speed, int fadeTicks) {
        this.playerId = playerId;
        this.animation = animation;
        this.mirrored = mirrored;
        this.speed = speed;
        this.fadeTicks = fadeTicks;
    }
    public ClientboundStartPlayerAnimationPacket(UUID playerId, ResourceLocation animation, boolean mirrored,
                                                 float speed) {
        this(playerId, animation, mirrored, speed, 2);
    }

    public ClientboundStartPlayerAnimationPacket(FriendlyByteBuf buf) {
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


    public void handle(ClientboundStartPlayerAnimationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> AnimationHelper.playAnimation(packet.playerId, packet.animation, packet.mirrored, packet.speed, packet.fadeTicks));
        ctx.get().setPacketHandled(true);
    }
}