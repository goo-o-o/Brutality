package net.goo.brutality.item.curios;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class ResplendentFeatherCharm extends BrutalityCurioItem {


    public ResplendentFeatherCharm(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {

            if (player.level().isClientSide()) return;

            List<LivingEntity> livingEntities = player.level().
                    getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT.selector(Entity::isOnFire), player, player.getBoundingBox().inflate(12));

            player.heal((float) livingEntities.size() * 0.15F);

            if (player.getHealth() < player.getMaxHealth() * 0.2F) {
                player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 3, 1, false, true));
            }
        }
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }
}
