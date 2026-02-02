package net.goo.brutality.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.common.block.block_entity.PedestalOfWizardryBlockEntity;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.block.custom.PedestalOfWizardryBlock;
import net.goo.brutality.util.RenderUtils;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PedestalOfWizardryRenderer implements BlockEntityRenderer<PedestalOfWizardryBlockEntity> {

    public PedestalOfWizardryRenderer(BlockEntityRendererProvider.Context pContext) {
    }


    // From BetterEnd https://github.com/paulevsGitch/BetterEnd/blob/a1ad58cf5462fcb7b3e26eda5300c25e8f5603c5/src/main/java/ru/betterend/client/render/PedestalItemRenderer.java#L26
    @Override
    public void render(PedestalOfWizardryBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource vertexConsumers, int light, int overlay) {
        Level level = blockEntity.getLevel();
        if (level == null || blockEntity.getStoredItem().isEmpty()) return;

        BlockState state = level.getBlockState(blockEntity.getBlockPos());
        if (!(state.getBlock() instanceof PedestalOfWizardryBlock)) return;

        if (blockEntity.getTableOfWizardryPosition() != null &&
                level.getBlockEntity(blockEntity.getTableOfWizardryPosition()) instanceof TableOfWizardryBlockEntity tableOfWizardryBlockEntity) {
            if (tableOfWizardryBlockEntity.isCrafting) return;

        }

        ItemStack activeItem = blockEntity.getStoredItem();
        int lightAbove = LevelRenderer.getLightColor(level, blockEntity.getBlockPos().above());

        poseStack.pushPose();
        poseStack.translate(0.5, 1 + (activeItem.getItem() instanceof BlockItem ? 0 : 0.15), 0.5);
        RenderUtils.renderItemInWorld(activeItem, poseStack, vertexConsumers, partialTick, overlay, lightAbove);
        poseStack.popPose();
    }
}