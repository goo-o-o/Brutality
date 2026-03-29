package net.goo.brutality.common.network.clientbound;

import net.goo.brutality.common.network.IBrutalityPacket;
import net.goo.brutality.util.lightning.ChainLightningHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ClientboundChainLightningPacket implements IBrutalityPacket<ClientboundChainLightningPacket> {
    public Vector3f start;
    public Vector3f end;
    public float size;
    public int lifespan, iterations, delay;
    public ChainLightningHelper.LightningType lightningType;

    public ClientboundChainLightningPacket(Vector3f start, Vector3f end, float size, int lifespan, ChainLightningHelper.LightningType lightningType, int iterations, int delay) {
        this.start = start;
        this.end = end;
        this.size = size;
        this.lifespan = lifespan;
        this.lightningType = lightningType;
        this.iterations = iterations;
        this.delay = delay;
    }

    public ClientboundChainLightningPacket(FriendlyByteBuf buf) {
        this.start = buf.readVector3f();
        this.end = buf.readVector3f();
        this.size = buf.readFloat();
        this.lifespan = buf.readInt();
        this.lightningType = buf.readEnum(ChainLightningHelper.LightningType.class);
        this.iterations = buf.readInt();
        this.delay = buf.readInt();
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVector3f(this.start);
        buf.writeVector3f(this.end);
        buf.writeFloat(this.size);
        buf.writeInt(this.lifespan);
        buf.writeEnum(this.lightningType);
        buf.writeInt(this.iterations);
        buf.writeInt(this.delay);
    }

    public void handle(ClientboundChainLightningPacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();

        context.enqueueWork(() ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ChainLightningHelper.Client.handlePacket(
                packet.iterations,
                packet.delay,
                packet.lightningType,
                packet.start,
                packet.end,
                packet.size,
                packet.lifespan
        )));

        context.setPacketHandled(true);
    }

}