package net.goo.brutality.client.player_animation;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;

public class BrutalityPoseHandler {
    // KeyframeAnimation is null if the file isn't found, so keep that in mind
    public static final KeyframeAnimation MAX_SPIN = PlayerAnimationRegistry.getAnimation(
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "max_spin")
    );

    // Use a LinkedHashMap if you want to prioritize certain poses (order matters)
    private static final Map<Predicate<AbstractClientPlayer>, KeyframeAnimation> REGISTERED_POSES = new LinkedHashMap<>();

    static {
        // Register your MAX weapon logic
        register(player -> player.getMainHandItem().is(BrutalityItems.MAX.get()) && player.isUsingItem(), MAX_SPIN);
    }

    public static void updatePose(AbstractClientPlayer player) {
        // Get the specific stack/layer for this player
        PlayerAnimationAccess.PlayerAssociatedAnimationData animationStack = PlayerAnimationAccess.getPlayerAssociatedData(player);
        ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) animationStack.get(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animation"));

        if (layer == null) return;

        // Find the first matching animation based on the player's current state
        KeyframeAnimation activeAnim = null;
        for (var entry : REGISTERED_POSES.entrySet()) {
            if (entry.getKey().test(player)) {
                activeAnim = entry.getValue();
                break;
            }
        }

        // Logic: Fade in if a match is found, fade out if not
        if (activeAnim != null) {
            // We only replace if it's a DIFFERENT animation to avoid restarting every tick
            layer.replaceAnimationWithFade(
                    AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
                    new KeyframeAnimationPlayer(activeAnim)
            );
        } else {
            if (layer.getAnimation() != null) {
                layer.replaceAnimationWithFade(
                        AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
                        null
                );
            }
        }
    }

    public static void register(Predicate<AbstractClientPlayer> predicate, KeyframeAnimation anim) {
        REGISTERED_POSES.put(predicate, anim);
    }
}