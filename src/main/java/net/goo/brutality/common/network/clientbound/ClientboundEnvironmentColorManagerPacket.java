package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.util.EnvironmentColorManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.FastColor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientboundEnvironmentColorManagerPacket {
    private final EnvironmentColorManager.ColorType type;
    private final int r, g, b;
    private final boolean isReset;

    public ClientboundEnvironmentColorManagerPacket(EnvironmentColorManager.ColorType type, int r, int g, int b, boolean isReset) {
        this.type = type;
        this.r = r;
        this.g = g;
        this.b = b;
        this.isReset = isReset;
    }

    public ClientboundEnvironmentColorManagerPacket(FriendlyByteBuf buf) {
        this.type = buf.readEnum(EnvironmentColorManager.ColorType.class);
        this.r = buf.readInt();
        this.g = buf.readInt();
        this.b = buf.readInt();
        this.isReset = buf.readBoolean();
    }

    public void write(FriendlyByteBuf buf) {
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
                EnvironmentColorManager.setColor(type, FastColor.ARGB32.color(255, r, g, b));
        });
        context.get().setPacketHandled(true);
    }
}
