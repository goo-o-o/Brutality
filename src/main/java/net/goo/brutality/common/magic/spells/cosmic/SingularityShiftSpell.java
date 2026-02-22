package net.goo.brutality.common.magic.spells.cosmic;

import net.goo.brutality.common.entity.spells.cosmic.SingularityShiftEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.DURATION;
import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.SIZE;

public class SingularityShiftSpell extends BrutalitySpell {


    public SingularityShiftSpell() {
        super(MagicSchool.COSMIC,
                List.of(SpellCategory.INSTANT, SpellCategory.UTILITY, SpellCategory.AOE),
                "singularity_shift",
                80, 0, 1200, 0, 1, List.of(
                        new SpellTooltipRenderer.SpellStatComponent(SIZE, 2, 1F, 2F, 7F),
                        new SpellTooltipRenderer.SpellStatComponent(DURATION, 300, 100, 300F, 1100F)
                ));
    }

    @Override
    public float getManaCostLevelScaling() {
        return 10;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            SingularityShiftEntity singularityShiftEntity = new SingularityShiftEntity(BrutalityEntities.SINGULARITY_SHIFT_ENTITY.get(), serverLevel);
            singularityShiftEntity.setSpellLevel(spellLevel);
            singularityShiftEntity.setPos(player.getEyePosition());
            singularityShiftEntity.setOwner(player);
            singularityShiftEntity.setWeightless(player.isShiftKeyDown());
            singularityShiftEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, Math.min(0.5F + (spellLevel * 0.1F), 1), 0);
            serverLevel.addFreshEntity(singularityShiftEntity);
        }
        return true;
    }

}
