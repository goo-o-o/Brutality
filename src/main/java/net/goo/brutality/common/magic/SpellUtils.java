package net.goo.brutality.common.magic;

import net.goo.brutality.common.registry.BrutalityItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import top.theillusivec4.curios.api.CuriosApi;

import javax.annotation.Nullable;

public class SpellUtils {
    private static final float ECHO_CHAMBER_MAX = 0.25F, ECHO_CHAMBER_MANA_RATIO = 10F;


    public static float getCurioDamageMultiplier(@Nullable Entity entity, @Nullable IBrutalitySpell spell, @Nullable Integer spellLevel) {
        return 1;
    }

    public static float getCurioCastTimeMultiplier(@Nullable Entity entity, @Nullable IBrutalitySpell spell, @Nullable Integer spellLevel) {
        return 1;
    }

    public static float getCurioCooldownMultiplier(@Nullable Entity entity, @Nullable IBrutalitySpell spell, @Nullable Integer spellLevel) {
        if (!(entity instanceof Player caster)) return 1;
        float[] multiplier = new float[]{1};
        float[] manaCost = new float[]{1};
        if (spell != null && spellLevel != null) {
            manaCost[0] = spell.getActualManaCost(caster, spellLevel);
        }

        CuriosApi.getCuriosInventory(caster).ifPresent(handler -> {
            handler.findFirstCurio(BrutalityItems.ECHO_CHAMBER.get()).ifPresent(slot -> {
                multiplier[0] -= Math.min((manaCost[0] / ECHO_CHAMBER_MANA_RATIO) * 0.01F, ECHO_CHAMBER_MAX);
            });
        });
        return multiplier[0];
    }
}