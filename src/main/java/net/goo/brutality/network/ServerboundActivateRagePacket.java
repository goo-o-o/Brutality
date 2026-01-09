package net.goo.brutality.network;

import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.network.NetworkEvent;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Supplier;

public class ServerboundActivateRagePacket {
    public ServerboundActivateRagePacket() {
    }

    public ServerboundActivateRagePacket(FriendlyByteBuf buf) {
    }

    public void write(FriendlyByteBuf buf) {
    }

    public static void handle(ServerboundActivateRagePacket packet, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
//            System.out.println("packet received");
            ServerPlayer sender = context.getSender();
            if (sender == null) return;

//            sender.sendSystemMessage(Component.literal("player not null"));

            CuriosApi.getCuriosInventory(sender).ifPresent(
                    handler -> sender.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).ifPresent(cap -> {
                        int maxRage = (int) sender.getAttributeValue(BrutalityModAttributes.MAX_RAGE.get());

                        if (cap.rageValue() >= maxRage) {
                            // Don't trigger if wearing Anger Management
                            int duration = 40;
                            int rageLevel = (int) Math.floor(cap.rageValue() / 100);
                            AttributeInstance rageTimeAttr = sender.getAttribute(BrutalityModAttributes.RAGE_TIME.get());
                            if (rageTimeAttr != null) {
                                duration = (int) (duration * rageTimeAttr.getValue());
                            }
                            AttributeInstance rageLevelAttr = sender.getAttribute(BrutalityModAttributes.RAGE_LEVEL.get());
                            if (rageLevelAttr != null) {
                                rageLevel += (int) rageLevelAttr.getValue();
                            }

                            sender.addEffect(new MobEffectInstance(BrutalityModMobEffects.ENRAGED.get(), duration, rageLevel, false, true));
                            cap.setRageValue(0);

                            sender.level().playSound(null, sender.getX(), sender.getY(), sender.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.PLAYERS, 1F, 1F);
                        }

                        cap.setRageValue(Math.min(cap.rageValue(), maxRage));
                        PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(sender.getId(), sender));
                    }));
        });
        context.setPacketHandled(true);
    }
}