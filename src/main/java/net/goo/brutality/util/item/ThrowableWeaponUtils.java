package net.goo.brutality.util.item;

import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.common.item.weapon.throwing.VampireKnives;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundHandleThrowingProjectilePacket;
import net.goo.brutality.common.network.serverbound.ServerboundPlayerAnimationPacket;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class ThrowableWeaponUtils {
    private static InteractionHand previousHand = InteractionHand.OFF_HAND;

    public static void handleAttacksWithoutBetterCombat(Player player) {
        Minecraft mc = Minecraft.getInstance();
        if (!ModList.get().isLoaded("bettercombat")) {
            if (mc.options.keyAttack.isDown()) {
                ItemStack mainHand = player.getMainHandItem();
                ItemStack offHand = player.getOffhandItem();
                ItemCooldowns cooldowns = player.getCooldowns();

                if (mainHand.getItem() instanceof BrutalityThrowingItem mainHandThrowingItem && offHand.getItem() instanceof BrutalityThrowingItem offHandThrowingItem) { // Dual wielding Throwing Items
                    if (!cooldowns.isOnCooldown(mainHandThrowingItem) && !cooldowns.isOnCooldown(offHandThrowingItem)) {
                        if (previousHand == InteractionHand.OFF_HAND) { // Throw Main Hand
                            mainHandThrowingItem.handleAttributesAndAnimation(player, mainHand, false);
                            player.resetAttackStrengthTicker();
                            previousHand = InteractionHand.MAIN_HAND;

                        } else {
                            offHandThrowingItem.handleAttributesAndAnimation(player, offHand, true);
                            player.resetAttackStrengthTicker();
                            previousHand = InteractionHand.OFF_HAND;
                        }
                    }
                } else if (mainHand.getItem() instanceof BrutalityThrowingItem mainHandThrowingItem) {
                    if (!cooldowns.isOnCooldown(mainHandThrowingItem)) {
                        mainHandThrowingItem.handleAttributesAndAnimation(player, mainHand, false);
                        player.resetAttackStrengthTicker();
                        previousHand = InteractionHand.OFF_HAND;
                    }
                }
            }
        }

    }


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
     * Handles cooldown, sound and throwing of Projectiles.
     * If called from server, will handle the throw action immediately.
     * If called from client, will send a packet to the server that handles the throw action
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
        player.playSound(BrutalitySounds.THROW.get(), 2F, Mth.randomBetween(player.getRandom(), 0.8F, 1.2F));
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
