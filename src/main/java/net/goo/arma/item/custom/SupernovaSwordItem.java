package net.goo.arma.item.custom;

import net.goo.arma.particle.ModParticles;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;


public class SupernovaSwordItem extends SwordItem {

    public SupernovaSwordItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    private int particleSpawnerCounter = 0;

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (level.isClientSide() && entity instanceof Player player && selected) {
            particleSpawnerCounter++;

            if (particleSpawnerCounter >= 5) {
                spawnSwordParticles(player, level); // Spawn particles dynamically based on the hand
                particleSpawnerCounter = 0;
            }
        }
        super.inventoryTick(stack, level, entity, slot, selected);
    }


    private void spawnSwordParticles(Player player, Level level) {
        if (level.isClientSide()) {
            // Get the direction perpendicular to the player's body direction
            Vec3 bodyDirection = player.getForward();
            Vec3 perpendicularDirection = bodyDirection.cross(new Vec3(0, 1, 0)).normalize();

                // Generate random offsets within a 3x3x3 cube centered around the player
                double offsetX = (level.random.nextFloat() - 0.5) * 3.0;
                double offsetY = (level.random.nextFloat()) * 3.0;
                double offsetZ = (level.random.nextFloat() - 0.5) * 3.0;

                // Calculate particle position
                Vec3 particlePosition = player.position().add(offsetX, offsetY, offsetZ);

                // Randomize the perpendicular velocity direction
                double velocityScale = 0.3; // Adjust speed
                Vec3 velocity = perpendicularDirection.scale((level.random.nextFloat() - 0.5) * velocityScale);

                // Spawn the particle
                level.addParticle(ModParticles.SUPERNOVA_SWORD_PARTICLE.get(),
                        particlePosition.x, particlePosition.y, particlePosition.z,
                        velocity.x, velocity.y, velocity.z);
        }
    }

}
