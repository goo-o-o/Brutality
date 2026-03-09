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
public abstract class AbstractClientPlayerEntityMixin {
    @Unique
    private PoseSubStack brutality$customPoseSubStack;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initBrutalityPose(ClientLevel world, GameProfile profile, CallbackInfo ci) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;

        // 1. Create the instance
        this.brutality$customPoseSubStack = new PoseSubStack(null, true, true);

        // 2. Attach it to the player's animation data
        // The "set" method adds your ModifierLayer to the player's rendering stack
        PlayerAnimationAccess.getPlayerAssociatedData(player).set(
                ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "pose_layer"),
                this.brutality$customPoseSubStack.base
        );
    }

    @Unique
    private ResourceLocation brutality$currentPoseId = null;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
        if (!player.level().isClientSide()) return;

        ModifierLayer<IAnimation> poseLayer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player)
                .get(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "pose"));

        System.out.println("poseLayer null? " + poseLayer);
        // 2. Logic to determine the correct pose
        ResourceLocation targetPoseId = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "spinning_pose");

        // 3. Only update if the pose has changed
        if (targetPoseId == null) {
            poseLayer.setAnimation(null);
        } else {
            KeyframeAnimation animation = PlayerAnimationRegistry.getAnimation(targetPoseId);
            System.out.println(animation);
            if (animation != null) {
                // Wrap it in a player that holds the state
                System.out.println("ReplaceAnimationWithFade: " + animation);
                if (poseLayer != null) {
                    poseLayer.replaceAnimationWithFade(
                            AbstractFadeModifier.standardFadeIn(5, Ease.LINEAR),
                            new KeyframeAnimationPlayer(animation)
                    );
                }
            }
        }
        brutality$currentPoseId = targetPoseId;
    }
}