package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.client.renderers.item.BrutalityAutoFullbrightItemRenderer;
import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.item.base.BrutalityHammerItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.ModExplosionHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
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
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.function.Consumer;


public class AtomicJudgementHammer extends BrutalityHammerItem {


    public AtomicJudgementHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityAutoFullbrightItemRenderer.class);
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Level level = pAttacker.level();

        doCustomExplosion(level, pTarget, pAttacker, pStack);

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }


    public static void doCustomExplosion(Level level, LivingEntity pTarget, LivingEntity pAttacker, ItemStack pStack) {
        if (!level.isClientSide() && pAttacker instanceof Player player) {
            System.out.println("explode");
            ModExplosionHelper.sendCustomExplode(EXPLOSION_TYPES.NUCLEAR, level, player,
                    pTarget.getX(), pTarget.getY() + pTarget.getBbHeight() / 5, pTarget.getZ(),
                    player.getAttackStrengthScale(1F) * 10, false,
                    BrutalityCommonConfig.ATOMIC_JUDGEMENT_GRIEFING.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE,
//                    Level.ExplosionInteraction.BLOCK,

                    true);
            pStack.hurtAndBreak(5, pTarget, e -> pTarget.broadcastBreakEvent(InteractionHand.MAIN_HAND));

        }
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
