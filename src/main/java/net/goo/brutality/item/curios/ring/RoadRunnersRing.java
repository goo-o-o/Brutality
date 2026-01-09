package net.goo.brutality.item.curios.ring;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.ServerboundDamageEntityPacket;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class RoadRunnersRing extends BrutalityCurioItem {


    private float xOld, zOld;

    public RoadRunnersRing(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 3, 14, false, true));

            if (player.level().isClientSide()) {
                Vec3 currentPos = player.position();
                Vec3 direction = currentPos.subtract(new Vec3(xOld, player.getY(), zOld));

                if (direction.lengthSqr() > 0.2 && player.horizontalCollision) {
                    Vec3 wallNormal = getWallNormal(player, direction);

                    float impactFactor = (float) Math.abs(direction.normalize().dot(wallNormal));

                    if (impactFactor > 0.7f) {
                        float damage = (float) (direction.lengthSqr() * 15);
                        triggerCollisionEffects(player, damage);
                    }
                }

                xOld = (float) currentPos.x;
                zOld = (float) currentPos.z;
            }
        }
    }

    private Vec3 getWallNormal(Player player, Vec3 moveDirection) {
        BlockPos collisionPos = player.blockPosition();
        if (player.level().getBlockState(collisionPos.east()).isSolid()) return new Vec3(-1, 0, 0);
        if (player.level().getBlockState(collisionPos.west()).isSolid()) return new Vec3(1, 0, 0);
        if (player.level().getBlockState(collisionPos.north()).isSolid()) return new Vec3(0, 0, 1);
        if (player.level().getBlockState(collisionPos.south()).isSolid()) return new Vec3(0, 0, -1);

        return moveDirection.normalize().scale(-1);
    }

    private SoundEvent getFallDamageSound(float damage) {
        return damage > 4F ? SoundEvents.GENERIC_SMALL_FALL : SoundEvents.GENERIC_BIG_FALL;
    }

    private void triggerCollisionEffects(Player player, float damage) {
        PacketHandler.sendToServer(new ServerboundDamageEntityPacket(player.getId(), damage, player.damageSources().flyIntoWall()));
        player.playSound(getFallDamageSound(damage), 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
    }

}
