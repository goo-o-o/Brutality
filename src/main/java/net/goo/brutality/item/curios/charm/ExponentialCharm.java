package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.event.LivingEntityEventHandler;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class ExponentialCharm extends BrutalityCurioItem {


    float eMult = 0.0314f;
    float e = 3.14f;

    private final UUID EXPONENTIAL_CHARM_ARMOR_TOUGHNESS_UUID = UUID.fromString("3d5a255a-ba29-4ef8-a062-c1e2f60b4de5");
    private final UUID EXPONENTIAL_CHARM_ARMOR_UUID = UUID.fromString("34071937-161d-4ffd-9389-470bec894a2a");
    private final UUID EXPONENTIAL_CHARM_LUCK_UUID = UUID.fromString("6773ccc6-f8ed-4c68-856a-aa99c5ded49c");
    private final UUID EXPONENTIAL_CHARM_MAX_HP_UUID = UUID.fromString("721ac022-478d-4161-ba45-91a8dfe21d2e");
    private final UUID EXPONENTIAL_CHARM_KB_RESIST_UUID = UUID.fromString("25343e75-c9fa-4d8d-8485-a884e4e3e467");

    public ExponentialCharm(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(EXPONENTIAL_CHARM_KB_RESIST_UUID, "KB Resist Buff", eMult, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(Attributes.LUCK, new AttributeModifier(EXPONENTIAL_CHARM_LUCK_UUID, "Luck Buff", eMult, AttributeModifier.Operation.MULTIPLY_TOTAL));
            builder.put(Attributes.ARMOR, new AttributeModifier(EXPONENTIAL_CHARM_ARMOR_UUID, "Armor Buff", e, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(EXPONENTIAL_CHARM_ARMOR_TOUGHNESS_UUID, "Armor Toughness Buff", e, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(EXPONENTIAL_CHARM_MAX_HP_UUID, "HP Buff", e, AttributeModifier.Operation.ADDITION));
            return builder.build();

        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntityEventHandler.attackCombos.remove(slotContext.entity().getUUID());
        super.onUnequip(slotContext, newStack, stack);
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        LivingEntityEventHandler.attackCombos.remove(slotContext.entity().getUUID());
        super.onEquip(slotContext, prevStack, stack);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler ->
                        handler.findFirstCurio(BrutalityModItems.SCIENTIFIC_CALCULATOR_BELT.get()).isPresent()
                )
                .orElse(false);
    }

}
