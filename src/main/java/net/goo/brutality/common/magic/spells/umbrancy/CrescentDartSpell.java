package net.goo.brutality.common.magic.spells.umbrancy;

import net.goo.brutality.common.entity.spells.umbrancy.CrescentDart;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.tooltip.SpellTooltips;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.*;
import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.PIERCE;

public class CrescentDartSpell extends BrutalitySpell {


    public CrescentDartSpell() {
        super(MagicSchool.UMBRANCY,
                List.of(CONTINUOUS, TARGET, AOE),
                "crescent_dart",
                12, 1, 20, 7, 1, List.of(
                        new SpellTooltips.SpellStatComponent(PIERCE, 1, 0.5F, 1F, 5F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.25F;
    }


    @Override
    public boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            CrescentDart crescentDart = new CrescentDart(BrutalityEntities.CRESCENT_DART_ENTITY.get(), serverLevel);
            crescentDart.setOwner(player);
            crescentDart.setPos(player.getEyePosition().add(player.getLookAngle().scale(1.5)));
            crescentDart.setSpellLevel(spellLevel);
            crescentDart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1,0);
            serverLevel.addFreshEntity(crescentDart);
        }
        return true;
    }
}
