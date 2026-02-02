package net.goo.brutality.common.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.entity.projectile.generic.SupernovaAsteroid;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SupernovaSword extends BrutalitySwordItem {
    private final RandomSource random = RandomSource.create();

    public SupernovaSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.KNOCKBACK, 3);
        return stack;
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Level level = pAttacker.level();

        causeStarburstExplosion(pTarget, (Player) pAttacker);

        if (level instanceof ServerLevel serverLevel) {
            for (int i = 0; i < pAttacker.level().random.nextInt(50); i++) {

                serverLevel.sendParticles(ModUtils.getRandomParticle(BrutalityParticles.COSMIC_PARTICLE), pTarget.getX(),
                        pTarget.getY() + pTarget.getBbHeight() / 2, pTarget.getZ(), 1,
                        0, 0, 0, 100);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);

    }

    int tickCount;

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        tickCount++;
        if (!pIsSelected & !pLevel.isClientSide()) {
            clearAsteroids(((Player) pEntity), ((ServerLevel) pLevel));
        }
    }

    public static Stream<SupernovaAsteroid> getAllAsteroidsOwnedBy(LivingEntity owner, ServerLevel level) {
        return StreamSupport.stream(level.getAllEntities().spliterator(), false)
                .filter(entity -> entity instanceof SupernovaAsteroid)
                .map(SupernovaAsteroid.class::cast)
                .filter(asteroid -> asteroid.getOwner() == owner);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel instanceof ServerLevel serverLevel) {
            Stream<SupernovaAsteroid> asteroids = getAllAsteroidsOwnedBy(pPlayer, serverLevel);

            if (asteroids.findAny().isEmpty()) {
                for (int i = 1; i <= 6; i++) {
                    int angleOffset = (int) Math.toRadians(60 * i);
                    SupernovaAsteroid meteor = new SupernovaAsteroid(BrutalityEntities.SUPERNOVA_ASTEROID.get(), pLevel, angleOffset);
                    meteor.setOwner(pPlayer);
                    meteor.setPos(pPlayer.getX(), pPlayer.getY() + 2, pPlayer.getZ());
                    pLevel.addFreshEntity(meteor);
                }
                pPlayer.getCooldowns().addCooldown(stack.getItem(), 40);
                stack.hurtAndBreak(6, pPlayer, player -> pPlayer.broadcastBreakEvent(pUsedHand));
                // Return success
            } else if (pPlayer.isCrouching()) {
                clearAsteroids(pPlayer, serverLevel);
            } else
                pPlayer.displayClientMessage(Component.translatable("item." + Brutality.MOD_ID + ".supernova.asteroid_fail").withStyle(ChatFormatting.RED), true);
        }

        return InteractionResultHolder.fail(stack);
    }

    private void causeStarburstExplosion(LivingEntity target, Player player) {
        player.level().playSound(null, target.getOnPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1F, 1F);

        List<Entity> nearbyEntities = target.level().getEntities(target, target.getBoundingBox().inflate(5));

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != player) {
                entity.hurt(entity.damageSources().explosion(player, entity), 10F);
            }
        }

    }


    public static void clearAsteroids(Player player, ServerLevel level) {
        Stream<SupernovaAsteroid> asteroidStream = getAllAsteroidsOwnedBy(player, level);
        asteroidStream.forEach(asteroid -> {
            if (asteroid != null) {
                asteroid.discard();
            }
        });
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {

            if (player.hasEffect(BrutalityEffects.STUNNED.get()))
                player.removeEffect(BrutalityEffects.STUNNED.get());

            if (player.level() instanceof ServerLevel serverLevel) {
                clearAsteroids(player, serverLevel);
            }
        }
    }

    @SubscribeEvent
    public void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            clearAsteroids(player, ((ServerLevel) player.level()));
        }
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            clearAsteroids(player, ((ServerLevel) player.level()));
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            player.level().playSound(null, player.getOnPos(), ModUtils.getRandomSound(BrutalitySounds.SUPERNOVA), SoundSource.PLAYERS, 1, Mth.nextFloat(random, 0.8F, 1F));
            if (player.level() instanceof ServerLevel serverLevel)
                triggerAnim(player, GeoItem.getOrAssignId(stack, serverLevel), "controller", "swing");
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> state.
                setAndContinue(RawAnimation.begin().thenPlay("idle")))
                .triggerableAnim("swing", RawAnimation.begin().thenPlay("swing"))
                .triggerableAnim("stab", RawAnimation.begin().thenPlay("stab"))
        );

    }
}
