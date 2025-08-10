package net.goo.brutality.item.weapon.hammer;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.base.BrutalityHammerItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ForgeMod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

public class TheGoldenSpatulaHammer extends BrutalityHammerItem implements BrutalityGeoItem {


    public TheGoldenSpatulaHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pAttacker.level().playSound(null, pAttacker.getOnPos(), ModUtils.getRandomSound(BrutalityModSounds.SPATULA), SoundSource.PLAYERS);
        pTarget.push(0D, 0.5D, 0D);

        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.MASHED.get(), 100, 1, false, true));
        pTarget.addEffect(new MobEffectInstance(BrutalityModMobEffects.SCORED.get(), 100, 1, false, true));


        if (pTarget instanceof ServerPlayer playerTarget)
            playerTarget.connection.send(new ClientboundSetEntityMotionPacket(playerTarget));

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    UUID GOLDEN_SPATULA_RANGE_UUID = UUID.fromString("c8782dce-024d-40a3-9ff6-8fb733f42469");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            GOLDEN_SPATULA_RANGE_UUID,
                            "Reach bonus",
                            2,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }
}
