package net.goo.brutality.mixin.mixins;

import com.google.common.collect.ImmutableList;
import net.goo.brutality.common.item.curios.charm.PoseidonsBlessing;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;
// Thanks to PierceArrow mod

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin implements net.goo.brutality.client.models.IRandomModelPart {

    @Unique
    private List<ModelPart> brutality$parts;

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;Ljava/util/function/Function;)V", at = @At("TAIL"))
    public void onConstructor(ModelPart modelPart, Function<ResourceLocation, RenderType> renderTypeFunction, CallbackInfo callbackInfo) {
        this.brutality$parts = modelPart.getAllParts().filter((p_170824_) -> !p_170824_.isEmpty()).collect(ImmutableList.toImmutableList());
    }

    @Override
    public ModelPart brutality$getRandomModelPart(RandomSource paramRandom) {
        return this.brutality$parts.get(paramRandom.nextInt(this.brutality$parts.size()));
    }

    @Shadow
    public float swimAmount;

    /**
     * Force swimAmount to 0 at the start of setupAnim to prevent swimming animations
     */
    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private <T extends LivingEntity> void preventSwimAnimation(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch, CallbackInfo ci) {
        if (PoseidonsBlessing.shouldApply(pEntity))
            this.swimAmount = 0.0F;
    }
}