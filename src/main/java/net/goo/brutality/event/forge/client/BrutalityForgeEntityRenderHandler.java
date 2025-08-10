package net.goo.brutality.event.forge.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.Brutality;
import net.goo.brutality.entity.capabilities.EntityCapabilities;
import net.goo.brutality.item.weapon.generic.LastPrismItem;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.magic.IBrutalitySpellEntity;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.util.BrutalityEntityRotations;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.event.GeoRenderEvent;


@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BrutalityForgeEntityRenderHandler {


    @SubscribeEvent
    public static void onRenderSpell(GeoRenderEvent.Entity.Pre event) {
        if (event.getEntity() instanceof IBrutalitySpellEntity entity) {
            int spellLevel = entity.getSpellLevel();
            if (spellLevel < 1) return;

            float scaleIncrement = entity.getSizeScaling(); // Per-level increment (0.5 for 1 block per level)
            float finalScale = 1.0F + (spellLevel - 1) * scaleIncrement; // Scale factor: 1.0 at level 1, 1.5 at level 2, 2.0 at level 3, etc.

            BrutalitySpell spell = entity.getSpell();
            BrutalityTooltipHelper.SpellStatComponent rangeStat = spell.getStat(BrutalityTooltipHelper.SpellStatComponents.SIZE);
            finalScale = Mth.clamp(finalScale, 1.0F, rangeStat.max() / 2F); // Ensure scale doesn't exceed max range relative to base size

            PoseStack poseStack = event.getPoseStack();
            poseStack.popPose();
            poseStack.scale(finalScale, finalScale, finalScale);
            poseStack.pushPose();
        }
    }


    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.getMainHandItem().getItem() instanceof LastPrismItem) {
            if (event.getHand() == InteractionHand.OFF_HAND)
                event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static <T extends LivingEntity, M extends EntityModel<T>> void onLivingRender(RenderLivingEvent.Pre<T, M> event) {
        T entity = (T) event.getEntity();
        PoseStack poseStack = event.getPoseStack();
        float partialTick = event.getPartialTick();
        int packedLight = event.getPackedLight();
        LivingEntityRenderer<T, M> renderer = event.getRenderer();
        M model = renderer.getModel();
        MultiBufferSource originalBuffer = event.getMultiBufferSource();


//        CuriosApi.getCuriosInventory(entity).ifPresent(handler ->
//                handler.findFirstCurio(BrutalityModItems.SUSPICIOUS_SLOT_MACHINE.get()).ifPresent(slot ->
//                event.setCanceled(true)));



        if (entity.getCapability(BrutalityCapabilities.ENTITY_EFFECT_CAP).map(EntityCapabilities.EntityEffectCap::isRage).orElse(false)) {
            poseStack.scale(1.25F, 1.25F, 1.25F);
        }


        if (entity instanceof BrutalityEntityRotations rotations) {
            entity.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP).ifPresent(entityShouldRotateCap -> {
                if (entityShouldRotateCap.isShouldRotate()) {

//                System.out.println("rendering with rotation");

                    float lerpedRoll = Mth.lerp(partialTick, rotations.brutality$getBrutalityPrevRoll(), rotations.brutality$getBrutalityRoll());
                    float lerpedYaw = Mth.lerp(partialTick, rotations.brutality$getBrutalityPrevYaw(), rotations.brutality$getBrutalityYaw());
                    float lerpedPitch = Mth.lerp(partialTick, rotations.brutality$getBrutalityPrevPitch(), rotations.brutality$getBrutalityPitch());

                    float halfHeight = entity.getBbHeight() / 2.0f;

                    poseStack.translate(0, halfHeight, 0);

                    poseStack.mulPose(Axis.YP.rotationDegrees(lerpedYaw));
                    poseStack.mulPose(Axis.ZP.rotationDegrees(lerpedPitch));
                    poseStack.mulPose(Axis.XP.rotationDegrees(lerpedRoll));

                    poseStack.translate(0, -halfHeight, 0);

                }
            });
        }

    }

}
