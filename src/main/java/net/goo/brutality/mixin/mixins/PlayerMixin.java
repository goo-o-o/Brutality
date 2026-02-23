package net.goo.brutality.mixin.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.goo.brutality.common.item.base.BrutalityGeoItem;
import net.goo.brutality.common.item.base.BrutalityThrowingItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.item.curios.charm.Cosine;
import net.goo.brutality.common.item.weapon.sword.MurasamaSword;
import net.goo.brutality.common.item.weapon.sword.ShadowstepSword;
import net.goo.brutality.common.item.weapon.sword.SupernovaSword;
import net.goo.brutality.common.item.weapon.throwing.VampireKnives;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.build_archetypes.GastronomyHelper;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.item.SealUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

        Optional<ICuriosItemHandler> handlerOptional = CuriosApi.getCuriosInventory(player).resolve();
        if (handlerOptional.isPresent()) {
            ICuriosItemHandler handler = handlerOptional.get();
            if (!stack.isEmpty() && handler.isEquipped(BrutalityItems.SUSPICIOUSLY_LARGE_HANDLE.get())) {
                float attackSpeed = (float) player.getAttributeValue(Attributes.ATTACK_SPEED);
                float difference = attackSpeed - 0.5F;
                float damageBoost = difference * 5F;
                originalDamage += damageBoost;
            }
        }

        return originalDamage;
    }


    @ModifyReturnValue(
            method = "getCurrentItemAttackStrengthDelay",
            at = @At("RETURN")
    )
    private float modifyAttackSpeed(float originalDelay) {
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
                return (float) (1.0D / 0.5F * 20.0D);
            }
        }

        return modifiedDelay;
    }


    @Redirect(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
            )
    )
    private boolean handleWeaponAttacks(Entity entity, DamageSource pSource, float pAmount) {
        if (!(entity instanceof LivingEntity victim)) return entity.hurt(pSource, pAmount);

        Player attacker = (Player) pSource.getEntity();
        if (attacker == null) return entity.hurt(pSource, pAmount);

        ItemStack stack = ModUtils.getAttackStack(attacker);
        Item item = stack.getItem();
        Level level = level();

        float modifiedAmount = pAmount;

        AttributeCalculationHelper.handleLifesteal(modifiedAmount, attacker);
        AttributeCalculationHelper.handleStunChance(attacker, victim);
        modifiedAmount = BrutalityCurioItem.Hooks.applyOnWearerMeleeHit(attacker, victim, stack, pSource, modifiedAmount);
        modifiedAmount = GastronomyHelper.applyGastronomyDamageMultiplier(attacker, victim, stack, modifiedAmount);

        if (item instanceof BrutalityGeoItem geoItem) {
            modifiedAmount = geoItem.hurtEnemyModifiable(attacker, victim, stack, pSource, modifiedAmount);
        }

        SealUtils.handleSealProcOffensive(level, attacker, victim.getPosition(1).add(0, victim.getBbHeight() * 0.5F, 0), stack);

        return victim.hurt(pSource, modifiedAmount);
    }

}





