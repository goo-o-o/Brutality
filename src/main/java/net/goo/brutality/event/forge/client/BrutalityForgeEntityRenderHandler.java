package net.goo.brutality.event.forge.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.entity.spells.IBrutalitySpellEntity;
import net.goo.brutality.common.item.armor.BrutalityArmorMaterials;
import net.goo.brutality.common.item.weapon.RotatingAttackWeapon;
import net.goo.brutality.common.item.weapon.generic.LastPrism;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.BrutalityEntityRotations;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import software.bernie.geckolib.event.GeoRenderEvent;
import top.theillusivec4.curios.api.CuriosApi;


@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class BrutalityForgeEntityRenderHandler extends RenderStateShard {


    public BrutalityForgeEntityRenderHandler(String pName, Runnable pSetupState, Runnable pClearState) {
        super(pName, pSetupState, pClearState);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        GuiGraphics gui = event.getGuiGraphics();
        if (mc.player == null || level == null) return;

        CuriosApi.getCuriosInventory(mc.player).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.SANGUINE_SPECTACLES.get())) {
                TRANSLUCENT_TRANSPARENCY.setupRenderState();
                RenderSystem.setShaderColor(1f, 1f, 1f, 0.3f);
                mc.gui.renderTextureOverlay(gui, ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/overlay/sanguine_spectacles.png"), 1.0F);
                TRANSLUCENT_TRANSPARENCY.clearRenderState();
            }

        });

    }

    @SubscribeEvent
    public static void onRenderSpell(GeoRenderEvent.Entity.Pre event) {
        if (event.getEntity() instanceof IBrutalitySpellEntity entity) {
            int spellLevel = entity.getSpellLevel();
            if (spellLevel < 1) return;

            float scaleIncrement = entity.getSizeScaling();
            if (scaleIncrement == 0) return;
            float finalScale = 1.0F + (spellLevel - 1) * scaleIncrement; // Scale factor: 1.0 at level 1, 1.5 at level 2, 2.0 at level 3, etc.

            BrutalitySpell spell = entity.getSpell();
            SpellTooltipRenderer.SpellStatComponent sizeStat = spell.getStat(SpellTooltipRenderer.SpellStatComponentType.SIZE);
            if (sizeStat == null) return;
//            finalScale = Mth.clamp(finalScale, 1.0F, sizeStat.max() / 2F); // Ensure scale doesn't exceed max range relative to base size
            PoseStack poseStack = event.getPoseStack();
            poseStack.popPose();
            poseStack.scale(finalScale, finalScale, finalScale);
            poseStack.pushPose();
        }
    }


    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            if (player.getMainHandItem().getItem() instanceof LastPrism) {
                if (event.getHand() == InteractionHand.OFF_HAND)
                    event.setCanceled(true);
            }
            if (player.isHolding(stack -> stack.getItem() instanceof RotatingAttackWeapon)) {
                if (player.isUsingItem()) event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderNametag(RenderNameTagEvent event) {
        if (event.getEntity() instanceof Player player)
            if (ModUtils.hasFullArmorSet(player, BrutalityArmorMaterials.NOIR)) {
                event.setResult(Event.Result.DENY);
            }

    }

    @SubscribeEvent
    public static <T extends LivingEntity, M extends EntityModel<T>> void postRender(RenderLivingEvent.Post<T, M> event) {
        CuriosApi.getCuriosInventory(event.getEntity()).ifPresent(handler -> {
            if (handler.isEquipped(BrutalityItems.CENSORED.get())) {
                LivingEntity entity = event.getEntity();
                PoseStack poseStack = event.getPoseStack();
                Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;

                Vec3 camPos = camera.getPosition();
                Vec3 entPos = entity.getPosition(event.getPartialTick()).add(0, entity.getBbHeight() * 0.5, 0);

                double dx = entPos.x - camPos.x;
                double dy = entPos.y - camPos.y;
                double dz = entPos.z - camPos.z;
                double horizontalDistance = Math.sqrt(dx * dx + dz * dz);

                // 3. Calculate Angles (Radiant to Degrees)
                float yaw = (float) (Math.atan2(dx, dz) * (180 / Math.PI));
                float pitch = (float) (Math.atan2(dy, horizontalDistance) * (180 / Math.PI));

                poseStack.pushPose();

                poseStack.translate(0, entity.getBbHeight() * 0.5f, 0);


                poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
                poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

                // Determine depth based on the vertical angle (pitch)
                float lookDownFactor = Math.abs(pitch) / 90.0f;
                float bbWidth = entity.getBbWidth();
                float bbHeight = entity.getBbHeight();

                float depthOffset = Mth.lerp(lookDownFactor, bbWidth * 0.5f, bbHeight * 0.5f);

                // Translate forward toward camera (Negative Z in local rotated space)
                poseStack.translate(0, 0, -(depthOffset + 0.35f));

                // 7. Dynamic Scaling
                float dynamicHeight = Mth.lerp(lookDownFactor, bbHeight, bbWidth);
                poseStack.scale(bbWidth * 1.5f, dynamicHeight * 1.5f, 1.0f);

                // 8. Render
                VertexConsumer builder = event.getMultiBufferSource().getBuffer(RenderType.debugQuads());
                renderVoidQuad(builder, poseStack.last().pose(), 0.5f);

                poseStack.popPose();
            }
        });
    }

    private static void renderVoidQuad(VertexConsumer builder, Matrix4f matrix, float s) {
        builder.vertex(matrix, -s, -s, 0).color(0, 0, 0, 255).uv(0, 0).endVertex();
        builder.vertex(matrix, -s,  s, 0).color(0, 0, 0, 255).uv(0, 1).endVertex();
        builder.vertex(matrix,  s,  s, 0).color(0, 0, 0, 255).uv(1, 1).endVertex();
        builder.vertex(matrix,  s, -s, 0).color(0, 0, 0, 255).uv(1, 0).endVertex();
    }


    @SubscribeEvent
    public static <T extends LivingEntity, M extends EntityModel<T>> void preRender(RenderLivingEvent.Pre<T, M> event) {
        LivingEntity entity = event.getEntity();
        ItemStack useItem = entity.getUseItem();
        if (!useItem.isEmpty() && useItem.getItem() instanceof
                RotatingAttackWeapon weapon && entity.isUsingItem()) {
            if (useItem.is(BrutalityItems.MAX.get()) || useItem.is(BrutalityItems.THE_SILVER_PERIMETER.get())) {
                RotatingAttackWeapon.handleRenderEvent(event, weapon);
            }
        } else {
            RotatingAttackWeapon.SPIN_ANCHORS.remove(entity);
        }

        if (entity instanceof
                BrutalityEntityRotations rotations) {
            entity.getCapability(BrutalityCapabilities.SHOULD_ROTATE).ifPresent(entityShouldRotateCap -> {
                if (entityShouldRotateCap.isShouldRotate()) {

                    float lerpedRoll = Mth.lerp(event.getPartialTick(), rotations.brutality$getBrutalityPrevRoll(), rotations.brutality$getBrutalityRoll());
                    float lerpedYaw = Mth.lerp(event.getPartialTick(), rotations.brutality$getBrutalityPrevYaw(), rotations.brutality$getBrutalityYaw());
                    float lerpedPitch = Mth.lerp(event.getPartialTick(), rotations.brutality$getBrutalityPrevPitch(), rotations.brutality$getBrutalityPitch());

                    float halfHeight = entity.getBbHeight() / 2.0f;

                    event.getPoseStack().translate(0, halfHeight, 0);

                    event.getPoseStack().mulPose(Axis.YP.rotationDegrees(lerpedYaw));
                    event.getPoseStack().mulPose(Axis.ZP.rotationDegrees(lerpedPitch));
                    event.getPoseStack().mulPose(Axis.XP.rotationDegrees(lerpedRoll));

                    event.getPoseStack().translate(0, -halfHeight, 0);

                }
            });
        }

    }
}