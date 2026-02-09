package net.goo.brutality.common.item.weapon.hammer;

import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.common.entity.explosion.BrutalityExplosion;
import net.goo.brutality.common.entity.explosion.NuclearExplosion;
import net.goo.brutality.common.item.base.BrutalityHammerItem;
import net.goo.brutality.util.ModExplosionHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;


public class AtomicJudgementHammer extends BrutalityHammerItem {


    public AtomicJudgementHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, @NotNull LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        AtomicJudgementHammer.doExplosion(pAttacker, pTarget.getPosition(1).add(0, pTarget.getBbHeight() / 2, 0));
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
                .triggerableAnim("controller", RawAnimation.begin().thenPlay("attack"))
        );
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }


    public static void doExplosion(Entity entity, Vec3 loc) {

        BrutalityExplosion explosion = new NuclearExplosion(entity.level(), entity, null, null, loc.x, loc.y, loc.z, 3, false,
                BrutalityCommonConfig.ATOMIC_JUDGEMENT_GRIEFING.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);

        ModExplosionHelper.Server.explode(explosion, entity.level(), true);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        Vec3 viewVec = pLivingEntity.getViewVector(1F).normalize();
        int charge = getUseDuration(pStack) - pTimeCharged;
        float powerForTime = BowItem.getPowerForTime(charge);
        if (pLivingEntity.onGround()) {
            pLivingEntity.addDeltaMovement(viewVec.scale(powerForTime * -3F));
            if (pLivingEntity instanceof ServerPlayer player) {
                player.getCooldowns().addCooldown(pStack.getItem(), (int) (powerForTime * 100));
                player.connection.send(new ClientboundSetEntityMotionPacket(pLivingEntity));

            }
        }
        pStack.hurtAndBreak((int) (powerForTime * 20), pLivingEntity, livingEntity -> pLivingEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(stack);
    }




    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}
