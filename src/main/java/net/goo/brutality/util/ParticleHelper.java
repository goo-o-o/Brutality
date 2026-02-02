package net.goo.brutality.util;

import net.goo.brutality.Brutality;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

/**
 * Utility class providing helper methods to interact with particles in both client and server worlds.
 * This includes spawning and sending particle effects with varying configurations.
 */
public class ParticleHelper {

    /**
     * Adds particles to the provided client-level instance, either as a single particle or a group of particles,
     * based on the specified parameters.
     *
     * @param level         The {@link ClientLevel} where the particles will be added.
     * @param pType         The type of particle to be added, extending {@link ParticleOptions}.
     * @param override      Whether to override existing particle conditions.
     * @param posX          The X position where the particles will spawn.
     * @param posY          The Y position where the particles will spawn.
     * @param posZ          The Z position where the particles will spawn.
     * @param particleCount The number of particles to spawn. If 0, only one particle is added with a fixed offset.
     * @param xDelta        The standard deviation of the X offset or variability when spawning multiple particles.
     * @param yDelta        The standard deviation of the Y offset or variability when spawning multiple particles.
     * @param zDelta        The standard deviation of the Z offset or variability when spawning multiple particles.
     * @param speed         The speed multiplier of the particles' direction and movement.
     */
    public static <T extends ParticleOptions> void addParticles(ClientLevel level, T pType, boolean override, double posX, double posY, double posZ, int particleCount, double xDelta, double yDelta, double zDelta, double speed) {
        RandomSource random = level.random;
        if (particleCount == 0) {
            double xOffset = speed * xDelta;
            double yOffset = speed * yDelta;
            double zOffset = speed * zDelta;
            try {
                level.addParticle(pType, override, posX, posY, posZ, xOffset, yOffset, zOffset);
            } catch (Throwable throwable1) {
                Brutality.LOGGER.warn("Could not spawn particle effect {}", pType);
            }
        } else {
            for (int i = 0; i < particleCount; ++i) {
                double xOffset = random.nextGaussian() * xDelta;
                double yOffset = random.nextGaussian() * yDelta;
                double zOffset = random.nextGaussian() * zDelta;
                double xSpeed = random.nextGaussian() * speed;
                double ySpeed = random.nextGaussian() * speed;
                double zSpeed = random.nextGaussian() * speed;

                try {
                    level.addParticle(pType, override, posX + xOffset, posY + yOffset, posZ + zOffset, xSpeed, ySpeed, zSpeed);
                } catch (Throwable throwable) {
                    Brutality.LOGGER.warn("Could not spawn particle effect {}", pType);
                }
            }
        }
    }

    /**
     * Sends particle effects to all players within the given {@link ServerLevel} who are near the specified position.
     *
     * @param serverLevel   The {@link ServerLevel} instance where particles should be sent.
     * @param pType         The type of particle to be displayed, extending {@link ParticleOptions}.
     * @param longDistance  If {@code true}, particles will be visible from a greater distance.
     * @param pPosX         The X-coordinate where the particles will spawn.
     * @param pPosY         The Y-coordinate where the particles will spawn.
     * @param pPosZ         The Z-coordinate where the particles will spawn.
     * @param pParticleCount The number of particles to spawn.
     * @param pXOffset      The X-axis offset applied to particle positions.
     * @param pYOffset      The Y-axis offset applied to particle positions.
     * @param pZOffset      The Z-axis offset applied to particle positions.
     * @param pSpeed        The speed modifier for particle movement.
     * @return The number of players who successfully received the particle packet.
     */
    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType, boolean longDistance, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXOffset, double pYOffset, double pZOffset, double pSpeed) {
        ClientboundLevelParticlesPacket clientboundlevelparticlespacket = new ClientboundLevelParticlesPacket(
                pType, longDistance, pPosX, pPosY, pPosZ, (float) pXOffset, (float) pYOffset, (float) pZOffset, (float) pSpeed, pParticleCount
        );
        int i = 0;
        for (ServerPlayer serverPlayer : serverLevel.players()) {
            if (sendParticles(serverLevel, serverPlayer, longDistance, pPosX, pPosY, pPosZ, clientboundlevelparticlespacket)) {
                ++i;
            }
        }
        return i;
    }

    /**
     * Sends particles to all players within the specified {@link ServerLevel} near the position of the given entity.
     * The particle effects will originate around the entity with the specified offsets, speed, and count.
     *
     * @param serverLevel   The {@link ServerLevel} instance where particles should be sent.
     * @param pType         The type of particle to be displayed, extending {@link ParticleOptions}.
     * @param longDistance  If {@code true}, particles will be visible from a greater distance.
     * @param toSpawnOn     The {@link Entity} around which the particles will spawn.
     * @param pXOffset      The X-axis offset applied to the particle positions in relation to the entity.
     * @param pYOffset      The Y-axis offset applied to the particle positions in relation to the entity.
     * @param pZOffset      The Z-axis offset applied to the particle positions in relation to the entity.
     * @param pParticleCount The number of particles to spawn.
     * @param pSpeed        The speed modifier for particle movement.
     * @return The number of players who successfully received the particle packet.
     */
    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, Entity toSpawnOn, double pXOffset,
                                                                double pYOffset, double pZOffset, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, toSpawnOn.getX(), toSpawnOn.getY(0.5), toSpawnOn.getZ(), pParticleCount, pXOffset, pYOffset, pZOffset, pSpeed);
    }

    /**
     * Sends particle effects to all players within the given {@link ServerLevel} who are near the specified position.
     * This method determines particle positions based on the provided {@link Vec3} spawn position and other parameters,
     * and sends them to the server for rendering.
     *
     * @param serverLevel   The {@link ServerLevel} instance where particles should be sent.
     * @param pType         The type of particle to be displayed, extending {@link ParticleOptions}.
     * @param longDistance  If {@code true}, particles will be visible from a greater distance.
     * @param spawnPos      A {@link Vec3} specifying the exact spawn position of the particles.
     * @param pXOffset      The X-axis offset applied to particle positions.
     * @param pYOffset      The Y-axis offset applied to particle positions.
     * @param pZOffset      The Z-axis offset applied to particle positions.
     * @param pParticleCount The number of particles to spawn.
     * @param pSpeed        The speed modifier for particle movement.
     * @return The number of players who successfully received the particle packet.
     */
    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, Vec3 spawnPos,
                                                                double pXOffset, double pYOffset, double pZOffset, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, spawnPos.x, spawnPos.y, spawnPos.z, pParticleCount, pXOffset, pYOffset, pZOffset, pSpeed);
    }

    /**
     * Sends particle effects to all players within the given {@link ServerLevel} who are near the specified position.
     * This variant of the method does not use individual axis offsets and sets them to zero internally.
     *
     * @param serverLevel   The {@link ServerLevel} instance where particles should be sent.
     * @param pType         The type of particle to be displayed, extending {@link ParticleOptions}.
     * @param longDistance  If {@code true}, particles will be visible from a greater distance.
     * @param pPosX         The X-coordinate where the particles will spawn.
     * @param pPosY         The Y-coordinate where the particles will spawn.
     * @param pPosZ         The Z-coordinate where the particles will spawn.
     * @param pParticleCount The number of particles to spawn.
     * @param pSpeed        The speed modifier for particle movement.
     * @return The number of players who successfully received the particle packet.
     */
    public static <T extends ParticleOptions> int sendParticles(ServerLevel serverLevel, T pType,
                                                                boolean longDistance, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pSpeed) {
        return sendParticles(serverLevel, pType, longDistance, pPosX, pPosY, pPosZ, pParticleCount, 0, 0, 0, pSpeed);
    }

    /**
     * Sends a particle effect to the specified player if they are within the proper range
     * and belong to the same {@link ServerLevel} instance as specified.
     *
     * @param serverLevel   The {@link ServerLevel} where the particles should be sent.
     * @param pPlayer       The {@link ServerPlayer} who will receive the particle effect.
     * @param pLongDistance Whether the particle effect is intended to be visible over long distances.
     * @param pPosX         The X-coordinate of the particle effect's position.
     * @param pPosY         The Y-coordinate of the particle effect's position.
     * @param pPosZ         The Z-coordinate of the particle effect's position.
     * @param pPacket       The {@link Packet} containing the particle data to send to the player.
     * @return {@code true} if the particle effect was successfully sent to the player;
     *         {@code false} otherwise.
     */
    private static boolean sendParticles(ServerLevel serverLevel, ServerPlayer pPlayer, boolean pLongDistance, double pPosX, double pPosY, double pPosZ, Packet<?> pPacket) {
        if (pPlayer.level() != serverLevel) {
            return false;
        } else {
            BlockPos blockpos = pPlayer.blockPosition();
            if (blockpos.closerToCenterThan(new Vec3(pPosX, pPosY, pPosZ), pLongDistance ? 512.0D : 32.0D)) {
                pPlayer.connection.send(pPacket);
                return true;
            } else {
                return false;
            }
        }
    }
}
