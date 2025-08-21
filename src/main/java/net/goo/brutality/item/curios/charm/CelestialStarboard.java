package net.goo.brutality.item.curios.charm;

import com.lowdragmc.photon.PhotonNetworking;
import com.lowdragmc.photon.client.fx.EntityEffect;
import com.lowdragmc.photon.command.EntityEffectCommand;
import com.lowdragmc.photon.command.RemoveEntityEffectCommand;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class CelestialStarboard extends BrutalityCurioItem {

    public CelestialStarboard(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
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

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
        if (ModList.get().isLoaded("photon")) {
            if (!(slotContext.entity() instanceof Player player)) return;
            EntityEffectCommand command = new EntityEffectCommand();
            command.setLocation(ResourceLocation.parse("photon:celestial_starboard_trail"));
            command.setEntities(List.of(player));
            command.setAutoRotate(EntityEffect.AutoRotate.FORWARD);
            PhotonNetworking.NETWORK.sendToAll(command);

        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        super.onUnequip(slotContext, newStack, stack);
        if (ModList.get().isLoaded("photon")) {
            if (!(slotContext.entity() instanceof Player player)) return;
            RemoveEntityEffectCommand command = new RemoveEntityEffectCommand();
            command.setLocation(ResourceLocation.parse("photon:celestial_starboard_trail"));
            command.setEntities(List.of(player));
            PhotonNetworking.NETWORK.sendToAll(command);

        }
    }

    private boolean wasOnGround;

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
                    if (player.getDeltaMovement().y() > 0 && player.getDeltaMovement().y() < 0.5)
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
