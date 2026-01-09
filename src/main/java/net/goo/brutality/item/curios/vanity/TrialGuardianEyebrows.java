package net.goo.brutality.item.curios.vanity;

import net.goo.brutality.item.curios.BrutalityCurioItem;
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

public class TrialGuardianEyebrows extends BrutalityCurioItem {

    public TrialGuardianEyebrows(Rarity rarity) {
        super(rarity);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
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

}
