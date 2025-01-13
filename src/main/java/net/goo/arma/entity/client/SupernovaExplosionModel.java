package net.goo.arma.entity.client;// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class SupernovaExplosionModel<T extends Entity> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	private final ModelPart supernova;
	private final ModelPart ring;

	public SupernovaExplosionModel(ModelPart root) {
		this.supernova = root.getChild("supernova");
		this.ring = root.getChild("ring");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition supernova = partdefinition.addOrReplaceChild("supernova", CubeListBuilder.create().texOffs(0, 55).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(9.0F, 9.0F, 9.0F, -18.0F, -18.0F, -18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition ring = partdefinition.addOrReplaceChild("ring", CubeListBuilder.create().texOffs(-16, 0).addBox(-24.0F, -1.0F, -24.0F, 48.0F, 1.0F, 48.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		supernova.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		ring.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}