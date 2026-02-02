package net.goo.brutality.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.common.registry.BrutalityBlockEntities;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.HashMap;
import java.util.Map;

public class BrutalityBlockEntityWithoutLevelRenderer extends BlockEntityWithoutLevelRenderer {
    private static BrutalityBlockEntityWithoutLevelRenderer INSTANCE = null;
    private final Map<Block, BlockEntity> blockEntityMap = new HashMap<>();
    private final BlockEntityRenderDispatcher dispatcher;

    private BrutalityBlockEntityWithoutLevelRenderer(BlockEntityRendererProvider.Context context) {
        super(context.getBlockEntityRenderDispatcher(), context.getModelSet());
        this.dispatcher = context.getBlockEntityRenderDispatcher();
        registerBlockEntity(BrutalityBlocks.WHITE_FILING_CABINET.get(), BrutalityBlockEntities.WHITE_FILING_CABINET_BLOCK_ENTITY.get());
        registerBlockEntity(BrutalityBlocks.LIGHT_GRAY_FILING_CABINET.get(), BrutalityBlockEntities.LIGHT_GRAY_FILING_CABINET_BLOCK_ENTITY.get());
        registerBlockEntity(BrutalityBlocks.GRAY_FILING_CABINET.get(), BrutalityBlockEntities.GRAY_FILING_CABINET_BLOCK_ENTITY.get());
    }

    private void registerBlockEntity(Block block, BlockEntityType<?> blockEntityType) {
        BlockEntity blockEntity = blockEntityType.create(BlockPos.ZERO, block.defaultBlockState());
        if (blockEntity != null) {
            blockEntityMap.put(block, blockEntity);
        }
    }

    public static BrutalityBlockEntityWithoutLevelRenderer getInstance(BlockEntityRendererProvider.Context context) {
        if (INSTANCE == null) {
            INSTANCE = new BrutalityBlockEntityWithoutLevelRenderer(context);
        }
        return INSTANCE;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Block block = Block.byItem(stack.getItem());
        BlockEntity blockEntity = blockEntityMap.get(block);
        if (blockEntity != null) {
            this.dispatcher.renderItem(blockEntity, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}