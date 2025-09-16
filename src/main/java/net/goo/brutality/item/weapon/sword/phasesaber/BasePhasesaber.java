package net.goo.brutality.item.weapon.sword.phasesaber;

import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.List;

public class BasePhasesaber extends BrutalitySwordItem {
    public BasePhasesaber(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String animation(@Nullable ItemStack stack) {
        return "phasesaber";
    }

    @Override
    public String model(@Nullable ItemStack stack) {
        return "phasesaber";
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putBoolean("show", true);
        return stack;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<GeoAnimatable>(this, "controller", state -> PlayState.CONTINUE)
                .triggerableAnim("show", RawAnimation.begin().thenPlayAndHold("show"))
                .triggerableAnim("hide", RawAnimation.begin().thenPlayAndHold("hide")));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        entity.playSound(BrutalityModSounds.PHASESABER_SWING.get());
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack pStack, LivingEntity pTarget, @NotNull LivingEntity pAttacker) {
        pTarget.setSecondsOnFire(3);
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        CompoundTag tag = stack.getOrCreateTag();

        if (pLevel instanceof ServerLevel serverLevel)
            if (tag.getBoolean("show")) {
                tag.putBoolean("show", false);
                triggerAnim(pPlayer, GeoItem.getOrAssignId(stack, serverLevel), "controller", "hide");
            } else {
                tag.putBoolean("show", true);
                triggerAnim(pPlayer, GeoItem.getOrAssignId(stack, serverLevel), "controller", "show");
            }

        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
