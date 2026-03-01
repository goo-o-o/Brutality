package net.goo.brutality.common.registry;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

import java.util.Map;

public interface BrutalityCauldronInteraction extends CauldronInteraction {
    Map<Item, CauldronInteraction> MANA_INTERACTIONS = CauldronInteraction.newInteractionMap();

    CauldronInteraction FILL_MANA = (blockState, level, blockPos, player, hand, stack) ->
            CauldronInteraction.emptyBucket(level, blockPos, player, hand, stack, BrutalityBlocks.MANA_CAULDRON.get().defaultBlockState().setValue(LayeredCauldronBlock.LEVEL, 3), SoundEvents.BUCKET_EMPTY);
    CauldronInteraction SOLIDIFIED_MANA = (blockState, level, blockPos, player, hand, stack) -> {
        if (WalkNodeEvaluator.isBurningBlock(level.getBlockState(blockPos.below()))) {
            int currentLevel = 0;
            // Check if the current block is already a Mana Cauldron to get its level
            if (blockState.hasProperty(LayeredCauldronBlock.LEVEL)) {
                currentLevel = blockState.getValue(LayeredCauldronBlock.LEVEL);
            }

            if (currentLevel < 3) {
                if (!level.isClientSide()) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }

                    level.setBlockAndUpdate(blockPos, BrutalityBlocks.MANA_CAULDRON.get().defaultBlockState()
                            .setValue(LayeredCauldronBlock.LEVEL, currentLevel + 1));

                    level.playSound(null, blockPos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.0F);
                    level.playSound(null, blockPos, SoundEvents.BUBBLE_COLUMN_UPWARDS_AMBIENT, SoundSource.BLOCKS, 0.5F, 1.0F);
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide());
        }
        return InteractionResult.PASS;
    };

    static void bootstrap() {

        CauldronInteraction.addDefaultInteractions(MANA_INTERACTIONS);
        addManaInteractions(MANA_INTERACTIONS);
        addManaInteractions(POWDER_SNOW);
        addManaInteractions(EMPTY);
        addManaInteractions(LAVA);
        addManaInteractions(WATER);


        MANA_INTERACTIONS.put(Items.BUCKET, (blockState, level, blockPos, player, hand, stack) ->
                CauldronInteraction.fillBucket(blockState, level, blockPos, player, hand, stack, new ItemStack(BrutalityItems.LIQUIFIED_MANA_BUCKET.get()),
                        (blockState1) ->
                                blockState1.getValue(LayeredCauldronBlock.LEVEL) == 3, SoundEvents.BUCKET_FILL));

        CauldronInteraction.EMPTY.put(BrutalityItems.SOLIDIFIED_MANA.get(), SOLIDIFIED_MANA);
        MANA_INTERACTIONS.put(BrutalityItems.SOLIDIFIED_MANA.get(), SOLIDIFIED_MANA);

    }

    static void addManaInteractions(Map<Item, CauldronInteraction> interactionMap) {
        interactionMap.put(BrutalityItems.LIQUIFIED_MANA_BUCKET.get(), FILL_MANA);
    }


}
