package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    @ModifyReturnValue(method = "getDarknessGamma", at = @At("RETURN"))
    private float applyDespairGamma(float originalGamma, float pPartialTick) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return originalGamma;

        MobEffectInstance despair = player.getEffect(BrutalityEffects.DESPAIR.get());

        if (despair != null) {
            int level = despair.getAmplifier() + 1;
            // 5% increase per levels (0.05)
            float despairBonus = level * 0.05F;

            // If vanilla darkness is present, originalGamma > 0.
            // If not, we provide a base starting point so Despair works alone.

            // Returns the vanilla value + bonus, or base + bonus
            return Math.min(1.0F, originalGamma + despairBonus);
        }

        return originalGamma;
    }
}