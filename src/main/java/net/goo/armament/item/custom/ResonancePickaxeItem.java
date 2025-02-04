package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sTerraBeamPacket;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ResonancePickaxeItem extends PickaxeItem implements GeoItem {
    private final String BLOCK_KEY = "activeBlock";

    public ResonancePickaxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        String blockString = pStack.getOrCreateTag().getString(BLOCK_KEY);
        if (!blockString.isEmpty()) {
            ResourceLocation blockID = new ResourceLocation(blockString);
            Optional<Block> optionalBlock = Optional.ofNullable(ForgeRegistries.BLOCKS.getValue(blockID));
            if (optionalBlock.isPresent() && pState.getBlock() == optionalBlock.get()) {
                return calculateSpeed(pStack, pState, 3.5F);
            }
        }
        return calculateSpeed(pStack, pState, 0.05F);
    }

    public float calculateSpeed(ItemStack pStack, BlockState pState, float scaleFactor) {
        return super.getDestroySpeed(pStack, pState) * scaleFactor;
    }



    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Player player = pContext.getPlayer();
        Level level = pContext.getLevel();
        ItemStack stack = pContext.getItemInHand();
        if (player != null) {
            player.getCooldowns().addCooldown(stack.getItem(), 20);
            BlockPos blockPos = LookingAtBlock(player, false, 5F);
            Block selectedBlock = level.getBlockState(blockPos).getBlock();
            ResourceLocation blockID = ForgeRegistries.BLOCKS.getKey(selectedBlock);
            if (!level.isClientSide && blockID != null) {
                MutableComponent blockName = selectedBlock.getName();
                player.displayClientMessage(Component.literal("Selected ").append(blockName), true);
                stack.getOrCreateTag().putString(BLOCK_KEY, blockID.toString());
            }
        }
        return InteractionResult.FAIL;
    }

    public BlockPos LookingAtBlock(Player player, boolean isFluid, float hitDistance){
        HitResult block =  player.pick(hitDistance, 1.0F, isFluid);
        if (block.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)block).getBlockPos();
            return blockpos;
        }
        return null;
    }

    int[] color1 = new int[]{255, 0, 0};
    int[] color2 = new int[]{0, 255, 0};
    int[] color3 = new int[]{0, 0, 255};

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.resonance_pickaxe.desc.1").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color1))));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.resonance_pickaxe.desc.2").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));
        pTooltipComponents.add(Component.translatable("item.armament.resonance_pickaxe.desc.3").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color3))));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.addColorGradientText((Component.translatable("item.armament.resonance_pickaxe")), color1, color2, color3).withStyle(Style.EMPTY.withBold(true));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
