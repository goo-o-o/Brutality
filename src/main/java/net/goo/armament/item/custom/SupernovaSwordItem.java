package net.goo.armament.item.custom;

import net.goo.armament.item.custom.client.SupernovaSwordItemRenderer;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.s2cSpawnParticleFromStarburstPacket;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.RenderUtils;

import java.util.List;
import java.util.function.Consumer;


public class SupernovaSwordItem extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private float width;

    public SupernovaSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.supernova_sword.desc1"));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.supernova_sword.desc3"));
        pTooltipComponents.add(Component.translatable("item.armament.supernova_sword.desc4"));
        pTooltipComponents.add(Component.literal(""));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SupernovaSwordItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    renderer = new SupernovaSwordItemRenderer();
                }
                return this.renderer;
            }
        });
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Level level = pAttacker.level();

        causeStarburstExplosion(pTarget, pAttacker);
        spawnStarburstExplosionParticles(pAttacker, pTarget, level);

        return super.hurtEnemy(pStack, pTarget, pAttacker);

    }

    private static void causeStarburstExplosion(LivingEntity target, LivingEntity player) {
        // Get the radius within which to damage entities (e.g., 5 blocks)
        double radius = 2.5;

        // Get all entities within the radius around the target
        List<Entity> nearbyEntities = target.level().getEntities(target, target.getBoundingBox().inflate(radius));

        // Apply damage to each nearby entity
        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != target) { // Don't damage the target itself
                entity.hurt(entity.damageSources().explosion(player, entity), 7.5F); // Adjust damage as needed
            }
        }
    }

    private void spawnStarburstExplosionParticles(LivingEntity player, LivingEntity pTarget, Level level) {
        width = pTarget.getBbWidth();

        PacketHandler.sendToAllClients(new s2cSpawnParticleFromStarburstPacket(
                pTarget.getX() + (level.random.nextFloat() - 0.5 * width),
                pTarget.getY() + (level.random.nextFloat() - 0.5 * pTarget.getBbHeight() + 2),
                pTarget.getZ() + (level.random.nextFloat() - 0.5 * width)));
    }

}
