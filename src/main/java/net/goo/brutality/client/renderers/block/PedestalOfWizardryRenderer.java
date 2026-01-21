package net.goo.brutality.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.goo.brutality.block.block_entity.PedestalOfWizardryBlockEntity;
import net.goo.brutality.block.custom.PedestalOfWizardryBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class PedestalOfWizardryRenderer implements BlockEntityRenderer<PedestalOfWizardryBlockEntity> {

    public PedestalOfWizardryRenderer(BlockEntityRendererProvider.Context pContext) {
    }


    // From BetterEnd https://github.com/paulevsGitch/BetterEnd/blob/a1ad58cf5462fcb7b3e26eda5300c25e8f5603c5/src/main/java/ru/betterend/client/render/PedestalItemRenderer.java#L26
    @Override
    public void render(PedestalOfWizardryBlockEntity blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource vertexConsumers, int light, int overlay) {
        Level world = blockEntity.getLevel();
        if (world == null || blockEntity.getStoredItem().isEmpty()) return;

        BlockState state = world.getBlockState(blockEntity.getBlockPos());
        if (!(state.getBlock() instanceof PedestalOfWizardryBlock)) return;

        ItemStack activeItem = blockEntity.getStoredItem();

        poseStack.pushPose();
        Minecraft minecraft = Minecraft.getInstance();
        BakedModel model = minecraft.getItemRenderer().getModel(activeItem, world, null, 0);
        Vector3f translate = model.getTransforms().ground.translation;

        if (activeItem.getItem() instanceof BlockItem) {
            poseStack.translate(translate.x() + 0.5, translate.y() + 1, translate.z() + 0.5);
        } else {
            poseStack.translate(translate.x() + 0.5, translate.y() + 1.15, translate.z() + 0.5);
        }
        poseStack.scale(1.25F, 1.25F, 1.25F);

        int age = 0;
        if (minecraft.level != null) {
            age = (int) (minecraft.level.getGameTime() % 314);
        }
        int lightAbove = LevelRenderer.getLightColor(world, blockEntity.getBlockPos().above());

        float rotation = (age + tickDelta) / 25.0F + 6.0F;
        poseStack.mulPose(Axis.YP.rotation(rotation));
        minecraft.getItemRenderer().render(activeItem, ItemDisplayContext.GROUND, false, poseStack, vertexConsumers, lightAbove, overlay, model);

        poseStack.popPose();
    }
}