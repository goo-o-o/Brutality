package net.goo.brutality.network;

import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.magic.SpellStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerboundChangeSpellPacket {
    private final int direction;
    private final int index;

    public ServerboundChangeSpellPacket(int direction, int index) {
        this.direction = direction;
        this.index = index;
    }

    public ServerboundChangeSpellPacket(FriendlyByteBuf buf) {
        this.direction = buf.readInt();
        this.index = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(direction);
        buf.writeInt(index);
    }

    public static void handle(ServerboundChangeSpellPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = player.getInventory().getItem(packet.index);
                if (stack.getItem() instanceof BaseMagicTome) {
                    SpellStorage.cycleSelectedSpell(stack, packet.direction);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}