package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Style.class)
public class StyleMixin {
    @ModifyReturnValue(method = "isObfuscated", at = @At("RETURN"))
    private boolean forceObfuscated(boolean original) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return original;
        LocalPlayer player = mc.player;
        if (player == null || !player.hasEffect(BrutalityModMobEffects.TERRAMITICULOSIS.get())) return original;

        return true;
    }
}
