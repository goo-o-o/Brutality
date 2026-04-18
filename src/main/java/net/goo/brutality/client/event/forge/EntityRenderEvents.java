package net.goo.brutality.client.event.forge;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityRenderEvents {
//
//    @SubscribeEvent
//    public static <T extends LivingEntity, M extends EntityModel<T>> void onRenderPlayer(RenderLivingEvent.Post<T, M> event) {
//        LivingEntity livingEntity = event.getEntity();
//        if (!(livingEntity instanceof Player player)) return;
//        boolean hasCensoredItem = CuriosApi.getCuriosInventory(player)
//                .map(h -> h.isEquipped(BrutalityItems.CENSORSHIP.get()))
//                .orElse(false);
//
//        if (hasCensoredItem) {
//            PostEffectRegistry.renderEffectForNextTick(BrutalityShaders.PIXELATED_SHADER);
//
//            PoseStack poseStack = event.getPoseStack();
//            MultiBufferSource buffer = event.getMultiBufferSource();
//            int packedLight = event.getPackedLight();
//
//            VertexConsumer consumer = buffer.getBuffer(BrutalityRenderTypes.PIXELATE);
//
//            poseStack.pushPose();
//
//            poseStack.scale(1.1F, 1.1F, 1.1F);
//            event.getRenderer().getModel().renderToBuffer(
//                    poseStack,
//                    consumer,
//                    packedLight,
//                    LivingEntityRenderer.getOverlayCoords(player, 0),
//                    1.0F, 1.0F, 1.0F, 1.0F
//            );
//
//            poseStack.popPose();
//        }
//    }
}