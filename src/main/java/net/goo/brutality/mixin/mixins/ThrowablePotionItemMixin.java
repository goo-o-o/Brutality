package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.util.SealUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ThrowablePotionItem.class)
public class ThrowablePotionItemMixin {

    @Inject(
            method = "use",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
                    shift = At.Shift.BEFORE
            )
    )
    private void attachSealToEnderPearl(Level pLevel, Player pPlayer, InteractionHand pHand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir, @Local ThrownPotion potion) {
        ItemStack pStack = pPlayer.getItemInHand(pHand);
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(pStack);
        if (sealType != null) {
            potion.getCapability(BrutalityCapabilities.SEAL_TYPE_CAP).ifPresent(cap -> cap.setSealType(sealType));
        }
    }
}