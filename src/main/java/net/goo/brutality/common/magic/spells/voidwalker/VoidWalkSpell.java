package net.goo.brutality.common.magic.spells.voidwalker;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.ParticleHelper;
import net.goo.brutality.util.tooltip.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.*;
import static net.goo.brutality.util.tooltip.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class VoidWalkSpell extends BrutalitySpell {


    public VoidWalkSpell() {
        super(MagicSchool.VOIDWALKER,
                List.of(INSTANT, TARGET, UTILITY),
                "void_walk",
                40, 0, 160, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 20, 10, 0F, 250F)
                ));
    }


    @Override
    public int getCooldownLevelScaling() {
        return -10;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        float baseRange = getFinalStat(spellLevel, getStat(RANGE));

        BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, baseRange);
        if (blockPos == null) {
            player.displayClientMessage(Component.translatable("message." + Brutality.MOD_ID + ".condition.teleport_failed"), true);
            return false;
        }
        BlockPos validPos = getValidPos(player.level(), blockPos, 3);
        if (validPos == null) {
            player.displayClientMessage(Component.translatable("message." + Brutality.MOD_ID + ".condition.teleport_failed"), true);
            return false;
        }

        Vec3 startPos = player.getPosition(1);
        Vec3 blockCenter = validPos.getCenter();

        player.teleportTo(blockCenter.x(), validPos.getY() + 0.125, blockCenter.z());

        Vec3 endPos = validPos.getCenter();

        Vec3 direction = startPos.vectorTo(endPos);
        double distance = direction.length();
        Vec3 unitDir = direction.normalize();

        if (player.level() instanceof ServerLevel serverLevel) {
            for (double d = 0; d < distance; d += 0.5F) {
                Vec3 intervalPos = new Vec3(unitDir.toVector3f()).scale(d);
                Vec3 particlePos = startPos.add(intervalPos);
                ParticleHelper.sendParticles(serverLevel, TerramityModParticleTypes.ANTIMATTER.get(), true, particlePos, 0.05, 0.05, 0.05, 1, 0);
            }
            ParticleHelper.sendParticles(serverLevel, TerramityModParticleTypes.ANTIMATTER.get(), true, player, 0, 0, 0, 20, 0);

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
            if (!level.getBlockState(floorPos).isFaceSturdy(level, floorPos, Direction.UP, SupportType.CENTER)) continue;

            BlockPos air1 = floorPos.above();
            BlockPos air2 = air1.above();

            if (level.getBlockState(air1).isAir() && level.getBlockState(air2).isAir())
                return air1;
        }
        return null;
    }
}
