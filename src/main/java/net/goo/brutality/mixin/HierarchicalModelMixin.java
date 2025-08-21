package net.goo.brutality.mixin;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
// Thanks to PierceArrow mod
@Mixin(HierarchicalModel.class)
public abstract class HierarchicalModelMixin implements net.goo.brutality.client.models.IRandomModelPart {
	@Unique
	private List<ModelPart> brutality$parts;

	@Unique
	public ModelPart brutality$getRandomModelPart(RandomSource paramRandom) {
		if (this.brutality$parts == null) {
			this.brutality$parts = this.root().getAllParts().filter((p_170824_) -> !p_170824_.isEmpty()).collect(ImmutableList.toImmutableList());
		}
		return this.brutality$parts.get(paramRandom.nextInt(this.brutality$parts.size()));
	}

	@Shadow
	public abstract ModelPart root();
}