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

public class c2sSwordBeamPacket {
    private final String identifier;

    public c2sSwordBeamPacket(String identifier) {
        this.identifier = identifier;
    }

    public c2sSwordBeamPacket(FriendlyByteBuf buf) {
        this.identifier = buf.readUtf();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(identifier);
    }

    public static void handle(c2sSwordBeamPacket packet, Supplier<NetworkEvent.Context> ctx) {
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

                    float velocity, damage, renderScale;
                    int lifespan, pierceCap;
                    boolean randomizeRoll;

                    switch (packet.identifier) {
                        case "terra_beam" -> {
                            lifespan = 60;
                            randomizeRoll = true;
                            pierceCap = 3;
                            damage = 7.5F;
                            renderScale = 10F;
                            velocity = 3.5F;
                        }
                        default -> {
                            lifespan = 0;
                            randomizeRoll = false;
                            pierceCap = 1;
                            damage = 1F;
                            renderScale = 1F;
                            velocity = 2.5F;
                        }
                    }

                    SwordBeam swordBeam = new SwordBeam(ModEntities.SWORD_BEAM.get(), level, packet.identifier, lifespan, true, pierceCap, damage, 4, 8, renderScale);
                    swordBeam.setOwner(sender);
                    swordBeam.setPos(spawnX, spawnY - 0.25D, spawnZ);
                    swordBeam.shootFromRotation(sender, sender.getXRot(), sender.getYRot(), 0.0F, velocity, 0.0F);

                    level.addFreshEntity(swordBeam);
                }
            }
        });

        ctx.get().setPacketHandled(true);

    }

}
