package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.mobs.SummonedStray;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.particle.providers.WaveParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FrostmourneSword extends BrutalitySwordItem {
    private static final Map<UUID, Set<UUID>> PLAYER_SUMMONED_STRAYS = new ConcurrentHashMap<>();

    public FrostmourneSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @SubscribeEvent
    public static void onStrayDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Stray stray) {
            // Iterate through all players' sets to find and remove the stray
            for (Map.Entry<UUID, Set<UUID>> entry : PLAYER_SUMMONED_STRAYS.entrySet()) {
                Set<UUID> summonedStrays = entry.getValue();
                if (summonedStrays.remove(stray.getUUID())) {
                    // If the player's set is now empty, remove the player's entry
                    if (summonedStrays.isEmpty()) {
                        PLAYER_SUMMONED_STRAYS.remove(entry.getKey());
                    }
                    break; // Stop searching once the stray is found
                }
            }
        }
    }

    @SubscribeEvent
    public static void onStrayDespawn(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Stray stray) {
            // Iterate through all players' sets to find and remove the stray
            for (Map.Entry<UUID, Set<UUID>> entry : PLAYER_SUMMONED_STRAYS.entrySet()) {
                Set<UUID> summonedStrays = entry.getValue();
                if (summonedStrays.remove(stray.getUUID())) {
                    // If the player's set is now empty, remove the player's entry
                    if (summonedStrays.isEmpty()) {
                        PLAYER_SUMMONED_STRAYS.remove(entry.getKey());
                    }
                    break; // Stop searching once the stray is found
                }
            }
        }
    }


    private static void despawnStraysForPlayer(Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            // Get or create the player's set of summoned strays
            Set<UUID> summonedStrays = PLAYER_SUMMONED_STRAYS.computeIfAbsent(player.getUUID(), k -> ConcurrentHashMap.newKeySet());

            // Create a copy of the set to avoid ConcurrentModificationException
            Set<UUID> straysToRemove = new HashSet<>(summonedStrays);

            for (UUID strayUUID : straysToRemove) {
                Entity entity = serverLevel.getEntity(strayUUID);
                if (entity instanceof Stray) {
                    entity.discard(); // Despawn the stray
                    summonedStrays.remove(strayUUID); // Remove the stray from the set
                }
            }

            // If the player's set is now empty, remove the player's entry
            if (summonedStrays.isEmpty()) {
                PLAYER_SUMMONED_STRAYS.remove(player.getUUID());
            }
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            despawnStraysForPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        despawnStraysForPlayer(event.getEntity());
    }

    @SubscribeEvent
    public static void onChangeDimensions(PlayerEvent.PlayerChangedDimensionEvent event) {
        despawnStraysForPlayer(event.getEntity());
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        if (!pPlayer.isShiftKeyDown()) {
            pPlayer.getCooldowns().addCooldown(stack.getItem(), 80);
            pLevel.playSound(pPlayer, pPlayer.getOnPos(), BrutalityModSounds.ICE_WAVE.get(), SoundSource.PLAYERS, 1,
                    Mth.nextFloat(pLevel.random, 0.65F, 1));
            if (pLevel.isClientSide()) {
                return InteractionResultHolder.pass(stack);
            }
            performSoulrendAttack(pLevel, pPlayer, stack);
        } else if (!pLevel.isClientSide()) {
            pPlayer.getCooldowns().addCooldown(stack.getItem(), 200);
            summonStrayArmy(pLevel, pPlayer, stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    public void summonStrayArmy(Level level, Player player, ItemStack stack) {
        player.getCooldowns().addCooldown(stack.getItem(), 20);

        // Get the player's set of summoned strays
        Set<UUID> summonedStrays = PLAYER_SUMMONED_STRAYS.computeIfAbsent(player.getUUID(), k -> ConcurrentHashMap.newKeySet());
        // Define the maximum number of strays allowed
        int maxStrays = 10;

        // Calculate how many strays can still be spawned
        int remainingStrays = maxStrays - summonedStrays.size();

        // If no more strays can be spawned, return early
        if (remainingStrays <= 0) {
            player.displayClientMessage(Component.translatable("item.frostmourne.stray_army.fail").withStyle(ChatFormatting.RED), true);
            return;
        }

        // Define the number of strays to spawn (random or fixed)
        int spawnAmt = level.random.nextInt(3, 6); // Random between 1 and 3

        // Ensure we don't exceed the remaining strays limit
        spawnAmt = Math.min(spawnAmt, remainingStrays);

        // Spawn the strays
        for (int i = 0; i < spawnAmt; i++) {
            stack.hurtAndBreak(1, player, pPlayer -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            Stray stray = new SummonedStray(level, player, false); // Do not add to set in constructor
            if (!level.isClientSide()) {
                stray.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(stray.getOnPos()), MobSpawnType.MOB_SUMMONED, null, null);
            }
            stray.setPos(
                    player.getX() + level.random.nextInt(-3, 3),
                    player.getY(),
                    player.getZ() + level.random.nextInt(-3, 3));
            stray.setOldPosAndRot();
            level.addFreshEntity(stray);

            // Add the stray to the player's set
            summonedStrays.add(stray.getUUID());
        }
    }


    public void performSoulrendAttack(Level level, Player player, ItemStack stack) {
        if (level instanceof ServerLevel serverLevel) {

            serverLevel.sendParticles(
                    new WaveParticleData<>(BrutalityModParticles.FROSTMOURNE_WAVE.get(), 15, 80),
                    player.getX(),
                    player.getY() + player.getBbHeight() / 3,
                    player.getZ(),
                    1,
                    0, 0, 0,
                    0
            );

            player.getCooldowns().addCooldown(stack.getItem(), 80);
            // Get nearby entities within the shockwave radius

            List<? extends Entity> entities = ModUtils.applyWaveEffect(serverLevel, player.getX(), player.getY(0.33F), player.getZ(), Entity.class,
                    new WaveParticleData<>(BrutalityModParticles.FROSTMOURNE_WAVE.get(), 15, 80), e -> e != player, entity -> {
                entity.hurt(player.damageSources().playerAttack(player), 5);
                entity.setTicksFrozen(40);
                if (entity instanceof LivingEntity)
                    ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 30, 3, false, true), entity);
                player.heal(1);
            });

            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, entities.size() * 3, entities.size() / 20, false, true), player);
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, entities.size() * 3, entities.size() / 20, false, true), player);

            stack.hurtAndBreak(entities.size() / 2, player, pPlayer -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
    }
}
