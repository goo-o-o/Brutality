package net.goo.brutality.common.block.custom;

import net.goo.brutality.common.block.IBrutalityMagicBlock;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ManaCandle extends CandleBlock implements IBrutalityMagicBlock {
    public ManaCandle(Properties pProperties) {
        super(pProperties);
    }

    // Enchant power is handled in super which uses tags

    @Override
    public int getMagicPower(BlockState state) {
        return 5 * state.getValue(CANDLES);
    }

    @Override
    public int getMaxCount() {
        return 5;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pState.getValue(LIT)) {
            this.getParticleOffsets(pState).forEach((p_220695_) -> addParticlesAndSound(pLevel, p_220695_.add(pPos.getX(), pPos.getY(), pPos.getZ()), pRandom));
        }
    }

    private static void addParticlesAndSound(Level pLevel, Vec3 pOffset, RandomSource pRandom) {
        float f = pRandom.nextFloat();
        if (f < 0.3F) {
            pLevel.addParticle(BrutalityParticles.WIZARDRY_PARTICLE.get(), pOffset.x, pOffset.y, pOffset.z, 0.0D, 0.0D, 0.0D);
            if (f < 0.17F) {
                pLevel.playLocalSound(pOffset.x + 0.5D, pOffset.y + 0.5D, pOffset.z + 0.5D, SoundEvents.CANDLE_AMBIENT, SoundSource.BLOCKS, 1.0F + pRandom.nextFloat(), pRandom.nextFloat() * 0.7F + 0.3F, false);
            }
        }

        pLevel.addParticle(BrutalityParticles.BLUE_FLAME_PARTICLE.get(), pOffset.x, pOffset.y, pOffset.z, 0.0D, 0.0D, 0.0D);
    }


}
