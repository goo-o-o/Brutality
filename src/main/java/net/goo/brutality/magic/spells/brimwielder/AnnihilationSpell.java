package net.goo.brutality.magic.spells.brimwielder;

import net.goo.brutality.entity.spells.brimwielder.AnnihilationEntity;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.particle.providers.FlatParticleData;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.*;

public class AnnihilationSpell extends BrutalitySpell {


    public AnnihilationSpell() {
        super(MagicSchool.BRIMWIELDER, List.of(SpellCategory.CHANNELLING, SpellCategory.AOE), "annihilation", 100, 5, 200, 80, 1, List.of(
                new BrutalityTooltipHelper.SpellStatComponent(SIZE, 5, 1, 5F, 15F),
                new BrutalityTooltipHelper.SpellStatComponent(CHANCE, 5, 3, 5F, 50F),
                new BrutalityTooltipHelper.SpellStatComponent(QUANTITY, 25, 5, 25F, 75F)));
    }

    @Override
    public int getCastTimeLevelScaling() {
        return 5;
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.5F;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 5;
    }

    @Override
    public int getCooldownLevelScaling() {
        return 10;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        if (!player.onGround()) {
            player.displayClientMessage(Component.translatable("message.brutality.condition.on_ground"), true);
            return false;
        }
        float radius = getFinalStat(spellLevel, getStat(SIZE));
        int quantity = (int) getFinalStat(spellLevel, getStat(QUANTITY));
        int globalCount = 1;
        Vec3 spawnPos = player.getPosition(1);

        if (player.level() instanceof ServerLevel serverLevel) {
            List<Vec3> points = new ArrayList<>();
            int pointCount = 5;
            for (int i = 0; i < pointCount; i++) {
                double angle = 2 * Math.PI * i / pointCount - Math.PI / 2;
                double x = spawnPos.x() + radius * Math.cos(angle);
                double y = spawnPos.y() + Mth.nextDouble(player.getRandom(), 15, 20);
                double z = spawnPos.z() + radius * Math.sin(angle);
                points.add(new Vec3(x, y, z));
            }

            int[] starOrder = {0, 2, 4, 1, 3, 0}; // Indices for continuous star path
            List<Vec3> lancePositions = new ArrayList<>();
            int segmentsPerEdge = quantity / pointCount;

            for (int i = 0; i < pointCount; i++) {
                Vec3 current = points.get(starOrder[i]);
                Vec3 next = points.get(starOrder[i + 1]);
                Vec3 direction = next.subtract(current);
                for (int j = 0; j < segmentsPerEdge; j++) {
                    double t = (double) j / segmentsPerEdge;
                    Vec3 finalPos = current.add(direction.scale(t));
                    lancePositions.add(finalPos);
                }
            }

            for (Vec3 finalPos : lancePositions) {
                AnnihilationEntity annihilationEntity = new AnnihilationEntity(BrutalityModEntities.ANNIHILATION_ENTITY.get(), serverLevel);
                annihilationEntity.setPos(finalPos.x, finalPos.y, finalPos.z);
                annihilationEntity.setOwner(player);
                annihilationEntity.setSpellLevel(spellLevel);
                annihilationEntity.setBaseDamage(getFinalDamage(player, spellLevel));
                DelayedTaskScheduler.queueServerWork(serverLevel, globalCount, () -> serverLevel.addFreshEntity(annihilationEntity));
                globalCount++;
            }
        } else {
            FlatParticleData<?> data = new FlatParticleData<>(BrutalityModParticles.HEXING_CIRCLE_PARTICLE.get(), radius);
            player.level().addAlwaysVisibleParticle(data, true, spawnPos.x, spawnPos.y + 0.01F, spawnPos.z - 0.5F, 0, 0, 0);
        }
        return true;
    }
}
