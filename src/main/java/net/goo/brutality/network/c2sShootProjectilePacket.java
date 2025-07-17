package net.goo.brutality.network;

import net.goo.brutality.util.helpers.ProjectileHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sSwordBeamPacket {
    private final ResourceLocation projectileId;
    private final float vel;
    private final Float delta;
    private final Boolean random;

    public c2sSwordBeamPacket(ResourceLocation projectileId, float vel, Boolean random, Float delta) {
        this.projectileId = projectileId;
        this.vel = vel;
        this.random = random;
        this.delta = delta;
    }

    public c2sSwordBeamPacket(FriendlyByteBuf buf) {
        this.projectileId = buf.readResourceLocation();
        this.vel = buf.readFloat();
        this.random = buf.readBoolean();
        this.delta = buf.readFloat();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.projectileId);
        buf.writeFloat(this.vel);
    }

    public static void handle(c2sSwordBeamPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Level level = sender.level();
            var entityType = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(packet.projectileId);
            ProjectileHelper.shootProjectile(() -> (net.minecraft.world.entity.projectile.Projectile) entityType.create(level), sender, level, packet.vel, packet.random, packet.delta);
        });
        ctx.get().setPacketHandled(true);
    }

}