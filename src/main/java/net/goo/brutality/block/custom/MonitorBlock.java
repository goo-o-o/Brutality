package net.goo.brutality.block.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.block.HorizontalDirectionalBlock;
import net.goo.brutality.registry.BrutalityModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MonitorBlock extends HorizontalDirectionalBlock {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public MonitorBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        pTooltip.add(Component.translatable("block." + Brutality.MOD_ID + ".monitor.description.1"));
    }

    @Override
    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 0.6F;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return 10;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(ACTIVE, FACING);
    }


    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide()) return InteractionResult.SUCCESS;

        BlockState state = pState.cycle(ACTIVE);
        pLevel.setBlock(pPos, state, 3);
        float pitch = pState.getValue(ACTIVE) ? 0.5F : 0.7F;
        Vec3 loc = pHit.getLocation();
        pLevel.playSound(null, loc.x, loc.y, loc.z, BrutalityModSounds.LIGHT_SWITCH_MONO.get(), SoundSource.BLOCKS, 1F, pitch);

        return InteractionResult.CONSUME;
    }
}
