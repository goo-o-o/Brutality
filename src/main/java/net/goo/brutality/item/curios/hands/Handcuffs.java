package net.goo.brutality.item.curios.hands;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Handcuffs extends BrutalityCurioItem {
    public Handcuffs(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        DispenserBlock.registerBehavior(this, DISPENSE_ITEM_BEHAVIOUR);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.HANDS;
    }

    public static final DispenseItemBehavior DISPENSE_ITEM_BEHAVIOUR = new DefaultDispenseItemBehavior() {
        @Override
        protected @NotNull ItemStack execute(BlockSource pSource, ItemStack pStack) {
            return Handcuffs.equipToFirstEmptySlot(pSource, pStack) ? pStack : super.execute(pSource, pStack);
        }
    };

    public static boolean equipToFirstEmptySlot(BlockSource source, ItemStack stack) {
        BlockPos pos = source.getPos().relative(source.getBlockState().getValue(DispenserBlock.FACING));
        List<LivingEntity> list = source.getLevel().getEntitiesOfClass(LivingEntity.class,
                new AABB(pos), EntitySelector.NO_SPECTATORS);
        if (list.isEmpty()) return false;

        LivingEntity target = list.get(0);

        return CuriosApi.getCuriosInventory(target)
                .map(handler -> {
                    Optional<ICurio> curioOpt = CuriosApi.getCurio(stack).resolve();
                    if (curioOpt.isEmpty()) return false;
                    ICurio curio = curioOpt.get();

                    Set<String> validSlots = CuriosApi.getItemStackSlots(stack, target).keySet();

                    for (String id : validSlots) {
                        ICurioStacksHandler curioHandler = handler.getCurios().get(id);
                        if (curioHandler == null) continue;

                        IDynamicStackHandler stacks = curioHandler.getStacks();
                        NonNullList<Boolean> renders = curioHandler.getRenders();

                        for (int i = 0; i < stacks.getSlots(); i++) {
                            if (!stacks.getStackInSlot(i).isEmpty()) continue;

                            SlotContext ctx = new SlotContext(id, target, i, false,
                                    renders.size() > i && renders.get(i));

                            if (curio.canEquip(ctx)) {  // Use canEquipFromUse!
                                ItemStack toEquip = stack.split(1);  // Safe: split 1
                                stacks.setStackInSlot(i, toEquip);
                                curio.onEquipFromUse(ctx);
                                return true;
                            }
                        }
                    }
                    return false;
                })
                .orElse(false);
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.BINDING_CURSE, 1);
        return stack;
    }

    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            ICuriosItemHandler handler = CuriosApi.getCuriosInventory(livingEntity).orElse(null);
            return !handler.isEquipped(this);
        }

        return super.canEquip(slotContext, stack);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (livingEntity.tickCount % 20 == 0) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 21, 1, false, false, true));
                livingEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 21, 2, false, false, true));
            }
        }
    }
}
