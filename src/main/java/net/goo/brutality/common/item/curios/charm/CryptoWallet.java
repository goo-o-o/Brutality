package net.goo.brutality.common.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

import static net.goo.brutality.common.item.curios.charm.PortableMiningRig.END_COIN;
import static net.goo.brutality.common.item.curios.charm.PortableMiningRig.NETHER_COIN;

public class CryptoWallet extends BrutalityCurioItem {
    public CryptoWallet(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }
//
//    UUID CRYPTO_CHARM_AD_UUID = UUID.fromString("a0dcfef5-8322-4542-a40a-ce5c7f3bb236");
//    UUID CRYPTO_CHARM_AS_UUID = UUID.fromString("99e26a45-2d98-4064-b934-2faa1e6dcae3");
//
//    private static final Object2FloatOpenHashMap<UUID> END_COIN_OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();
//    private static final Object2FloatOpenHashMap<UUID> NETHER_COIN_OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();
//
//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        if (slotContext.entity() != null) {
//            LivingEntity livingEntity = slotContext.entity();
//            UUID uuid = livingEntity.getUUID();
//            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
//                AttributeInstance attackDamage = livingEntity.getAttribute(Attributes.ATTACK_DAMAGE);
//                float endCoinOriginalBonus = END_COIN_OLD_BONUS_MAP.getOrDefault(uuid, 0);
//                float endCoinNewBonus = stack.getOrCreateTag().getFloat(END_COIN);
//                if (attackDamage != null & endCoinOriginalBonus != endCoinNewBonus) {
//                    END_COIN_OLD_BONUS_MAP.put(uuid, endCoinNewBonus);
//                    attackDamage.removeModifier(CRYPTO_CHARM_AD_UUID);
//                    attackDamage.addTransientModifier(
//                            new AttributeModifier(
//                                    CRYPTO_CHARM_AD_UUID,
//                                    "Temporary AD Bonus",
//                                    endCoinNewBonus,
//                                    AttributeModifier.Operation.ADDITION
//                            )
//                    );
//                }
//
//
//                AttributeInstance attackSpeed = livingEntity.getAttribute(Attributes.ATTACK_SPEED);
//                float netherCoinOriginalBonus = NETHER_COIN_OLD_BONUS_MAP.getOrDefault(uuid, 0);
//                float netherCoinNewBonus = stack.getOrCreateTag().getFloat(NETHER_COIN);
//
//                if (attackSpeed != null & netherCoinOriginalBonus != netherCoinNewBonus) {
//                    NETHER_COIN_OLD_BONUS_MAP.put(uuid, netherCoinNewBonus);
//                    attackSpeed.removeModifier(CRYPTO_CHARM_AS_UUID);
//                    attackSpeed.addTransientModifier(
//                            new AttributeModifier(
//                                    CRYPTO_CHARM_AS_UUID,
//                                    "Temporary Speed Bonus",
//                                    netherCoinNewBonus,
//                                    AttributeModifier.Operation.ADDITION
//                            )
//                    );
//                }
//
//            });
//        }
//    }


    @Override
    public double getDynamicAttributeBonus(LivingEntity owner, ItemStack stack, Attribute attribute, double currentBonus) {
        if (attribute == Attributes.ATTACK_DAMAGE)
            return stack.getOrCreateTag().getFloat(END_COIN);
        if (attribute == Attributes.ATTACK_SPEED)
            return stack.getOrCreateTag().getFloat(NETHER_COIN);

        return super.getDynamicAttributeBonus(owner, stack, attribute, currentBonus);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null && slotContext.entity().level().isClientSide()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(uuid, "Temporary AD Bonus",
                            stack.getOrCreateTag().getFloat(END_COIN),
                            AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED,
                    new AttributeModifier(uuid, "Temporary AS Bonus",
                            stack.getOrCreateTag().getFloat(NETHER_COIN),
                            AttributeModifier.Operation.ADDITION));
            return builder.build();
        }

        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}

