package net.goo.armament.mixin;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.goo.armament.util.helpers.AttributeHelper.ATTACK_DAMAGE_BONUS;
import static net.goo.armament.util.helpers.AttributeHelper.ATTACK_SPEED_BONUS;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Redirect(
            method = "getTooltipLines",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeBaseValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D",
                    ordinal = 0
            )
    )
    private double modifyBaseDamageValue(Player instance, Attribute attribute) {
        double baseValue = instance.getAttributeBaseValue(attribute);
        if (attribute == Attributes.ATTACK_DAMAGE) {
            ItemStack stack = (ItemStack)(Object)this;
            if (stack.getOrCreateTag().contains(ATTACK_DAMAGE_BONUS)) {
                return stack.getOrCreateTag().getFloat(ATTACK_DAMAGE_BONUS) + 1;
            }
        } else if (attribute == Attributes.ATTACK_SPEED) {
            ItemStack stack = (ItemStack)(Object)this;
            if (stack.getOrCreateTag().contains(ATTACK_SPEED_BONUS)) {
                return stack.getOrCreateTag().getFloat(ATTACK_SPEED_BONUS);
            }
        }
        return baseValue;
    }
}