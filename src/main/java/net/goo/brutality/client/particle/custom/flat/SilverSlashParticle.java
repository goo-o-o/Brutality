package net.goo.brutality.client.particle.custom.flat;

import net.goo.brutality.client.particle.base.FlatParticle;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.goo.brutality.common.item.weapon.RotatingAttackWeapon;
import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SilverSlashParticle extends FlatParticle {

    public SilverSlashParticle(ClientLevel level, double x, double y, double z, FlatParticleData<?> data, SpriteSet sprites) {
        super(level, x, y, z, data, sprites);
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.rotXOld = this.rotX;
        this.rotYOld = this.rotY;
        this.rotZOld = this.rotZ;

        this.setSpriteFromAge(this.sprites);

        if (this.relatedEntity != null && this.relatedEntity.isAlive()) {
            if (relatedEntity instanceof LivingEntity livingEntity) {
                ItemStack stack = livingEntity.getUseItem();
                if (stack.is(BrutalityItems.THE_SILVER_PERIMETER.get()) && livingEntity.isUsingItem()) {
                    RotatingAttackWeapon weapon = (RotatingAttackWeapon) stack.getItem();

                    this.setPos(livingEntity.getX(), livingEntity.getY(0.75), livingEntity.getZ());

                    int useTicks = stack.getUseDuration() - livingEntity.getUseItemRemainingTicks();

                    this.rotY = -RotatingAttackWeapon.calculateSpinRotation(useTicks, weapon);
                } else {
                    this.remove();
                }
            }
        } else {
            this.remove();
        }
    }

    public static class SilverSlashParticleProvider implements ParticleProvider<FlatParticleData<?>> {
        private final SpriteSet sprites;

        public SilverSlashParticleProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull FlatParticleData<?> data, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new SilverSlashParticle(pLevel, pX, pY, pZ, data, sprites);
        }
    }

}
