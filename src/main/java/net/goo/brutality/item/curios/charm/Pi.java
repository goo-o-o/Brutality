package net.goo.brutality.item.curios.charm;

import net.goo.brutality.entity.projectile.generic.PiEntity;
import net.goo.brutality.item.curios.BrutalityMathFunctionCurio;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Pi extends BrutalityMathFunctionCurio {


    public Pi(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler ->
                        handler.findFirstCurio(BrutalityModItems.SCIENTIFIC_CALCULATOR.get()).isPresent()
                )
                .orElse(false);
    }

    boolean canSpawn = false;

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                canSpawn = handler.isEquipped(BrutalityModItems.SCIENTIFIC_CALCULATOR.get());
            });
        }
    }

    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        super.onEquip(slotContext, prevStack, stack);
        LivingEntity owner = slotContext.entity();

        if (!owner.level().isClientSide() && canSpawn)
            for (int i = 0; i < 3; i++) {
                int angleOffset = (int) Math.toRadians(120 * i);

                PiEntity pi = new PiEntity(BrutalityModEntities.PI_ENTITY.get(), owner.level(), angleOffset);
                pi.setPos(owner.getPosition(1));
                pi.setOwner(owner);
                owner.level().addFreshEntity(pi);
            }
    }


    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        LivingEntity owner = slotContext.entity();
        for (PiEntity piEntity : owner.level().getEntitiesOfClass(PiEntity.class, owner.getBoundingBox().inflate(10))) {
            piEntity.discard();
        }
    }
}
