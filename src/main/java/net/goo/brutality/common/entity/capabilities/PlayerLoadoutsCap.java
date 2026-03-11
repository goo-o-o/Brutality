package net.goo.brutality.common.entity.capabilities;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.loadouts.Loadout;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.event.CurioDropsEvent;
import top.theillusivec4.curios.api.event.DropRulesEvent;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.CuriosConfig;
import top.theillusivec4.curios.common.capability.CurioInventoryCapability;

import java.util.*;
import java.util.function.Predicate;

public class PlayerLoadoutsCap implements IBrutalityData {
    // Index 0 is always "Default"
    private final List<Loadout> loadoutStack = new ArrayList<>();
    private int activeIndex = 0;
    public static final ResourceLocation DEFAULT_LOADOUT_BUTTON = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "textures/gui/loadout/arm_up_green.png");

    public PlayerLoadoutsCap() {
        loadoutStack.add(new Loadout(new ListTag(), Component.translatable("message." + Brutality.MOD_ID + ".default_loadout").getString(), DEFAULT_LOADOUT_BUTTON));
    }

    public void addLoadout(Player player, String name, ResourceLocation icon) {
        CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
            // 1. Save current gear to the current slot using its OWN name and icon
            Loadout current = loadoutStack.get(activeIndex);
            loadoutStack.set(activeIndex, new Loadout(handler.saveInventory(false), current.name(), current.icon()));

            // 2. Create the brand new empty loadout
            loadoutStack.add(new Loadout(new ListTag(), name, icon));

            // 3. Move to it
            activeIndex = loadoutStack.size() - 1;

            // 4. Clear the player's active Curios so they start fresh
            handler.reset();
        });
    }

    public static ICuriosItemHandler createHandlerFromLoadout(Player player, Loadout loadout) {
        return createHandlerFromNbt(player, loadout.data());
    }

    public static ICuriosItemHandler createHandlerFromNbt(Player player, ListTag loadoutNbt) {
        ICuriosItemHandler virtualInventory = new CurioInventoryCapability.CurioInventoryWrapper(player);
        virtualInventory.loadInventory(loadoutNbt);
        return virtualInventory;
    }

    public void switchLoadout(Player player, int index) {
        if (index < 0 || index > loadoutStack.size()) return;
        Optional<ICuriosItemHandler> curiosOpt = CuriosApi.getCuriosInventory(player).resolve();
        if (curiosOpt.isPresent()) {
            loadoutStack.set(activeIndex, new Loadout(curiosOpt.get().saveInventory(true), loadoutStack.get(activeIndex).name(), loadoutStack.get(activeIndex).icon()));
            activeIndex = index;
            curiosOpt.get().clearCachedSlotModifiers();
            curiosOpt.get().loadInventory(loadoutStack.get(index).data());
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
            this.loadoutStack.add(new Loadout(new ListTag(), "Default", DEFAULT_LOADOUT_BUTTON));
        }
    }

    public List<Loadout> getStoredLoadouts() {
        return loadoutStack;
    }


    public int getActiveLoadout() {
        return activeIndex;
    }

    public static void handleDropEvent(LivingDropsEvent evt, LivingEntity livingEntity) {
        if (!livingEntity.isSpectator() && livingEntity instanceof Player player) {
            livingEntity.getCapability(BrutalityCapabilities.LOADOUTS).ifPresent(cap -> {
                List<Loadout> storedLoadouts = cap.getStoredLoadouts();
                for (int i = 0, storedLoadoutsSize = storedLoadouts.size(); i < storedLoadoutsSize; i++) {
                    Loadout loadout = storedLoadouts.get(i);
                    if (cap.activeIndex == i) continue;

                    ICuriosItemHandler handler = PlayerLoadoutsCap.createHandlerFromLoadout(player, loadout);
                    Collection<ItemEntity> drops = evt.getDrops();
                    Collection<ItemEntity> curioDrops = new ArrayList<>();
                    Map<String, ICurioStacksHandler> curios = handler.getCurios();
                    DropRulesEvent dropRulesEvent = new DropRulesEvent(livingEntity, handler, evt.getSource(), evt.getLootingLevel(), evt.isRecentlyHit());
                    MinecraftForge.EVENT_BUS.post(dropRulesEvent);
                    List<Tuple<Predicate<ItemStack>, ICurio.DropRule>> dropRules = dropRulesEvent.getOverrides();
                    boolean keepInventory = false;
                    if (livingEntity instanceof Player) {
                        keepInventory = livingEntity.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY);
                        if (CuriosConfig.SERVER.keepCurios.get() != CuriosConfig.KeepCurios.DEFAULT) {
                            keepInventory = CuriosConfig.SERVER.keepCurios.get() == CuriosConfig.KeepCurios.ON;
                        }
                    }

                    boolean finalKeepInventory = keepInventory;
                    curios.forEach((id, stacksHandler) -> {
                        PlayerLoadoutsCap.handleDrops(id, livingEntity, dropRules, stacksHandler.getRenders(), stacksHandler.getStacks(), false, curioDrops, finalKeepInventory, evt);
                        PlayerLoadoutsCap.handleDrops(id, livingEntity, dropRules, stacksHandler.getRenders(), stacksHandler.getCosmeticStacks(), true, curioDrops, finalKeepInventory, evt);
                    });
                    if (!MinecraftForge.EVENT_BUS.post(new CurioDropsEvent(livingEntity, handler, evt.getSource(), curioDrops, evt.getLootingLevel(), evt.isRecentlyHit()))) {
                        drops.addAll(curioDrops);
                    }
                    cap.loadoutStack.set(i, new Loadout(
                            handler.saveInventory(false),
                            loadout.name(),
                            loadout.icon()
                    ));
                }
            });
        }
    }

    public static void handleDrops(String identifier, LivingEntity livingEntity, List<Tuple<Predicate<ItemStack>, ICurio.DropRule>> dropRules, NonNullList<Boolean> renders, IDynamicStackHandler stacks, boolean cosmetic, Collection<ItemEntity> drops, boolean keepInventory, LivingDropsEvent evt) {
        for (int i = 0; i < stacks.getSlots(); ++i) {
            ItemStack stack = stacks.getStackInSlot(i);
            SlotContext slotContext = new SlotContext(identifier, livingEntity, i, cosmetic, renders.size() > i && renders.get(i));
            if (!stack.isEmpty()) {
                ICurio.DropRule dropRuleOverride = null;

                for (Tuple<Predicate<ItemStack>, ICurio.DropRule> override : dropRules) {
                    if (override.getA().test(stack)) {
                        dropRuleOverride = override.getB();
                    }
                }

                ICurio.DropRule dropRule = dropRuleOverride != null ? dropRuleOverride : CuriosApi.getCurio(stack).map((curio) -> curio.getDropRule(slotContext, evt.getSource(), evt.getLootingLevel(), evt.isRecentlyHit())).orElse(ICurio.DropRule.DEFAULT);
                if (dropRule == ICurio.DropRule.DEFAULT) {
                    dropRule = CuriosApi.getSlot(identifier, livingEntity.level()).map(ISlotType::getDropRule).orElse(ICurio.DropRule.DEFAULT);
                }

                if ((dropRule != ICurio.DropRule.DEFAULT || !keepInventory) && dropRule != ICurio.DropRule.ALWAYS_KEEP) {
                    if (!EnchantmentHelper.hasVanishingCurse(stack) && dropRule != ICurio.DropRule.DESTROY) {
                        drops.add(getDroppedItem(stack, livingEntity));
                    }

                    stacks.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
        }

    }

    @Override
    public boolean syncOnDeath() {
        return true;
    }

    private static ItemEntity getDroppedItem(ItemStack droppedItem, LivingEntity livingEntity) {
        double d0 = livingEntity.getY() - (double) 0.3F + (double) livingEntity.getEyeHeight();
        ItemEntity entityitem = new ItemEntity(livingEntity.level(), livingEntity.getX(), d0, livingEntity.getZ(), droppedItem);
        entityitem.setPickUpDelay(40);
        float f = livingEntity.level().random.nextFloat() * 0.5F;
        float f1 = livingEntity.level().random.nextFloat() * ((float) Math.PI * 2F);
        entityitem.setDeltaMovement(-Mth.sin(f1) * f, 0.2F, Mth.cos(f1) * f);
        return entityitem;
    }

}