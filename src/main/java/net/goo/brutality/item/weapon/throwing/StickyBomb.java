package net.goo.brutality.item.weapon.throwing;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityThrowingItem;
import net.goo.brutality.network.ClientboundSyncCapabilitiesPacket;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

public class StickyBomb extends BrutalityThrowingItem {


    public StickyBomb(int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }


    @Override
    public ResourceLocation getAnimationResourceLocation() {
        return THROW_ANIMATION.DROP.getAnimationResource();
    }


    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.BLUNT;
    }

    @Override
    public EntityType<? extends Projectile> getThrownEntity() {
        return BrutalityModEntities.STICKY_BOMB.get();
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pUsedHand) {
        List<Entity> entities = level.getEntities(null, player.getBoundingBox().inflate(250));
        UUID uuid = player.getUUID();
        entities.forEach(entity -> {
            if (entity instanceof net.goo.brutality.entity.projectile.trident.physics_projectile.StickyBomb stickyBomb && stickyBomb.getOwner() == player) {
                if (!level.isClientSide())
                    level.explode(player, null, null,
                            stickyBomb.getPosition(1).add(0, stickyBomb.getBbHeight() / 2, 0), 1,
                            false, ModUtils.getThrowingWeaponExplosionInteractionFromConfig());

                stickyBomb.discard();
            }

            entity.getCapability(BrutalityCapabilities.ENTITY_STICKY_BOMB_CAP).ifPresent(cap -> {
                level.explode(player, null, null,
                        entity.getPosition(1).add(0, entity.getBbHeight() / 2, 0), Math.min(10, cap.getStickyBombCount(uuid, entity.getId())),
                        false, ModUtils.getThrowingWeaponExplosionInteractionFromConfig());
                cap.clearStickyBombCount(uuid, entity.getId());
                if (!level.isClientSide()) {
                    PacketHandler.sendToAllClients(new ClientboundSyncCapabilitiesPacket(entity.getId(), entity));
                }

            });

        });

        return super.use(level, player, pUsedHand);
    }


    @Override
    public float getInitialThrowVelocity() {
        return 1F;
    }
}
