package net.goo.brutality.common.item.curios.charm;


import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CelestialStarboard extends BrutalityCurioItem {

    public CelestialStarboard(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state
                .setAndContinue(RawAnimation.begin().thenLoop("hide")))
                .triggerableAnim("spin", RawAnimation.begin().thenLoop("spin"))
        );
    }


    private boolean wasOnGround;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof Player player)) return;

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            if (Minecraft.getInstance().player == player) {
                if (Minecraft.getInstance().options.keyJump.isDown()) {
                    if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
                        if (Minecraft.getInstance().options.keyShift.isDown()) {
                            Vec3 lookVec = player.getViewVector(1).normalize();
                            Vec3 lockedVec = new Vec3(lookVec.x, 0, lookVec.z);
                            player.addDeltaMovement(lockedVec.scale(2));
                            player.getCooldowns().addCooldown(stack.getItem(), 60);
                        }
                    }
                    if (player.getDeltaMovement().y() < 0.5)
                        player.addDeltaMovement(new Vec3(0, 0.125, 0));
                }
            }

        } else {
            if (!player.onGround() && wasOnGround) {
                triggerArmorAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "spin");
                wasOnGround = false;
            } else if (player.onGround() && !wasOnGround) {
                stopTriggeredArmorAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "spin");
                wasOnGround = true;
            }
        }
    }


}
