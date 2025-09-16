package net.goo.brutality.mixin;

import com.google.common.collect.Multimap;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.hammer.JackpotHammer;
import net.goo.brutality.util.ModUtils;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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

import java.util.UUID;

import static net.goo.brutality.util.helpers.AttributeHelper.ATTACK_DAMAGE_BONUS;
import static net.goo.brutality.util.helpers.AttributeHelper.ATTACK_SPEED_BONUS;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();
    private static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    private static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");
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
            double modified = 0.0;
            double itemBase = 0.0;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(slot);
                for (AttributeModifier mod : modifiers.get(Attributes.ATTACK_DAMAGE)) {
                    if (mod.getId().equals(BASE_ATTACK_DAMAGE_UUID)) {
                        modified = mod.getAmount();
                        itemBase = mod.getAmount();
                        break; // Take first matching modifier
                    }
                }
                if (modified != 0.0) break; // Exit loop once a valid modifier is found
            }
            if (stack.getItem() instanceof RhittaAxe) {
                modified += RhittaAxe.computeAttackDamageBonus(player.level());
            } else if (stack.getItem() instanceof JackpotHammer) {
                modified = JackpotHammer.getRandomDamage(player);
            }

            if (stack.getOrCreateTag().contains(ATTACK_DAMAGE_BONUS)) {
                modified += stack.getOrCreateTag().getFloat(ATTACK_DAMAGE_BONUS);
            }
            modified += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            modified = ModUtils.computeAttributes(player, stack, modified);

            return modified - itemBase;
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
