package net.goo.brutality.common.entity.capabilities;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.loadouts.Loadout;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerLoadoutsCap implements IBrutalityData {
    // Index 0 is always "Default"
    private final List<Loadout> loadoutStack = new ArrayList<>();
    private int activeIndex = 0;

    public PlayerLoadoutsCap() {
        loadoutStack.add(new Loadout(new ListTag(), Component.translatable("message." + Brutality.MOD_ID + ".default_loadout").getString(), Items.DIAMOND_SWORD));
    }

    public void addLoadout(Player player, String name, Item icon) {
        // 1. Save current gear to the CURRENT active slot first
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            loadoutStack.set(activeIndex, new Loadout(handler.saveInventory(false), loadoutStack.get(activeIndex).name(), icon));

            // 2. Push new loadout onto the stack
            loadoutStack.add(new Loadout(new ListTag(), name, icon));

            // 3. Switch to the new one (the new top)
            activeIndex = loadoutStack.size() - 1;
            handler.reset(); // New loadouts start empty
        });
    }


    public static List<ItemStack> getItemStacksFromCuriosNBT(ListTag data) {
        List<ItemStack> stacks = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            CompoundTag slotTag = data.getCompound(i);

            // 1. Get the main items for this slot type
            CompoundTag stacksData = slotTag.getCompound("Stacks");
            if (!stacksData.isEmpty()) {
                ItemStackHandler tempHandler = new ItemStackHandler();
                tempHandler.deserializeNBT(stacksData);
                for (int j = 0; j < tempHandler.getSlots(); j++) {
                    ItemStack stack = tempHandler.getStackInSlot(j);
                    if (!stack.isEmpty()) {
                        stacks.add(stack);
                    }
                }
            }

            // 2. Optional: Get the cosmetic items
            CompoundTag cosmeticData = slotTag.getCompound("Cosmetics");
            if (!cosmeticData.isEmpty()) {
                ItemStackHandler tempHandler = new ItemStackHandler();
                tempHandler.deserializeNBT(cosmeticData);
                for (int j = 0; j < tempHandler.getSlots(); j++) {
                    ItemStack stack = tempHandler.getStackInSlot(j);
                    if (!stack.isEmpty()) {
                        stacks.add(stack);
                    }
                }
            }
        }
        return stacks;
    }

    public void removeLoadout(Player player, int index) {
        if (loadoutStack.isEmpty()) return;
        if (index < 0 || index >= loadoutStack.size()) return;

        dropLoadout(player, loadoutStack.get(index));
        if (index == activeIndex) --activeIndex;
        loadoutStack.remove(index);
    }

    public void dropLoadout(Player player, Loadout loadout) {
        getItemStacksFromCuriosNBT(loadout.items()).forEach(stack -> {
            ItemEntity ent = player.spawnAtLocation(stack, 1.0F);
            RandomSource rand = player.getRandom();
            if (ent != null) {
                ent.setDeltaMovement(ent.getDeltaMovement().add(
                        (rand.nextFloat() - rand.nextFloat()) * 0.1F,
                        rand.nextFloat() * 0.05F,
                        (rand.nextFloat() - rand.nextFloat()) * 0.1F));
            }
        });
    }

    public void switchLoadout(Player player, int index) {
        if (index < 0 || index >= loadoutStack.size()) return;
        Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(player).resolve();
        if (curiosOpt.isPresent()) {
            loadoutStack.set(activeIndex, new Loadout(curiosOpt.get().saveInventory(true), loadoutStack.get(activeIndex).name(), loadoutStack.get(activeIndex).icon()));
            // Switch and load
            activeIndex = index;
            curiosOpt.get().clearCachedSlotModifiers();
            curiosOpt.get().loadInventory(loadoutStack.get(index).items());
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag root = new CompoundTag();
        root.putInt("ActiveIndex", this.activeIndex);

        ListTag stackTag = new ListTag();
        for (Loadout loadout : this.loadoutStack) {
            // loadout.serialize() returns a CompoundTag
            stackTag.add(loadout.serialize());
        }
        root.put("StackData", stackTag);

        return root;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.activeIndex = nbt.getInt("ActiveIndex");
        this.loadoutStack.clear();

        ListTag stackTag = nbt.getList("StackData", Tag.TAG_COMPOUND);

        for (int i = 0; i < stackTag.size(); i++) {
            CompoundTag loadoutTag = stackTag.getCompound(i);
            this.loadoutStack.add(Loadout.deserialize(loadoutTag));
        }

        // Safety check: Ensure "Default" exists if the file was empty
        if (this.loadoutStack.isEmpty()) {
            this.loadoutStack.add(new Loadout(new ListTag(), "Default", Items.DIAMOND_SWORD));
        }
    }

    public List<Loadout> getStoredLoadouts() {
        return loadoutStack;
    }


    public int getActiveLoadout() {
        return activeIndex;
    }

}