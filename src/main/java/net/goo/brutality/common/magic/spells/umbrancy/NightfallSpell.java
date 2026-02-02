package net.goo.brutality.common.magic.spells.umbrancy;

import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.util.tooltip.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.*;
import static net.goo.brutality.util.tooltip.BrutalityTooltipHelper.SpellStatComponents.DURATION;
import static net.goo.brutality.util.tooltip.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class NightfallSpell extends BrutalitySpell {


    public NightfallSpell() {
        super(MagicSchool.UMBRANCY,
                List.of(CHANNELLING, AOE, UTILITY),
                "nightfall",
                60, 0, 400, 40, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 25, 1, 25F, 50F),
                        new BrutalityTooltipHelper.SpellStatComponent(DURATION, 200, 10, 200F, 400F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0F;
    }

    @Override
    public int getCastTimeLevelScaling() {
        return -1;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        Level level = player.level();
        long originalTime = level.getDayTime();
        int duration = (int) getFinalStat(spellLevel, getStat(DURATION));
        level.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT.ignoreInvisibilityTesting().ignoreLineOfSight(), player,
                player.getBoundingBox().inflate(getFinalStat(spellLevel, getStat(RANGE)) / 2)).forEach(e -> {
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration));
            e.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, duration));
        });

        if (level instanceof ServerLevel serverLevel) {
            serverLevel.setDayTime(18000);

            DelayedTaskScheduler.queueServerWork(serverLevel, duration, () -> serverLevel.setDayTime(originalTime));
        }
        return true;
    }
}
