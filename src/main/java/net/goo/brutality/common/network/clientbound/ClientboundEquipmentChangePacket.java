package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.client.ClientPacketListener;
import net.goo.brutality.common.network.IBrutalityPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

// instead of mixin into net.minecraft.network.protocol.game.ClientGamePacketListener.handleSetEquipment lets just make our own
public class ClientboundEquipmentChangePacket implements IBrutalityPacket {

    public ItemStack itemStack;
    public int entityId;
    public boolean isUnequip;

    public ClientboundEquipmentChangePacket(LivingEntity entity, ItemStack itemStack, boolean isUnequip) {
        this.itemStack = itemStack;
        this.entityId = entity.getId();
        this.isUnequip = isUnequip;
    }

    public ClientboundEquipmentChangePacket(FriendlyByteBuf buf) {
        this.itemStack = buf.readItem();
        this.entityId = buf.readInt();
        this.isUnequip = buf.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeItem(itemStack);
        buf.writeInt(entityId);
        buf.writeBoolean(isUnequip);
    }

    @Override
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> ClientPacketListener.handleEquipmentChange(this));
        ctx.get().setPacketHandled(true);
    }

}