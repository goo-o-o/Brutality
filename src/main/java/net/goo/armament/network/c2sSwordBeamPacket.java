package net.goo.armament.network;

import net.goo.armament.client.entity.BEAM_TYPES;
import net.goo.armament.entity.base.SwordBeam;
import net.goo.armament.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sSwordBeamPacket {
    private final BEAM_TYPES identifier;
    private final float velocity;

    public c2sSwordBeamPacket(BEAM_TYPES identifier, float velocity) {
        this.identifier = identifier;
        this.velocity = velocity;
    }

    public c2sSwordBeamPacket(FriendlyByteBuf buf) {
        this.identifier = buf.readEnum(BEAM_TYPES.class);
        this.velocity = buf.readFloat();
    }


    public void encode(FriendlyByteBuf buf) {
        buf.writeEnum(identifier);
        buf.writeFloat(velocity);
    }

    public static void handle(c2sSwordBeamPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Player sender = ctx.get().getSender();
                Level level = sender.level();

                Vec3 senderPos = sender.position();
                Vec3 viewVector = sender.getViewVector(1.0F);

                double spawnX = senderPos.x + viewVector.x;
                double spawnY = senderPos.y + sender.getEyeHeight() + viewVector.y;
                double spawnZ = senderPos.z + viewVector.z;

                EntityType selectedBeam;

                switch (packet.identifier) {
                    case TERRA_BEAM -> selectedBeam = ModEntities.TERRA_BEAM.get();
                    default -> selectedBeam = null;
                }

                if (selectedBeam == null) return;
                SwordBeam swordBeam = new SwordBeam(selectedBeam, level);
                swordBeam.setOwner(sender);
                swordBeam.setPos(spawnX, spawnY - 0.25F, spawnZ);
                swordBeam.shootFromRotation(sender, sender.getXRot(), sender.getYRot(), 0.0F, packet.velocity, 0.0F);

                level.addFreshEntity(swordBeam);
            }
        });

        ctx.get().setPacketHandled(true);

    }

}
