package net.goo.brutality.network;

import net.goo.brutality.entity.custom.beam.ExcaliburBeam;
import net.goo.brutality.entity.custom.beam.TerraBeam;
import net.goo.brutality.registry.ModEntities;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.helpers.ProjectileHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sSwordBeamPacket {
    private final ModResources.BEAM_TYPES beamType; // Add the explosion type

    public c2sSwordBeamPacket(ModResources.BEAM_TYPES beamType) {
        this.beamType = beamType;
    }

    public c2sSwordBeamPacket(FriendlyByteBuf pBuffer) {
        this.beamType = pBuffer.readEnum(ModResources.BEAM_TYPES.class);
    }

    public void encode(FriendlyByteBuf pBuffer) {
        pBuffer.writeEnum(this.beamType);
    }


    public static void handle(c2sSwordBeamPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;

            Level level = sender.level();

            switch (packet.beamType) {
                case TERRA ->
                        ProjectileHelper.shootProjectile(() -> new TerraBeam(ModEntities.TERRA_BEAM.get(), level), sender, level, 3.5F);
                case EXCALIBUR ->
                        ProjectileHelper.shootProjectile(() -> new ExcaliburBeam(ModEntities.EXCALIBUR_BEAM.get(), level), sender, level, 0.25F);
            }

        });
        ctx.get().setPacketHandled(true);
    }
}