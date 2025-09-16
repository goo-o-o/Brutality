package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Pride extends BrutalityCurioItem {

    public Pride(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }


    UUID PRIDE_AD_UUID = UUID.fromString("81d46a1b-a3aa-4cb8-8999-d60df56c0cb7");

    private static final Object2FloatOpenHashMap<UUID> OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance attackDamage = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
                float newBonus = livingEntity.getHealth() / livingEntity.getMaxHealth() * 0.4F;

                UUID uuid = livingEntity.getUUID();
                float oldBonus = OLD_BONUS_MAP.getOrDefault(uuid, 0);
                if (attackDamage != null && oldBonus != newBonus) {
                    OLD_BONUS_MAP.put(uuid, newBonus);
                    attackDamage.removeModifier(PRIDE_AD_UUID);
                    attackDamage.addTransientModifier(
                            new AttributeModifier(
                                    PRIDE_AD_UUID,
                                    "AD Bonus",
                                    newBonus,
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_BONUS_MAP.removeFloat(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            LivingEntity livingEntity = slotContext.entity();
            float bonus = livingEntity.getHealth() / livingEntity.getMaxHealth() * 0.4F;

            if (bonus > 0)
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(PRIDE_AD_UUID, "AD Buff", bonus, AttributeModifier.Operation.ADDITION));


            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
