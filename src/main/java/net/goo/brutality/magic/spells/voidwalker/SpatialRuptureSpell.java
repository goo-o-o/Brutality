package net.goo.brutality.magic.spells.voidwalker;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.network.ClientboundParticlePacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
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

import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.AOE;
import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.INSTANT;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.SIZE;

public class SpatialRuptureSpell extends BrutalitySpell {

    public SpatialRuptureSpell() {
        super(MagicSchool.VOIDWALKER,
                List.of(INSTANT, AOE),
                "spatial_rupture",
                30, 2, 80, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(SIZE, 3, 1, 0, 100)
                ));
    }


    @Override
    public int getManaCostLevelScaling() {
        return 5;
    }


    @Override
    public int getCooldownLevelScaling() {
        return 0;
    }

    @Override
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {


        if (player.level() instanceof ServerLevel serverLevel) {
            double playerX = player.getX(), playerY = player.getY(0.5), playerZ = player.getZ();
            float offset = 0.1F + 0.025F * (spellLevel + 1);
            for (int i = 0; i < 16 + spellLevel * 4; i++) {
                PacketHandler.sendToNearbyClients(serverLevel, playerX, playerY, playerZ, 128, new ClientboundParticlePacket(
                        TerramityModParticleTypes.ANTIMATTER.get(), true, (float) playerX, (float) playerY, (float) playerZ, 0, 0, 0,
                        (player.getRandom().nextFloat() - 0.5F) * offset,
                        (player.getRandom().nextFloat() - 0.5F) * offset,
                        (player.getRandom().nextFloat() - 0.5F) * offset, 1
                ));
            }
            WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityModParticles.ANTIMATTER_WAVE.get(), getFinalStat(spellLevel, getStat(SIZE)), 40);

            PacketHandler.sendToNearbyClients(serverLevel, playerX, playerY, playerZ, 128, new ClientboundParticlePacket(
                    waveParticleData, true, (float) playerX, (float) playerY, (float) playerZ, 0, 0, 0,
                    0, 0, 0, 1
            ));


            ModUtils.applyWaveEffect(serverLevel, playerX, playerY, playerZ, Entity.class, waveParticleData, e -> (e instanceof Projectile || e instanceof LivingEntity) && e != player,
                    e -> {
                        Vec3 ePos = e.getPosition(1);
                        Vec3 playerToEntity = player.getPosition(1).vectorTo(ePos).normalize();
                        playerToEntity.add(0, 0.1, 0).scale(0.2 * spellLevel);

                        e.push(playerToEntity.x, playerToEntity.y, playerToEntity.z);
                        e.hurt(e.damageSources().flyIntoWall(), getFinalDamage(player, spellLevel));
                        if (e instanceof Player) {
                            ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(e));
                        }
                    });


            serverLevel.playSound(null, player.getOnPos(), BrutalityModSounds.BASS_BOOM.get(), SoundSource.PLAYERS, spellLevel, 1F);
        }
        return true;
    }
}
