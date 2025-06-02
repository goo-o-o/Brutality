package net.goo.armament.util.helpers;

import net.goo.armament.entity.base.SwordBeam;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class ProjectileHelper {
    public static void shootProjectile(Supplier<? extends SwordBeam> projectileSupplier, Player player, Level level, float velocity) {
        Vec3 playerPos = player.position();
        Vec3 viewVector = player.getViewVector(1.0F);

        double spawnX = playerPos.x + viewVector.x;
        double spawnY = playerPos.y + viewVector.y + player.getBbHeight() / 2;
        double spawnZ = playerPos.z + viewVector.z;

        SwordBeam swordBeam = projectileSupplier.get();

        swordBeam.setOwner(player);
        swordBeam.setPos(spawnX, spawnY - 0.25F, spawnZ);
        swordBeam.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity, 0.0F);
        level.addFreshEntity(swordBeam);
    }
}
