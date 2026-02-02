package net.goo.brutality.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.block.block_entity.WhiteFilingCabinetBlockEntity;
import net.goo.brutality.common.block.custom.WhiteFilingCabinetBlock;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FilingCabinetBlockEntityRenderer<T extends WhiteFilingCabinetBlockEntity> implements BlockEntityRenderer<T> {
    public static final ModelLayerLocation FILING_CABINET_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "filing_cabinet"), "main");
    private static final String CABINET = "cabinet";
    private static final String UPPER_DRAWER = "upper_drawer";
    private static final String LOWER_DRAWER = "lower_drawer";
    private final ModelPart cabinet;
    private final ModelPart upperDrawer;
    private final ModelPart lowerDrawer;
    private static final Map<Block, Material> MATERIAL_MAP = new HashMap<>();

    static {
        BrutalityBlocks.FILING_CABINETS.forEach(blockRegistryObject ->
                MATERIAL_MAP.put(blockRegistryObject.get(), new Material(
                        InventoryMenu.BLOCK_ATLAS,
                        ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "block/" + blockRegistryObject.getId().getPath())
                )));
    }


    public FilingCabinetBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(FILING_CABINET_LAYER);
        this.cabinet = modelPart.getChild(CABINET);
        this.upperDrawer = modelPart.getChild(UPPER_DRAWER);
        this.lowerDrawer = modelPart.getChild(LOWER_DRAWER);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        // Cabinet (static part)
        partDefinition.addOrReplaceChild(
                CABINET,
                CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
                PartPose.offsetAndRotation(16, 16, 0, 0, 0, (float) Math.PI)
        );

        // Upper drawer (y=9 to 15, z=0 to 8)
        partDefinition.addOrReplaceChild(
                UPPER_DRAWER,
                CubeListBuilder.create().texOffs(0, 32).addBox(1.0F, 9.0F, 7.9F, 14.0F, 6.0F, 8.0F),
                PartPose.offsetAndRotation(16, 24, 0, 0, 0, (float) Math.PI)
        );

        // Lower drawer (y=1 to 7, z=0 to 8)
        partDefinition.addOrReplaceChild(
                LOWER_DRAWER,
                CubeListBuilder.create().texOffs(0, 32).addBox(1.0F, 1.0F, 7.9F, 14.0F, 6.0F, 8.0F),
                PartPose.offsetAndRotation(16, 8, 0, 0, 0, (float) Math.PI)
        );

        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void render(WhiteFilingCabinetBlockEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.getValue(WhiteFilingCabinetBlock.FACING);
        float upperOpenness = blockEntity.getOpenness(true, partialTicks);
        float lowerOpenness = blockEntity.getOpenness(false, partialTicks);

        poseStack.pushPose();

        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-facing.toYRot()));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

        Material material = MATERIAL_MAP.getOrDefault(state.getBlock(), MATERIAL_MAP.get(BrutalityBlocks.WHITE_FILING_CABINET.get()));

        VertexConsumer vertexConsumer = material
                .buffer(buffer, RenderType::entityCutout);

        this.cabinet.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        this.upperDrawer.z = upperOpenness * 8.0F; // (0.5 blocks)
        this.upperDrawer.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        this.lowerDrawer.z = lowerOpenness * 8.0F; // (0.5 blocks)
        this.lowerDrawer.render(poseStack, vertexConsumer, packedLight, packedOverlay);

        poseStack.popPose();

    }
}