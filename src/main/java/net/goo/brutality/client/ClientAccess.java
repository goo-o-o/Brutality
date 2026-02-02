package net.goo.brutality.client;

import net.goo.brutality.client.sounds.DeathsawSoundInstance;
import net.goo.brutality.client.sounds.ExtinctionSpellSoundInstance;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.entity.spells.brimwielder.ExtinctionEntity;
import net.goo.brutality.common.item.base.BrutalityAnkletItem;
import net.goo.brutality.common.network.clientbound.ClientboundDodgePacket;
import net.goo.brutality.common.network.clientbound.ClientboundParticlePacket;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.event.LivingDodgeEvent;
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
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

import static net.goo.brutality.Brutality.LOGGER;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientAccess {
    private static ExtinctionSpellSoundInstance extinctionSpellSoundInstance;
    private static DeathsawSoundInstance deathsawSoundInstance;

    /**
     * Handles the synchronization of a capability's data on the client side based on the provided key and
     * serialized data. This method resolves the registered capability using the provided key, and updates
     * the specific instance associated with the local player using the deserialized data.
     *
     * @param key A unique identifier for the target capability to be synchronized. Used to retrieve the correct
     *            capability instance.
     * @param data The serialized {@link CompoundTag} containing the new data to be applied to the capability.
     */
    public static void handleSync(int entityId, String key, CompoundTag data) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        Entity entity = level.getEntity(entityId);
        if (entity == null) return;

        BrutalityCapabilities.getByKey(key).ifPresent(cap ->
                entity.getCapability(cap).ifPresent(instance ->
                        instance.deserializeNBT(data)
                )
        );
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
        player.playSound(BrutalitySounds.CHAINSAW_START.get(), 1.0F, 1.0F);
        SoundManager soundManager = Minecraft.getInstance().getSoundManager();

        if (deathsawSoundInstance.canPlaySound() && !soundManager.isActive(deathsawSoundInstance))
            Minecraft.getInstance().getSoundManager().playDelayed(ClientAccess.deathsawSoundInstance, 23);
    }

    public static void stopDeathsawSound(LivingEntity livingEntity) {
        livingEntity.playSound(BrutalitySounds.CHAINSAW_STOP.get(), 1.0F, 1.0F);
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
            ModLoader.get().postEvent(client);
            if (packet.anklet.getItem() instanceof BrutalityAnkletItem ankletItem) {
                ankletItem.onDodgeClient(livingEntity, source, packet.amount, packet.anklet);
            }
        }
    }

}

