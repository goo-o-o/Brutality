package net.goo.armament.network;

import net.goo.armament.registry.ModEntities;
import net.goo.armament.entity.custom.TerraBeam;
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

                    TerraBeam terraBeamEntity = new TerraBeam(ModEntities.TERRA_BEAM_ENTITY.get(), level);
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
