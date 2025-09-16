package net.goo.brutality.network;

import net.goo.brutality.event.LivingDodgeEvent;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundDodgePacket {
    private final int entityId;
    private final ResourceLocation damageTypeId;
    private final float amount;
    private final Integer directEntityId, causingEntityId;
    private final boolean hasDirectEntity, hasCausingEntity;
    private final ItemStack anklet;

    public ClientboundDodgePacket(int entityId, DamageSource source, float amount, ItemStack anklet) {
        this.entityId = entityId;
        this.damageTypeId = source.typeHolder().unwrapKey().map(ResourceKey::location).orElse(null);
        this.hasDirectEntity = source.getDirectEntity() != null;
        this.directEntityId = source.getDirectEntity() != null ? source.getDirectEntity().getId() : null;
        this.hasCausingEntity = source.getEntity() != null;
        this.causingEntityId = source.getEntity() != null ? source.getEntity().getId() : null;
        this.amount = amount;
        this.anklet = anklet;
    }

    public ClientboundDodgePacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.damageTypeId = buf.readResourceLocation();
        this.amount = buf.readFloat();
        this.hasDirectEntity = buf.readBoolean();
        this.directEntityId = hasDirectEntity ? buf.readInt() : null;
        this.hasCausingEntity = buf.readBoolean();
        this.causingEntityId = hasCausingEntity ? buf.readInt() : null;
        this.anklet = buf.readItem();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeResourceLocation(damageTypeId);
        buf.writeFloat(amount);
        buf.writeBoolean(hasDirectEntity);
        if (hasDirectEntity) {
            buf.writeInt(directEntityId);
        }
        if (hasCausingEntity) {
            buf.writeInt(causingEntityId);
        }
        buf.writeItem(this.anklet);
    }

    public static void handle(ClientboundDodgePacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null) return;
            Holder<DamageType> damageType = level.registryAccess()
                    .registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, packet.damageTypeId));
            Entity directEntity = packet.hasDirectEntity && packet.directEntityId != null ? level.getEntity(packet.directEntityId) : null;
            Entity causingEntity = packet.hasCausingEntity && packet.causingEntityId != null ? level.getEntity(packet.causingEntityId) : null;
            DamageSource source = new DamageSource(damageType, directEntity, causingEntity);
            if (level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
                LivingDodgeEvent.Client client = new LivingDodgeEvent.Client(livingEntity, source, packet.amount);
                MinecraftForge.EVENT_BUS.post(client);
                if (packet.anklet.getItem() instanceof BrutalityAnkletItem ankletItem) {
                    ankletItem.onDodgeClient(livingEntity, source, packet.amount, packet.anklet);
                }
            }
        });
        context.setPacketHandled(true);
    }


}