package net.goo.brutality.mixin.mixins;

import com.mojang.authlib.GameProfile;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.player_animation.PoseManager;
import net.goo.brutality.client.player_animation.PoseSubStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initBrutalityPose(ClientLevel world, GameProfile profile, CallbackInfo ci) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;

        PoseSubStack brutality$customPoseSubStack = new PoseSubStack(null, true, true);

        PlayerAnimationAccess.getPlayerAssociatedData(player).set(
                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "pose_layer"),
                brutality$customPoseSubStack.base
        );
    }


    @Unique
    private KeyframeAnimation brutality$currentPose = null;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
        if (!player.level().isClientSide()) return;

        var animationData = PlayerAnimationAccess.getPlayerAssociatedData(player);
        ModifierLayer<IAnimation> poseLayer = (ModifierLayer<IAnimation>) animationData.get(
                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "pose")
        );

        if (poseLayer != null) {
            KeyframeAnimation nextPose = PoseManager.getActivePose(player);

            if (nextPose != brutality$currentPose) {
                if (nextPose != null) {
                    poseLayer.replaceAnimationWithFade(
                            AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
                            new KeyframeAnimationPlayer(nextPose)
                    );
                } else {
                    poseLayer.replaceAnimationWithFade(AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE), null);
                }
                brutality$currentPose = nextPose;
            }
        }
    }
}