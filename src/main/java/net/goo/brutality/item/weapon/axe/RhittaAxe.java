package net.goo.brutality.item.weapon.axe;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.projectile.generic.CruelSunEntity;
import net.goo.brutality.item.base.BrutalityAxeItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RhittaAxe extends BrutalityAxeItem {


    public RhittaAxe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        if (pLivingEntity instanceof Player player) {
            if (pStack.getOrCreateTag().contains("ID")) {
                despawnCruelSun(player, pStack);
            } else {
                spawnCruelSun(player, pStack);
            }

            player.getCooldowns().addCooldown(pStack.getItem(), 40);
        }
    }

    public void spawnCruelSun(Player player, ItemStack pStack) {
        CruelSunEntity cruelSun = new CruelSunEntity(BrutalityModEntities.CRUEL_SUN_ENTITY.get(), player.level());
        cruelSun.setOwner(player);
        cruelSun.setPos(player.getPosition(1f).add(0, 10, 0));
        player.level().addFreshEntity(cruelSun);
        pStack.getOrCreateTag().putInt("ID", cruelSun.getId());

    }

    public void despawnCruelSun(Player player, ItemStack pStack) {
        if (player.level() instanceof ServerLevel) {

            CruelSunEntity cruelSun = getOwnedCruelSunFromID(player, pStack);
            if (cruelSun != null) {
                cruelSun.discard();
            }
            pStack.getOrCreateTag().remove("ID");
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.level() instanceof ServerLevel serverLevel && serverLevel.isDay()) {
            pPlayer.startUsingItem(pUsedHand);

            return InteractionResultHolder.consume(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            if (pStack.getOrCreateTag().contains("ID"))
                if (pIsSelected) {
                    if (getOwnedCruelSunFromID(player, pStack) == null) {
                        despawnCruelSun(player, pStack);
                    }

                } else despawnCruelSun(player, pStack);

            if (!(pLevel.isClientSide) && pLevel.isNight()) {
                despawnCruelSun(player, pStack);
            }
        }
    }

    public static float computeAttackDamageBonus(Level world) {
        float time = world.getDayTime() % 24000;
        float bonus = (float) Math.cos((Math.PI * (time - 6000)) / 12000);
        bonus = Math.max(0, bonus);
        return bonus * 20f;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    private CruelSunEntity getOwnedCruelSunFromID(Player player, ItemStack pStack) {
        Entity cruelSun = player.level().getEntity(pStack.getOrCreateTag().getInt("ID"));
        if (cruelSun instanceof CruelSunEntity) {
            return (CruelSunEntity) cruelSun;
        }
        return null;
    }


    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        despawnCruelSun(player, item);
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }
}
