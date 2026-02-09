package net.goo.brutality.mixin.mixins;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.brutality.common.registry.BrutalityRarities;
import net.goo.brutality.mixin.accessors.BrutalityFontHooks;
import net.goo.brutality.util.ColorUtils;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BakedGlyph.class)
public abstract class BakedGlyphMixin {
    @Shadow
    @Final
    private float left;
    @Shadow
    @Final
    private float right;
    @Shadow
    @Final
    private float up;
    @Shadow
    @Final
    private float down;
    @Shadow
    @Final
    private float u0;
    @Shadow
    @Final
    private float u1;
    @Shadow
    @Final
    private float v0;
    @Shadow
    @Final
    private float v1;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(boolean pItalic, float pX, float pY, Matrix4f pMatrix, VertexConsumer pBuffer, float pRed, float pGreen, float pBlue, float pAlpha, int pPackedLight, CallbackInfo ci) {
        String rarityTag = BrutalityFontHooks.getActiveRarity();
        if (rarityTag == null) return;
        BrutalityRarities.RarityData rarityData = BrutalityRarities.RarityData.getSafe(rarityTag);
        if (rarityData == null) return;
        ci.cancel();
        // 1. Position Setup

        float leftPos = pX + this.left;
        float rightPos = pX + this.right;
        float upOffset = this.up - 3.0F;
        float downOffset = this.down - 3.0F;
        float topY = pY + upOffset;
        float bottomY = pY + downOffset;
        float italicTopOffset = pItalic ? 1.0F - 0.25F * upOffset : 0.0F;
        float italicBottomOffset = pItalic ? 1.0F - 0.25F * downOffset : 0.0F;


        int colorLeft = ColorUtils.getGradientAt(leftPos, rarityData.spread, rarityData.waveSpeed, rarityData.colors);
        int colorRight = ColorUtils.getGradientAt(rightPos, rarityData.spread, rarityData.waveSpeed, rarityData.colors);

        // Left side components

        float dimFactor = BrutalityFontHooks.getDimFactor();
        float redLeft = (colorLeft >> 16 & 255) / 255.0F * dimFactor;
        float greenLeft = (colorLeft >> 8 & 255) / 255.0F * dimFactor;
        float blueLeft = (colorLeft & 255) / 255.0F * dimFactor;

        // Right side components
        float redRight = (colorRight >> 16 & 255) / 255.0F * dimFactor;
        float greenRight = (colorRight >> 8 & 255) / 255.0F * dimFactor;
        float blueRight = (colorRight & 255) / 255.0F * dimFactor;

        // Render vertices with split colors
        pBuffer.vertex(pMatrix, leftPos + italicTopOffset, topY, 0.0F).color(redLeft, greenLeft, blueLeft, pAlpha).uv(this.u0, this.v0).uv2(pPackedLight).endVertex();
        pBuffer.vertex(pMatrix, leftPos + italicBottomOffset, bottomY, 0.0F).color(redLeft, greenLeft, blueLeft, pAlpha).uv(this.u0, this.v1).uv2(pPackedLight).endVertex();
        pBuffer.vertex(pMatrix, rightPos + italicBottomOffset, bottomY, 0.0F).color(redRight, greenRight, blueRight, pAlpha).uv(this.u1, this.v1).uv2(pPackedLight).endVertex();
        pBuffer.vertex(pMatrix, rightPos + italicTopOffset, topY, 0.0F).color(redRight, greenRight, blueRight, pAlpha).uv(this.u1, this.v0).uv2(pPackedLight).endVertex();
    }

}