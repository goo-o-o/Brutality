package net.goo.brutality.item.weapon.lance;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.projectile.generic.BlackHole;
import net.goo.brutality.item.base.BrutalityLanceItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
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
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHorizonLance extends BrutalityLanceItem {


    public EventHorizonLance(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);

        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(itemstack);

    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeLeft) {
        if (pLivingEntity instanceof Player player) {
            if (pStack.getOrCreateTag().contains("ID")) {
                despawnBlackHole(player, pStack);
            } else {
                spawnBlackHole(player, pStack);
            }

            player.getCooldowns().addCooldown(pStack.getItem(), 40);
        }
    }

    public void spawnBlackHole(Player player, ItemStack pStack) {
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getOnPos(), BrutalityModSounds.EVENT_HORIZON_DISLODGE.get(), SoundSource.AMBIENT);

            BlackHole blackHole = new BlackHole(BrutalityModEntities.BLACK_HOLE_ENTITY.get(), serverLevel);
            blackHole.setOwner(player);
            blackHole.setPos(player.getPosition(1f).add(0, 2, 0));
            serverLevel.addFreshEntity(blackHole);
            pStack.getOrCreateTag().putInt("ID", blackHole.getId());
            triggerAnim(player, GeoItem.getOrAssignId(pStack, serverLevel), "controller_2", "dislodged");
        }
    }

    public void despawnBlackHole(Player player, ItemStack pStack) {
        if (player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getOnPos(), BrutalityModSounds.EVENT_HORIZON_RETURN.get(), SoundSource.AMBIENT);

            BlackHole blackHole = getOwnedBlackHoleFromID(player, pStack);
            if (blackHole != null) {
                blackHole.discard();
            }
            pStack.getOrCreateTag().remove("ID");
            triggerAnim(player, GeoItem.getOrAssignId(pStack, serverLevel), "controller_2", "return");
        }
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            if (pStack.getOrCreateTag().contains("ID"))
                if (pIsSelected) {
                    if (getOwnedBlackHoleFromID(player, pStack) == null) {
                        despawnBlackHole(player, pStack);
                    }
                } else despawnBlackHole(player, pStack);
        }
    }

    private BlackHole getOwnedBlackHoleFromID(Player player, ItemStack pStack) {
        Entity blackHole = player.level().getEntity(pStack.getOrCreateTag().getInt("ID"));
        if (blackHole instanceof BlackHole) {
            return (BlackHole) blackHole;
        }
        return null;
    }


    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        despawnBlackHole(player, item);
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))

        );

        controllers.add(new AnimationController<>(this, "controller_2", state -> null)
                .triggerableAnim("dislodged", RawAnimation.begin().thenPlayAndHold("dislodged"))
                .triggerableAnim("return", RawAnimation.begin().thenPlayAndHold("return")));
    }
}
