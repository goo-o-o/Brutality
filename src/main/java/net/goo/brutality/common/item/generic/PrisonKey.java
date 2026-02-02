package net.goo.brutality.common.item.generic;

import net.goo.brutality.common.item.base.BrutalityGenericItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.List;

public class PrisonKey extends BrutalityGenericItem {

    public PrisonKey(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                handler.findCurios(BrutalityItems.HANDCUFFS.get()).forEach(slotResult -> {
                    ItemStack handcuffStack = slotResult.stack();
                    handler.setEquippedCurio("hands", slotResult.slotContext().index(), ItemStack.EMPTY);
                    livingEntity.spawnAtLocation(handcuffStack);
                });
                stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));


            });
        }

        return super.onLeftClickEntity(stack, player, entity);
    }
}
