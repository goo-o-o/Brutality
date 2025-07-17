package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.item.base.BrutalityGeoItem;
import net.goo.brutality.item.base.BrutalityHammerItem;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

public class SpatulaHammer extends BrutalityHammerItem implements BrutalityGeoItem {


    public SpatulaHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pAttacker.level().playSound(null, pAttacker.getOnPos(), ModUtils.getRandomSound(ModSounds.SPATULA), SoundSource.PLAYERS);
        pTarget.push(0D, 0.5D, 0D);

        if (pTarget instanceof ServerPlayer playerTarget)
            playerTarget.connection.send(new ClientboundSetEntityMotionPacket(playerTarget));

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }


}
