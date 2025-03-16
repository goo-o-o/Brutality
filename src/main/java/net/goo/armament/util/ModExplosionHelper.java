package net.goo.armament.util;

import net.goo.armament.entity.explosion.ArmaExplosion;
import net.goo.armament.entity.explosion.NuclearExplosion;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.s2cCustomExplosionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class ModExplosionHelper {


    public static ArmaExplosion customExplode(ArmaExplosion explosion, Level pLevel, boolean pSpawnParticles) {

        if (net.minecraftforge.event.ForgeEventFactory.onExplosionStart(pLevel, explosion)) return explosion;
        explosion.explode();

        explosion.finalizeExplosion(pSpawnParticles);
        return explosion;
    }

    private static Explosion.BlockInteraction getDestroyType(Level pLevel, GameRules.Key<GameRules.BooleanValue> pGameRule) {
        return pLevel.getGameRules().getBoolean(pGameRule) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }


    public static Explosion sendCustomExplode(ExplosionType explosionType, Level pLevel, @Nullable Entity pSource, double pX, double pY, double pZ, float pRadius, boolean pFire, Level.ExplosionInteraction pExplosionInteraction, boolean pSpawnParticles) {

        Explosion.BlockInteraction explosion$blockinteraction1 = switch (pExplosionInteraction) {
            case NONE -> Explosion.BlockInteraction.KEEP;
            case BLOCK -> getDestroyType(pLevel, GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
            case MOB ->
                    net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(pLevel, pSource) ? getDestroyType(pLevel, GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case TNT -> getDestroyType(pLevel, GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
        };

        ArmaExplosion explosion = ModExplosionHelper.customExplode(createExplosion(explosionType, pLevel, pSource, pX, pY, pZ, pRadius, pFire, explosion$blockinteraction1), pLevel, pSpawnParticles);
        if (!explosion.interactsWithBlocks()) {
            explosion.clearToBlow();
        }

        Vec3 pos = explosion.getPosition();
            for (Player player : pLevel.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    if (serverPlayer.distanceToSqr(pos.x, pos.y, pos.z) < 4096.0D) {
                        PacketHandler.sendToPlayer(new s2cCustomExplosionPacket(pos.x, pos.y, pos.z, explosion.getRadius(), explosion.getToBlow(), explosion.getHitPlayers().get(serverPlayer), explosionType), serverPlayer);
                    }
                }

        }

        return explosion;
    }

    public enum ExplosionType {
        DEFAULT,
        NUCLEAR,
        FIRE,
        HOLY
    }


    public static ArmaExplosion createExplosion(ExplosionType type, Level level, @Nullable Entity source, double x, double y, double z, float power, boolean fire, Explosion.BlockInteraction interaction) {
        switch (type) {
            case NUCLEAR:
                return new NuclearExplosion(level, source, x, y, z, power, fire, interaction);
            case FIRE:
//                    return new FireExplosion(level, source, x, y, z, power, fire, interaction);
            case HOLY:
//                    return new MagicExplosion(level, source, x, y, z, power, fire, interaction);
            case DEFAULT:
            default:
                return new ArmaExplosion(level, source, x, y, z, power, fire, interaction);
        }

    }

}
