package net.goo.brutality.util.helpers;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ProjectileHelper {
    public static void shootProjectile(Supplier<? extends Projectile> projectileSupplier,
                                       Player player, Level level, float velocity, @Nullable Boolean random, @Nullable Float delta, int angleOffset) {
        Vec3 playerPos;
        if (random != null && random) {
            playerPos = new Vec3(player.getRandomX(delta != null ? delta : 1), player.getRandomY(),player.getRandomZ(delta != null ? delta : 1));
        } else {
            playerPos = player.position();
        }

        Vec3 viewVector = player.getViewVector(1.0F);

        double spawnX = playerPos.x + viewVector.x;
        double spawnY = playerPos.y + viewVector.y + player.getEyeHeight();
        double spawnZ = playerPos.z + viewVector.z;

        Projectile swordBeam = projectileSupplier.get();

        swordBeam.setOwner(player);
        swordBeam.setPos(spawnX, spawnY, spawnZ);
        swordBeam.shootFromRotation(player, player.getXRot(), player.getYRot() + angleOffset, 0.0F, velocity, 0.0F);
        level.addFreshEntity(swordBeam);
    }
}
