package net.goo.brutality.common.item.weapon.scythe;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityScytheItem;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class DarkinScythe extends BrutalityScytheItem {
    protected String[] types = new String[]{"", "_shadow_assassin", "_rhaast"};
    private final UUID DARKIN_SCYTHE_MS_UUID = UUID.fromString("c0aa6baf-16a7-45e1-a65c-cfb82a65ff4c");
    private final UUID DARKIN_SCYTHE_AS_UUID = UUID.fromString("5386a75c-a1df-4b83-a62d-04dcb0fce7ff");
    private final UUID DARKIN_SCYTHE_AD_UUID = UUID.fromString("57b3f5d9-9f58-45d0-80f5-11ad07675bc6");

    public DarkinScythe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, DamageSource source, float amount) {
        int tex = ModUtils.getTextureIdx(weapon);
        if (tex == 1) {
            victim.invulnerableTime = 0;
            victim.hurt(attacker.damageSources().indirectMagic(attacker, null), amount * 0.25F);
            return 0;
        } else if (tex == 2) {
            attacker.heal(amount * 0.2F);
        }

        return super.hurtEnemyModifiable(attacker, victim, weapon, source, amount);
    }

    @Override
    public String texture(@javax.annotation.Nullable ItemStack stack) {
        return "darkin_scythe" + types[ModUtils.getTextureIdx(stack)];
    }

    @Override
    public String model(ItemStack stack) {
        return "darkin_scythe" + types[ModUtils.getTextureIdx(stack)];
    }


    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int index = ModUtils.getTextureIdx(pStack);

        pTooltipComponents.add(Component.translatable("brutality.description.type.on_shift_right_click"));
        pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.on_shift_right_click.1"));
        pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.on_shift_right_click.2"));

        if (index == 1) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.shadow_assasin"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("brutality.description.type.on_hit"));
            pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.on_hit.1"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("brutality.description.type.on_right_click"));
            pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.on_right_click.1"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("brutality.description.type.passive"));
            pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.passive.1"));
        }

        if (index == 2) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.darkin"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("brutality.description.type.on_hit"));
            pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.on_hit.2"));
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("brutality.description.type.on_right_click"));
            pTooltipComponents.add(Component.translatable("item.brutality.darkin_scythe.on_right_click.2"));
        }


    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

        if (slot == EquipmentSlot.MAINHAND) {
            float attackSpeed, attackDamage;
            int index = ModUtils.getTextureIdx(stack);

            switch (index) {
                case 1 -> {
                    attackSpeed = -0F;
                    attackDamage = 6;
                }
                case 2 -> {
                    attackSpeed = -3.1F;
                    attackDamage = 21;
                }
                default -> {
                    attackSpeed = -2.5F;
                    attackDamage = 6;
                }

            }

            modifiers.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(DARKIN_SCYTHE_AD_UUID,
                    "Weapon damage", attackDamage, AttributeModifier.Operation.ADDITION));

            modifiers.put(Attributes.ATTACK_SPEED, new AttributeModifier(DARKIN_SCYTHE_AS_UUID,
                    "Weapon speed", attackSpeed, AttributeModifier.Operation.ADDITION));

            modifiers.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(DARKIN_SCYTHE_MS_UUID,
                    "Move speed", (index == 1) ? .4 : 0, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
        return modifiers;
    }


    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pUsedHand == InteractionHand.OFF_HAND) return InteractionResultHolder.fail(stack);
        int index = ModUtils.getTextureIdx(stack);

        if (pPlayer.isCrouching()) {
            pPlayer.getCooldowns().addCooldown(stack.getItem(), 80);
            ModUtils.setTextureIdx(stack, (index + 1) % 3);
            pLevel.playSound(null, pPlayer.getOnPos(), BrutalitySounds.DARKIN_SCYTHE_TRANSFORM.get(index).get(), SoundSource.PLAYERS);

        } else {
            float length = (index == 1) ? 14 : 10;
            List<LivingEntity> entities = ModUtils.getEntitiesInRay(LivingEntity.class, pPlayer, length, ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, 1, e -> e != pPlayer, 100, null).entityList();

            if (index == 1) {


                for (LivingEntity entity : entities) {
                    pLevel.addParticle(BrutalityParticles.SHADOW_ASSASIN_PARTICLE.get(), entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), pLevel.random.nextFloat() - 0.5, pLevel.random.nextFloat() - 0.5, pLevel.random.nextFloat() - 0.5);
                    entity.hurt(pPlayer.damageSources().indirectMagic(pPlayer, null), 6);
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                }

                pPlayer.getCooldowns().addCooldown(stack.getItem(), 30);
                pLevel.playSound(pPlayer, pPlayer.getOnPos(), BrutalitySounds.GROUND_IMPACT.get(), SoundSource.PLAYERS, 1.0F, Mth.nextFloat(pLevel.random, 0.75F, 1F));
                return InteractionResultHolder.consume(stack);


            } else if (index == 2) {

                for (LivingEntity entity : entities) {
                    pLevel.addParticle(BrutalityParticles.RHAAST_PARTICLE.get(), entity.getX(), entity.getY() + entity.getBbHeight() / 2, entity.getZ(), pLevel.random.nextFloat() - 0.5, pLevel.random.nextFloat() - 0.5, pLevel.random.nextFloat() - 0.5);
                    entity.hurt(pPlayer.damageSources().playerAttack(pPlayer), 8);
                    entity.push(0, 0.35, 0);
                    entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                }

                pPlayer.getCooldowns().addCooldown(stack.getItem(), 30);
                pLevel.playSound(pPlayer, pPlayer.getOnPos(), BrutalitySounds.GROUND_IMPACT.get(), SoundSource.PLAYERS, 1.0F, Mth.nextFloat(pLevel.random, 0.75F, 1F));

                return InteractionResultHolder.consume(stack);
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }


}
