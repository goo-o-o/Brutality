package net.goo.brutality.util.helpers;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.explosion.BrutalityExplosion;
import net.goo.brutality.network.ClientboundBrutalityExplodePacket;
import net.goo.brutality.network.PacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ModExplosionHelper {


    public static class ProgressiveExplosion {
        private final Level level;
        private final Entity owner;
        private final BlockPos center;
        private final float maxRadius;
        private float currentRadius = 0;
        private int ticksSinceLastLayer = 0;
        private final int layersPerTick;
        private int tickDelay = 4; // Delay between layers (in ticks)

        public ProgressiveExplosion(Level level, Entity owner, BlockPos center, float maxRadius, int layersPerTick) {
            this.level = level;
            this.owner = owner;
            this.center = center;
            this.maxRadius = maxRadius;
            this.layersPerTick = layersPerTick;
        }

        public boolean tick() {
            if (currentRadius >= maxRadius) {
                return true; // Explosion complete
            }

            ticksSinceLastLayer++;
            if (ticksSinceLastLayer >= tickDelay) {
                destroyLayer(currentRadius, layersPerTick);
                currentRadius += layersPerTick;
                ticksSinceLastLayer = 0;
            }

            return false;
        }

        private void destroyLayer(float startRadius, int layersToDestroy) {
            int endRadiusInt = (int) Math.ceil(startRadius + layersToDestroy);
            BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

            // Process all layers in one pass
            for (int x = -endRadiusInt; x <= endRadiusInt; x++) {
                for (int y = -endRadiusInt; y <= endRadiusInt; y++) {
                    for (int z = -endRadiusInt; z <= endRadiusInt; z++) {
                        double distanceSq = x * x + y * y + z * z;

                        // Check if block is within any of the target layers
                        for (int l = 0; l < layersToDestroy; l++) {
                            float currentLayer = startRadius + l;
                            double outerSq = currentLayer * currentLayer;
                            double innerSq = (currentLayer - 1) * (currentLayer - 1);

                            if (distanceSq > innerSq && distanceSq <= outerSq) {
                                mutablePos.set(center.getX() + x, center.getY() + y, center.getZ() + z);
                                if (!level.getBlockState(mutablePos).isAir()) {
                                    level.removeBlock(mutablePos, false);
                                    // Skip checking other layers for this block
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            // Entity damage (optimized to only check once for all layers)
            if (startRadius + layersToDestroy >= maxRadius) {
                AABB searchArea = new AABB(center).inflate(startRadius + layersToDestroy);
                List<Entity> entities = level.getEntitiesOfClass(Entity.class, searchArea);

                for (Entity target : entities) {
                    double distanceSq = target.distanceToSqr(center.getX(), center.getY(), center.getZ());
                    if (distanceSq <= (startRadius + layersToDestroy) * (startRadius + layersToDestroy)) {
                        float damage = calculateDamage(distanceSq, startRadius + layersToDestroy);
                        target.hurt(target.damageSources().explosion(owner, target), damage);
                    }
                }
            }
        }

        private float calculateDamage(double distanceSq, float currentRadius) {
            return (float) ((maxRadius / 3) * (1 - Math.sqrt(distanceSq) / maxRadius) * (currentRadius / maxRadius));
        }

    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT)
    public static class Client {
        public static void handleExplosion(ClientboundBrutalityExplodePacket packet) {
            Minecraft mc = Minecraft.getInstance();
            Level level = mc.level;
            if (level == null) return;
            try {
                Class<?> explosionClass = Class.forName(packet.getClazz());
                Constructor<?> constructor = explosionClass.getConstructor(Level.class, Entity.class, double.class, double.class, double.class, float.class, List.class);

                BrutalityExplosion explosion = (BrutalityExplosion) constructor.newInstance(
                        level, null, packet.getX(), packet.getY(), packet.getZ(), packet.getPower(), packet.getToBlow());
                explosion.explode();
                explosion.finalizeExplosion(packet.isSpawnParticles());
                if (mc.player == null) return;
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(packet.getKnockbackX(), packet.getKnockbackY(), packet.getKnockbackZ()));
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                Explosion explosion = new Explosion(level, null, packet.getX(), packet.getY(), packet.getZ(), packet.getPower(), packet.getToBlow());
                explosion.finalizeExplosion(packet.isSpawnParticles());
                if (mc.player == null) return;
                mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(packet.getKnockbackX(), packet.getKnockbackY(), packet.getKnockbackZ()));
            }
        }
    }


    @Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
    public static class Server {
        private static final List<ProgressiveExplosion> activeExplosions = new ArrayList<>();

        public static void addExplosion(Level level, Entity owner, BlockPos center, float radius, int layersPerTick) {
            activeExplosions.add(new ProgressiveExplosion(level, owner, center, radius, layersPerTick));
            level.removeBlock(center, false);
        }

        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                activeExplosions.removeIf(ProgressiveExplosion::tick);
            }
        }


        /**
         * Must be called from the Server, like {@link ServerLevel#explode(Entity, double, double, double, float, Level.ExplosionInteraction)}
         */
        public static Explosion explode(BrutalityExplosion explosion, Level level, boolean spawnParticles) {
            if (level instanceof ServerLevel serverLevel) {
                Explosion.BlockInteraction blockInteraction = switch (explosion.getExplosionInteraction()) {
                    case NONE -> Explosion.BlockInteraction.KEEP;
                    case BLOCK -> level.getDestroyType(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
                    case MOB ->
                            ForgeEventFactory.getMobGriefingEvent(level, explosion.getDirectSourceEntity()) ? level.getDestroyType(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
                    case TNT -> level.getDestroyType(GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
                };

                if (ForgeEventFactory.onExplosionStart(level, explosion)) return explosion;
                explosion.blockInteraction = blockInteraction;
                explosion.explode();
                explosion.finalizeExplosion(spawnParticles);
                if (!explosion.interactsWithBlocks()) {
                    explosion.clearToBlow();
                }

                for (ServerPlayer serverplayer : serverLevel.getPlayers(serverPlayer -> serverPlayer.distanceToSqr(explosion.getPosition()) < 4096)) {
                    PacketHandler.sendToPlayerClient(new ClientboundBrutalityExplodePacket(explosion.getPosition().x, explosion.getPosition().y, explosion.getPosition().z, explosion.getRadius(), explosion.getToBlow(), explosion.getHitPlayers().get(serverplayer), spawnParticles, explosion.getClass()), serverplayer);
                }
            }
            return explosion;

        }
    }


}
