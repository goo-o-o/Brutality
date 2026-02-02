package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.entity.projectile.generic.PiEntity;
import net.goo.brutality.common.item.curios.BrutalityMathFunctionCurio;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class Pi extends BrutalityMathFunctionCurio {


    public Pi(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        LivingEntity owner = slotContext.entity();

        if (!owner.level().isClientSide())
            for (int i = 0; i < 3; i++) {
                int angleOffset = (int) Math.toRadians(120 * i);

                PiEntity pi = new PiEntity(BrutalityEntities.PI_ENTITY.get(), owner.level(), angleOffset);
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
