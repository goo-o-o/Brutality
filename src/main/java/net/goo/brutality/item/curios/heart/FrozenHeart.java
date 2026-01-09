package net.goo.brutality.item.curios.heart;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class FrozenHeart extends BrutalityCurioItem {

    public FrozenHeart(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (entity.tickCount % 10 == 0) {
            Level level = entity.level();

            List<LivingEntity> nearbyEntities = level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, entity, entity.getBoundingBox().inflate(10));

            for (LivingEntity nearbyEntity : nearbyEntities) {
                nearbyEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 11, 0));
                nearbyEntity.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 11, 0));
            }
        }
    }

}
