package net.goo.brutality.mixin.mixins;

import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.player_animation.PoseManager;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void initBrutalityPose(ClientLevel world, GameProfile profile, CallbackInfo ci) {
//        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
//
//        PoseSubStack brutality$customPoseSubStack = new PoseSubStack(null, true, true);
//
//        PlayerAnimationAccess.getPlayerAssociatedData(player).set(
//                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "pose_layer"),
//                brutality$customPoseSubStack.base
//        );
//    }


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
            // 1. Get the definition (which might be null)
            PoseManager.PoseDefinition poseDefinition = PoseManager.getActivePose(player);
            // 2. Extract the animation (null if definition is null)
            KeyframeAnimation nextPose = (poseDefinition != null) ? poseDefinition.animation() : null;

            // 3. This block must trigger if we are transitioning FROM a pose TO null
            if (nextPose != brutality$currentPose) {
                if (nextPose != null) {
                    boolean armsFlag = true;
                    boolean itemsFlag = true;
                    KeyframeAnimationPlayer keyframeAnimationPlayer = new KeyframeAnimationPlayer(nextPose);

                    if (armsFlag || itemsFlag) {
                        keyframeAnimationPlayer.setFirstPersonMode(FirstPersonMode.THIRD_PERSON_MODEL);
                        keyframeAnimationPlayer.setFirstPersonConfiguration(new FirstPersonConfiguration(armsFlag, armsFlag, itemsFlag, itemsFlag));
                    } else {
                        keyframeAnimationPlayer.setFirstPersonMode(FirstPersonMode.DISABLED);
                    }

                    poseLayer.replaceAnimationWithFade(
                            AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
                            keyframeAnimationPlayer
                    );
                } else {
                    // 4. This now correctly executes when getActivePose returns null
                    poseLayer.replaceAnimationWithFade(
                            AbstractFadeModifier.standardFadeIn(5, Ease.INOUTSINE),
                            null
                    );
                }
                // Update the tracker so we don't spam the fade every tick
                brutality$currentPose = nextPose;
            }
        }
    }

}