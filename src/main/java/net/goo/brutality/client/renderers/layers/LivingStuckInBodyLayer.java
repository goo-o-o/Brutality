package net.goo.brutality.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.client.models.IRandomModelPart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

// Thanks to Pierce Arrows mod
public abstract class LivingStuckInBodyLayer <T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {
	public LivingStuckInBodyLayer(LivingEntityRenderer<T, M> livingEntityRenderer) {
		super(livingEntityRenderer);
	}

	protected abstract int numStuck(T t);

	protected abstract void renderStuckItem(PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, Entity entity, float pX, float pY, float pZ, float pPartialTick);

	public void render(@NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight, @NotNull T pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		if(this.getParentModel() instanceof IRandomModelPart) {
			int i = this.numStuck(pLivingEntity);
			RandomSource randomsource = RandomSource.create(pLivingEntity.getId());
			if (i > 0) {
				for (int j = 0; j < i; ++j) {
					pPoseStack.pushPose();
					ModelPart modelpart = ((IRandomModelPart) this.getParentModel()).brutality$getRandomModelPart(randomsource);
					ModelPart.Cube modelpart$cube = modelpart.getRandomCube(randomsource);
					modelpart.translateAndRotate(pPoseStack);
					float f = randomsource.nextFloat();
					float f1 = randomsource.nextFloat();
					float f2 = randomsource.nextFloat();
					float f3 = Mth.lerp(f, modelpart$cube.minX, modelpart$cube.maxX) / 16.0F;
					float f4 = Mth.lerp(f1, modelpart$cube.minY, modelpart$cube.maxY) / 16.0F;
					float f5 = Mth.lerp(f2, modelpart$cube.minZ, modelpart$cube.maxZ) / 16.0F;
					pPoseStack.translate(f3, f4, (double) f5);
					f = -1.0F * (f * 2.0F - 1.0F);
					f1 = -1.0F * (f1 * 2.0F - 1.0F);
					f2 = -1.0F * (f2 * 2.0F - 1.0F);
					this.renderStuckItem(pPoseStack, pBuffer, pPackedLight, pLivingEntity, f, f1, f2, pPartialTick);
					pPoseStack.popPose();
				}

			}
		}
	}
}