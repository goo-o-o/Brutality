//package net.goo.brutality.block.entity;
//
//import net.goo.brutality.block.BrutalityGeoBlockEntity;
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.Tag;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.Nameable;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import org.jetbrains.annotations.NotNull;
//import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//import software.bernie.geckolib.core.animation.AnimationController;
//import software.bernie.geckolib.core.animation.RawAnimation;
//import software.bernie.geckolib.core.object.PlayState;
//import software.bernie.geckolib.util.GeckoLibUtil;
//
//import javax.annotation.Nullable;
//
//public class CoffeeMachineBlockEntity extends BlockEntity implements BrutalityGeoBlockEntity, Nameable {
//    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
//    private Component name;
//
//    public CoffeeMachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    public String texture(Block block) {
//        if (this.hasCustomName()) {
//            Component customName = this.getCustomName();
//            if (customName != null && customName.getString().equals("Eeffoc Machine")) {
//                return "eeffoc_machine";
//            }
//        }
//        return BrutalityGeoBlockEntity.super.texture(block);
//    }
//
//    @Override
//    public void setChanged() {
//        super.setChanged();
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(
//                new AnimationController<>(this, "controller", state -> PlayState.STOP)
//                        .triggerableAnim("right_click", RawAnimation.begin().thenPlay("right_click"))
//        );
//        controllers.add(
//                new AnimationController<>(this, state -> {
//                    if (this.hasCustomName()) {
//                        Component customName = this.getCustomName();
//                        if (customName != null && customName.contains(Component.translatable("block.brutality.eeffoc_machine"))) {
//                            state.setAndContinue(RawAnimation.begin().thenLoop("eeffoc"));
//                        }
//                    }
//                    return PlayState.CONTINUE;
//                })
//        );
//    }
//
//    @Override
//    public CompoundTag getUpdateTag() {
//        CompoundTag tag = super.getUpdateTag();
//        if (this.hasCustomName()) {
//            tag.putString("CustomName", Component.Serializer.toJson(this.name));
//        }
//        return tag;
//    }
//
//    @Override
//    public void handleUpdateTag(CompoundTag tag) {
//        super.handleUpdateTag(tag);
//        if (tag.contains("CustomName", Tag.TAG_STRING)) {
//            this.name = Component.Serializer.fromJson(tag.getString("CustomName"));
//        }
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag tag) {
//        if (this.name != null) {
//            tag.putString("CustomName", Component.Serializer.toJson(this.name));
//        }
//        super.saveAdditional(tag);
//    }
//
//    @Override
//    public void load(CompoundTag tag) {
//        super.load(tag);
//        if (tag.contains("CustomName", Tag.TAG_STRING)) {
//            String json = tag.getString("CustomName");
//            this.name = Component.Serializer.fromJson(json);
//        }
//    }
//
//
//    @Override
//    public AnimatableInstanceCache getAnimatableInstanceCache() {
//        return cache;
//    }
//
//    public void setCustomName(@Nullable Component name) {
//        this.name = name;
//        this.setChanged();
//
//        if (this.level != null && !this.level.isClientSide) {
//            BlockState state = this.getBlockState();
//            this.level.sendBlockUpdated(this.getBlockPos(), state, state, 3);
//        }
//    }
//
//    public @NotNull Component getName() {
//        return this.name != null ? this.name : this.getDefaultName();
//    }
//
//    public @NotNull Component getDisplayName() {
//        return this.getName();
//    }
//
//    @Nullable
//    public Component getCustomName() {
//        return this.name;
//    }
//
//    protected Component getDefaultName() {
//        return Component.translatable("block.brutality.coffee_machine");
//    }
//
//}
