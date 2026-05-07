package net.goo.brutality.common.item.generic.coins;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MirrorCoin extends BrutalityCoinItem {


    public MirrorCoin(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties, cooldownTime, descriptionComponents);
    }

    @Override
    protected float getBasePixelDiameter() {
        return 10;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);

        BrutalityCoinItem previousCoin = getPreviousCoin(pStack);
        if (previousCoin != null) {
            pTooltipComponents.add(Component.empty());
            pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".mirror_coin.previous_coin", previousCoin.getDescription()));
        }
    }

    private static final String PREVIOUS_COIN = "previous_coin";

    public static BrutalityCoinItem getPreviousCoin(ItemStack mirrorCoin) {
        String coinString = mirrorCoin.getOrCreateTag().getString(PREVIOUS_COIN);
        if (coinString.isEmpty()) return null;
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(coinString));
        return item instanceof BrutalityCoinItem coinItem ? coinItem : null;
    }

    public static void setPreviousCoin(ItemStack mirrorCoin, BrutalityCoinItem coin) {
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(coin);
        if (registryName != null) {
            mirrorCoin.getOrCreateTag().putString(PREVIOUS_COIN, registryName.toString());
        }
    }

    @Override
    public void onHeads(Player player, ItemStack stack, Vec3 location) {
        playBuffSounds(player);
        BrutalityCoinItem previousCoin = getPreviousCoin(stack);
        if (previousCoin != null) {
            previousCoin.onHeads(player, stack, location);
        }
    }

    @Override
    public void onTails(Player player, ItemStack stack, Vec3 location) {
        playDebuffSounds(player);
        BrutalityCoinItem previousCoin = getPreviousCoin(stack);
        if (previousCoin != null) {
            previousCoin.onTails(player, stack, location);
        }
    }

}
