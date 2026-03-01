package net.goo.brutality.mixin.mixins;

import net.goo.brutality.common.mixin_helpers.BrutalityFontHooks;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.Random;

@Mixin({Font.StringRenderOutput.class})
public abstract class StringRenderOutputMixin {
    @Shadow
    @Final
    private float dimFactor;

    @Shadow
    float x;

    @Shadow
    float y;


    @Inject(method = "<init>", at = @At("RETURN"))
    private void brutality$captureShadow(CallbackInfo ci) {
        BrutalityFontHooks.setDimFactor(this.dimFactor);
    }

    @Inject(method = "accept", at = @At("HEAD"))
    public void brutality$captureStyle(int pos, Style style, int codePoint, CallbackInfoReturnable<Boolean> cir) {
        BrutalityFontHooks.setActiveColorData(style.getInsertion());
    }

    // Cache variables
    @Unique
    private long brutality$lastJitterFrame = -1;
    @Unique
    private float brutality$cachedOffsetX = 0;
    @Unique
    private float brutality$cachedOffsetY = 0;

    @ModifyArgs(
            method = "accept",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Font;renderChar(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V"
            )
    )
    private void brutality$applyJitter(Args args) {
        String rarityTag = BrutalityFontHooks.getActiveColorData();
        if (rarityTag == null) {
            return;
        }
        ColorUtils.ColorData colorData = ColorUtils.ColorData.getSafe(rarityTag);
        if (colorData == null) {
            return;
        }

        float pX = args.get(4);
        float pY = args.get(5);

        switch (colorData.shakeType) {
            case NONE -> {
            }
            case SMOOTH -> {
                float timeFunc = (float) (System.currentTimeMillis()) * colorData.shakeSpeed;
                brutality$cachedOffsetX = (float) Math.sin(timeFunc + (this.x * 0.1F)) * colorData.shakeAmount;
                brutality$cachedOffsetY = (float) Math.cos(timeFunc + (this.y * 0.1F) + 0.5F) * colorData.shakeAmount;
            }
            case JITTER -> {
                long interval = 1000 / (long) colorData.shakeSpeed;
                long currentJitterFrame = System.currentTimeMillis() / interval;


                if (currentJitterFrame != brutality$lastJitterFrame) {
                    Random random = new Random(); // Create random once per frame, not once per character
                    brutality$cachedOffsetX = (random.nextFloat() * 2.0F - 1.0F) * colorData.shakeAmount;
                    brutality$cachedOffsetY = (random.nextFloat() * 2.0F - 1.0F) * colorData.shakeAmount;
                    brutality$lastJitterFrame = currentJitterFrame;
                }

            }
        }

        args.set(4, pX + brutality$cachedOffsetX);
        args.set(5, pY + brutality$cachedOffsetY);
    }

}
