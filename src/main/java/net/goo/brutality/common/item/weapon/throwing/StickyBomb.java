package net.goo.brutality.common.item.weapon.throwing;

import net.goo.brutality.common.entity.capabilities.BrutalityCapabilities;
import net.goo.brutality.common.entity.explosion.NapalmExplosion;
import net.goo.brutality.common.entity.projectile.trident.physics_projectile.ThrownStickyBomb;
import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.util.ModExplosionHelper;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.Supplier;

public class StickyBomb extends BrutalityThrowingItem {


    public StickyBomb(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents, Supplier<? extends EntityType<? extends Projectile>> entityTypeSupplier) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents, entityTypeSupplier);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        // 1. Get all bombs owned by the player
        List<ThrownStickyBomb> playerBombs = level.getEntitiesOfClass(ThrownStickyBomb.class,
                player.getBoundingBox().inflate(250),
                e -> e.getOwner() == player);

        // 2. Map groups by their Grid Position (BlockPos divided by 3)
        Map<BlockPos, List<ThrownStickyBomb>> clusters = new HashMap<>();
        float radius = 2;
        for (ThrownStickyBomb bomb : playerBombs) {
            // We divide coordinates by 3 to "bin" them into 3x3 areas
            BlockPos gridPos = new BlockPos(
                    (int) Math.floor(bomb.getX() / radius),
                    (int) Math.floor(bomb.getY() / radius),
                    (int) Math.floor(bomb.getZ() / radius)
            );
            clusters.computeIfAbsent(gridPos, k -> new ArrayList<>()).add(bomb);
        }

        // 3. Process each cluster
        clusters.forEach((gridPos, bombs) -> {
            double sumX = 0, sumY = 0, sumZ = 0;

            // Base power + extra power per additional bomb
            // Example: 3.0 base + 0.5 per extra bomb
            float combinedPower = (float) (2 * Math.sqrt(bombs.size()));

            for (ThrownStickyBomb b : bombs) {
                sumX += b.getX();
                sumY += b.getY();
                sumZ += b.getZ();
                b.discard();
            }

            // Get the centroid of this specific 3x3 clump
            double avgX = sumX / bombs.size();
            double avgY = sumY / bombs.size();
            double avgZ = sumZ / bombs.size();

            if (!level.isClientSide()) {
                NapalmExplosion explosion = new NapalmExplosion(level, player, null, null,
                        avgX, avgY, avgZ, combinedPower, true,
                        ModUtils.getThrowingWeaponExplosionInteractionFromConfig());

                ModExplosionHelper.Server.explode(explosion, level, true);
            }
        });

        List<Entity> entities = level.getEntities(null, player.getBoundingBox().inflate(250));
        UUID uuid = player.getUUID();
        entities.forEach(entity -> {

            entity.getCapability(BrutalityCapabilities.STICKY_BOMB).ifPresent(cap -> {
                int stickyBombCount = cap.getCountForPlayer(uuid);
                if (stickyBombCount <= 0) return;

                stickyBombCount = Math.min(25, stickyBombCount);

                NapalmExplosion explosion = new NapalmExplosion(level, player, null, null,
                        entity.getX(), entity.getY(0.5), entity.getZ(), stickyBombCount, true,
                        ModUtils.getThrowingWeaponExplosionInteractionFromConfig());
                ModExplosionHelper.Server.explode(explosion, level, true);

                cap.removeAllBombs();
                BrutalityCapabilities.syncToAllTracking(entity, BrutalityCapabilities.STICKY_BOMB);
            });

        });

        return super.use(level, player, pUsedHand);
    }

}
