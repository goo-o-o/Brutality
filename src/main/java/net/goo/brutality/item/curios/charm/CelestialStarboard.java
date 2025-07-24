//package net.goo.brutality.item.curios.charm;
//
//import net.goo.brutality.item.BrutalityCategories;
//import net.goo.brutality.item.base.BrutalityCurioItem;
//import net.goo.brutality.particle.custom.CelestialStarboardParticle;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.phys.Vec3;
//import software.bernie.geckolib.constant.DataTickets;
//import software.bernie.geckolib.core.animation.*;
//import software.bernie.geckolib.core.object.PlayState;
//import top.theillusivec4.curios.api.SlotContext;
//
//import java.util.List;
//
//public class CelestialStarboard extends BrutalityCurioItem {
//
//    public CelestialStarboard(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
//        super(rarity, descriptionComponents);
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(new AnimationController<>(this, 0, this::predicate));
//    }
//
//    private PlayState predicate(AnimationState animationState) {
//        Entity entity = (Entity) animationState.getData(DataTickets.ENTITY);
//
//        if (entity.onGround())
//            animationState.getController().setAnimation(RawAnimation.begin().then("hide", Animation.LoopType.LOOP));
//        else animationState.getController().setAnimation(RawAnimation.begin().then("spin", Animation.LoopType.LOOP));
//
//        return PlayState.CONTINUE;
//    }
//
//    @Override
//    public BrutalityCategories category() {
//        return BrutalityCategories.CurioType.CHARM;
//    }
//
//    private boolean trailSpawned = false;
//    private boolean wasOnGround = true;
//
//    @Override
//    public void curioTick(SlotContext slotContext, ItemStack stack) {
//        if (!(slotContext.entity() instanceof Player player)) return;
//
//        if (player.level().isClientSide) {
//            if (Minecraft.getInstance().player == player) {
//                if (Minecraft.getInstance().options.keyJump.isDown()) {
//                    if (!player.getCooldowns().isOnCooldown(stack.getItem())) {
//                        if (Minecraft.getInstance().options.keyShift.isDown()) {
//                            Vec3 lookVec = player.getViewVector(1).normalize();
//                            Vec3 lockedVec = new Vec3(lookVec.x, 0, lookVec.z);
//                            player.addDeltaMovement(lockedVec.scale(2));
//                            player.getCooldowns().addCooldown(stack.getItem(), 60);
//                        }
//                    }
//                    if (player.getDeltaMovement().length() < 1)
//                        player.addDeltaMovement(new Vec3(0, 0.125, 0));
//                }
//            }
//
//            if (!player.onGround() && wasOnGround && !trailSpawned) {
//                player.level().addAlwaysVisibleParticle((new CelestialStarboardParticle.OrbData(1, 1, 1,
//                                0.75F, player.getId(), 10)), player.getX(),
//                        player.getY(), player.getZ(), 0, 0, 0);
//
//
//                trailSpawned = true;
//            }
//
//            if (player.onGround()) {
//                trailSpawned = false;
//                wasOnGround = true;
//            } else {
//                wasOnGround = false;
//            }
//        }
//    }
//
//}
