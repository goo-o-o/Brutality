package net.goo.brutality.common.item.generic;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;


public class CoinStackItem extends Item {
    public static final String NBT_COINS = "StackedCoins";
    public static final int MAX_STACK_SIZE = 16;


    public CoinStackItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        // Detect "Drag and Drop"
        if (pAction == ClickAction.SECONDARY && pOther.getItem() instanceof BrutalityCoinItem coinItem) {
            if (getStackSize(pStack) < MAX_STACK_SIZE) {
                pushCoin(pStack, coinItem);
                pOther.shrink(1); // Consume 1 coin from the cursor
                return true; // Logic handled
            }
        }
        return false;
    }

    public void pushCoin(ItemStack stack, BrutalityCoinItem coinItem) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = tag.getList(NBT_COINS, Tag.TAG_STRING);

        // Store the registry name of the coin
        list.add(StringTag.valueOf(String.valueOf(coinItem)));
        tag.put(NBT_COINS, list);

    }

    public BrutalityCoinItem popCoin(ItemStack stack) {
        int size = getStackSize(stack);
        if (size == 0) return null;
        return removeCoinAt(stack, size - 1);
    }

    public BrutalityCoinItem removeCoinAt(ItemStack stack, int index) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(NBT_COINS)) return null;

        ListTag list = tag.getList(NBT_COINS, Tag.TAG_STRING);
        if (index < 0 || index >= list.size()) return null;

        String idString = list.getString(index);
        list.remove(index);

        // Update tag
        if (list.isEmpty()) {
            tag.remove(NBT_COINS);
        } else {
            tag.put(NBT_COINS, list);
        }

        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(idString));
        return item instanceof BrutalityCoinItem ? (BrutalityCoinItem) item : null;
    }

    public BrutalityCoinItem getCoinAt(ItemStack stack, int index) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(NBT_COINS)) return null;

        ListTag list = tag.getList(NBT_COINS, Tag.TAG_STRING);
        if (index < 0 || index >= list.size()) return null;

        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(list.getString(index)));
        return item instanceof BrutalityCoinItem ? (BrutalityCoinItem) item : null;
    }

    public int getStackSize(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(NBT_COINS)) return 0;
        return tag.getList(NBT_COINS, Tag.TAG_STRING).size();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int count = getStackSize(pStack);
        pTooltipComponents.add(Component.literal("Coins: " + count + "/" + MAX_STACK_SIZE).withStyle(ChatFormatting.GRAY));
        if (count > 0) {
            BrutalityCoinItem top = getCoinAt(pStack, count - 1);
            if (top != null) {
                pTooltipComponents.add(Component.literal("Top: ").append(top.getDescription()).withStyle(ChatFormatting.GOLD));
            }
        }
    }
}
