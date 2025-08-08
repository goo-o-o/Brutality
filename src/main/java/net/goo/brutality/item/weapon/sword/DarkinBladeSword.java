package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
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
import java.util.WeakHashMap;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class DarkinBladeSword extends BrutalitySwordItem {


    public DarkinBladeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }


    private static final WeakHashMap<ItemStack, DarkinBladeData> INSTANCE_DATA = new WeakHashMap<>();

    private static class DarkinBladeData {
        int passiveCD = 0;
        boolean soundPlayed = false;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        DarkinBladeData data = INSTANCE_DATA.computeIfAbsent(stack, k -> new DarkinBladeData());

        if (isSelected) {
            if (data.passiveCD > 0) {
                data.passiveCD--;
            } else if (data.passiveCD == 0 && !data.soundPlayed) {
                level.playSound(
                        null,
                        entity.getOnPos(),
                        BrutalityModSounds.DEATHBRINGER_STANCE_READY.get(),
                        SoundSource.PLAYERS,
                        1.0F,
                        Mth.nextFloat(level.random, 0.75F, 1F)
                );
                data.soundPlayed = true;
            }
        }
    }

    public boolean hurtEnemy(ItemStack stack, LivingEntity pTarget, LivingEntity pAttacker) {
        DarkinBladeData data = INSTANCE_DATA.get(stack);
        if (data != null && data.passiveCD <= 0) {
            float damage = pTarget.getMaxHealth() * 0.05F;
            pTarget.invulnerableTime = 0;
            pTarget.hurt(pAttacker.damageSources().indirectMagic(pAttacker, null), damage);
            pAttacker.heal(damage);
            if (pAttacker instanceof Player player)
                player.level().playSound(null, player.getOnPos(), BrutalityModSounds.DEATHBRINGER_STANCE_HIT.get(),
                        SoundSource.PLAYERS, 1.0F, Mth.nextFloat(player.level().random, 0.75F, 1F));
            data.passiveCD = 100;
            data.soundPlayed = false;
        }
        return super.hurtEnemy(stack, pTarget, pAttacker);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        entity.level().playSound(null, entity.blockPosition(), ModUtils.getRandomSound(BrutalityModSounds.DARKIN_BLADE), SoundSource.PLAYERS, 1, Mth.nextFloat(entity.level().getRandom(), 0.5F, 1.25F));


        return super.onEntitySwing(stack, entity);
    }
}
