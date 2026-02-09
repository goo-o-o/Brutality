package net.goo.brutality.client.renderers.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.sounds.ClientSoundManager;
import net.goo.brutality.common.block.block_entity.PedestalOfWizardryBlockEntity;
import net.goo.brutality.common.block.block_entity.TableOfWizardryBlockEntity;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.recipe.ConjureRecipe;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalityRecipes;
import net.goo.brutality.util.RenderUtils;
import net.goo.brutality.util.math.CoordinateUtils;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class TableOfWizardryRenderer implements BlockEntityRenderer<TableOfWizardryBlockEntity> {
    static Color BLUE = new Color(0.184F, 0.212F, 0.639F, 1F);
    static Color BLUE_TRANSPARENT = new Color(0.216F, 0.275F, 0.745F, 0F);
    static Color YELLOW = new Color(0.984F, 0.875F, 0.443F, 1F);
    static int[] rayColors = new int[]{BLUE.getRGB(), YELLOW.getRGB()};
    static Color GREEN = new Color(0.341F, 0.808F, 0.035F, 1F);
    static Color GREEN_TRANSPARENT = new Color(0.592F, 0.937F, 0.071F, 0F);
    public static Color RED = new Color(0.753F, 0.039F, 0.039F, 1F);
    static Color RED_TRANSPARENT = new Color(0.961F, 0.282F, 0.055F, 0F);

    public static final ResourceLocation BOOK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/entity/table_of_wizardry_book.png");

    private final BookModel bookModel;

    public TableOfWizardryRenderer(BlockEntityRendererProvider.Context pContext) {
        this.bookModel = new BookModel(pContext.bakeLayer(ModelLayers.BOOK));
    }

    public void render(TableOfWizardryBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Level level = pBlockEntity.getLevel();
        if (level == null) return;

        BlockPos blockEntityPos = pBlockEntity.getBlockPos();
        int lightAbove = LevelRenderer.getLightColor(level, blockEntityPos.above(1));
        float smoothProgressTicks = Mth.lerp(pPartialTick, (float) pBlockEntity.prevProgress, (float) pBlockEntity.progress);
        float normalizedProgress = smoothProgressTicks / (float) pBlockEntity.maxProgress;
        float gameTime = (float) pBlockEntity.progress + pPartialTick;
        Optional<ConjureRecipe> recipeOpt = level.getRecipeManager().getAllRecipesFor(BrutalityRecipes.CONJURE_TYPE.get()).stream()
                .filter(r -> r.spell() == pBlockEntity.currentSpell)
                .findFirst();

        List<Pair<ItemStack, Vec3>> items = pBlockEntity.getPedestalItemsWithOffset();
        // --- 1. Render the Pedestal Gradients (Markers) ---
        // We loop through the constant offsets directly so they show even if empty
        if (pBlockEntity.currentSpell == null) {
            for (Vec3 offset : TableOfWizardryBlockEntity.PEDESTAL_OFFSETS) {
                pPoseStack.pushPose();
                pPoseStack.translate(offset.x, offset.y, offset.z);
                VertexConsumer consumer = pBuffer.getBuffer(RenderType.debugQuads());
                // Blue marker for potential pedestal locations
                RenderUtils.renderBlockHorizontalGradientSidesLocal(pPoseStack, consumer, 0.5F, 0.49F, 0.49F, BLUE, BLUE_TRANSPARENT);
                pPoseStack.popPose();
            }
        } else {

            if (recipeOpt.isPresent()) {
                List<Vec3> pedestalOffsets = TableOfWizardryBlockEntity.PEDESTAL_OFFSETS;
                ConjureRecipe recipe = recipeOpt.get();
                for (int i = 0, pedestalOffsetsSize = pedestalOffsets.size(); i < pedestalOffsetsSize; i++) {
                    Vec3 offset = pedestalOffsets.get(i);
                    Ingredient ing = (i < recipe.ingredients().size()) ? recipe.ingredients().get(i) : Ingredient.EMPTY;
                    if (ing.isEmpty()) continue;
                    BlockEntity blockEntityAtPedestalLoc = level.getBlockEntity(pBlockEntity.getBlockPos().offset(CoordinateUtils.toVector3i(offset)));
                    ItemStack pedestalStack = ItemStack.EMPTY;
                    pPoseStack.pushPose();
                    pPoseStack.translate(offset.x, offset.y, offset.z);

                    if (blockEntityAtPedestalLoc instanceof PedestalOfWizardryBlockEntity pedestal) {
                        pedestalStack = pedestal.getStoredItem();

                        if (pedestalStack.isEmpty()) {
                            ItemStack required = ing.getItems()[0];
                            pPoseStack.pushPose();
                            pPoseStack.translate(0.5, 1 + (required.getItem() instanceof BlockItem ? 0 : 0.15), 0.5);
                            MultiBufferSource ghostBuffer = RenderUtils.makeGhostBuffer(pBuffer, 0.25f);
                            RenderUtils.renderItemInWorld(required, pPoseStack, ghostBuffer, pPartialTick, pPackedOverlay, lightAbove);
                            pPoseStack.popPose();
                        }
                    }
                    boolean match = ing.test(pedestalStack);


                    VertexConsumer consumer = pBuffer.getBuffer(RenderType.debugQuads());
                    if (match)
                        RenderUtils.renderBlockHorizontalGradientSidesLocal(pPoseStack, consumer, 0.5F, 0.49F, 0.49F, GREEN, GREEN_TRANSPARENT);
                    else
                        RenderUtils.renderBlockHorizontalGradientSidesLocal(pPoseStack, consumer, 0.5F, 0.49F, 0.49F, RED, RED_TRANSPARENT);
                    pPoseStack.popPose();

                }
            }
        }
        ClientSoundManager.handleTableOfWizardrySound(pBlockEntity);

        // --- 2. Render Animated Items (If Crafting) ---
        if (pBlockEntity.isCrafting && normalizedProgress < 0.65F) {
            for (int i = 0; i < items.size(); i++) {
                Pair<ItemStack, Vec3> pair = items.get(i);
                Vec3 pedestalOffset = pair.getRight();

                Vec3 itemOffset = TableOfWizardryBlockEntity.getConjureItemOffset(pedestalOffset, normalizedProgress, items.size(), i);
                pPoseStack.pushPose();
                // Translate to the dynamic conjure position
                pPoseStack.translate(0.5 + itemOffset.x, 1 + (pair.getLeft().getItem() instanceof BlockItem ? 0 : 0.15) + itemOffset.y, 0.5 + itemOffset.z);

                RenderUtils.renderItemInWorld(pair.getLeft(), pPoseStack, pBuffer, pPartialTick, pPackedOverlay, lightAbove);
                pPoseStack.popPose();

                // Particles
                if (level.random.nextFloat() < 0.075f) {
                    level.addParticle(BrutalityParticles.WIZARDRY_PARTICLE.get(),
                            blockEntityPos.getX() + 0.5 + itemOffset.x,
                            blockEntityPos.getY() + 1.15 + itemOffset.y,
                            blockEntityPos.getZ() + 0.5 + itemOffset.z,
                            0, 0.02, 0);
                }
            }
        }

        if (pBlockEntity.isCrafting && normalizedProgress > 0.65) {

            if (recipeOpt.isPresent()) {

                ItemStack resultStack = recipeOpt.get().getResultItem(level.registryAccess());

                if (!resultStack.isEmpty()) {
                    pPoseStack.pushPose();
                    // Hover at the center merge point
                    pPoseStack.translate(0.5, 1 + 2.5, 0.5);

                    pPoseStack.mulPose(Axis.YP.rotationDegrees(gameTime * 2.0f));
                    pPoseStack.pushPose();
                    pPoseStack.translate(0, 0.15, 0);

                    IBrutalitySpell.MagicSchool school = pBlockEntity.currentSpell.getSchool();

                    float deathRayProgress = Mth.clamp((normalizedProgress - 0.65F) / 0.35F, 0.0F, 1.0F);
                    int totalBeams = 45;
                    if (school.colors.length > 0) {
                        int perColor = totalBeams / school.colors.length;
                        for (int i = 0; i < school.colors.length; i++) {
                            RenderUtils.renderDragonDeathRays(deathRayProgress, pPoseStack, pBuffer, 0.5F, 0.25F, perColor, school.colors[i], 432L + i);
                        }
                    } else {
                        int perColor = totalBeams / rayColors.length;
                        for (int i = 0; i < rayColors.length; i++) {
                            RenderUtils.renderDragonDeathRays(deathRayProgress, pPoseStack, pBuffer, 0.5F, 0.25F, perColor, rayColors[i], 432L + i);
                        }
                    }


                    pPoseStack.popPose();
                    RenderUtils.renderItemInWorld(resultStack, pPoseStack, pBuffer, pPartialTick, pPackedOverlay, lightAbove);
                    pPoseStack.popPose();


                }
            }
        }

        pPoseStack.pushPose();
        pPoseStack.translate(0.5, 1.1, 0.5); // 1.1 height to sit on the table

        float f = (float) pBlockEntity.time + pPartialTick;
        pPoseStack.translate(0.0F, 0.1F + Mth.sin(f * 0.1F) * 0.01F, 0.0F);

        // Rotation normalization
        float f1 = pBlockEntity.rot - pBlockEntity.oRot;
        while (f1 >= (float) Math.PI) f1 -= ((float) Math.PI * 2F);
        while (f1 < -(float) Math.PI) f1 += ((float) Math.PI * 2F);
        float f2 = pBlockEntity.oRot + f1 * pPartialTick;

        pPoseStack.mulPose(Axis.YP.rotation(-f2));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(80.0F));

        // Animation setup
        float f3 = Mth.lerp(pPartialTick, pBlockEntity.oFlip, pBlockEntity.flip);
        float f4 = Mth.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = Mth.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = Mth.lerp(pPartialTick, pBlockEntity.oOpen, pBlockEntity.open);
        this.bookModel.setupAnim(f, Mth.clamp(f4, 0.0F, 1.0F), Mth.clamp(f5, 0.0F, 1.0F), f6);

        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityCutoutNoCull(BOOK_TEXTURE));
        this.bookModel.render(pPoseStack, vertexconsumer, pPackedLight, pPackedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        pPoseStack.popPose(); // End Book
    }
}