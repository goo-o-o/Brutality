package net.goo.brutality.block.block_entity;

import net.goo.brutality.registry.BrutalityModBlockEntities;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TableOfWizardryBlockEntity extends BlockEntity implements Nameable {
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
    private static final RandomSource RANDOM = RandomSource.create();
    private Component name;

    public TableOfWizardryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BrutalityModBlockEntities.TABLE_OF_WIZARDRY_BLOCK_ENTITY.get(), pPos, pBlockState);
    }


    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        if (this.hasCustomName()) {
            pTag.putString("CustomName", Component.Serializer.toJson(this.name));
        }

    }
    public static final List<BlockPos> PEDESTAL_OFFSETS = List.of(
            new BlockPos(0, 0, -3),   // North
            new BlockPos(-2, 0, -2),  // North-West
            new BlockPos(2, 0, -2),   // North-East
            new BlockPos(-3, 0, 0),   // West
            new BlockPos(3, 0, 0),    // East
            new BlockPos(-2, 0, 2),   // South-West
            new BlockPos(2, 0, 2),    // South-East
            new BlockPos(0, 0, 3)     // South
    );

    public List<BlockPos> getNearbyPedestals() {
        List<BlockPos> pedestals = new ArrayList<>();
        Level level = this.getLevel();

        if (level == null) return pedestals;

        for (BlockPos offset : PEDESTAL_OFFSETS) {
            BlockPos checkPos = this.worldPosition.offset(offset);

            // Check if the block at the offset is the correct Pedestal block
            if (level.getBlockState(checkPos).is(BrutalityModBlocks.PEDESTAL_OF_WIZARDRY.get())) {
                pedestals.add(checkPos.immutable());
            }
        }

        return pedestals;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        if (pTag.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(pTag.getString("CustomName"));
        }

    }

    @Override
    public AABB getRenderBoundingBox() {
        BlockPos pos = getBlockPos();
        return new AABB(pos, pos.offset(1, 1, 1)).inflate(5, 2, 5);
    }

    public static void bookAnimationTick(Level pLevel, BlockPos pPos, BlockState pState, TableOfWizardryBlockEntity pBlockEntity) {
        pBlockEntity.oOpen = pBlockEntity.open;
        pBlockEntity.oRot = pBlockEntity.rot;
        Player $$4 = pLevel.getNearestPlayer((double) pPos.getX() + (double) 0.5F, (double) pPos.getY() + (double) 0.5F, (double) pPos.getZ() + (double) 0.5F, (double) 3.0F, false);
        if ($$4 != null) {
            double $$5 = $$4.getX() - ((double) pPos.getX() + (double) 0.5F);
            double $$6 = $$4.getZ() - ((double) pPos.getZ() + (double) 0.5F);
            pBlockEntity.tRot = (float) Mth.atan2($$6, $$5);
            pBlockEntity.open += 0.1F;
            if (pBlockEntity.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float $$7 = pBlockEntity.flipT;

                do {
                    pBlockEntity.flipT += (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while ($$7 == pBlockEntity.flipT);
            }
        } else {
            pBlockEntity.tRot += 0.02F;
            pBlockEntity.open -= 0.1F;
        }

        while (pBlockEntity.rot >= (float) Math.PI) {
            pBlockEntity.rot -= ((float) Math.PI * 2F);
        }

        while (pBlockEntity.rot < -(float) Math.PI) {
            pBlockEntity.rot += ((float) Math.PI * 2F);
        }

        while (pBlockEntity.tRot >= (float) Math.PI) {
            pBlockEntity.tRot -= ((float) Math.PI * 2F);
        }

        while (pBlockEntity.tRot < -(float) Math.PI) {
            pBlockEntity.tRot += ((float) Math.PI * 2F);
        }

        float $$8;
        for ($$8 = pBlockEntity.tRot - pBlockEntity.rot; $$8 >= (float) Math.PI; $$8 -= ((float) Math.PI * 2F)) {
        }

        while ($$8 < -(float) Math.PI) {
            $$8 += ((float) Math.PI * 2F);
        }

        pBlockEntity.rot += $$8 * 0.4F;
        pBlockEntity.open = Mth.clamp(pBlockEntity.open, 0.0F, 1.0F);
        ++pBlockEntity.time;
        pBlockEntity.oFlip = pBlockEntity.flip;
        float $$9 = (pBlockEntity.flipT - pBlockEntity.flip) * 0.4F;
        float $$10 = 0.2F;
        $$9 = Mth.clamp($$9, -0.2F, 0.2F);
        pBlockEntity.flipA += ($$9 - pBlockEntity.flipA) * 0.9F;
        pBlockEntity.flip += pBlockEntity.flipA;
    }

    public @NotNull Component getName() {
        return this.name != null ? this.name : Component.translatable("container.enchant");
    }

    public void setCustomName(@Nullable Component pName) {
        this.name = pName;
    }

    @Nullable
    public Component getCustomName() {
        return this.name;
    }
}
