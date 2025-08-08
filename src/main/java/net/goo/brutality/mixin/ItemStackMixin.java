package net.goo.brutality.mixin;

import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.goo.brutality.util.helpers.AttributeHelper.ATTACK_DAMAGE_BONUS;
import static net.goo.brutality.util.helpers.AttributeHelper.ATTACK_SPEED_BONUS;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Redirect(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeBaseValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D",
                    ordinal = 0
            )
    )
    private double modifyBaseDamageValue(Player player, Attribute attribute) {
        if (attribute == Attributes.ATTACK_DAMAGE) {
            ItemStack stack = (ItemStack) (Object) this;

            if (stack.getItem() instanceof RhittaAxe) {
                return RhittaAxe.computeAttackDamageBonus(player.level());
            }

            if (stack.getOrCreateTag().contains(ATTACK_DAMAGE_BONUS)) {
                return stack.getOrCreateTag().getFloat(ATTACK_DAMAGE_BONUS) + player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            }
        }
        return player.getAttributeBaseValue(attribute);
    }

    @Redirect(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeBaseValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D",
                    ordinal = 1
            )
    )
    private double modifyAttackSpeedValue(Player player, Attribute attribute) {
        ItemStack stack = (ItemStack) (Object) this;

        if (attribute == Attributes.ATTACK_SPEED) {
            if (stack.getOrCreateTag().contains(ATTACK_SPEED_BONUS)) {
                return stack.getTag().getFloat(ATTACK_SPEED_BONUS) + player.getAttributeBaseValue(Attributes.ATTACK_SPEED);
            }
        }
        return player.getAttributeBaseValue(attribute);
    }


    @Inject(method = "getMaxDamage()I", at = @At("HEAD"), cancellable = true)
    private void onGetMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        if (stack.getTag() != null && stack.getTag().getBoolean("fromDoubleDown")) {
            int original = this.getItem().getMaxDamage(stack);
            cir.setReturnValue(Math.min(50, original));
        }
    }

}
