package net.goo.brutality.network;

import net.goo.brutality.util.helpers.ProjectileHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sShootProjectilePacket {
    private final ResourceLocation projectileId;
    private final float vel;
    private final float delta;
    private final boolean random;
    private final int angleOffset;

    public c2sShootProjectilePacket(ResourceLocation projectileId, float vel, boolean random, float delta, int angleOffset) {
        this.projectileId = projectileId;
        this.vel = vel;
        this.random = random;
        this.delta = delta;
        this.angleOffset = angleOffset;
    }

    public c2sShootProjectilePacket(FriendlyByteBuf buf) {
        this.projectileId = buf.readResourceLocation();
        this.vel = buf.readFloat();
        this.random = buf.readBoolean();
        this.delta = buf.readFloat();
        this.angleOffset = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.projectileId);
        buf.writeFloat(this.vel);
        buf.writeBoolean(this.random);
        buf.writeFloat(this.delta);
        buf.writeInt(this.angleOffset);
    }

    public static void handle(c2sShootProjectilePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Level level = sender.level();
            var entityType = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.get(packet.projectileId);
            ProjectileHelper.shootProjectile(() -> (net.minecraft.world.entity.projectile.Projectile) entityType.create(level), sender, level, packet.vel, packet.random, packet.delta, packet.angleOffset);
        });
        ctx.get().setPacketHandled(true);
    }

}