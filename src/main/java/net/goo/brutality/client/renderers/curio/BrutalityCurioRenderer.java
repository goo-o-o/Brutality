package net.goo.brutality.client.renderers.curio;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.client.models.BrutalityCurioModel;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
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

        if (stack.getItem() instanceof BrutalityCurioItem curioItem) {
            if (!curioItem.followBodyRotations()) {
                float bodyYaw = Mth.lerp(partialTicks, slotContext.entity().yBodyRotO, slotContext.entity().yBodyRot);
                poseStack.mulPose(Axis.YP.rotationDegrees(-bodyYaw));
            }
        }

        this.prepForRender(slotContext.entity(), stack, EquipmentSlot.HEAD, (HumanoidModel<?>) renderLayerParent.getModel());
        VertexConsumer consumer = renderTypeBuffer.getBuffer(RenderType.armorCutoutNoCull(this.getTextureLocation((I) stack.getItem())));
        this.renderToBuffer(poseStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }


    public ItemStack getCurrentItemStack() {
        return this.currentStack;
    }
}