package net.goo.brutality.magic.spells.brimwielder;

import net.goo.brutality.entity.spells.brimwielder.ChthonicCapsuleEntity;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class ChthonicCapsuleSpell extends BrutalitySpell {


    public ChthonicCapsuleSpell() {
        super(MagicSchool.BRIMWIELDER,
                List.of(SpellCategory.INSTANT, SpellCategory.AOE, SpellCategory.UTILITY),
                "chthonic_capsule",
                30, 5, 80, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(BrutalityTooltipHelper.SpellStatComponents.SIZE, 3, 1, 3F, 50F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 1;
    }


    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            ChthonicCapsuleEntity chthonicCapsuleSpell = new ChthonicCapsuleEntity(BrutalityModEntities.CHTHONIC_CAPSULE_ENTITY.get(), serverLevel);
            chthonicCapsuleSpell.setSpellLevel(spellLevel);
            chthonicCapsuleSpell.setPos(player.getEyePosition());
            chthonicCapsuleSpell.setOwner(player);
            chthonicCapsuleSpell.shootFromRotation(player, player.getXRot(), player.getYRot(), 0F, Math.min(0.5F + (spellLevel * 0.1F), 1), 0);
            serverLevel.addFreshEntity(chthonicCapsuleSpell);
        }
        return true;
    }
}
