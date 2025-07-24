//package net.goo.brutality.item.weapon.sword;
//
//import com.google.common.collect.ImmutableMultimap;
//import com.google.common.collect.Multimap;
//import net.goo.brutality.item.base.BrutalitySwordItem;
//import net.goo.brutality.registry.BrutalityModMobEffects;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.Attribute;
//import net.minecraft.world.entity.ai.attributes.AttributeModifier;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.item.Tier;
//import net.minecraftforge.common.ForgeMod;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.UUID;
//
//public class VoidKnifeSword extends BrutalitySwordItem {
//
//
//    public VoidKnifeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
//        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
//    }
//
////    @Override
////    public <T extends Item & BrutalityGeoItem> void configureLayers(BrutalityItemRenderer<T> renderer) {
////        super.configureLayers(renderer);
////        renderer.addRenderLayer(new BrutalityAutoEndPortalLayer<>(renderer));
////    }
//
//    @Override
//    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
//        pStack.hurtAndBreak(1, pAttacker, (attacker) -> {
//            attacker.broadcastBreakEvent(EquipmentSlot.MAINHAND);
//        });
//
//        if (pTarget.hasEffect(BrutalityModMobEffects.SCORED.get())) {
//            int amplifier = Objects.requireNonNull(pTarget.getEffect(BrutalityModMobEffects.SCORED.get())).getAmplifier();
//
//            if (amplifier > 3) return true;
//
//            pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.SCORED.get(), 60, amplifier + 1));
//        }
//        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.SCORED.get(), 60, 0));
//        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.PULVERIZED.get(), 3, 3));
//
//        return true;
//    }
//
//    UUID VOID_KNIFE_RANGE_UUID = UUID.fromString("aefaa05a-6817-4539-b933-47baaba4b8e8");
//
//    @Override
//    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
//        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);
//
//        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
//            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
//            builder.putAll(modifiers);
//            builder.put(
//                    ForgeMod.ENTITY_REACH.get(),
//                    new AttributeModifier(
//                            VOID_KNIFE_RANGE_UUID,
//                            "Reach bonus",
//                            2,
//                            AttributeModifier.Operation.ADDITION
//                    )
//            );
//
//            return builder.build();
//        }
//        return modifiers;
//    }
//}
