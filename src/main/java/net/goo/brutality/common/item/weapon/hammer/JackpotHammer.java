package net.goo.brutality.common.item.weapon.hammer;

import net.goo.brutality.common.item.base.BrutalityHammerItem;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Set;

public class JackpotHammer extends BrutalityHammerItem {


    public JackpotHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, DamageSource source, float amount) {
        int damage = attacker.getRandom().nextInt(-5, 11);
        Level level = attacker.level();
        if (damage > 8) {

            MutableComponent displayComponent = Component.translatable(
                    "item.brutality.jackpot.message." + attacker.getRandom().nextIntBetweenInclusive(1, 3),
                    damage
            );

            attacker.displayClientMessage(displayComponent, true);

            level.playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                    ModUtils.getRandomSound(BrutalitySounds.JACKPOT_SOUNDS),
                    SoundSource.PLAYERS, 1F, Mth.nextFloat(level.random, 0.5f, 1F));

            if (level instanceof ServerLevel serverLevel) {
                Set<SimpleParticleType> particleTypes = Set.of(
                        TerramityModParticleTypes.COIN_DISPENSED.get(),
                        TerramityModParticleTypes.POKER_BLUE.get(),
                        TerramityModParticleTypes.POKER_BLACK.get(),
                        TerramityModParticleTypes.POKER_GREEN.get(),
                        TerramityModParticleTypes.POKER_RED.get(),
                        TerramityModParticleTypes.POKER_YELLOW.get(),
                        TerramityModParticleTypes.DICE_PARTICLE.get()
                );

                particleTypes.forEach(particle ->
                        serverLevel.sendParticles(particle, victim.getX(), victim.getY() + victim.getBbHeight() / 2, victim.getZ(),
                        10, 0.5, 0.5, 0.5, level.random.nextFloat()));

            }
        } else {
            attacker.displayClientMessage(Component.translatable(
                    "item.brutality.jackpot.message.4",
                    damage
            ), true);
        }

        return damage;
    }

    public static float getRandomDamage(Player player) {
        return player.tickCount % 10 + 4;
    }
}
