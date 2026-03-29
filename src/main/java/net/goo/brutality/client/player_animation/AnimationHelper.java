package net.goo.brutality.client.player_animation;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.MirrorModifier;
import dev.kosmx.playerAnim.api.layered.modifier.SpeedModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class AnimationHelper {
    private static final HashMap<Player, SpeedModifier> speedModifierMap = new HashMap<>();
    private static final HashMap<Player, MirrorModifier> mirrorModifierMap = new HashMap<>();

    /**
     * Adapted Iron's Spells
     */
    public static void playAnimation(Player player, ResourceLocation animationLocation, boolean mirrored, float speed) {
        playAnimation(player, animationLocation, mirrored, speed, 2);
    }
    public static void playAnimation(Player player, ResourceLocation animationLocation, boolean mirrored, float speed, int fadeTicks) {
        KeyframeAnimation keyframeAnimation = PlayerAnimationRegistry.getAnimation(animationLocation);
        if (keyframeAnimation != null) {
            if (player instanceof AbstractClientPlayer abstractClientPlayer) {
                ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(abstractClientPlayer)
                        .get(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animation"));

                if (layer != null) {
                    KeyframeAnimationPlayer keyframeAnimationPlayer = new KeyframeAnimationPlayer(keyframeAnimation);

                    boolean armsFlag = BrutalityClientConfig.THROWING_ANIMATION_SHOW_ARMS.get();
                    boolean itemsFlag = BrutalityClientConfig.THROWING_ANIMATION_SHOW_ITEMS.get();

                    if (armsFlag || itemsFlag) {
                        keyframeAnimationPlayer.setFirstPersonMode(/*resourceLocation.getPath().equals("charge_arrow") ? FirstPersonMode.VANILLA : */FirstPersonMode.THIRD_PERSON_MODEL);
                        keyframeAnimationPlayer.setFirstPersonConfiguration(new FirstPersonConfiguration(armsFlag, armsFlag, itemsFlag, itemsFlag));
                    } else {
                        keyframeAnimationPlayer.setFirstPersonMode(FirstPersonMode.DISABLED);
                    }


                    //                        layer.setAnimation(keyframeAnimationPlayer);

                    SpeedModifier speedModifier = speedModifierMap.computeIfAbsent(player, k -> new SpeedModifier());
                    MirrorModifier mirrorModifier = mirrorModifierMap.computeIfAbsent(player, k -> new MirrorModifier());
                    mirrorModifier.setEnabled(mirrored);
                    speedModifier.speed = speed;
                    layer.addModifierBefore(mirrorModifier);
                    layer.addModifierBefore(speedModifier);
                    layer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(fadeTicks, Ease.INOUTSINE), keyframeAnimationPlayer, true);
                }
            }
        }
    }


    /**
     * Plays an animation on a remote player, identified by their {@link UUID}. This method checks local conditions
     * to avoid processing
     */
    public static void playAnimation(UUID playerUUID, ResourceLocation animationLocation, boolean mirrored, float speed, int fadeTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        if (playerUUID.equals(mc.player.getUUID())) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer) mc.level.getPlayerByUUID(playerUUID);
        if (player == null) {
            return;
        }
        playAnimation(player, animationLocation, mirrored, speed, fadeTicks);
    }


    public static void stopAnimation(UUID playerUUID, int fadeTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) {
            return;
        }
        if (playerUUID.equals(mc.player.getUUID())) {
            return;
        }
        AbstractClientPlayer player = (AbstractClientPlayer) mc.level.getPlayerByUUID(playerUUID);
        if (player == null) {
            return;
        }
        stopAnimation(player, fadeTicks);
    }

    public static void stopAnimation(Player player, int fadeTicks) {
        if (player instanceof AbstractClientPlayer abstractClientPlayer) {
            var animationData = PlayerAnimationAccess.getPlayerAssociatedData(abstractClientPlayer);
            ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) animationData
                    .get(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animation"));

            if (layer != null && layer.getAnimation() != null) {

                layer.replaceAnimationWithFade(
                        AbstractFadeModifier.standardFadeIn(fadeTicks, Ease.LINEAR),
                        null
                );
            }
        }
    }
}
