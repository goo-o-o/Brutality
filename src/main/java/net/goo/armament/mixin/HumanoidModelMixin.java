package net.goo.armament.mixin;

import net.goo.armament.registry.ModItems;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
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

@Mixin(HumanoidModel.class)
public class HumanoidModelMixin {
    @Unique
    boolean armament$hasNoirItemMain, armament$hasNoirItemOff, armament$mainRight;

    @Final
    @Shadow
    public ModelPart rightArm;
    @Final
    @Shadow
    public ModelPart leftArm;

    @Inject(method = "setupAnim", at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/model/geom/ModelPart;zRot:F",
            ordinal = 1,
            shift = At.Shift.AFTER))
    private void setAngles(LivingEntity livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!(livingEntity instanceof Player player)) return;
        if (player.isCrouching()) return;

        armament$mainRight = player.getMainArm() == HumanoidArm.RIGHT;

        armament$hasNoirItemMain = player.getMainHandItem().is(ModItems.CANOPY_OF_SHADOWS.get());
        armament$hasNoirItemOff = player.getOffhandItem().is(ModItems.CANOPY_OF_SHADOWS.get());

        float xRotOffset = -1.2F;

        if (armament$hasNoirItemMain && armament$hasNoirItemOff) {
            this.leftArm.xRot = xRotOffset;
            this.rightArm.xRot = xRotOffset;

            this.leftArm.yRot = 0.5F;
            this.rightArm.yRot = -0.5F;

            this.leftArm.zRot = 0.1F;
            this.rightArm.zRot = -0.1F;
        } else if (armament$hasNoirItemMain) {
            if (armament$mainRight) {
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
        } else if (armament$hasNoirItemOff) {
            if (armament$mainRight) {
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
        } else if (player.getInventory().getArmor(2).getItem() == ModItems.NOIR_CHESTPLATE.get()) {
            this.rightArm.xRot = 0;
            this.leftArm.xRot = 0;
            this.rightArm.zRot = 0;
            this.leftArm.zRot = 0;
        }

    }


    @Redirect(
            method = "setupAnim",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/model/AnimationUtils;bobModelPart(Lnet/minecraft/client/model/geom/ModelPart;FF)V"
            ),
            expect = 2,
            require = 2
    )
    private void conditionalBobModelPart(ModelPart arm, float ageInTicks, float direction, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player.getInventory().getArmor(2).getItem() == ModItems.NOIR_CHESTPLATE.get()) {
                // skip bobbing entirely
                return;
            }
        }

        // default behavior: apply bob
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
            boolean wearingNoirChestplate = player.getInventory().getArmor(2).getItem() == ModItems.NOIR_CHESTPLATE.get();

            if (wearingNoirChestplate) {
                this.rightArm.xRot = 0.1F;
                this.leftArm.xRot = 0.1F;
                this.leftArm.z = 1.15F;
                this.rightArm.z = 1.15F;
            }

            armament$mainRight = player.getMainArm() == HumanoidArm.RIGHT;

            armament$hasNoirItemMain = player.getMainHandItem().is(ModItems.CANOPY_OF_SHADOWS.get());
            armament$hasNoirItemOff = player.getOffhandItem().is(ModItems.CANOPY_OF_SHADOWS.get());

            float xRotOffset = -1.5F;

            if (armament$hasNoirItemMain && armament$hasNoirItemOff) {
                this.leftArm.xRot = xRotOffset;
                this.rightArm.xRot = xRotOffset;

                this.leftArm.yRot = 0.25F;
                this.rightArm.yRot = -0.25F;
                this.leftArm.zRot = 0.1F;
                this.rightArm.zRot = -0.2F;


            } else if (armament$hasNoirItemMain) {
                if (armament$mainRight) {
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
            } else if (armament$hasNoirItemOff) {
                if (armament$mainRight) {
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