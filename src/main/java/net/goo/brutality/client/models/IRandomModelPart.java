package net.goo.brutality.client.models;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
// Thanks to PierceArrow mod

public interface IRandomModelPart {
	ModelPart armament$getRandomModelPart(RandomSource paramRandom);
}