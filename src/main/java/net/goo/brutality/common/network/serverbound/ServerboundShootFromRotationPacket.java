package net.goo.brutality.common.network.serverbound;

import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.item.SealUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ServerboundShootFromRotationPacket {
    private final ResourceLocation entityTypeId;
    private final float x;
    float y;
    float z;
    private final float vel, xRot, yRot;
    float inaccuracy;
    private final ItemStack stack;

    public ServerboundShootFromRotationPacket(ItemStack stack, EntityType<? extends Projectile> entityType, Vec3 pos, float xRot, float yRot, float vel, float inaccuracy) {
        this.stack = stack;
        this.entityTypeId = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        this.x = (float) pos.x();
        this.y = (float) pos.y();
        this.z = (float) pos.z();
        this.xRot = xRot;
        this.yRot = yRot;
        this.vel = vel;
        this.inaccuracy = inaccuracy;
    }

    public ServerboundShootFromRotationPacket(FriendlyByteBuf buf) {
        this.stack = buf.readItem();
        this.entityTypeId = buf.readResourceLocation();
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
        this.xRot = buf.readFloat();
        this.yRot = buf.readFloat();
        this.vel = buf.readFloat();
        this.inaccuracy = buf.readFloat();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeItem(this.stack);
        buf.writeResourceLocation(this.entityTypeId);
        buf.writeFloat(this.x);
        buf.writeFloat(this.y);
        buf.writeFloat(this.z);
        buf.writeFloat(this.xRot);
        buf.writeFloat(this.yRot);
        buf.writeFloat(this.vel);
        buf.writeFloat(this.inaccuracy);
    }

    public static void handle(ServerboundShootFromRotationPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            ServerLevel level = player.serverLevel();
            DelayedTaskScheduler.queueServerWork(level, 6, () -> {

            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(packet.entityTypeId);
                if (entityType != null) {
                    Entity entity = entityType.create(level);
                    if (entity instanceof Projectile projectile) {
                        projectile.setPos(packet.x, packet.y, packet.z);
                        projectile.shootFromRotation(player, packet.xRot, packet.yRot, 0, packet.vel, packet.inaccuracy);
                        projectile.setOwner(player);

                        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(packet.stack);
                        if (sealType != null) {
                            projectile.getCapability(BrutalityCapabilities.SEAL_TYPE).ifPresent(cap -> cap.setSealType(sealType));

                        }

                        level.addFreshEntity(projectile);
                    }
                }
            });
        });
        ctx.get().setPacketHandled(true);
    }

}