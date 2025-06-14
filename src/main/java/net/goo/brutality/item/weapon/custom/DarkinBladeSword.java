package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class DarkinBladeSword extends BrutalitySwordItem {
    int passiveCD = 0;
    boolean soundPlayed;

    public DarkinBladeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (passiveCD > 0) {
            passiveCD--;
        } else if (passiveCD == 0) {
            if (!soundPlayed && pEntity instanceof Player player)
                pLevel.playSound(null, player.getOnPos(), ModSounds.DEATHBRINGER_STANCE_READY.get(),
                        SoundSource.PLAYERS, 1.0F, Mth.nextFloat(pLevel.random, 0.75F, 1F));
        }


    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (passiveCD < 0) {
            float damage = pTarget.getMaxHealth() * 0.05F;
            pTarget.invulnerableTime = 0;
            pTarget.hurt(pAttacker.damageSources().indirectMagic(pAttacker, pAttacker), damage);
            pAttacker.heal(damage);
            if (pAttacker instanceof Player player)
                player.level().playSound(null, player.getOnPos(), ModSounds.DEATHBRINGER_STANCE_HIT.get(),
                    SoundSource.PLAYERS, 1.0F, Mth.nextFloat(player.level().random, 0.75F, 1F));
            soundPlayed = true;
            passiveCD = 100;
        }

        return true;

    }
}
