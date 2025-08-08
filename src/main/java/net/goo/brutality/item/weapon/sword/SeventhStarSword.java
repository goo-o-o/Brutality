package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.config.BrutalityCommonConfig;
import net.goo.brutality.entity.projectile.generic.StarEntity;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sShootProjectilePacket;
import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.ProjectileHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

public class SeventhStarSword extends BrutalitySwordItem {


    public SeventhStarSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pPlayer.isShiftKeyDown()) {
            UUID uuid = pPlayer.getUUID();
            List<Entity> nearbyEntities = pLevel.getEntitiesOfClass(
                    Entity.class, pPlayer.getBoundingBox().inflate(250));

            for (Entity entity : nearbyEntities) {
                if (entity instanceof StarEntity star && star.getOwner() == pPlayer) {
                    pLevel.explode(pPlayer, null, null,
                            star.getPosition(1).add(0, star.getBbHeight() / 2, 0), 1,
                            false,
                            BrutalityCommonConfig.SEVENTH_STAR_GRIEFING.get() ?
                                    Level.ExplosionInteraction.BLOCK :
                                    Level.ExplosionInteraction.NONE
                    );

                    star.discard();
                }

                entity.getCapability(BrutalityCapabilities.ENTITY_STAR_COUNT_CAP).ifPresent(cap -> {
                    if (cap.getStarCount(uuid) > 0) {
                        for (int i = 0; i < cap.getStarCount(uuid); i++)
                            pLevel.explode(pPlayer, null, null,
                                    entity.getPosition(1).add(0, entity.getBbHeight() / 2, 0), 1, false, Level.ExplosionInteraction.MOB);

                        cap.clearStarCount(uuid);
                        if (!pLevel.isClientSide())
                            PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(entity.getId(), entity));
                    }
                });


            }
        } else {
            for (int i = 0; i < 8; i++) {
                StarEntity star = new StarEntity(BrutalityModEntities.STAR_ENTITY.get(), pLevel);
                star.shootFromRotation(pPlayer, 0, pPlayer.getYRot() + (i * 45), 0, 1, 1);
                star.setOwner(pPlayer);
                star.setPos(pPlayer.getPosition(1).add(0, pPlayer.getEyeHeight(), 0));
                pLevel.addFreshEntity(star);
            }

            pPlayer.getCooldowns().addCooldown(this, 20);
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.level().isClientSide && !ModList.get().isLoaded("bettercombat")) {
            shootTriStar(player);
        }
        return super.onEntitySwing(stack, entity);
    }


    public void shootTriStar(Player player) {
        Level level = player.level();
        if (level.isClientSide()) {
            for (int i = -15; i <= 15; i += 15)
                PacketHandler.sendToServer(new c2sShootProjectilePacket(BrutalityModEntities.STAR_ENTITY.getId(), 2.5F, false, 0F, i));
        } else {
            for (int i = -15; i <= 15; i += 15)
                ProjectileHelper.shootProjectile(() ->
                        new StarEntity(BrutalityModEntities.STAR_ENTITY.get(), level), player, level, 2.5F, false, 0F, i);
        }
    }

}
