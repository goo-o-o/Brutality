package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class CartonOfPrismSolutionMilk extends BrutalityCurioItem {


    public CartonOfPrismSolutionMilk(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID PRISM_SOLUTION_MILK_HEALTH_UUID = UUID.fromString("73bd02a9-73ba-4cc5-a263-4f74d2e8a72d");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        builder.put(Attributes.MAX_HEALTH, new AttributeModifier(PRISM_SOLUTION_MILK_HEALTH_UUID, "Prism Milk Health Boost", -0.4, AttributeModifier.Operation.MULTIPLY_TOTAL));
        return builder.build();
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        super.curioTick(slotContext, stack);

        if (slotContext.entity().tickCount % 10 == 0) {
            LivingEntity wearer = slotContext.entity();
            wearer.addEffect(new MobEffectInstance(TerramityModMobEffects.IMMUNITY.get(), 11, 0, false, true));
            wearer.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 11, 1, false, true));
        }
    }
}
