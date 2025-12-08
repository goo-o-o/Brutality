package net.goo.brutality.client;

import net.goo.brutality.entity.spells.brimwielder.ExtinctionEntity;
import net.goo.brutality.event.LivingDodgeEvent;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.network.ClientboundDodgePacket;
import net.goo.brutality.network.ClientboundParticlePacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.sounds.DeathsawSoundInstance;
import net.goo.brutality.sounds.ExtinctionSpellSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

import static net.goo.brutality.Brutality.LOGGER;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientAccess {
    private static ExtinctionSpellSoundInstance extinctionSpellSoundInstance;
    private static DeathsawSoundInstance deathsawSoundInstance;


    public static void syncCapabilities(int entityId, Map<String, CompoundTag> data) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        Entity entity = level.getEntity(entityId);
        if (entity == null) return;

        for (Map.Entry<String, CompoundTag> entry : data.entrySet()) {
            String key = entry.getKey();
            CompoundTag tag = entry.getValue();

            Capability<?> cap = BrutalityCapabilities.CapabilitySyncRegistry.get(key);
            if (cap != null) {
                entity.getCapability(cap).ifPresent(inst -> {
                    ((INBTSerializable<CompoundTag>) inst).deserializeNBT(tag);
                });
            }
        }
    }

    public static void playExtinctionSpellSound(ExtinctionEntity extinctionEntity) {
        ClientAccess.extinctionSpellSoundInstance = new ExtinctionSpellSoundInstance(extinctionEntity);
        Minecraft.getInstance().getSoundManager().queueTickingSound(ClientAccess.extinctionSpellSoundInstance);
    }


    public static void stopExtinctionSpellSound() {
        Minecraft.getInstance().getSoundManager().stop(extinctionSpellSoundInstance);
    }

    public static void startDeathsawSound(Player player) {
        ClientAccess.deathsawSoundInstance = new DeathsawSoundInstance(player);
        player.playSound(BrutalityModSounds.CHAINSAW_START.get(), 1.0F, 1.0F);
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();

        if (deathsawSoundInstance.canPlaySound() && !soundManager.isActive(deathsawSoundInstance))
            Minecraft.getInstance().getSoundManager().playDelayed(ClientAccess.deathsawSoundInstance, 23);
    }

    public static void stopDeathsawSound(LivingEntity livingEntity) {
        livingEntity.playSound(BrutalityModSounds.CHAINSAW_STOP.get(), 1.0F, 1.0F);
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if (soundManager.isActive(deathsawSoundInstance))
            soundManager.stop(ClientAccess.deathsawSoundInstance);
    }



    public static void syncItemCooldowns(Map<Item, ItemCooldowns.CooldownInstance> cooldowns, int tickCount) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemCooldowns newCooldowns = player.getCooldowns();
            newCooldowns.cooldowns.clear();
            newCooldowns.cooldowns.putAll(cooldowns);
            newCooldowns.tickCount = tickCount;
        }
    }

    public static void spawnParticles(ClientboundParticlePacket packet) {
        if (Minecraft.getInstance().level == null) return;
        ClientLevel level = Minecraft.getInstance().level;

        for (int i = 0; i < packet.count; ++i) {
            float xOffset = (float) (level.random.nextGaussian() * packet.xDist);
            float yOffset = (float) (level.random.nextGaussian() * packet.yDist);
            float zOffset = (float) (level.random.nextGaussian() * packet.zDist);

            try {
                level.addParticle(packet.particle, packet.overrideLimiter, packet.x + xOffset, packet.y + yOffset, packet.z + zOffset, packet.xSpeed, packet.ySpeed, packet.zSpeed);
            } catch (Throwable throwable) {
                LOGGER.warn("Could not spawn particle effect {}: {}", packet.particle, throwable.getMessage());
                return;
            }

        }
    }

    public static void handleDodgeClient(ClientboundDodgePacket packet) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        Holder<DamageType> damageType = level.registryAccess()
                .registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, packet.damageTypeId));
        Entity directEntity = packet.hasDirectEntity && packet.directEntityId != null ? level.getEntity(packet.directEntityId) : null;
        Entity causingEntity = packet.hasCausingEntity && packet.causingEntityId != null ? level.getEntity(packet.causingEntityId) : null;
        DamageSource source = new DamageSource(damageType, directEntity, causingEntity);
        if (level.getEntity(packet.entityId) instanceof LivingEntity livingEntity) {
            LivingDodgeEvent.Client client = new LivingDodgeEvent.Client(livingEntity, source, packet.amount);
            MinecraftForge.EVENT_BUS.post(client);
            if (packet.anklet.getItem() instanceof BrutalityAnkletItem ankletItem) {
                ankletItem.onDodgeClient(livingEntity, source, packet.amount, packet.anklet);
            }
        }
    }

}

