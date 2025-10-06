package net.goo.brutality.mixin;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public abstract class KeyboardInputMixin extends Input {
    @Final
    @Shadow
    private Options options;

    @Inject(method = "tick(ZF)V", at = @At("HEAD"), cancellable = true)
    private void lockMovement(boolean pIsSneaking, float pSneakingSpeedMultiplier, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        AbstractClientPlayer player = mc.player;
        if (player == null) {
            return;
        }


        boolean isStunnedOrBound = player.hasEffect(BrutalityModMobEffects.STUNNED.get()) || player.hasEffect(BrutalityModMobEffects.LIGHT_BOUND.get());

        ModifierLayer<IAnimation> layer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "animation"));

        boolean isLayerActive = layer != null && layer.isActive();
        boolean isAnimationActive = isLayerActive && layer.getAnimation() != null && layer.isActive();


        if (isStunnedOrBound) {
            this.up = false;
            this.down = false;
            this.left = false;
            this.right = false;
            this.jumping = false;
            this.shiftKeyDown = false;
            this.forwardImpulse = 0;
            this.leftImpulse = 0;
            ci.cancel();
            return;
        }

        // Vanilla logic
        this.up = this.options.keyUp.isDown();
        this.down = this.options.keyDown.isDown();
        this.left = this.options.keyLeft.isDown();
        this.right = this.options.keyRight.isDown();
        this.forwardImpulse = brutality$calculateImpulse(this.up, this.down);
        this.leftImpulse = brutality$calculateImpulse(this.left, this.right);
        this.jumping = this.options.keyJump.isDown();
        if (isAnimationActive && player.isHolding(stack -> stack.getItem() instanceof BrutalityThrowingItem) &&
                !ModList.get().isLoaded("bettercombat")) {
            this.shiftKeyDown = false;
        } else {
            this.shiftKeyDown = this.options.keyShift.isDown();
        }

        if (pIsSneaking) {
            this.leftImpulse *= pSneakingSpeedMultiplier;
            this.forwardImpulse *= pSneakingSpeedMultiplier;
        }


        ci.cancel();
    }

    @Unique
    private static float brutality$calculateImpulse(boolean pInput, boolean pOtherInput) {
        if (pInput == pOtherInput) {
            return 0.0F;
        } else {
            return pInput ? 1.0F : -1.0F;
        }
    }
}
