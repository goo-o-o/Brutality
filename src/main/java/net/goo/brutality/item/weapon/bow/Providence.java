package net.goo.brutality.item.weapon.bow;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityBowItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class Providence extends BrutalityBowItem {


    public Providence(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1000;
    }

    @Override
    protected boolean requiresArrows() {
        return false;
    }

    @Override
    protected int getFullDrawTicks() {
        return 4;
    }

    @Override
    protected float getPowerMultiplier() {
        return 3.5F;
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
                .triggerableAnim("draw", RawAnimation.begin().thenPlayAndHold("draw"))
                .triggerableAnim("release", RawAnimation.begin().thenPlay("release"))
        );
    }


    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            triggerAnim(player, GeoItem.getOrAssignId(item, serverLevel), "controller", "release");
        }
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pIsSelected && pLevel instanceof ServerLevel serverLevel)
            triggerAnim(pEntity, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "release");
    }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack bowStack = player.getItemInHand(hand);

        if (level instanceof ServerLevel serverLevel) {
            InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(bowStack, level, player, hand, true);
            if (ret != null) return ret;

            if (level.isDay() && level.canSeeSky(player.blockPosition().above())) {
                player.startUsingItem(hand);
                triggerAnim(player, GeoItem.getOrAssignId(bowStack, serverLevel), "controller", "draw");

                return InteractionResultHolder.consume(bowStack);
            }
        }

        return InteractionResultHolder.fail(bowStack);
    }


    @Override
    protected SoundEvent getShootSound() {
        return BrutalityModSounds.WINGS_FLAP.get();
    }

    @Override
    protected EntityType<? extends AbstractArrow> getArrowEntity() {
        return BrutalityModEntities.LIGHT_ARROW.get();
    }

    @Override
    protected void onFullDraw(Player player, Level level) {
        List<LivingEntity> nearbyEntities = level.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.DEFAULT,
                null,
                player.getBoundingBox().inflate(3)
        );

        level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(3), e -> {
            if (e instanceof Mob mob) {
                return mob.getTarget() != player || mob.isAlliedTo(player);
            }
            return true;
        });

        for (LivingEntity entity : nearbyEntities) {
            entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0), entity);
        }
    }

    @Override
    public void releaseUsing(@NotNull ItemStack bowStack, @NotNull Level level, @NotNull LivingEntity shooter, int timeLeft) {
        super.releaseUsing(bowStack, level, shooter, timeLeft);

        if (level instanceof ServerLevel serverLevel)
            triggerAnim(shooter, GeoItem.getOrAssignId(bowStack, serverLevel), "controller", "release");
    }
}
