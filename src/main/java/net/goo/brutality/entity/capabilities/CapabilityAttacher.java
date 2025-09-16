package net.goo.brutality.entity.capabilities;

import net.goo.brutality.Brutality;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class CapabilityAttacher {
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity entity) {
            BrutalityCapabilities.CapabilitySyncRegistry.getAll().forEach((string, capability) -> {
                if (!entity.getCapability(capability).isPresent()) {
                    ICapabilityProvider provider = BrutalityCapabilities.CapabilitySyncRegistry.getProvider(capability);
                    event.addCapability(
                            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, string),
                            provider);
                }
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player oldPlayer = event.getOriginal();
            oldPlayer.reviveCaps();
            Player newPlayer = event.getEntity();
            BrutalityCapabilities.CapabilitySyncRegistry.getAll().forEach(((string, capability) -> {
                oldPlayer.getCapability(capability).ifPresent(oldCap -> {
                    newPlayer.getCapability(capability).ifPresent(newCap ->{
                        CompoundTag tag = oldCap.serializeNBT();
                        newCap.deserializeNBT(tag);
                    });
                });
            }));

            if (newPlayer.level() instanceof ServerLevel) {
                PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(oldPlayer.getId(), oldPlayer));
            }


            oldPlayer.invalidateCaps();


        }

    }
}