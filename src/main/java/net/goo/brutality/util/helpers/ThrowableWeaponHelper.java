package net.goo.brutality.util.helpers;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.item.weapon.throwing.VampireKnives;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ServerboundHandleThrowingProjectilePacket;
import net.goo.brutality.network.ServerboundPlayerAnimationPacket;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ThrowableWeaponHelper {
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

    public static void handleAttributesAndAnimation(Player player, BrutalityThrowingItem throwingItem, ItemStack stack, boolean isOffhand) {
        ResourceLocation animationLocation = throwingItem.throwAnimation.getAnimationResource();
        if (isOffhand) {
            offhandAttributes(player, () -> {
                handleCooldownAndSound(player, stack, throwingItem);
                playThrowAnimation(player, animationLocation, true);
            });
        } else {
            handleCooldownAndSound(player, stack, throwingItem);
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

    /**
    Handles cooldown, sound and throwing of Projectiles.
    If called from server, will handle the throw action immediately.
    If called from client, will send a packet to the server that handles the throw action
     */
    public static void handleCooldownAndSound(Player player, ItemStack stack, BrutalityThrowingItem throwingItem) {
        if (handleCooldown(player, throwingItem)) {
            playThrowSound(player);
            if (player.level() instanceof ServerLevel serverLevel) {
                DelayedTaskScheduler.queueServerWork(serverLevel, 6, () -> throwingItem.handleThrowPacket(stack, player));
            } else {
                PacketHandler.sendToServer(new ServerboundHandleThrowingProjectilePacket(stack));
            }
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
