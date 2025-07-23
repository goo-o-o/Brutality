package net.goo.brutality.item.weapon.bow;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalityBowItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
public class ProvidenceBow extends BrutalityBowItem {


    public ProvidenceBow(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
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

//    @SubscribeEvent
//    public static void onFOVModifier(ComputeFovModifierEvent event) {
//        Player player = event.getPlayer();
//
//        if (player.isUsingItem()) {
//            ItemStack itemStack = player.getUseItem();
//            if (itemStack.getItem() instanceof ProvidenceBow) {
//                float pullProgress = getPowerForTime(player.getTicksUsingItem(), fullDrawTicks);
//                float FOVModifier = 1.0F - pullProgress * 0.5F;
//                event.setNewFovModifier(event.getFovModifier() * FOVModifier);
//            }
//        }
//    }

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

//    @Override
//    public <T extends Item & BrutalityGeoItem> void configureLayers(BrutalityItemRenderer<T> renderer) {
//        super.configureLayers(renderer);
//        renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer));
//        renderer.addRenderLayer(new BrutalityAutoFullbrightNoDepthLayer<>(renderer));
//    }

    @Override
    public void releaseUsing(ItemStack bowStack, Level level, LivingEntity shooter, int timeLeft) {
        super.releaseUsing(bowStack, level, shooter, timeLeft);

        if (level instanceof ServerLevel serverLevel)
            triggerAnim(shooter, GeoItem.getOrAssignId(bowStack, serverLevel), "controller", "release");
    }
}
