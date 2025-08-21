package net.goo.brutality.client.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.RandomSource;
// Thanks to PierceArrow mod

public interface IRandomModelPart {
	ModelPart brutality$getRandomModelPart(RandomSource paramRandom);
}