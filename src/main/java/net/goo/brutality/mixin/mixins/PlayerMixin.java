package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.common.item.base.BrutalityGeoItem;
import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.item.curios.charm.Cosine;
import net.goo.brutality.common.item.curios.charm.Censored;
import net.goo.brutality.common.item.curios.hands.SuspiciouslyLargeHandle;
import net.goo.brutality.common.item.generic.augments.BrutalityAugmentItem;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.item.weapon.sword.MurasamaSword;
import net.goo.brutality.common.item.weapon.sword.ShadowstepSword;
import net.goo.brutality.common.item.weapon.sword.SupernovaSword;
import net.goo.brutality.common.item.weapon.sword.max.MAX;
import net.goo.brutality.common.item.weapon.throwing.VampireKnives;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.build_archetypes.GastronomyHelper;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {


    @Unique
    private static final Map<Class<? extends Item>, Supplier<ParticleOptions>> PARTICLE_SUPPLIERS = Map.of(
            ShadowstepSword.class, BrutalityParticles.SHADOW_SWEEP_PARTICLE::get,
            MurasamaSword.class, BrutalityParticles.MURASAMA_SWEEP_PARTICLE::get,
            SupernovaSword.class, BrutalityParticles.SUPERNOVA_SWEEP_PARTICLE::get
    );

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }


    @Unique
    private static Optional<ParticleOptions> brutality$getParticleForItem(Item item) {
        return PARTICLE_SUPPLIERS.entrySet().stream()
                .filter(entry -> entry.getKey().isInstance(item))
                .findFirst()
                .map(entry -> entry.getValue().get());
    }

    @Shadow
    public abstract void remove(RemovalReason pReason);

    @Shadow
    public abstract void attack(Entity pTarget);


    @Inject(method = "sweepAttack", at = @At("HEAD"), cancellable = true)
    private void sweepAttackParticle(CallbackInfo ci) {
        Player player = (Player) (Object) this;

        if (player.level() instanceof ServerLevel world) {
            if (player.getAttackStrengthScale(0.5F) >= 1.0F) {
                double d0 = -Math.sin(Math.toRadians(player.getYRot())) * 2;
                double d1 = Math.cos(Math.toRadians(player.getYRot())) * 2;

                brutality$getParticleForItem(player.getMainHandItem().getItem())
                        .ifPresent(particle -> {
                            world.sendParticles(
                                    particle,
                                    player.getX() + d0,
                                    player.getY(0.5D),
                                    player.getZ() + d1,
                                    0, d0, 0.0D, d1, 0.0D
                            );

                            ci.cancel();

                        });
            }


        }
    }


    @ModifyVariable(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttackStrengthScale(F)F",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0 // Targets the first 'float' local variable (which is 'f')
    )

    private float modifyAttackDamage(float originalDamage) {
        Player player = (Player) (Object) this;
        ItemStack stack = player.getMainHandItem();
        // Subtract player damage first to get weapon damage, then reapply
        originalDamage -= (float) player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
        originalDamage = (float) AttributeCalculationHelper.computeAttributes(player, stack, originalDamage);
        originalDamage += (float) player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);

        originalDamage += SuspiciouslyLargeHandle.getDamageModification(player, stack);
        originalDamage += MAX.getDamageBonusFromHealth(player, stack);

        return originalDamage;
    }


    @ModifyReturnValue(
            method = "getCurrentItemAttackStrengthDelay",
            at = @At("RETURN")
    )
    private float modifyAttackDelay(float originalDelay) {
        Player player = (Player) (Object) this;

        if (player.isHolding(BrutalityItems.VAMPIRE_KNIVES.get())) return VampireKnives.ATTACK_SPEED;

        float modifiedDelay = originalDelay;

        Optional<ICuriosItemHandler> handler = CuriosApi.getCuriosInventory(player).resolve();
        if (handler.isPresent()) {
            if (handler.get().isEquipped(BrutalityItems.COSINE.get())) {
                float bonus = Cosine.getCurrentBonus(player.level());
                modifiedDelay /= (1.0f + bonus);
            }
        }

        if (player.getMainHandItem().getItem() instanceof BrutalityThrowingItem || player.getOffhandItem().getItem() instanceof BrutalityThrowingItem) {
            if (player.getMainHandItem().getItem() == player.getOffhandItem().getItem()) {
                modifiedDelay /= 2;
            }
        }

        if (!this.getMainHandItem().isEmpty() && handler.isPresent()) {
            if (handler.get().isEquipped(BrutalityItems.SUSPICIOUSLY_LARGE_HANDLE.get())) {
                return (float) (1.0D / SuspiciouslyLargeHandle.BASE_ATTACK_SPEED * 20.0D);
            }
        }

        return modifiedDelay;
    }


    @Redirect(
            method = "attack(Lnet/minecraft/world/entity/Entity;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;getAttributeValue(Lnet/minecraft/world/entity/ai/attributes/Attribute;)D",
                    ordinal = 0
            )
    )
    private double modifyInitialAttackDamage(Player instance, Attribute attribute, Entity pTarget) {
        Player attacker = (Player) ((Object) this);
        ItemStack stack = ModUtils.getAttackStack(attacker);
        Item item = stack.getItem();

        double pAmount = instance.getAttributeValue(attribute);
        if (!(pTarget instanceof LivingEntity livingEntity)) return pAmount;

        float modifiedAmount = (float) pAmount;

        AttributeCalculationHelper.handleLifesteal(modifiedAmount, attacker);
        AttributeCalculationHelper.handleStunChance(attacker, livingEntity);
        modifiedAmount = BrutalityCurioItem.Hooks.applyOnWearerMeleeHit(attacker, livingEntity, stack, modifiedAmount);
        modifiedAmount = GastronomyHelper.applyGastronomyDamageMultiplier(attacker, livingEntity, stack, modifiedAmount);

        if (item instanceof BrutalityGeoItem geoItem) {
            modifiedAmount = geoItem.hurtEnemyModifiable(attacker, livingEntity, stack, modifiedAmount);
        }

        for (Map.Entry<BrutalityAugmentItem, Integer> entry : AugmentHelper.getAugmentCounts(stack).entrySet()) {
            BrutalityAugmentItem brutalityAugmentItem = entry.getKey();
            Integer integer = entry.getValue();
            if (brutalityAugmentItem instanceof BrutalitySealAugmentItem sealAugmentItem) {
                sealAugmentItem.onHurtEntity(attacker, pTarget, modifiedAmount, integer);
            }
        }

        return modifiedAmount;
    }

    @Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
    private void modifyDisplayName(CallbackInfoReturnable<Component> cir) {
        if (Censored.shouldRedact((Player) (Object) this)) {
            cir.setReturnValue(Component.literal("████████"));
            cir.cancel();
        }
    }

}





