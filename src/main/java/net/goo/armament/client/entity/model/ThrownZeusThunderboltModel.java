package net.goo.armament.client.entity.model;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.goo.armament.Armament;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ThrownZeusThunderboltModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Armament.MOD_ID, "thrown_zeus_thunderbolt"), "main");
	private final ModelPart bolt;

	public ThrownZeusThunderboltModel(ModelPart root) {
		this.bolt = root.getChild("bolt");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		// Define the "bolt" part correctly
		PartDefinition bolt = partdefinition.addOrReplaceChild("bolt", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		// Add sub-parts to the "bolt" part
		PartDefinition cube_r1 = bolt.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(24, -4).mirror().addBox(0.0F, -24.0F, -2.0F, 0.0F, 24.0F, 4.0F, new CubeDeformation(0.0001F)).mirror(false), PartPose.offsetAndRotation(0.0F, -9.7738F, -9.0631F, -0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r2 = bolt.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(12, 0).addBox(-0.5F, -2.0F, -2.0F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -14.0F, 0.0F, -1.1345F, 0.0F, 0.0F));

		PartDefinition cube_r3 = bolt.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(33, -4).mirror().addBox(0.0F, 0.0F, -2.0F, 0.0F, 24.0F, 4.0F, new CubeDeformation(0.0001F)).mirror(false), PartPose.offsetAndRotation(0.0F, 9.7738F, 9.0631F, -0.2618F, 0.0F, 0.0F));

		PartDefinition cube_r4 = bolt.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(12, 16).addBox(-0.5F, -10.0F, -2.0F, 1.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 14.0F, 0.0F, -1.1345F, 0.0F, 0.0F));

		PartDefinition cube_r5 = bolt.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, 2.0F, -2.0F, 2.0F, 28.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -16.0F, 0.0F, 0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}


	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bolt.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}