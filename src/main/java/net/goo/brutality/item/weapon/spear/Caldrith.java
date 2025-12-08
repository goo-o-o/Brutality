package net.goo.brutality.item.weapon.spear;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.item.base.BrutalitySpearItem;
import net.goo.brutality.network.ClientboundPlayerAnimationPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.particle.providers.FlatParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.phys.OrientedBoundingBox;
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
    public Caldrith(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    public static final OrientedBoundingBox HITBOX = new OrientedBoundingBox(Vec3.ZERO, new Vec3(0.5F, 7.5, 18).scale(0.5F), 0, 0, 0);


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ResourceLocation animation = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "two_handed_slam_heavy");
        pPlayer.setYBodyRot(pPlayer.getYRot());
        if (pLevel instanceof ServerLevel serverLevel) {
            PacketHandler.sendToNearbyClients(serverLevel, pPlayer.getX(), pPlayer.getY(.5), pPlayer.getZ(), 128,
                    new ClientboundPlayerAnimationPacket(pPlayer.getUUID(), animation,
                            false, 1F));


            FlatParticleData<?> data = new FlatParticleData<>(BrutalityModParticles.VOID_SLASH_PARTICLE.get(),
                    10F, 90, 90, Mth.wrapDegrees(pPlayer.getYRot() - 180)
            );

            OrientedBoundingBox.TargetResult<LivingEntity> targets =
                    OrientedBoundingBox.findAttackTargetResult(pPlayer, LivingEntity.class, HITBOX, new Vec3(0, 2.5, -9), 0, pPlayer.getYRot(), 0, false);


            DelayedTaskScheduler.queueServerWork(serverLevel, 7, () -> {

                targets.entities.forEach(livingEntity -> livingEntity.hurt(livingEntity.damageSources().playerAttack(pPlayer), 20));

                ModUtils.sendParticles(serverLevel, data, true, pPlayer.getX(), pPlayer.getY(2), pPlayer.getZ(), 1, 0);
                pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(0.5), pPlayer.getZ(), BrutalityModSounds.VOID_SLASH.get(), SoundSource.PLAYERS, 10F, 1F);

            });

        } else {
            AnimationHelper.playAnimation(pPlayer, animation, false, 1);
        }

        pPlayer.getCooldowns().addCooldown(this, 80);
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}


