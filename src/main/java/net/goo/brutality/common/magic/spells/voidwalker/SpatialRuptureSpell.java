package net.goo.brutality.common.magic.spells.voidwalker;

import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.clientbound.ClientboundParticlePacket;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.AOE;
import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.INSTANT;
import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.SIZE;

public class SpatialRuptureSpell extends BrutalitySpell {

    public SpatialRuptureSpell() {
        super(MagicSchool.VOIDWALKER,
                List.of(INSTANT, AOE),
                "spatial_rupture",
                30, 2, 80, 0, 1, List.of(
                        new SpellTooltipRenderer.SpellStatComponent(SIZE, 3, 1, 0F, 100F)
                ));
    }


    @Override
    public float getManaCostLevelScaling() {
        return 5;
    }


    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityParticles.ANTIMATTER_WAVE.get(), getFinalStat(spellLevel, getStat(SIZE)), 40);
        double playerX = player.getX(), playerY = player.getY(0.5), playerZ = player.getZ();

        if (player.level() instanceof ServerLevel serverLevel) {
            float offset = 0.1F + 0.025F * (spellLevel + 1);
            for (int i = 0; i < 16 + spellLevel * 4; i++) {
                PacketHandler.sendToNearbyClients(serverLevel, playerX, playerY, playerZ, 128, new ClientboundParticlePacket(
                        TerramityModParticleTypes.ANTIMATTER.get(), true, (float) playerX, (float) playerY, (float) playerZ, 0, 0, 0,
                        (player.getRandom().nextFloat() - 0.5F) * offset,
                        (player.getRandom().nextFloat() - 0.5F) * offset,
                        (player.getRandom().nextFloat() - 0.5F) * offset, 1
                ));
            }

            ModUtils.applyWaveEffect(serverLevel, playerX, playerY, playerZ, Entity.class, waveParticleData, e -> (e instanceof Projectile || e instanceof LivingEntity) && e != player,
                    e -> {
                        Vec3 ePos = e.getPosition(1);
                        Vec3 playerToEntity = player.getPosition(1).vectorTo(ePos).normalize();
                        playerToEntity.add(0, 0.1, 0).scale(0.2 * spellLevel);

                        e.push(playerToEntity.x, playerToEntity.y, playerToEntity.z);
                        e.hurt(e.damageSources().flyIntoWall(), getActualDamage(player, spellLevel));
                        if (e instanceof Player) {
                            ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(e));
                        }
                    });


            serverLevel.playSound(null, player.getOnPos(), BrutalitySounds.BASS_BOOM.get(), SoundSource.PLAYERS, spellLevel, 1F);
        } else {
            player.level().addParticle(waveParticleData, true, playerX, playerY, playerZ, 0, 0, 0);
        }
        return true;
    }
}
