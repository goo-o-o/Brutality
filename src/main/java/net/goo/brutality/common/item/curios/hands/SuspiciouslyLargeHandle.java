package net.goo.brutality.common.item.curios.hands;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SuspiciouslyLargeHandle extends BrutalityCurioItem {
    private static final float BASE_ATTACK_SPEED = 0.65F;

    public SuspiciouslyLargeHandle(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static float getDamageModification(Player player, ItemStack stack) {
        Optional<ICuriosItemHandler> handlerOptional = CuriosApi.getCuriosInventory(player).resolve();
        if (handlerOptional.isPresent()) {
            ICuriosItemHandler handler = handlerOptional.get();
            if (!stack.isEmpty() && handler.isEquipped(BrutalityItems.SUSPICIOUSLY_LARGE_HANDLE.get())) {
                float attackSpeed = (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
                float difference = attackSpeed - 0.65F;
                return difference * 5F;
            }
        }
        return 0;
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            LivingEntity entity = slotContext.entity();
            Double currentAttackSpeed = AttributeCalculationHelper.getAttributeValueSafe(entity, Attributes.ATTACK_SPEED);
            if (currentAttackSpeed != null) {
                float speedToModify = (float) (BASE_ATTACK_SPEED - currentAttackSpeed);
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(uuid, "Speed Modification", speedToModify, AttributeModifier.Operation.ADDITION));
                float damageToModify = speedToModify * -5F;
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(uuid, "Attack Damage Modification", damageToModify, AttributeModifier.Operation.ADDITION));
                return builder.build();
            }
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

}
