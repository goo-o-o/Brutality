package net.goo.armament.item.custom.unused;

import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.client.renderers.item.AutoGlowingRenderer;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaPickaxeItem;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.Optional;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ResonancePickaxeItem extends ArmaPickaxeItem implements GeoItem {
    private final String BLOCK_KEY = "activeBlock";
    int[] color1 = new int[]{255, 0, 0};
    int[] color2 = new int[]{0, 255, 0};
    int[] color3 = new int[]{0, 0, 255};
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ResonancePickaxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
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
            player.getCooldowns().addCooldown(stack.getItem(), 60);
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


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, AutoGlowingRenderer.class);
    }
}
