package net.goo.brutality.item.curios.head;

import net.goo.brutality.item.curios.base.BaseHeadCurio;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class BikersHelmet extends BaseHeadCurio {


    public BikersHelmet(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            AABB aabb = new AABB(livingEntity.getEyePosition(), livingEntity.getEyePosition()).inflate(0.25F);

            List<Entity> projectilesNearHead = livingEntity.level().getEntities(livingEntity, aabb, e -> (e instanceof Projectile projectile) && projectile.getOwner() != livingEntity);
            if (projectilesNearHead.isEmpty()) return;
            projectilesNearHead.forEach(proj -> proj.setDeltaMovement(proj.getDeltaMovement().scale(-1)));

        }
    }
}
