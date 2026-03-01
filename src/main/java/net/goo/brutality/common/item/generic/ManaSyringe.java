package net.goo.brutality.common.item.generic;

import net.goo.brutality.common.item.base.BrutalityGenericItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.magic.ManaHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.List;

public class ManaSyringe extends BrutalityGenericItem {

    public ManaSyringe(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BLOCK;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BLOCK_EFFICIENCY;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (ManaHelper.getMana(pPlayer) < getManaPerItem(pPlayer.getItemInHand(pUsedHand))) {
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        }
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    private int getManaPerItem(ItemStack stack) {
        int efficiencyLvl = stack.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY);
        return Math.max(1, 50 - efficiencyLvl * 5);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pRemainingUseDuration % 20 == 0 && pLivingEntity instanceof Player player) {


            int manaPerItem = getManaPerItem(pStack);
            if (ManaHelper.getMana(player) >= manaPerItem) {
                ManaHelper.modifyManaValue(player, -manaPerItem);
                player.addItem(BrutalityItems.SOLIDIFIED_MANA.get().getDefaultInstance());
                RandomSource randomSource = player.getRandom();
                player.playSound(SoundEvents.AMETHYST_BLOCK_CHIME);
                for (int i = 0; i < 10; i++)
                    pLevel.addParticle(BrutalityParticles.WIZARDRY_PARTICLE.get(), player.getRandomX(0.5), player.getY(0.5), player.getRandomZ(0.5),
                            Mth.nextFloat(randomSource, -0.25F, 0.25F),
                            Mth.nextFloat(randomSource, -0.25F, 0.25F),
                            Mth.nextFloat(randomSource, -0.25F, 0.25F)
                    );
            }
        }

    }
}
