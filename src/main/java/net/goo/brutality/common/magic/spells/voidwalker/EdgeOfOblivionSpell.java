package net.goo.brutality.common.magic.spells.voidwalker;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.network.clientbound.ClientboundParticlePacket;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.BrutalityTooltipHelper;
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

import static net.goo.brutality.util.tooltip.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class EdgeOfOblivionSpell extends BrutalitySpell {

    public EdgeOfOblivionSpell() {
        super(MagicSchool.VOIDWALKER,
                List.of(SpellCategory.INSTANT, SpellCategory.AOE),
                "edge_of_oblivion",
                30, 2, 80, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 3, 1, 0F, 100F)
                ));
    }


    @Override
    public float getManaCostLevelScaling() {
        return 5;
    }


    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        float radius = getFinalStat(spellLevel, getStat(RANGE));

        if (player.level() instanceof ServerLevel serverLevel) {
            double playerX = player.getX(), playerY = player.getY(), playerZ = player.getZ();
            float offset = 0.1F + 0.025F * (spellLevel + 1);
            for (int i = 0; i < 16 + spellLevel * 4; i++) {
                PacketHandler.sendToNearbyClients(serverLevel, playerX, playerY, playerZ, 128, new ClientboundParticlePacket(
                        TerramityModParticleTypes.ANTIMATTER.get(), true, (float) playerX, (float) playerY, (float) playerZ, 0, 0, 0,
                        (player.getRandom().nextFloat() - 0.5F) * offset,
                        (player.getRandom().nextFloat() - 0.5F) * offset,
                        (player.getRandom().nextFloat() - 0.5F) * offset, 1
                ));
            }
            WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityParticles.ANTIMATTER_WAVE.get(), radius, 40);

            serverLevel.sendParticles(
                    waveParticleData,
                    playerX,
                    playerY + player.getBbHeight() / 3,
                    playerZ,
                    1,
                    0, 0, 0,
                    0
            );

            ModUtils.applyWaveEffect(serverLevel, player, Entity.class, waveParticleData, e -> (e instanceof Projectile || e instanceof LivingEntity) && e != player,
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


            serverLevel.playSound(null, player.getOnPos(), BrutalitySounds.BASS_BOOM.get(), SoundSource.PLAYERS, spellLevel, 1F);
        }
        return true;
    }
}
