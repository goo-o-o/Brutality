package net.goo.brutality.item.weapon.hammer;

import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.entity.explosion.BrutalityExplosion;
import net.goo.brutality.entity.explosion.NuclearExplosion;
import net.goo.brutality.item.base.BrutalityHammerItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.ModExplosionHelper;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
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
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;


public class AtomicJudgementHammer extends BrutalityHammerItem {


    public AtomicJudgementHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

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


    public static void doExplosion(Player player, Vec3 loc) {
        Level level = player.level();

        BrutalityExplosion explosion = new NuclearExplosion(player.level(), player, null, null, loc.x, loc.y, loc.z, 3, false,
                BrutalityCommonConfig.ATOMIC_JUDGEMENT_GRIEFING.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);

        ModExplosionHelper.Server.explode(explosion, level, true);
    }

    @Override
    public void releaseUsing(@NotNull ItemStack pStack, @NotNull Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        Vec3 viewVec = pLivingEntity.getViewVector(1F).normalize();
        int charge = getUseDuration(pStack) - pTimeCharged;
        float powerForTime = getPowerForTime(charge);
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


    public static float getPowerForTime(int pCharge) {
        float f = (float) pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}
