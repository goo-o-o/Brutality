package net.goo.brutality.mixin.mixins;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
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
	@Shadow public @Final ModelPart rightArm;
	@Shadow public @Final ModelPart leftArm;
	@Shadow public @Final ModelPart head;

	@Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
	private void brutality$applyDynamicPoses(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
//		if (entity instanceof Player player) {
//			BrutalityPoseHandler.applyBodyOnly((HumanoidModel<?>)(Object)this, player, ageInTicks, player.isCrouching());
//		}
	}

	@Redirect(
			method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V")
	)
	private void brutality$conditionalBob(ModelPart part, float ageInTicks, float direction, LivingEntity entity) {
//		if (entity instanceof Player player && BrutalityPoseHandler.getPoseForEntity(player).shouldSkipBobbing(part)) {
//			return;
//		}
//		AnimationUtils.bobModelPart(part, ageInTicks, direction);
	}
}