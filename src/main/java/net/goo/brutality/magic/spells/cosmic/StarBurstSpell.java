package net.goo.brutality.magic.spells.cosmic;

import net.goo.brutality.entity.spells.cosmic.StarStreamEntity;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.AOE;
import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.INSTANT;
import static net.goo.brutality.util.helpers.tooltip.BrutalityTooltipHelper.SpellStatComponents.QUANTITY;

public class StarBurstSpell extends BrutalitySpell {


    public StarBurstSpell() {
        super(MagicSchool.COSMIC,
                List.of(INSTANT, AOE),
                "star_burst",
                50, 3, 20, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(QUANTITY, 5, 3, 5F, null)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.5F;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 15;
    }

    @Override
    public int getCooldownLevelScaling() {
        return 2;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            RandomSource random = player.getRandom();
            for (int i = 0; i < getFinalStat(spellLevel, getStat(QUANTITY)); i++) {
            StarStreamEntity spellEntity = new StarStreamEntity(BrutalityModEntities.STAR_STREAM_ENTITY.get(), serverLevel);
                spellEntity.setSpellLevel(spellLevel);
                spellEntity.setOwner(player);
                Vec3 randomPos = player.getEyePosition().add(random.nextDouble() * 0.2, random.nextDouble() * 0.2, random.nextDouble() * 0.2);
                spellEntity.setPos(randomPos);
                spellEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 2, 15);
                serverLevel.addFreshEntity(spellEntity);
            }
            serverLevel.playSound(null, player.getX(), player.getY(0.5), player.getZ(), BrutalityModSounds.BASS_BOOM.get(), SoundSource.AMBIENT,
                    spellLevel * 0.5F, Mth.nextFloat(random, 0.7F, 1.2F));
        }
        return true;
    }
}
