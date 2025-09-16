package net.goo.brutality.network;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundDamageEntityPacket {
    private final int entityId;
    private final float damage;
    private final ResourceKey<DamageType> damageTypeKey;

    public ServerboundDamageEntityPacket(int entityId, float damage, DamageSource damageSource) {
        this.entityId = entityId;
        this.damage = damage;
        this.damageTypeKey = damageSource.typeHolder().unwrapKey()
                .orElseThrow(() -> new IllegalArgumentException("Unregistered damage type"));
    }

    public ServerboundDamageEntityPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readVarInt();
        this.damage = buf.readFloat();
        this.damageTypeKey = ResourceKey.create(
                Registries.DAMAGE_TYPE,
                buf.readResourceLocation()
        );
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeFloat(damage);
        buf.writeResourceLocation(damageTypeKey.location());
    }

    public DamageSource createDamageSource(RegistryAccess registryAccess) {
        return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(damageTypeKey));
    }

    public static void handle(ServerboundDamageEntityPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

            ServerLevel level = sender.serverLevel();
            Entity target = level.getEntity(packet.entityId);

            if (target == null || !target.isAlive()) return;
            DamageSource source = packet.createDamageSource(level.registryAccess());
            target.hurt(source, packet.damage);
        });
        context.setPacketHandled(true);
    }
}