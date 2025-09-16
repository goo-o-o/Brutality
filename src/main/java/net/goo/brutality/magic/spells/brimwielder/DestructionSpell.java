package net.goo.brutality.magic.spells.brimwielder;

import net.goo.brutality.entity.spells.brimwielder.DestructionEntity;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.SIZE;

public class DestructionSpell extends BrutalitySpell {


    public DestructionSpell() {
        super(MagicSchool.BRIMWIELDER, List.of(SpellCategory.INSTANT, SpellCategory.AOE), "destruction", 50, 10, 140, 0, 1, List.of(
                new BrutalityTooltipHelper.SpellStatComponent(SIZE, 3, 0, 3F, 3F)
        ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 2;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 10F;
    }


    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        DestructionEntity destructionEntity = new DestructionEntity(BrutalityModEntities.DESTRUCTION_ENTITY.get(), player.level());
        destructionEntity.setSpellLevel(spellLevel);
        destructionEntity.setPos(player.getX(), player.getY() + 1, player.getZ());
        destructionEntity.setYRot(player.getViewYRot(1));
        player.level().addFreshEntity(destructionEntity);

        return true;
    }

}
