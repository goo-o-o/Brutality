package net.goo.brutality.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.Brutality;
import net.goo.brutality.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.util.RenderUtils;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public class TableOfWizardryRenderer implements BlockEntityRenderer<TableOfWizardryBlockEntity> {
    Color BLUE = new Color(0.043F, 0.0275F, 0.745F, 1F);
    Color TRANSPARENT = new Color(0.047F, 0.435F, 0.843F,0F);

    public static final ResourceLocation BOOK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/entity/table_of_wizardry_book.png");

    private final BookModel bookModel;

    public TableOfWizardryRenderer(BlockEntityRendererProvider.Context pContext) {
        this.bookModel = new BookModel(pContext.bakeLayer(ModelLayers.BOOK));
    }

    public void render(TableOfWizardryBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        pPoseStack.pushPose(); // [A-Outline] Save the initial, correct world-space pose
        BlockPos blockEntityPos = pBlockEntity.getBlockPos();
        VertexConsumer consumer = pBuffer.getBuffer(RenderType.debugQuads()); // Use a reliable RenderType

        for (BlockPos relative : TableOfWizardryBlockEntity.PEDESTAL_OFFSETS) {
            BlockPos target = pBlockEntity.getBlockPos().offset(relative);

            pPoseStack.pushPose(); // [A-Target] Save state before pedestal translation

            // Calculate the vector needed to move from the current block entity's origin
            // to the target pedestal's origin.
            int dx = target.getX() - blockEntityPos.getX();
            int dy = target.getY() - blockEntityPos.getY();
            int dz = target.getZ() - blockEntityPos.getZ();

            // Apply the translation
            pPoseStack.translate(dx, dy, dz);

            // Call the new local rendering method
            RenderUtils.renderBlockHorizontalGradientSidesLocal(
                    pPoseStack,
                    consumer,
                    0.5F, 0.49F, 0.49F,
                    BLUE,
                    TRANSPARENT
            );

            pPoseStack.popPose(); // [A-Target] Restore the pose to the original block entity origin
        }

        pPoseStack.popPose(); // [A-Outline] Restore the initial pose
        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 0, 0.5); // move to center

        // start book layer
        pPoseStack.pushPose();
        float f = (float) pBlockEntity.time + pPartialTick;
        pPoseStack.translate(0.0F, 1.1F + Mth.sin(f * 0.1F) * 0.01F, 0.0F);

        // Rotation normalization logic (which is fine)
        float f1;
        for (f1 = pBlockEntity.rot - pBlockEntity.oRot; f1 >= (float) Math.PI; f1 -= ((float) Math.PI * 2F)) {
        }
        while (f1 < -(float) Math.PI) {
            f1 += ((float) Math.PI * 2F);
        }
        float f2 = pBlockEntity.oRot + f1 * pPartialTick;

        // THESE ROTATIONS ONLY APPLY TO THE BOOK
        pPoseStack.mulPose(Axis.YP.rotation(-f2));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));

        // [Book Animation Setup]
        float f3 = Mth.lerp(pPartialTick, pBlockEntity.oFlip, pBlockEntity.flip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(pPartialTick, pBlockEntity.oOpen, pBlockEntity.open);
        this.bookModel.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);

        // [Book Rendering]
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(BOOK_TEXTURE));
        this.bookModel.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose();
        pPoseStack.popPose();

    }

}