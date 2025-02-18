package net.goo.armament.network;

import net.goo.armament.entity.base.SwordBeam;
import net.goo.armament.registry.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class c2sTerraBeamPacket {

    public c2sTerraBeamPacket() {
    }

    public c2sTerraBeamPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public static void handle(c2sTerraBeamPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().level != null) {
                Player sender = ctx.get().getSender();
                Level level = sender.level();

                if (sender != null) {
                    Vec3 senderPos = sender.position();
                    Vec3 viewVector = sender.getViewVector(1.0F);

                    double spawnX = senderPos.x + viewVector.x;
                    double spawnY = senderPos.y + sender.getEyeHeight() + viewVector.y;
                    double spawnZ = senderPos.z + viewVector.z;

                    SwordBeam terraBeamEntity = new SwordBeam(ModEntities.TERRA_BEAM.get(), level, "terra_beam", 60, true, 3, 7.5f, 2.5F);
                    terraBeamEntity.setOwner(sender);
                    terraBeamEntity.setPos(spawnX, spawnY - 0.25D, spawnZ);
                    terraBeamEntity.shootFromRotation(sender, sender.getXRot(), sender.getYRot(), 0.0F, 3.5F, 0.0F);

                    level.addFreshEntity(terraBeamEntity);
                }
            }
        });

        ctx.get().setPacketHandled(true);

    }

}
