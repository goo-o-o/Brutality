package net.goo.brutality.common.fluid;

import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalityRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;

import java.util.function.Supplier;

public class LiquifiedManaBlock extends LiquidBlock {
    public LiquifiedManaBlock(Supplier<? extends FlowingFluid> supplier, Properties properties) {
        super(supplier, properties);
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pos, RandomSource pRandom) {
        if (pRandom.nextInt(10) == 0) {
            double x = pos.getX() + pRandom.nextDouble();
            double y = pos.getY() + 0.9;
            double z = pos.getZ() + pRandom.nextDouble();

            pLevel.addParticle(BrutalityParticles.WIZARDRY_PARTICLE.get(), x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (pEntity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(BrutalityEffects.CELESTIAL_FLUX.get(), 20, 2));
        }
        else if (pEntity instanceof ItemEntity itemEntity && !pLevel.isClientSide()) {
            // We use a custom "mana_delay" tag to track time spent specifically IN the fluid
            int transformDelay = itemEntity.getPersistentData().getInt("mana_delay");

            if (transformDelay < 40) { // 40 ticks = 2 seconds
                itemEntity.getPersistentData().putInt("mana_delay", transformDelay + 1);

                if (pLevel.getRandom().nextFloat() < 0.1f) {
                    ((ServerLevel) pLevel).sendParticles(BrutalityParticles.WIZARDRY_PARTICLE.get(),
                            itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(),
                            1, 0, 0, 0, 0);
                }
                return;
            }


            // recipe
            pLevel.getRecipeManager().getRecipeFor(BrutalityRecipes.MANA_TRANSFORMATION_TYPE.get(),
                    new SimpleContainer(itemEntity.getItem()), pLevel).ifPresent(recipe -> {

                ItemStack input = itemEntity.getItem();
                ItemStack result = recipe.getResultItem(pLevel.registryAccess()).copyWithCount(input.getCount());

                ItemEntity outputEntity = new ItemEntity(pLevel, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), result);

                // Give it a little "pop" upwards after sinking
                outputEntity.setDeltaMovement(pLevel.getRandom().nextGaussian() * 0.05, 0.3, pLevel.getRandom().nextGaussian() * 0.05);

                pLevel.addFreshEntity(outputEntity);

                // Big finish particles
                ((ServerLevel) pLevel).sendParticles(BrutalityParticles.WIZARDRY_PARTICLE.get(),
                        outputEntity.getX(), outputEntity.getY(), outputEntity.getZ(),
                        20, 0.25, 0.25, 0.25, 0.05);

                pLevel.playSound(null, pPos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.0F);

                itemEntity.discard();
            });
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 15;
    }

}
