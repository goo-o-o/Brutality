package net.goo.brutality.client;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.entity.spells.brimwielder.ExtinctionEntity;
import net.goo.brutality.event.LivingDodgeEvent;
import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.item.weapon.throwing.VampireKnives;
import net.goo.brutality.network.*;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.sounds.ExtinctionSpellSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
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
    private static ExtinctionSpellSoundInstance sound;


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
        ExtinctionSpellSoundInstance sound = new ExtinctionSpellSoundInstance(extinctionEntity);
        ClientAccess.sound = sound;
        Minecraft.getInstance().getSoundManager().play(sound);
    }

    public static void stopExtinctionSpellSound() {
        Minecraft.getInstance().getSoundManager().stop(sound);
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

    public static class ThrowableWeaponHelper {
        public static boolean handleCooldown(Player player, BrutalityThrowingItem item) {
            if (player.getCooldowns().isOnCooldown(item)) return false;

            if (item instanceof VampireKnives) {
                player.getCooldowns().addCooldown(item, 5);
                return true;
            }

            int cooldownTicks = (int) (20 / player.getAttributeValue(Attributes.ATTACK_SPEED));
            if (player.getMainHandItem().getItem() instanceof BrutalityThrowingItem || player.getOffhandItem().getItem() instanceof BrutalityThrowingItem) {
                if (player.getMainHandItem().getItem() == player.getOffhandItem().getItem()) {
                    cooldownTicks /= 2;
                }
            }

            player.getCooldowns().addCooldown(item, cooldownTicks);
            return true;
        }

        public static void throwProjectileAndHandleAttributesAndAnimation(Player player, BrutalityThrowingItem throwingItem, boolean isOffhand) {
            if (!player.level().isClientSide()) return; // Exit if server-side
            ResourceLocation animationLocation = throwingItem.getAnimationResourceLocation();


            if (isOffhand) {
                offhandAttributes(player, () -> {
                    throwProjectile(player, throwingItem);
                    playThrowAnimation(player, animationLocation, true);
                });
            } else {
                throwProjectile(player, throwingItem);
                playThrowAnimation(player, animationLocation, false);
            }
        }

        public static void playThrowAnimation(Player player, ResourceLocation animationLocation, boolean offHand) {
            float speed = 1;
            KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(animationLocation);

            if (keyframeAnimation != null) {
                float attackSpeed = (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
                float animationLength = new KeyframeAnimationPlayer(keyframeAnimation).getData().getLength();
                float targetDurationTicks = 20 / attackSpeed;
                speed = animationLength / targetDurationTicks;
            }

            AnimationHelper.playAnimation(player, animationLocation, offHand, speed);
            PacketHandler.sendToServer(new ServerboundPlayerAnimationPacket(player.getUUID(), animationLocation, offHand, speed));
        }

        public static void throwProjectile(Player player, BrutalityThrowingItem throwingItem) {

            if (handleCooldown(player, throwingItem)) {
                playThrowSound(player);
                if (throwingItem instanceof VampireKnives) {
                    int quantity = 4;
                    quantity += player.getRandom().nextFloat() < 0.5F ? 1 : 0;
                    quantity += player.getRandom().nextFloat() < 0.25F ? 1 : 0;
                    quantity += player.getRandom().nextFloat() < 0.125F ? 1 : 0;
                    quantity += player.getRandom().nextFloat() < 0.0625F ? 1 : 0;
                    float gap = 7.5F;
                    for (int i = 0; i < quantity; i++) {
                        float angleOffset = (i - (quantity - 1) / 2f) * gap; // Center the arc
                        angleOffset += (player.getRandom().nextFloat() - 0.5F) * 5F;
                        PacketHandler.sendToServer(new ServerboundShootFromRotationPacket(throwingItem.getThrownEntity(), player.getEyePosition(),
                                player.getXRot(), player.getYRot() + angleOffset, throwingItem.getThrowVelocity(player), throwingItem.getThrowInaccuracy()));
                    }
                    return;
                }

                PacketHandler.sendToServer(new ServerboundShootFromRotationPacket(throwingItem.getThrownEntity(), player.getEyePosition(),
                        player.getXRot(), player.getYRot(), throwingItem.getThrowVelocity(player), throwingItem.getThrowInaccuracy()));
            }
        }

        private static final Object attributesLock = new Object();

        // Thanks to BetterCombat
        private static void offhandAttributes(Player player, Runnable runnable) {
            synchronized (attributesLock) {
                setAttributesForOffHandAttack(player, true);
                runnable.run();
                setAttributesForOffHandAttack(player, false);
            }
        }

        private static void playThrowSound(Player player) {
            player.playSound(BrutalityModSounds.THROW.get(), 2F, Mth.randomBetween(player.getRandom(), 0.8F, 1.2F));
        }

        private static void setAttributesForOffHandAttack(Player player, boolean useOffHand) {
            ItemStack mainHandStack = player.getMainHandItem();
            ItemStack offHandStack = player.getOffhandItem();
            ItemStack add;
            ItemStack remove;
            if (useOffHand) {
                remove = mainHandStack;
                add = offHandStack;
            } else {
                remove = offHandStack;
                add = mainHandStack;
            }

            player.getAttributes().removeAttributeModifiers(remove.getAttributeModifiers(EquipmentSlot.MAINHAND));
            player.getAttributes().addTransientAttributeModifiers(add.getAttributeModifiers(EquipmentSlot.MAINHAND));

        }
    }

}

