//package net.goo.brutality.event.forge.client;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import net.goo.brutality.Brutality;
//import net.goo.brutality.item.weapon.generic.LastPrismItem;
//import net.goo.brutality.registry.BrutalityCapabilities;
//import net.goo.brutality.registry.BrutalityModMobEffects;
//import net.goo.brutality.util.BrutalityEntityRotations;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.model.EntityModel;
//import net.minecraft.util.Mth;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.client.event.RenderHandEvent;
//import net.minecraftforge.client.event.RenderLivingEvent;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
//public class BrutalityForgeEntityRenderHandler {
//
//    @SubscribeEvent
//    public static void onRenderHand(RenderHandEvent event) {
//        Player player = Minecraft.getInstance().player;
//        if (player != null && player.getMainHandItem().getItem() instanceof LastPrismItem) {
//            if (event.getHand() == InteractionHand.OFF_HAND)
//                event.setCanceled(true);
//        }
//    }
//
//    @SubscribeEvent
//    public static <T extends LivingEntity, M extends EntityModel<T>> void onLivingRender(RenderLivingEvent.Pre<T, M> event) {
//        LivingEntity entity = event.getEntity();
//        PoseStack poseStack = event.getPoseStack();
//        float partialTick = event.getPartialTick();
//
//        if (entity.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
//            poseStack.scale(1.25F, 1.25F, 1.25F);
//        }
//
//
//        if (entity instanceof BrutalityEntityRotations rotations) {
//            entity.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP).ifPresent(entityShouldRotateCap -> {
//                if (entityShouldRotateCap.isShouldRotate()) {
//
////                System.out.println("rendering with rotation");
//
//                    float lerpedRoll = Mth.lerp(partialTick, rotations.brutality$getBrutalityPrevRoll(), rotations.brutality$getBrutalityRoll());
//                    float lerpedYaw = Mth.lerp(partialTick, rotations.brutality$getBrutalityPrevYaw(), rotations.brutality$getBrutalityYaw());
//                    float lerpedPitch = Mth.lerp(partialTick, rotations.brutality$getBrutalityPrevPitch(), rotations.brutality$getBrutalityPitch());
//
//                    float halfHeight = entity.getBbHeight() / 2.0f;
//
//                    poseStack.translate(0, halfHeight, 0);
//
//                    poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw));
//                    poseStack.mulPose(Axis.ZP.rotationDegrees(lerpedPitch));
//                    poseStack.mulPose(Axis.XP.rotationDegrees(lerpedRoll));
//
//                    poseStack.translate(0, -halfHeight, 0);
//
//                }
//            });
//        }
//
//    }
//
//}
