package net.goo.brutality.common.item.weapon.spear;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.common.item.base.BrutalitySpearItem;
import net.goo.brutality.common.network.clientbound.ClientboundPlayerAnimationPacket;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.client.particle.providers.FlatParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ParticleHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class Caldrith extends BrutalitySpearItem {
    public Caldrith(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public static final OrientedBoundingBox HITBOX = new OrientedBoundingBox(Vec3.ZERO, new Vec3(0.5F, 7.5, 18).scale(0.5F), 0, 0, 0);
    public static final Vec3 OFFSET = new Vec3(0, 2.5, 0);

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ResourceLocation animation = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "two_handed_slam_heavy");
        pPlayer.setYBodyRot(pPlayer.getYRot());
        if (pLevel instanceof ServerLevel serverLevel) {
            PacketHandler.sendToNearbyClients(serverLevel, pPlayer.getX(), pPlayer.getY(.5), pPlayer.getZ(), 128,
                    new ClientboundPlayerAnimationPacket(pPlayer.getUUID(), animation,
                            false, 1F));


            FlatParticleData<?> data = new FlatParticleData<>(BrutalityParticles.VOID_SLASH_PARTICLE.get(),
                    10F, 90, 90, Mth.wrapDegrees(pPlayer.getYRot() - 180)
            );

            List<LivingEntity> targets = HITBOX.inWorld(pPlayer, OFFSET, 0, pPlayer.getYRot()).findEntitiesHit(pPlayer, LivingEntity.class, null);


            DelayedTaskScheduler.queueServerWork(serverLevel, 7, () -> {

                targets.forEach(livingEntity -> livingEntity.hurt(livingEntity.damageSources().playerAttack(pPlayer), 20));

                ParticleHelper.sendParticles(serverLevel, data, true, pPlayer.getX(), pPlayer.getY(2), pPlayer.getZ(), 1, 0);
                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), BrutalitySounds.VOID_SLASH.get(), SoundSource.PLAYERS, 10F, 1F);

            });

        } else {
            AnimationHelper.playAnimation(pPlayer, animation, false, 1);
        }

        pPlayer.getCooldowns().addCooldown(this, 80);
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}


