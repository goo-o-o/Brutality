package net.goo.brutality.magic.spells.umbrancy;

import net.goo.brutality.magic.BrutalitySpell;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.*;

public class MoonlitMendingSpell extends BrutalitySpell {


    public MoonlitMendingSpell() {
        super(MagicSchool.UMBRANCY,
                List.of(INSTANT, SELF, UTILITY),
                "moonlit_mending",
                100, 0, 200, 0, 1, List.of(
                ));
    }

    @Override
    public int getCooldownLevelScaling() {
        return -2;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        long noonTime = 6000;
        long midnightTime = 18000;
        Level level = player.level();
        long currentTime = level.getDayTime() % 24000;

        float timeDiffFromNoon = Math.abs(currentTime - noonTime);
        float healingFactor = Math.min(timeDiffFromNoon / (midnightTime - noonTime), 1);
        float healPercent = 0.1F + (0.9F * healingFactor);

        MutableComponent component = Component.translatable("spell.brutality.moonlit_mending.healed_for").append((healPercent * 100) + "% Max Health").withStyle(ChatFormatting.GREEN);
        player.displayClientMessage(component, true);

        float maxHealth = player.getMaxHealth();
        float currentHealth = player.getHealth();
        float healAmount = maxHealth * healPercent;

        // Apply healing first
        player.heal(healAmount);

        // Then we calculate excess healing (amount that would exceed maxHealth)
        float missingHealth = maxHealth - currentHealth;
        float excessHealing = Math.max(0, healAmount - missingHealth);

        // Apply excess as absorption, capped at maxHealth for balancing
        if (excessHealing > 0) {
            float currentAbsorption = player.getAbsorptionAmount();
            float newAbsorption = Math.min(maxHealth, currentAbsorption + excessHealing);
            player.setAbsorptionAmount(newAbsorption);
        }

        return true;
    }
}
