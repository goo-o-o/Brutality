package net.goo.brutality.mixin.mixins;

import com.google.common.collect.Multimap;
import net.goo.brutality.item.weapon.axe.RhittaAxe;
import net.goo.brutality.item.weapon.hammer.JackpotHammer;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.SealUtils;
import net.goo.brutality.util.helpers.NbtHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @Unique
    private static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");
    @Unique
    private static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    @Inject(method = "getAttributeModifiers", at = @At("RETURN"), cancellable = true)
    private void addSealAttributeModifiers(EquipmentSlot pSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        ItemStack stack = (ItemStack) (Object) this;
        SealUtils.SEAL_TYPE sealType = SealUtils.getSealType(stack);
        SealUtils.handleSealAttributes(sealType, stack, pSlot, cir);
    }

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
            double itemBase = 0.0;

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                Multimap<Attribute, AttributeModifier> modifiers = stack.getAttributeModifiers(slot);
                for (AttributeModifier mod : modifiers.get(Attributes.ATTACK_DAMAGE)) {
                    if (mod.getId().equals(BASE_ATTACK_DAMAGE_UUID)) {
                        itemBase = mod.getAmount();
                        break;
                    }
                }
            }

            double modified = itemBase;
//            AttributeInstance attackAttribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
//            if (attackAttribute != null) {
//                modified += attackAttribute.calculateValue();
//                System.out.println(attackAttribute.getModifiers());
//            }

            if (stack.getItem() instanceof RhittaAxe) {
                modified += RhittaAxe.computeAttackDamageBonus(player.level());
            } else if (stack.getItem() instanceof JackpotHammer) {
                modified = JackpotHammer.getRandomDamage(player);
            }


            modified = ModUtils.computeAttributes(player, stack, modified);
            modified += player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            return modified - itemBase;
        }
        return player.getAttributeBaseValue(attribute);
    }

//    @Redirect(
//            method = "getTooltipLines",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeBaseValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D",
//                    ordinal = 1
//            )
//    )
//    private double modifyAttackSpeedValue(Player player, Attribute attribute) {
//        ItemStack stack = (ItemStack) (Object) this;
//        return player.getAttributeBaseValue(attribute);
//    }


    @Inject(method = "getMaxDamage()I", at = @At("HEAD"), cancellable = true)
    private void onGetMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = (ItemStack) (Object) this;

        boolean fromDoubleDown = NbtHelper.getBool(stack, "fromDoubleDown", false);
        if (fromDoubleDown) {
            int original = this.getItem().getMaxDamage(stack);
            cir.setReturnValue(Math.min(50, original));
        }

    }

    @Inject(method = "isEnchantable", at = @At("HEAD"), cancellable = true)
    private void restrictDoubleDown(CallbackInfoReturnable<Boolean> cir) {
        if (NbtHelper.getBool((((ItemStack) (Object) this)), "fromDoubleDown", false)) {
            cir.setReturnValue(false);
        }
    }
}
