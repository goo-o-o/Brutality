package net.goo.brutality.mixin;

import com.google.common.collect.ImmutableList;
import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;
// Thanks to PierceArrow mod

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin implements net.goo.brutality.client.models.IRandomModelPart {

	@Unique
	private List<ModelPart> brutality$parts;

	@Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V", at = @At("TAIL"))
	public void onConstructor(ModelPart p_170679_, Function<ResourceLocation, RenderType> p_170680_, CallbackInfo callbackInfo) {
		this.brutality$parts = p_170679_.getAllParts().filter((p_170824_) -> !p_170824_.isEmpty()).collect(ImmutableList.toImmutableList());
	}
	@Override
	public ModelPart brutality$getRandomModelPart(RandomSource paramRandom) {
		return this.brutality$parts.get(paramRandom.nextInt(this.brutality$parts.size()));
	}

	@Unique
	boolean brutality$hasNoirItemMain, brutality$hasNoirItemOff, brutality$mainRight;

	@Final
	@Shadow
	public ModelPart rightArm;
	@Final
	@Shadow
	public ModelPart leftArm;

	@Inject(method = "setupAnim*", at = @At(value = "FIELD",
			target = "Lnet/minecraft/client/model/geom/ModelPart;zRot:F",
			ordinal = 1,
			shift = At.Shift.AFTER))
	private void setAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
		if (!(livingEntity instanceof Player player)) return;
		if (player.isCrouching()) return;

		brutality$mainRight = player.getMainArm() == HumanoidArm.RIGHT;

		brutality$hasNoirItemMain = player.getMainHandItem().is(BrutalityModItems.CANOPY_OF_SHADOWS.get());
		brutality$hasNoirItemOff = player.getOffhandItem().is(BrutalityModItems.CANOPY_OF_SHADOWS.get());

		float xRotOffset = -1.2F;

		if (brutality$hasNoirItemMain && brutality$hasNoirItemOff) {
			this.leftArm.xRot = xRotOffset;
			this.rightArm.xRot = xRotOffset;

			this.leftArm.yRot = 0.5F;
			this.rightArm.yRot = -0.5F;

			this.leftArm.zRot = 0.1F;
			this.rightArm.zRot = -0.1F;
		} else if (brutality$hasNoirItemMain) {
			if (brutality$mainRight) {
				this.rightArm.xRot = xRotOffset;
				this.leftArm.xRot = 0;

			} else {
				this.leftArm.xRot = xRotOffset;
				this.rightArm.xRot = 0;

			}
			this.leftArm.yRot = 0.5F;
			this.rightArm.yRot = -0.5F;
			this.leftArm.zRot = 0.2F;
			this.rightArm.zRot = -0.2F;
		} else if (brutality$hasNoirItemOff) {
			if (brutality$mainRight) {
				this.leftArm.xRot = xRotOffset;
				this.rightArm.xRot = 0;

			} else {
				this.rightArm.xRot = xRotOffset;
				this.leftArm.xRot = 0;

			}
			this.leftArm.yRot = 0.5F;
			this.rightArm.yRot = -0.5F;
			this.leftArm.zRot = 0.2F;
			this.rightArm.zRot = -0.2F;
		} else if (player.getInventory().getArmor(2).getItem() == BrutalityModItems.NOIR_CHESTPLATE.get()) {
			this.rightArm.xRot = 0;
			this.leftArm.xRot = 0;
			this.rightArm.zRot = 0;
			this.leftArm.zRot = 0;
		}

		if (player.getMainHandItem().is(BrutalityModItems.PANDORAS_CAULDRON.get())) {
			this.leftArm.xRot = -(float)Math.PI;
			this.rightArm.xRot = (float) (-300 * Math.PI / 180);
		} else if (player.getOffhandItem().is(BrutalityModItems.PANDORAS_CAULDRON.get())) {
			this.rightArm.xRot = -(float)Math.PI;
			this.leftArm.xRot = (float) (-300 * Math.PI / 180);
		}

		if (player.getMainHandItem().is(BrutalityModItems.STYROFOAM_CUP.get()) || player.getMainHandItem().is(BrutalityModItems.MUG.get())) {
			this.rightArm.xRot = (float) (-90 * Math.PI / 180);
		} else if (player.getOffhandItem().is(BrutalityModItems.STYROFOAM_CUP.get()) || player.getMainHandItem().is(BrutalityModItems.MUG.get())) {
			this.leftArm.xRot = (float) (-90 * Math.PI / 180);
		}


	}


	@Redirect(
			method = "setupAnim*",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V"
			),
			expect = 2,
			require = 2
	)
	private void conditionalBobModelPart(ModelPart arm, float ageInTicks, float direction, LivingEntity entity) {
		if (entity instanceof Player player) {
			// Skip bobbing if wearing Noir Chestplate
			if (player.getInventory().getArmor(2).getItem() == BrutalityModItems.NOIR_CHESTPLATE.get()) {
				return;
			}

			// Skip bobbing if holding Pandora's Cauldron
			if (player.isHolding(BrutalityModItems.PANDORAS_CAULDRON.get())) {
				return;
			}

			boolean isRightArm = arm == (player.getMainArm() == HumanoidArm.RIGHT ? this.rightArm : this.leftArm);
			boolean isPositiveDirection = direction > 0;

			// Styrofoam Cup logic
			if (player.getMainHandItem().is(BrutalityModItems.STYROFOAM_CUP.get()) || player.getMainHandItem().is(BrutalityModItems.MUG.get())) {
				// Only bob the opposite arm (positive direction is typically right arm)
				if (isPositiveDirection != isRightArm) return;
			}
			if (player.getOffhandItem().is(BrutalityModItems.STYROFOAM_CUP.get()) || player.getMainHandItem().is(BrutalityModItems.MUG.get())) {
				// Only bob the opposite arm
				if (isPositiveDirection == isRightArm) return;
			}
		}

		// Default behavior if none of the conditions are met
		AnimationUtils.bobModelPart(arm, ageInTicks, direction);
	}

	@Inject(
			method = "setupAnim*",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/model/HumanoidModel;crouching:Z",
					shift = At.Shift.AFTER
			)
	)
	private void modifyCrouchArmRotations(LivingEntity livingEntity, float limbSwing, float limbSwingAmount,
										  float ageInTicks, float netHeadYaw, float headPitch,
										  CallbackInfo ci) {
		if (!(livingEntity instanceof Player player)) return;


		if (player.isCrouching()) {
			if (player.isHolding(BrutalityModItems.PANDORAS_CAULDRON.get())) {
				this.leftArm.xRot -= 2F;
				this.rightArm.xRot -= 2F;
			}

			boolean wearingNoirChestplate = player.getInventory().getArmor(2).getItem() == BrutalityModItems.NOIR_CHESTPLATE.get();

			if (wearingNoirChestplate) {
				this.rightArm.xRot = 0.1F;
				this.leftArm.xRot = 0.1F;
				this.leftArm.z = 1.15F;
				this.rightArm.z = 1.15F;
			}

			brutality$mainRight = player.getMainArm() == HumanoidArm.RIGHT;

			brutality$hasNoirItemMain = player.getMainHandItem().is(BrutalityModItems.CANOPY_OF_SHADOWS.get());
			brutality$hasNoirItemOff = player.getOffhandItem().is(BrutalityModItems.CANOPY_OF_SHADOWS.get());

			float xRotOffset = -1.5F;

			if (brutality$hasNoirItemMain && brutality$hasNoirItemOff) {
				this.leftArm.xRot = xRotOffset;
				this.rightArm.xRot = xRotOffset;

				this.leftArm.yRot = 0.25F;
				this.rightArm.yRot = -0.25F;
				this.leftArm.zRot = 0.1F;
				this.rightArm.zRot = -0.2F;


			} else if (brutality$hasNoirItemMain) {
				if (brutality$mainRight) {
					this.rightArm.xRot = xRotOffset;
					if (wearingNoirChestplate) {
						this.leftArm.xRot = 0.1F;
						this.leftArm.z = 1.15F;

					}

				} else {
					this.leftArm.xRot = xRotOffset;
					if (wearingNoirChestplate) {
						this.rightArm.xRot = 0.1F;
						this.rightArm.z = 1.15F;

					}
				}
			} else if (brutality$hasNoirItemOff) {
				if (brutality$mainRight) {
					this.leftArm.xRot = xRotOffset;
					if (wearingNoirChestplate) {
						this.rightArm.xRot = 0.1F;
						this.rightArm.z = 1.15F;

					}

				} else {
					this.rightArm.xRot = xRotOffset;
					if (wearingNoirChestplate) {
						this.leftArm.xRot = 0.1F;
						this.leftArm.z = 1.15F;

					}
				}
			}

		}

	}
}