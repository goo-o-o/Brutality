package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.client.renderers.item.AutoGlowingRenderer;
import net.goo.armament.entity.custom.SupernovaAsteroid;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.registry.ModParticles;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SupernovaSword extends ArmaSwordItem {

    public SupernovaSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = SUPERNOVA_COLORS;
    }

    @Override
    public <T extends Item & ArmaGeoItem, R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, AutoGlowingRenderer.class);
    }


    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.KNOCKBACK, 3);
        return stack;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        Level level = pAttacker.level();

        causeStarburstExplosion(pTarget, (Player) pAttacker);
        spawnStarburstExplosionParticles(pAttacker, pTarget, level);

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

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (pLevel instanceof ServerLevel serverLevel) {
            List<SupernovaAsteroid> asteroids = SupernovaAsteroid.getAllAsteroidsOwnedBy(pPlayer, serverLevel)
                    .filter(asteroid -> asteroid.distanceToSqr(pPlayer) <= 32) // Limit to a certain range
                    .toList();

            if (asteroids.isEmpty()) {
                for (int i = 1; i <= 6; i++) {
                    float angleOffset = (float) Math.toRadians(60 * i); // 60, 120, 180, 240, 300, 360 degrees
                    SupernovaAsteroid meteor = new SupernovaAsteroid(ModEntities.SUPERNOVA_ASTEROID.get(), pLevel, angleOffset);
                    meteor.setOwner(pPlayer);
                    meteor.setPos(pPlayer.getX(), pPlayer.getY() + 2, pPlayer.getZ());
                    pLevel.addFreshEntity(meteor);
                }
                pPlayer.getCooldowns().addCooldown(stack.getItem(), 40);
                stack.hurtAndBreak(6, pPlayer, player -> pPlayer.broadcastBreakEvent(pUsedHand));
                // Return success
            } else if (pPlayer.isCrouching()) {
                clearAsteroids(pPlayer, serverLevel);
            } else pPlayer.displayClientMessage(Component.translatable("item.armament.supernova.asteroid_fail").withStyle(ChatFormatting.RED), true);
        }

        return InteractionResultHolder.fail(stack);
    }

    private void causeStarburstExplosion(LivingEntity target, Player player) {
        double radius = 2.5;
        player.level().playSound(player, target.getOnPos(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 1F, 1F);

        List<Entity> nearbyEntities = target.level().getEntities(target, target.getBoundingBox().inflate(radius));

        for (Entity entity : nearbyEntities) {
            if (entity instanceof LivingEntity && entity != player) {
                entity.hurt(entity.damageSources().explosion(player, entity), 2.5F);
            }
        }
    }

    private void spawnStarburstExplosionParticles(LivingEntity player, LivingEntity pTarget, Level level) {
        ((ServerLevel) level).sendParticles(ModParticles.STARBURST_PARTICLE.get(),
                pTarget.getX(), pTarget.getY() + pTarget.getBbHeight() / 2, pTarget.getZ(),
                0,
                level.random.nextFloat() * pTarget.getBbWidth(),
                level.random.nextFloat() * pTarget.getBbHeight(),
                level.random.nextFloat() * pTarget.getBbWidth(), 0);
    }

    private void clearAsteroids(Player player, ServerLevel level) {
        Stream<SupernovaAsteroid> asteroidStream = SupernovaAsteroid.getAllAsteroidsOwnedBy(player, level);
        asteroidStream.forEach(asteroid -> {
            if (asteroid != null) {
                asteroid.discard();
            }
        });
    }

    @SubscribeEvent
    public void onPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            clearAsteroids(player, ((ServerLevel) player.level()));
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
}
