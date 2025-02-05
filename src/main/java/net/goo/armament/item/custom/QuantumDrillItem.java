package net.goo.armament.item.custom;

import net.goo.armament.item.custom.client.renderer.QuantumDrillItemRenderer;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static net.minecraft.core.BlockPos.withinManhattan;

public class QuantumDrillItem extends Item implements GeoItem {
    private static final String ACTIVE = "drillActive";
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private static final String SPEED = "speed";

    public QuantumDrillItem(Properties pProperties) {
        super(pProperties);
    }

    int[] color1 = new int[]{255, 255, 255};
    int[] color2 = new int[]{0, 120, 190};

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.quantum_drill.desc.1").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.quantum_drill.desc.2").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color1))));
        pTooltipComponents.add(Component.translatable("item.armament.quantum_drill.desc.3").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.addColorGradientText((Component.translatable("item.armament.quantum_drill")), color1, color2).withStyle(Style.EMPTY.withBold(true));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private QuantumDrillItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if(this.renderer == null) {
                    renderer = new QuantumDrillItemRenderer();
                }
                return this.renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel.isClientSide) {
            // placeholder (sound, animation)
        }

        if (!pLevel.isClientSide) {
            setActive(pStack, !isActive(pStack));
            if (!isActive(pStack)) {
                setSpeed(pStack, 0);
            }
        }

        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
    }

    int efficiency = 1;
    int breakTickCount = 0;
    int tickCount = 0;
    Set<BlockState> blockSet = new HashSet<>();
    int[] efficiencyColor1 = new int[]{0, 200, 0};
    int[] efficiencyColor2 = new int[]{255, 225, 0};
    int[] efficiencyColor3 = new int[]{255, 0, 0};
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        Player pPlayer = ((Player) pEntity);
        tickCount++;
        if (isActive(pStack) && !pLevel.isClientSide) {
            if (tickCount % 15 == 0) {

                BlockPos origin = new BlockPos(pEntity.getBlockX(), pEntity.getBlockY(), pEntity.getBlockZ());
                int radius = 7;

                for (BlockPos pos : withinManhattan(origin, radius, radius, radius)) {
                    blockSet.add(pLevel.getBlockState(pos));
                }

                float efficiencyScaleFactor = 0.5F; // Lower is faster
                int uniqueBlocks = blockSet.size();
                int blockLimit = 55;
                float efficiencyFraction = Math.max((uniqueBlocks / (float) blockLimit), 0.01F);
                float efficiencyPercentage = ((efficiencyFraction) * 100);
                efficiency = (int) Math.max(efficiencyPercentage * 0.2F / efficiencyScaleFactor , 1); // Higher is faster 20 is highest
                pPlayer.displayClientMessage(Component.literal
                        (uniqueBlocks + " unique block" + (uniqueBlocks == 1 ? "" : "s") + " | ").append
                        (Component.literal(String.format("%.2f", efficiencyPercentage) + "% efficiency")
                                .withStyle(Style.EMPTY.withColor(ModUtils.getColorFromGradient((int) efficiencyPercentage, efficiencyColor3, efficiencyColor2, efficiencyColor1)).withBold(true))),
                        true);

                if ((int) efficiencyPercentage < 33) {
                    setSpeed(pStack, 1);
                } else if (((int) efficiencyPercentage) < 66) {
                    setSpeed(pStack, 2);
                } else {
                    setSpeed(pStack, 3);
                }

                blockSet.clear();
            }


            BlockPos blockPos = LookingAtBlock(pPlayer, false, 8F);
            if (blockPos != null) {
                BlockState block = pLevel.getBlockState(blockPos);
                float breakSpeed = block.getDestroyProgress(pPlayer, pLevel, blockPos);
                int breakTime  = Math.max((int) (1 / breakSpeed) / efficiency, 2);
                breakTickCount++;
                if (breakTime == 0) {
                    pLevel.destroyBlock(blockPos, true);
                } else if (breakTickCount == breakTime) {
                    pLevel.destroyBlock(blockPos, true);
                    breakTickCount = 0;
                } else if (breakTickCount > breakTime) {
                    breakTickCount = 0;
                }
            }

        }
    }

    public BlockPos LookingAtBlock(Player player, boolean isFluid, float hitDistance){
        HitResult block =  player.pick(hitDistance, 1.0F, isFluid);

        if (block.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)block).getBlockPos();
            return blockpos;
        }
        return null;
    }

    public boolean isActive(ItemStack stack) {
        return stack.getOrCreateTag().getBoolean(ACTIVE);
    }

    public static void setActive(ItemStack stack, boolean active) {
        stack.getOrCreateTag().putBoolean(ACTIVE, active);
    }

    public static int getSpeed(ItemStack stack) {
        return stack.getOrCreateTag().getInt(SPEED);
    }

    public static void setSpeed(ItemStack stack, int speed) {
        stack.getOrCreateTag().putInt(SPEED, speed);
    }

    private PlayState predicate(AnimationState animationState) {
        ItemStack stack = (ItemStack) animationState.getData(DataTickets.ITEMSTACK);
        if (getSpeed(stack) == 1) {
            animationState.getController().setAnimation(RawAnimation.begin().then("activelow", Animation.LoopType.LOOP));
        } else if (getSpeed(stack) == 2) {
            animationState.getController().setAnimation(RawAnimation.begin().then("activemed", Animation.LoopType.LOOP));
        } else if (getSpeed(stack) == 3) {
            animationState.getController().setAnimation(RawAnimation.begin().then("activehigh", Animation.LoopType.LOOP));
        } else {
            animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 10, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
