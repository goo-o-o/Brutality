package net.goo.brutality.block.entity;

import net.goo.brutality.block.BrutalityGeoBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SuperSnifferFigureBlockEntity extends BlockEntity implements BrutalityGeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int texture = 0; // Default texture
    private int pose = 0; // Default pose
    static String[] textures = new String[]{"", "_super", "_blue", "_ultra"};

    public SuperSnifferFigureBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public String texture(Block block) {
        System.out.println(this.getTexture());
        return "super_sniffer_figure" + textures[this.getTexture()];
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null && !this.level.isClientSide) {
            BlockState state = this.getBlockState();
            this.level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(
                new AnimationController<>(this, "controller", 0, this::predicate)
        );
    }

    private PlayState predicate(AnimationState<SuperSnifferFigureBlockEntity> state) {
        SuperSnifferFigureBlockEntity blockEntity = (SuperSnifferFigureBlockEntity) state.getData(DataTickets.BLOCK_ENTITY);
        System.out.println("PREDICATE POSE: " + blockEntity.getPose());
        state.getController().setAnimation(RawAnimation.begin().thenPlayAndHold("pose_" + blockEntity.getPose()));
        return PlayState.CONTINUE;
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt("texture", this.texture);
        tag.putInt("pose", this.pose);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        if (tag.contains("texture", CompoundTag.TAG_INT)) {
            this.texture = tag.getInt("texture");
        }
        if (tag.contains("pose", CompoundTag.TAG_INT)) {
            this.pose = tag.getInt("pose");
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("texture", this.texture);
        tag.putInt("pose", this.pose);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("texture", CompoundTag.TAG_INT)) {
            this.texture = tag.getInt("texture");
        }
        if (tag.contains("pose", CompoundTag.TAG_INT)) {
            this.pose = tag.getInt("pose");
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setTexture(int texture) {
        this.texture = texture;
        this.setChanged();
    }

    public void setPose(int pose) {
        this.pose = pose;
        this.setChanged();
    }

    public int getTexture() {
        return this.texture;
    }

    public int getPose() {
        return this.pose;
    }
}