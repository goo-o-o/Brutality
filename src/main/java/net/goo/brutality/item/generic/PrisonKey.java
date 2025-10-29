package net.goo.brutality.item.generic;

import net.goo.brutality.registry.BrutalityModItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public class PrisonKey extends Item {
    public PrisonKey(Properties pProperties) {
        super(pProperties.durability(1));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                handler.findCurios(BrutalityModItems.HANDCUFFS.get()).forEach(slotResult -> {
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
