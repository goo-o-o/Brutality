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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    @Unique
    private static Random brutality$random = new Random();

    @Inject(
            method = "accept",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Font;renderChar(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V"
            )
    )
    private void brutality$applyJitter(int pPositionInCurrentSequence, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir) {
        String rarityTag = BrutalityFontHooks.getActiveColorData();
        if (rarityTag == null) return;

        ColorUtils.ColorData colorData = ColorUtils.ColorData.getSafe(rarityTag);
        if (colorData == null) return;

        // Logic to calculate offsets
        switch (colorData.shakeType) {
            case SMOOTH -> {
                float timeFunc = (float) (System.currentTimeMillis()) * colorData.shakeSpeed;
                brutality$cachedOffsetX = (float) Math.sin(timeFunc + (this.x * 0.1F)) * colorData.shakeAmount;
                brutality$cachedOffsetY = (float) Math.cos(timeFunc + (this.y * 0.1F) + 0.5F) * colorData.shakeAmount;
            }
            case JITTER -> {
                long interval = 1000 / (long) colorData.shakeSpeed;
                long currentJitterFrame = System.currentTimeMillis() / interval;
                if (currentJitterFrame != brutality$lastJitterFrame) {
                    brutality$cachedOffsetX = (brutality$random.nextFloat() * 2.0F - 1.0F) * colorData.shakeAmount;
                    brutality$cachedOffsetY = (brutality$random.nextFloat() * 2.0F - 1.0F) * colorData.shakeAmount;
                    brutality$lastJitterFrame = currentJitterFrame;
                }
            }
            default -> {
                brutality$cachedOffsetX = 0;
                brutality$cachedOffsetY = 0;
            }
        }

        // Instead of using Args, we temporarily shift the class fields
        // that are about to be read by the renderChar call.
        this.x += brutality$cachedOffsetX;
        this.y += brutality$cachedOffsetY;
    }

    @Inject(
            method = "accept",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Font;renderChar(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph;ZZFFFLorg/joml/Matrix4f;Lcom/mojang/blaze3d/vertex/VertexConsumer;FFFFI)V",
                    shift = At.Shift.AFTER
            )
    )
    private void brutality$cleanupJitter(int pPositionInCurrentSequence, Style pStyle, int pCodePoint, CallbackInfoReturnable<Boolean> cir) {
        // Revert the fields so the next character advance isn't messed up
        this.x -= brutality$cachedOffsetX;
        this.y -= brutality$cachedOffsetY;
    }

}
