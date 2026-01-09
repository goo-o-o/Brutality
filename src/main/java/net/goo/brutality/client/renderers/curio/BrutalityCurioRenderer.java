package net.goo.brutality.client.renderers.curio;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.client.models.BrutalityCurioModel;
import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.RenderUtils;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import java.util.function.Consumer;

public class BrutalityCurioRenderer<I extends BrutalityCurioItem> extends GeoArmorRenderer<I> implements ICurioRenderer {

    public BrutalityCurioRenderer(Consumer<BrutalityCurioRenderer<I>> layerConfigurator) {
        super(new BrutalityCurioModel<>());
        layerConfigurator.accept(this);
    }

    public BrutalityCurioRenderer() {
        super(new BrutalityCurioModel<>());
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(
            ItemStack stack,
            SlotContext slotContext,
            PoseStack poseStack,
            RenderLayerParent<T, M> renderLayerParent,
            MultiBufferSource renderTypeBuffer, int light, float limbSwing,
            float limbSwingAmount,
            float partialTicks,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {
        this.animatable = ((I) stack.getItem());
        this.currentEntity = slotContext.entity();
        this.currentStack = stack;
        this.baseModel = renderLayerParent.getModel() instanceof HumanoidModel<?> ?
                (HumanoidModel<?>) renderLayerParent.getModel() :
                new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
        poseStack.pushPose();
        this.prepForRender(slotContext.entity(), stack, EquipmentSlot.HEAD, baseModel);

        if (!animatable.translateIfSneaking()) {
            if (currentEntity.isCrouching()) {
                poseStack.translate(0.0F, -0.1875F, 0.0F);
            }
        }

        if (!animatable.followBodyRotations()) {
            float bodyYaw = Mth.lerp(partialTicks, slotContext.entity().yBodyRotO, slotContext.entity().yBodyRot);
            poseStack.mulPose(Axis.YP.rotationDegrees(-bodyYaw));
        }


        RenderType renderType = getRenderType(animatable, getTextureLocation(animatable), renderTypeBuffer, partialTicks);
        VertexConsumer buffer = ItemRenderer.getArmorFoilBuffer(renderTypeBuffer, renderType, false, stack.hasFoil());
        defaultRender(poseStack, animatable, renderTypeBuffer, renderType, buffer, 0, partialTicks, light);
        poseStack.popPose();
    }

    @Override
    protected void applyBoneVisibilityBySlot(EquipmentSlot currentSlot) {
        setAllBonesVisible(true);
    }

    @Override
    public RenderType getRenderType(I animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.itemEntityTranslucentCull(texture);
    }


    public ItemStack getCurrentItemStack() {
        return this.currentStack;
    }

    @Override
    protected void applyBaseTransformations(HumanoidModel<?> baseModel) {
        boolean isCrouching = currentEntity.isCrouching();
        boolean applyCrouch = animatable.translateIfSneaking() || !isCrouching;

        if (this.head != null) {
            ModelPart headPart = baseModel.head;
            if (animatable.followHeadRotations()) {
                RenderUtils.matchModelPartRot(headPart, this.head);
            }
            // Counteract crouching Y-offset (4.2F -> 0.0F)
            float yOffset = applyCrouch ? headPart.y : 0.0F;
            this.head.updatePosition(headPart.x, -yOffset, headPart.z);
        }

        if (this.body != null) {
            ModelPart bodyPart = baseModel.body;
            if (animatable.rotateIfSneaking()) {
                RenderUtils.matchModelPartRot(bodyPart, this.body);
            } else if (isCrouching) {
                // Counteract crouching body rotation (0.5F -> 0.0F)
                this.body.setRotX(0.0F);
            }
            // Counteract crouching Y-offset (3.2F -> 0.0F)
            float yOffset = applyCrouch ? bodyPart.y : 0.0F;
            this.body.updatePosition(bodyPart.x, -yOffset, bodyPart.z);
        }

        if (this.rightArm != null) {
            ModelPart rightArmPart = baseModel.rightArm;
            if (applyCrouch) {
                RenderUtils.matchModelPartRot(rightArmPart, this.rightArm);
            } else {
                // Counteract crouching rotation (additional 0.4F)
                this.rightArm.setRotX(rightArmPart.xRot - 0.4F);
            }
            // Counteract crouching Y-offset (5.2F -> 2.0F)
            float yOffset = applyCrouch ? rightArmPart.y : 2.0F;
            this.rightArm.updatePosition(rightArmPart.x + 5, 2 - yOffset, rightArmPart.z);
        }

        if (this.leftArm != null) {
            ModelPart leftArmPart = baseModel.leftArm;
            if (applyCrouch) {
                RenderUtils.matchModelPartRot(leftArmPart, this.leftArm);
            } else {
                // Counteract crouching rotation (additional 0.4F)
                this.leftArm.setRotX(leftArmPart.xRot - 0.4F);
            }
            // Counteract crouching Y-offset (5.2F -> 2.0F)
            float yOffset = applyCrouch ? leftArmPart.y : 2.0F;
            this.leftArm.updatePosition(leftArmPart.x - 5f, 2 - yOffset, leftArmPart.z);
        }

        if (this.rightLeg != null) {
            ModelPart rightLegPart = baseModel.rightLeg;
            RenderUtils.matchModelPartRot(rightLegPart, this.rightLeg);
            // Counteract crouching Y-offset (12.2F -> 12.0F) and Z-offset (4.0F -> 0.0F)
            float yOffset = applyCrouch ? rightLegPart.y : 12.0F;
            float zOffset = applyCrouch ? rightLegPart.z : 0.0F;
            this.rightLeg.updatePosition(rightLegPart.x + 2, 12 - yOffset, zOffset);

            if (this.rightBoot != null) {
                RenderUtils.matchModelPartRot(rightLegPart, this.rightBoot);
                this.rightBoot.updatePosition(rightLegPart.x + 2, 12 - yOffset, zOffset);
            }
        }

        if (this.leftLeg != null) {
            ModelPart leftLegPart = baseModel.leftLeg;
            RenderUtils.matchModelPartRot(leftLegPart, this.leftLeg);
            // Counteract crouching Y-offset (12.2F -> 12.0F) and Z-offset (4.0F -> 0.0F)
            float yOffset = applyCrouch ? leftLegPart.y : 12.0F;
            float zOffset = applyCrouch ? leftLegPart.z : 0.0F;
            this.leftLeg.updatePosition(leftLegPart.x - 2, 12 - yOffset, zOffset);

            if (this.leftBoot != null) {
                RenderUtils.matchModelPartRot(leftLegPart, this.leftBoot);
                this.leftBoot.updatePosition(leftLegPart.x - 2, 12 - yOffset, zOffset);
            }
        }
    }
}