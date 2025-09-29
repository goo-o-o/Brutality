package net.goo.brutality.mixin;

import net.minecraft.client.renderer.ItemInHandRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
//    @ModifyVariable(method = "tick()V", at = @At(value = "STORE", ordinal = 0), name = "f")
//    private float modifyAttackStrength(float value) {
//        Minecraft mc = Minecraft.getInstance();
//        LocalPlayer player = mc.player;
//        if (player != null) {
//            if (player.isHolding(stack -> stack.getItem() instanceof BrutalityThrowingItem)) {
//                return 1;
//            }
//        }
//        return value;
//    }
}