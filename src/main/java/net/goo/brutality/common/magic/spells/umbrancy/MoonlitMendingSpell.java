package net.goo.brutality.common.magic.spells.umbrancy;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.*;

public class MoonlitMendingSpell extends BrutalitySpell {


    public MoonlitMendingSpell() {
        super(MagicSchool.UMBRANCY,
                List.of(INSTANT, SELF, UTILITY),
                "moonlit_mending",
                100, 0, 400, 0, 1, List.of(
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

        MutableComponent component = Component.translatable(
                "spell.brutality.moonlit_mending.healed_for",
                healPercent
        );

        player.displayClientMessage(component, true);

        float maxHealth = player.getMaxHealth();
        float healAmount = maxHealth * healPercent;

        // Apply healing first
        player.heal(healAmount);

        return true;
    }
}
