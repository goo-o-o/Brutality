package net.goo.brutality.item.curios.vanity;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class TrialGuardianEyebrows extends BrutalityCurioItem {

    public TrialGuardianEyebrows(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HEAD;
    }

    protected String[] expressions = new String[]{"angry", "neutral", "shocked", "sad", "suspicious"};
    protected static String EXPRESSIONS = "expressions";

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", (state) ->
                PlayState.STOP)
                .triggerableAnim("angry", RawAnimation.begin().thenPlayAndHold("angry"))
                .triggerableAnim("neutral", RawAnimation.begin().thenPlayAndHold("neutral"))
                .triggerableAnim("shocked", RawAnimation.begin().thenPlayAndHold("shocked"))
                .triggerableAnim("sad", RawAnimation.begin().thenPlayAndHold("sad"))
                .triggerableAnim("suspicious", RawAnimation.begin().thenPlayAndHold("suspicious"))
        );
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);

        if (slotContext.entity() instanceof Player player) {

            CompoundTag tag = stack.getOrCreateTag();
            int current = tag.getInt(EXPRESSIONS);
            int next = (current + 1) % expressions.length;
            tag.putInt(EXPRESSIONS, next);

            if (!player.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) player.level();
                long geoId = GeoItem.getOrAssignId(stack, serverLevel);
                triggerArmorAnim(player, geoId, "controller", expressions[next]);

            }
        }
    }

//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        super.curioTick(slotContext, stack);
//
//        if (slotContext.entity() instanceof Player player &&
//                Keybindings.RAGE_ACTIVATE_KEY.get().consumeClick()) {
//
//            // Cycle expression
//            CompoundTag tag = stack.getOrCreateTag();
//            int current = tag.getInt(EXPRESSIONS);
//            int next = (current + 1) % expressions.length;
//            tag.putInt(EXPRESSIONS, next);
//
//            // Debug output
//            Brutality.LOGGER.debug("Changing expression to: {}", expressions[next]);
//
//            // Trigger animation
//            if (!player.spellLevel().isClientSide()) {
//                ServerLevel serverLevel = (ServerLevel) player.spellLevel();
//                long geoId = GeoItem.getOrAssignId(stack, serverLevel);
//                System.out.println("Trying to play anim with geoID: " + geoId);
//
//                triggerAnim(player, geoId, "controller", expressions[next]);
//
//            }
//        }
//    }
}
