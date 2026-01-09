package net.goo.brutality.magic.spells.umbrancy;

import net.goo.brutality.entity.spells.umbrancy.CrescentDart;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.*;
import static net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper.SpellStatComponents.PIERCE;

public class CrescentDartSpell extends BrutalitySpell {


    public CrescentDartSpell() {
        super(MagicSchool.UMBRANCY,
                List.of(CONTINUOUS, TARGET, AOE),
                "crescent_dart",
                12, 1, 20, 7, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(PIERCE, 1, 0.5F, 1F, 5F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.25F;
    }


    @Override
    public boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            CrescentDart crescentDart = new CrescentDart(BrutalityModEntities.CRESCENT_DART_ENTITY.get(), serverLevel);
            crescentDart.setOwner(player);
            crescentDart.setPos(player.getEyePosition().add(player.getLookAngle().scale(1.5)));
            crescentDart.setSpellLevel(spellLevel);
            crescentDart.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1,0);
            serverLevel.addFreshEntity(crescentDart);
        }
        return true;
    }
}
