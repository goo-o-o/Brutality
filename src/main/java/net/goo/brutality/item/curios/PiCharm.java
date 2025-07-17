package net.goo.brutality.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.entity.custom.PiEntity;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModEntities;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class PiCharmCurio extends BrutalityCurioItem {
    public PiCharmCurio(Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler ->
                        handler.findFirstCurio(ModItems.SCIENTIFIC_CALCULATOR_BELT.get()).isPresent()
                )
                .orElse(false);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
        LivingEntity owner = slotContext.entity();

        if (!owner.level().isClientSide())
            for (int i = 0; i < 3; i++) {
                int angleOffset = (int) Math.toRadians(120 * i);

                PiEntity pi = new PiEntity(ModEntities.PI_ENTITY.get(), owner.level(), angleOffset);
                pi.setOwner(owner);
                owner.level().addFreshEntity(pi);
            }
    }

    float piMult = 0.0314f;
    float pi = 3.14f;

    UUID PI_CHARM_MS_UUID = UUID.fromString("5889431d-06af-4bef-b3c2-7d1cf5abda3c");
    UUID PI_CHARM_AS_UUID = UUID.fromString("d24bf9bb-e6d1-4b34-8758-5b294cc7db6d");
    UUID PI_CHARM_AD_UUID = UUID.fromString("a1c85971-cded-4c2a-adcf-e5b3ab6b9211");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(PI_CHARM_MS_UUID, "MS Buff", piMult, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(PI_CHARM_AS_UUID, "AS Buff", piMult, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(PI_CHARM_AD_UUID, "AD Buff", pi, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);

        LivingEntity owner = slotContext.entity();
        for (PiEntity piEntity : owner.level().getEntitiesOfClass(PiEntity.class, owner.getBoundingBox().inflate(10))) {
            piEntity.discard();
        }
    }
}
