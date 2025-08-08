package net.goo.brutality.network;

import net.goo.brutality.item.weapon.tome.BaseMagicTome;
import net.goo.brutality.magic.SpellStorage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sChangeSpellPacket {
    private final int direction;
    private final int index;

    public c2sChangeSpellPacket(int direction, int index) {
        this.direction = direction;
        this.index = index;
    }

    public c2sChangeSpellPacket(FriendlyByteBuf buf) {
        this.direction = buf.readInt();
        this.index = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(direction);
        buf.writeInt(index);
    }

    public static void handle(c2sChangeSpellPacket packet, Supplier<NetworkEvent.Context> ctx) {
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