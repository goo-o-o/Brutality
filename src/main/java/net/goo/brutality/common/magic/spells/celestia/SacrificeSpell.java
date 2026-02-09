package net.goo.brutality.common.magic.spells.celestia;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.SpellTooltips;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.RANGE;

public class SacrificeSpell extends BrutalitySpell {


    public SacrificeSpell() {
        super(MagicSchool.CELESTIA,
                List.of(SpellCategory.INSTANT, SpellCategory.AOE, SpellCategory.BUFF),
                "sacrifice",
                80, 0, 600, 0, 1, List.of(
                        new SpellTooltips.SpellStatComponent(SpellTooltips.SpellStatComponents.HEALING, 10, 2, null, null),
                        new SpellTooltips.SpellStatComponent(SpellTooltips.SpellStatComponents.RANGE, 5, 1, null, 25F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 10;
    }

    @Override
    public int getCooldownLevelScaling() {
        return -5;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        SpellTooltips.SpellStatComponent healthStat = getStat(SpellTooltips.SpellStatComponents.HEALING);
        float healPool = getFinalStat(spellLevel, healthStat);

        if (player.level() instanceof ServerLevel serverLevel) {
            WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityParticles.HEAL_WAVE.get(), getFinalStat(spellLevel, getStat(RANGE)), 20);

            serverLevel.sendParticles(
                    waveParticleData,
                    player.getX(),
                    player.getY(0.1),
                    player.getZ(),
                    1,
                    0, 0, 0,
                    0
            );
        }

        List<Player> nearbyPlayers = ModUtils.getEntitiesInSphere(Player.class, player, p -> p != player, getFinalStat(spellLevel, getStat(SpellTooltips.SpellStatComponents.RANGE)));

        float casterMissingHealth = player.getMaxHealth() - player.getHealth();
        float casterHealAmount = Math.min(casterMissingHealth, healPool);
        player.heal(casterHealAmount);
        healPool -= casterHealAmount;

        if (healPool < 0) return true;

        float excessHealing = 0f;

        if (!nearbyPlayers.isEmpty()) {
            float healingPerPlayer = healPool / nearbyPlayers.size();
            for (Player receiver : nearbyPlayers) {
                float requiredHealing = receiver.getMaxHealth() - receiver.getHealth();
                if (requiredHealing < healingPerPlayer) {
                    receiver.heal(requiredHealing);
                    excessHealing += healingPerPlayer - requiredHealing;
                } else {
                    receiver.heal(healingPerPlayer);
                }
            }
        } else {
            return true;
        }

        if (excessHealing > 0) {
            List<Player> eligiblePlayers = new ArrayList<>(nearbyPlayers);
            while (excessHealing > 0 && !eligiblePlayers.isEmpty()) {
                // Find player with the lowest health percentage
                Player lowestHealthPlayer = eligiblePlayers.stream()
                        .min(Comparator.comparingDouble(p -> p.getHealth() / p.getMaxHealth()))
                        .orElse(null);

                float requiredHealing = lowestHealthPlayer.getMaxHealth() - lowestHealthPlayer.getHealth();
                if (requiredHealing <= 0.001f) {
                    // Remove fully healed player
                    eligiblePlayers.remove(lowestHealthPlayer);
                    continue;
                }

                if (requiredHealing < excessHealing) {
                    lowestHealthPlayer.heal(requiredHealing);
                    excessHealing -= requiredHealing;
                    eligiblePlayers.remove(lowestHealthPlayer); // No more healing needed
                } else {
                    lowestHealthPlayer.heal(excessHealing);
                    break;
                }
            }
        }



        return true;
    }
}
