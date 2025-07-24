//package net.goo.brutality.mob_effect;
//
//import net.goo.brutality.registry.BrutalityModParticles;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.effect.MobEffect;
//import net.minecraft.world.effect.MobEffectCategory;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ai.attributes.AttributeInstance;
//import net.minecraft.world.entity.ai.attributes.AttributeMap;
//import net.minecraft.world.entity.ai.attributes.AttributeModifier;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//
//import java.util.UUID;
//
//public class EnragedEffect extends MobEffect {
//
//    private static final UUID ENRAGED_AD_UUID = UUID.fromString("e66b00e4-25b0-42df-bbc2-ba95445c2bf5");
//    private static final UUID ENRAGED_ARMOR_UUID = UUID.fromString("18918f9b-66f4-4190-8a2d-f8cc9c884ffb");
//    private static final UUID ENRAGED_AS_UUID = UUID.fromString("45783c22-2ed9-47fe-a46e-07f6fb8d0d37");
//    private static final UUID ENRAGED_MS_UUID = UUID.fromString("4e0f449b-3c0f-445b-b894-6b3dec2c4b03");
//
//    public EnragedEffect(MobEffectCategory category, int color) {
//        super(category, color);
//    }
//
//    @Override
//    public void applyEffectTick(LivingEntity entity, int amplifier) {
//        if (!entity.level().isClientSide) {
//            AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
//            if (movementSpeed != null) {
//                double dynamicSpeedBonus = 0.2 + (0.1 * amplifier);
//
//                AttributeModifier existing = movementSpeed.getModifier(ENRAGED_MS_UUID);
//                if (existing != null) {
//                    movementSpeed.removeModifier(existing);
//                }
//                movementSpeed.addTransientModifier(new AttributeModifier(
//                        ENRAGED_MS_UUID,
//                        "Enraged MS Bonus",
//                        dynamicSpeedBonus,
//                        AttributeModifier.Operation.MULTIPLY_TOTAL));
//            }
//            AttributeInstance attackSpeed = entity.getAttribute(Attributes.ATTACK_SPEED);
//            if (attackSpeed != null) {
//                double dynamicAttackSpeedBonus = 0.2 + (0.1 * amplifier);
//
//                AttributeModifier existing = attackSpeed.getModifier(ENRAGED_AS_UUID);
//                if (existing != null) {
//                    attackSpeed.removeModifier(existing);
//                }
//                attackSpeed.addTransientModifier(new AttributeModifier(
//                        ENRAGED_AS_UUID,
//                        "Enraged AS Bonus",
//                        dynamicAttackSpeedBonus,
//                        AttributeModifier.Operation.MULTIPLY_TOTAL));
//            }
//            AttributeInstance attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
//            if (attackDamage != null) {
//                double dynamicAttackDamageBonus = 0.5 + (0.05 * amplifier);
//
//                AttributeModifier existing = attackDamage.getModifier(ENRAGED_AD_UUID);
//                if (existing != null) {
//                    attackDamage.removeModifier(existing);
//                }
//                attackDamage.addTransientModifier(new AttributeModifier(
//                        ENRAGED_AD_UUID,
//                        "Enraged AD Bonus",
//                        dynamicAttackDamageBonus,
//                        AttributeModifier.Operation.MULTIPLY_TOTAL));
//            }
//            AttributeInstance armor = entity.getAttribute(Attributes.ARMOR);
//            if (armor != null) {
//                double dynamicArmorBonus = Math.min(-1 + (0.1 * amplifier), 0);
//
//                AttributeModifier existing = armor.getModifier(ENRAGED_ARMOR_UUID);
//                if (existing != null) {
//                    armor.removeModifier(existing);
//                }
//                armor.addTransientModifier(new AttributeModifier(
//                        ENRAGED_ARMOR_UUID,
//                        "Enraged MS Bonus",
//                        dynamicArmorBonus,
//                        AttributeModifier.Operation.MULTIPLY_TOTAL));
//            }
//
//            if (entity.tickCount % 5 == 0) {
//                if (entity.level() instanceof ServerLevel serverLevel) {
//                    serverLevel.sendParticles(BrutalityModParticles.ENRAGED_PARTICLE.get(),
//                            entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), 1,
//                            0.5, 0.5, 0.5
//                            , 0);
//
//
//                }
//            }
//        }
//    }
//
//    @Override
//    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
//        super.removeAttributeModifiers(entity, attributeMap, amplifier);
//        AttributeInstance movementSpeed = attributeMap.getInstance(Attributes.MOVEMENT_SPEED);
//        if (movementSpeed != null && movementSpeed.getModifier(ENRAGED_MS_UUID) != null) {
//            movementSpeed.removeModifier(ENRAGED_MS_UUID);
//        }
//        AttributeInstance attackSpeed = attributeMap.getInstance(Attributes.ATTACK_SPEED);
//        if (attackSpeed != null && attackSpeed.getModifier(ENRAGED_AS_UUID) != null) {
//            attackSpeed.removeModifier(ENRAGED_AS_UUID);
//        }
//        AttributeInstance attackDamage = attributeMap.getInstance(Attributes.ATTACK_DAMAGE);
//        if (attackDamage != null && attackDamage.getModifier(ENRAGED_AD_UUID) != null) {
//            attackDamage.removeModifier(ENRAGED_AD_UUID);
//        }
//        AttributeInstance armor = attributeMap.getInstance(Attributes.ARMOR);
//        if (armor != null && armor.getModifier(ENRAGED_ARMOR_UUID) != null) {
//            armor.removeModifier(ENRAGED_ARMOR_UUID);
//        }
//    }
//
//    @Override
//    public boolean isDurationEffectTick(int duration, int amplifier) {
//        return true;
//    }
//}
