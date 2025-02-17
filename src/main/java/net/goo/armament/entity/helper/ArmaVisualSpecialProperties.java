package net.goo.armament.entity.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.goo.armament.entity.ArmaEffectEntity;
import net.goo.armament.entity.ArmaVisualType;
import net.goo.armament.entity.ArmaVisualTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class ArmaVisualSpecialProperties {
    public static void set(ArmaEffectEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        float lerpBodyRot = Mth.rotLerp(partialTick, animatable.xRotO, animatable.getXRot()) - 165;

        //TODO Un-hardcode

        List<ArmaVisualType> z0 = new ArrayList<>();

        z0.add(ArmaVisualTypes.TERRA_BEAM.get());
//        z0.add(ArmaVisualTypes.CRESCENTIA_STRIKE_INVERTED.get());
//        z0.add(ArmaVisualTypes.CRESCENTIA_THROW.get());
//        z0.add(ArmaVisualTypes.CRESCENTIA_THROW_INVERTED.get());
//        z0.add(ArmaVisualTypes.BREEZEBREAKER_SLASH.get());
//        z0.add(ArmaVisualTypes.BREEZEBREAKER_SLASH_INVERTED.get());
//        z0.add(ArmaVisualTypes.FROSTBOUND_SLASH.get());
//        z0.add(ArmaVisualTypes.FROSTBOUND_SLASH_INVERTED.get());

        if (z0.contains(animatable.getVisualType())) poseStack.mulPose(Axis.ZP.rotationDegrees(((animatable.getRotationZ() / 360.0F) * 45.0F) - 22.5F));

        List<ArmaVisualType> x2 = new ArrayList<>();
//        x2.add(ArmaVisualTypes.CRESCENTIA_STRIKE.get());
//        x2.add(ArmaVisualTypes.CRESCENTIA_STRIKE_INVERTED.get());
//        x2.add(ArmaVisualTypes.CRESCENTIA_THROW.get());
//        x2.add(ArmaVisualTypes.CRESCENTIA_THROW_INVERTED.get());
//        x2.add(ArmaVisualTypes.BREEZEBREAKER_SLASH.get());
//        x2.add(ArmaVisualTypes.BREEZEBREAKER_SLASH_INVERTED.get());
//        x2.add(ArmaVisualTypes.AQUAFLORA_STAB.get());
//        x2.add(ArmaVisualTypes.AQUAFLORA_SLICE.get());
//        x2.add(ArmaVisualTypes.AQUAFLORA_SLICE_INVERTED.get());
//        x2.add(ArmaVisualTypes.FROSTBOUND_SLASH.get());
//        x2.add(ArmaVisualTypes.FROSTBOUND_SLASH_INVERTED.get());

        if (x2.contains(animatable.getVisualType())) poseStack.mulPose(Axis.YP.rotationDegrees(-180));

        List<ArmaVisualType> x0 = new ArrayList<>();
//        x0.add(ArmaVisualTypes.BREEZEBREAKER_WHEEL_IMPACT.get());
//        x0.add(ArmaVisualTypes.AQUAFLORA_PIERCE_START.get());

        if (x0.contains(animatable.getVisualType()))  poseStack.mulPose(Axis.XP.rotationDegrees(180F + lerpBodyRot));

        List<ArmaVisualType> x3 = new ArrayList<>();
//        x3.add(ArmaVisualTypes.AQUAFLORA_STAB.get());

        if (x3.contains(animatable.getVisualType()))  poseStack.mulPose(Axis.XP.rotationDegrees(180F - lerpBodyRot));

        List<ArmaVisualType> x1 = new ArrayList<>();
//        x1.add(ArmaVisualTypes.RAINFALL_SHOOT.get());

        if (x1.contains(animatable.getVisualType())) poseStack.mulPose(Axis.XP.rotationDegrees(180F + lerpBodyRot - 15f));

        List<ArmaVisualType> x4 = new ArrayList<>();
//        x4.add(ArmaVisualTypes.BREEZEBREAKER_WHEEL.get());

        if (x4.contains(animatable.getVisualType()))  poseStack.mulPose(Axis.YP.rotationDegrees(180));
    }
}