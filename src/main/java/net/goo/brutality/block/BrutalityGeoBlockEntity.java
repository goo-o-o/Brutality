package net.goo.brutality.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.Objects;

public interface BrutalityGeoBlockEntity extends GeoBlockEntity {
    default String getRegistryName() {
        if (!(this instanceof BlockEntity be)) {
            throw new IllegalStateException("BrutalityGeoBlockEntity must be a BlockEntity");
        }

        Block block = be.getBlockState().getBlock();

        return Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block)).getPath();
    }

    default String model(Block block) {
        return null;
    }

    default String texture(Block block) {
        return null;
    }

    @Override
    default void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }


}
