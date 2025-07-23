package net.goo.brutality.entity.capabilities;

import net.goo.brutality.Brutality;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class CapabilityAttacher {
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            if (!event.getObject().getCapability(BrutalityCapabilities.ENTITY_STAR_COUNT_CAP).isPresent())
                event.addCapability(
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "entity_star_count"),
                        new EntityStarCountCapProvider()
                );
            if (!event.getObject().getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).isPresent())
                event.addCapability(
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "entity_effects"),
                        new EntityEffectsCapProvider()
                );
            if (!event.getObject().getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP).isPresent())
                event.addCapability(
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "entity_should_rotate"),
                        new EntityShouldRotateCapProvider()
                );
            if (!event.getObject().getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP).isPresent())
                event.addCapability(
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "rage_value"),
                        new PlayerRageCapProvider()
                );
            if (!event.getObject().getCapability(BrutalityCapabilities.PLAYER_MANA_CAP).isPresent())
                event.addCapability(
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "mana_value"),
                        new PlayerManaCapProvider()
                );
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player original  = event.getOriginal();

            original.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP)
                    .ifPresent(oldCap -> {
                        Player player = event.getEntity();

                        player.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP)
                                .ifPresent(newCap -> {
                                    // Copy all states
                                    CompoundTag tag = oldCap.serializeNBT();
                                    newCap.deserializeNBT(tag);

                                    // Sync if needed
                                    if (player.level() instanceof ServerLevel serverLevel) {
                                        PacketHandler.sendToNearbyClients(serverLevel,
                                                player.getX(), player.getY(), player.getZ(), 96, new s2cSyncCapabilitiesPacket(player.getId(), player));
                                    }
                                });


                    });
            original.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP)
                    .ifPresent(oldCap -> {
                        Player player = event.getEntity();

                        player.getCapability(BrutalityCapabilities.PLAYER_RAGE_CAP)
                                .ifPresent(newCap -> {
                                    // Copy all states
                                    CompoundTag tag = oldCap.serializeNBT();
                                    newCap.deserializeNBT(tag);

                                    // Sync if needed
                                    if (player.level() instanceof ServerLevel) {
                                        PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(player.getId(), player));
                                    }
                                });


                    });

            original.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP)
                    .ifPresent(oldCap -> {
                        Player player = event.getEntity();

                        player.getCapability(BrutalityCapabilities.PLAYER_MANA_CAP)
                                .ifPresent(newCap -> {
                                    // Copy all states
                                    CompoundTag tag = oldCap.serializeNBT();
                                    newCap.deserializeNBT(tag);

                                    // Sync if needed
                                    if (player.level() instanceof ServerLevel) {
                                        PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(player.getId(), player));
                                    }
                                });


                    });

            original.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP)
                    .ifPresent(oldCap -> {
                        Player player = event.getEntity();

                        player.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP)
                                .ifPresent(newCap -> {
                                    // Copy all states
                                    CompoundTag tag = oldCap.serializeNBT();
                                    newCap.deserializeNBT(tag);

                                    // Sync if needed
                                    if (player.level() instanceof ServerLevel serverLevel) {
                                        PacketHandler.sendToNearbyClients(serverLevel,
                                                player.getX(), player.getY(), player.getZ(), 96, new s2cSyncCapabilitiesPacket(player.getId(), player));
                                    }
                                });


                    });

            original.getCapability(BrutalityCapabilities.ENTITY_STAR_COUNT_CAP)
                    .ifPresent(oldCap -> {
                        Player player = event.getEntity();

                        player.getCapability(BrutalityCapabilities.ENTITY_STAR_COUNT_CAP)
                                .ifPresent(newCap -> {
                                    // Copy all states
                                    CompoundTag tag = oldCap.serializeNBT();
                                    newCap.deserializeNBT(tag);

                                });


                    });
        }
    }
}