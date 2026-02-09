package net.goo.brutality.common.magic.spells.brimwielder;

import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.client.particle.providers.PointToPointParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.tooltip.SpellTooltips;
import net.mcreator.terramity.init.TerramityModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.SIZE;
import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.SPEED;

public class StygianStepSpell extends BrutalitySpell {


    public StygianStepSpell() {
        super(MagicSchool.BRIMWIELDER, List.of(SpellCategory.INSTANT, SpellCategory.TARGET, SpellCategory.UTILITY), "stygian_step", 30, 5, 20, 0, 1, List.of(
                new SpellTooltips.SpellStatComponent(SPEED, 1F, 0.25F, 1F, 5F),
                new SpellTooltips.SpellStatComponent(SIZE, 3, 0, 3F, 3F)
        ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.5F;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 1F;
    }

    @Override
    public int getCooldownLevelScaling() {
        return -1;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        if (!player.onGround()) {
            player.displayClientMessage(Component.translatable("message.brutality.condition.on_ground"), true);
            return false;
        }
        Vec3 lookAngle = player.getLookAngle();
        Vec3 oldPos = player.getPosition(1F);
        float speed = getFinalStat(spellLevel, getStat(SPEED));
        float size = getFinalStat(spellLevel, getStat(SIZE));
        Vec3 moveVec = lookAngle.scale(speed);

        if (player instanceof ServerPlayer) {
            player.push(moveVec.x(), 0, moveVec.z());
            ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));
        }

        DelayedTaskScheduler.queueServerWork(player.level(), 5, () -> {
            player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(size)).forEach(e -> {
                e.hurt(e.damageSources().flyIntoWall(), getFinalDamage(player, spellLevel));
                Vec3 pushAng = e.getPosition(1).subtract(player.getPosition(1));
                pushAng.normalize().scale(speed / 10F);
                e.push(pushAng.x(), pushAng.y(), pushAng.z());
                if (e instanceof ServerPlayer serverPlayer)
                    serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
            });

            PointToPointParticleData<?> pointToPointParticleData = new PointToPointParticleData<>(
                    BrutalityParticles.STYGIAN_STEP_PARTICLE.get(), (float) oldPos.x, (float) oldPos.y, (float) oldPos.z, (float) player.getX(), (float) player.getY(), (float) player.getZ());

            if (player.level() instanceof ServerLevel serverLevel && player instanceof ServerPlayer serverPlayer) {
                serverLevel.sendParticles(pointToPointParticleData, player.getX(), player.getY(), player.getZ(),
                        1, 0, 0, 0, 0);
                serverLevel.playSound(null, player.getOnPos(), TerramityModSounds.HEXED.get(), SoundSource.BLOCKS, 1F, 1F);
                serverPlayer.setDeltaMovement(Vec3.ZERO);
                serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
            }

        });

        return true;
    }

}
