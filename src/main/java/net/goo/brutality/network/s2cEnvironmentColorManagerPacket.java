package net.goo.brutality.network;

import net.goo.brutality.util.helpers.EnvironmentColorManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class s2cEnvironmentColorManagerPacket {
    private final EnvironmentColorManager.ColorType type;
    private final int r, g, b;
    private final boolean isReset;

    public s2cEnvironmentColorManagerPacket(EnvironmentColorManager.ColorType type, int r, int g, int b, boolean isReset) {
        this.type = type;
        this.r = r;
        this.g = g;
        this.b = b;
        this.isReset = isReset;
    }

    public s2cEnvironmentColorManagerPacket(FriendlyByteBuf buf) {
        this.type = buf.readEnum(EnvironmentColorManager.ColorType.class);
        this.r = buf.readInt();
        this.g = buf.readInt();
        this.b = buf.readInt();
        this.isReset = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(type);
        buf.writeInt(r);
        buf.writeInt(g);
        buf.writeInt(b);
        buf.writeBoolean(isReset);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (isReset)
                EnvironmentColorManager.resetAllColors();
            else
                EnvironmentColorManager.setColor(type, r, g, b);
        });
        context.get().setPacketHandled(true);
    }
}
