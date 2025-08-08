package net.goo.brutality.network;

import net.goo.brutality.magic.SpellCooldownTracker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class s2cSyncCooldownPacket {
    private final String spellId;
    private final int remaining;
    private final int original;

    public s2cSyncCooldownPacket(String spellId, int remaining, int original) {
        this.spellId = spellId;
        this.remaining = remaining;
        this.original = original;
    }

    public s2cSyncCooldownPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.remaining = buf.readInt();
        this.original = buf.readInt();
    }

    public static void encode(s2cSyncCooldownPacket msg, FriendlyByteBuf buf) {
        buf.writeUtf(msg.spellId);
        buf.writeInt(msg.remaining);
        buf.writeInt(msg.original);
    }


    public static void handle(s2cSyncCooldownPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SpellCooldownTracker.updateCooldownClient(msg.spellId, msg.remaining, msg.original);
        });
        ctx.get().setPacketHandled(true);
    }
}