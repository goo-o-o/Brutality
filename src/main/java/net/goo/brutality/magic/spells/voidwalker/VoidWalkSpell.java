package net.goo.brutality.magic.spells.voidwalker;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class VoidWalkSpell extends BrutalitySpell {


    public VoidWalkSpell() {
        super(MagicSchool.VOIDWALKER, SpellType.SINGLETON_NO_TARGET, "void_walk",
                40, 0, 160, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 20, 10, 0, 250)
                ));
    }


    @Override
    public int getManaCostLevelScaling() {
        return 0;
    }

    @Override
    public int getCooldownLevelScaling() {
        return -10;
    }

    @Override
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {
        BrutalityTooltipHelper.SpellStatComponent range = getStat(RANGE);
        int baseRange = Mth.clamp(range.base() + (range.levelDelta() * spellLevel), range.min(), range.max());

        if (player.level() instanceof ServerLevel serverLevel) {
            BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, baseRange);
            if (blockPos == null) return false;
            BlockPos validPos = getValidPos(serverLevel, blockPos, 3);
            if (validPos == null) return false;
            Vec3 startPos = player.getPosition(1);
            Vec3 blockCenter = validPos.getCenter();

            player.teleportTo(blockCenter.x(), validPos.getY(), blockCenter.z());

            Vec3 endPos = validPos.getCenter();

            Vec3 direction = startPos.vectorTo(endPos);
            double distance = direction.length();
            Vec3 unitDir = direction.normalize();

            for (double d = 0; d < distance; d += 0.5F) {
                Vec3 intervalPos = new Vec3(unitDir.toVector3f()).scale(d);
                Vec3 particlePos = startPos.add(intervalPos);
                ModUtils.sendParticles(serverLevel, TerramityModParticleTypes.ANTIMATTER.get(), true, particlePos, 0.05, 0.05, 0.05, 1, 0);
            }
            ModUtils.sendParticles(serverLevel, TerramityModParticleTypes.ANTIMATTER.get(), true, player, 0, 0, 0, 20, 0);

            serverLevel.sendParticles(TerramityModParticleTypes.ANTIMATTER.get(), player.getX(), player.getY(), player.getZ(),
                    20, 1, 1, 1, 0);

            serverLevel.playSound(null, BlockPos.containing(endPos), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
            player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0, false, false, true));
        }
        return true;
    }

    private BlockPos getValidPos(Level level, BlockPos basePos, int distance) {
        for (int i = 0; i < distance; i++) {
            BlockPos floorPos = basePos.above(i);
            if (!ModUtils.isStandable(level, floorPos)) continue;

            BlockPos air1 = floorPos.above();
            BlockPos air2 = air1.above();

            if (level.getBlockState(air1).isAir() && level.getBlockState(air2).isAir())
                return air1;
        }
        return null;
    }
}
