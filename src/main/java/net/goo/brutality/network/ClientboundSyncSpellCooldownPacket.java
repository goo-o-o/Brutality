package net.goo.brutality.network;

import net.goo.brutality.magic.SpellCooldownTracker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundSyncSpellCooldownPacket {
    private final String spellId;
    private final int remaining;
    private final int original;
    private final float progress;

    public ClientboundSyncSpellCooldownPacket(String spellId, int remaining, int original, float progress) {
        this.spellId = spellId;
        this.remaining = remaining;
        this.original = original;
        this.progress = progress;
    }

    public ClientboundSyncSpellCooldownPacket(FriendlyByteBuf buf) {
        this.spellId = buf.readUtf();
        this.remaining = buf.readInt();
        this.original = buf.readInt();
        this.progress = buf.readFloat();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(spellId);
        buf.writeInt(remaining);
        buf.writeInt(original);
        buf.writeFloat(progress);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            SpellCooldownTracker.updateCooldownClient(spellId, remaining, original, progress);
        });
        ctx.get().setPacketHandled(true);
    }
}