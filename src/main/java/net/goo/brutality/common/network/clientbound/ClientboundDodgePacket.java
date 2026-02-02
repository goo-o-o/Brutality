package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.ClientAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundDodgePacket {
    public final int entityId;
    public final ResourceLocation damageTypeId;
    public final float amount;
    public final Integer directEntityId;
    public final Integer causingEntityId;
    public final boolean hasDirectEntity;
    public final boolean hasCausingEntity;
    public final ItemStack anklet;

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
        this.hasDirectEntity = buf.readBoolean();
        this.directEntityId = hasDirectEntity ? buf.readInt() : null;
        this.hasCausingEntity = buf.readBoolean();
        this.causingEntityId = hasCausingEntity ? buf.readInt() : null;
        this.amount = buf.readFloat();
        this.anklet = buf.readItem();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeResourceLocation(damageTypeId);
        buf.writeBoolean(hasDirectEntity);
        if (hasDirectEntity) {
            buf.writeInt(directEntityId);
        }
        if (hasCausingEntity) {
            buf.writeInt(causingEntityId);
        }
        buf.writeFloat(amount);
        buf.writeItem(this.anklet);
    }

    public static void handle(ClientboundDodgePacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> ClientAccess.handleDodgeClient(packet));
        context.setPacketHandled(true);
    }


}