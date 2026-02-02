package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MuramasaSword extends BrutalitySwordItem {


    public MuramasaSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }




    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player player) {
            Vec3 lookAng = player.getLookAngle().normalize().scale(3);

            player.setNoGravity(false);
            player.push(lookAng.x(), 0, lookAng.z());
            player.startAutoSpinAttack(5);

        }
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }



    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player)
            playSlashSound(player);
        return super.onEntitySwing(stack, entity);
    }

    private void playSlashSound(Player player) {
        RandomSource random = player.getRandom();
        int randomIndex = random.nextInt(BrutalitySounds.MURASAMA.size());
        SoundEvent sliceSound = BrutalitySounds.MURASAMA.get(randomIndex).get();
        player.level().playSound(null, player.getOnPos(), sliceSound, SoundSource.PLAYERS, 1, Mth.nextFloat(random, 0.8F, 1F));

    }
}
