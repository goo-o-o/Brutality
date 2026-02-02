package net.goo.brutality.common.item.curios.anklet;

import net.goo.brutality.common.item.base.BrutalityAnkletItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class AnkleMonitor extends BrutalityAnkletItem {


    public AnkleMonitor(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (!(slotContext.entity() instanceof ServerPlayer player) || player.tickCount % 10 != 0) {
            return;
        }
        boolean applyEffects;
        BlockPos pos = BlockPos.containing(player.getPosition(1));

        if (player.getRespawnDimension().equals(player.level().dimension())) {
            BlockPos spawnPos = player.getRespawnPosition();
            if (spawnPos == null) {
                spawnPos = player.level().getSharedSpawnPos();
            }
            applyEffects = pos.distSqr(spawnPos) > 22500;
        } else {
            applyEffects = true;
        }

        if (applyEffects) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 11, 1, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 11, 0, false, false));
        }
    }

}
