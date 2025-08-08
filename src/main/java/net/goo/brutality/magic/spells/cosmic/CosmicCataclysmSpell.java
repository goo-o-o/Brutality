package net.goo.brutality.magic.spells.cosmic;

import net.goo.brutality.entity.spells.cosmic.CosmicCataclysmSpellEntity;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class CosmicCataclysmSpell extends BrutalitySpell {


    public CosmicCataclysmSpell() {
        super(MagicSchool.COSMIC, SpellType.SINGLETON_AOE, "cosmic_cataclysm",
                100, 10, 100, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 3, 1, 3, 50),
                        new BrutalityTooltipHelper.SpellStatComponent(BrutalityTooltipHelper.SpellStatComponents.SIZE, 3, 1, 3, 50)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 1;
    }

    @Override
    public int getManaCostLevelScaling() {
        return 5;
    }

    @Override
    public int getCooldownLevelScaling() {
        return -5;
    }

    @Override
    public boolean onCast(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, getFinalStat(spellLevel, getStat(RANGE)));
            if (blockPos == null) return false;
            Vec3 targetPos = blockPos.getCenter();
            Vec3 spawnPos = targetPos.add(Mth.nextFloat(serverLevel.random, -2F, 2F), 20, Mth.nextFloat(serverLevel.random, -2F, 2F));
            Vec3 targetVec = spawnPos.subtract(targetPos);


            CosmicCataclysmSpellEntity spellEntity = new CosmicCataclysmSpellEntity(BrutalityModEntities.COSMIC_CATACLYSM_ENTITY.get(), serverLevel);
            spellEntity.setSpellLevel(spellLevel);
            spellEntity.setPos(spawnPos);
            spellEntity.setOwner(player);
            spellEntity.setDeltaMovement(targetVec);
            serverLevel.addFreshEntity(spellEntity);
        }
        return true;
    }
}
