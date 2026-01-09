package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModMobEffects;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class MechanicalAorta extends BrutalityCurioItem {


    public MechanicalAorta(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            if (player.tickCount % 5 == 0) {
                if (player.hasEffect(BrutalityModMobEffects.ENRAGED.get())) {
                    player.invulnerableTime = 0;
                    player.hurt(player.damageSources().indirectMagic(player, null), 1);
                }
            }
        }
    }

}
