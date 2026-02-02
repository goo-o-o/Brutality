package net.goo.brutality.common.magic.spells.cosmic;

import net.goo.brutality.common.entity.spells.cosmic.StarStreamEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.tooltip.BrutalityTooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.CONTINUOUS;
import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.TARGET;
import static net.goo.brutality.util.tooltip.BrutalityTooltipHelper.SpellStatComponents.SPEED;

public class StarStreamSpell extends BrutalitySpell {


    public StarStreamSpell() {
        super(MagicSchool.COSMIC,
                List.of(CONTINUOUS, TARGET),
                "star_stream",
                8, 3, 20, 10, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(SPEED, 1, 0.25F, 1F, 3F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.25F;
    }

    @Override
    public int getCooldownLevelScaling() {
        return 2;
    }

    @Override
    public int getCastTimeLevelScaling() {
        return -1;
    }

    @Override
    public boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        if (player.level() instanceof ServerLevel serverLevel) {
            Vec3 target;
            ModUtils.RayData<Entity> rayData = ModUtils.getEntitiesInRay(Entity.class, player, 20, ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, 0.5F, e -> e != player && !(e instanceof StarStreamEntity), 1, null);

            if (!rayData.entityList().isEmpty()) {
                Entity targetEntity = rayData.entityList().get(0);
                target = targetEntity.position().add(0, targetEntity.getBbHeight() / 2, 0);
            } else {
                BlockPos blockPos = ModUtils.getBlockLookingAt(player, false, 20);
                if (blockPos == null) {
                    target = player.getEyePosition(1).add(player.getLookAngle().scale(20));
                } else {
                    target = Vec3.atCenterOf(blockPos);
                }
            }
            StarStreamEntity spellEntity = new StarStreamEntity(BrutalityEntities.STAR_STREAM_ENTITY.get(), serverLevel);
            spellEntity.setSpellLevel(spellLevel);
            Vec3 randomPos = player.position().add(player.getRandom().nextGaussian() * 2, player.getRandom().nextDouble() * 2 + 2, player.getRandom().nextGaussian() * 2);
            spellEntity.setPos(randomPos);
            spellEntity.setOwner(player);
            float inaccuracy = (5 - spellLevel) * 0.25F;
            target.add(
                    Mth.nextFloat(player.getRandom(), -inaccuracy, inaccuracy),
                    Mth.nextFloat(player.getRandom(), -inaccuracy, inaccuracy),
                    Mth.nextFloat(player.getRandom(), -inaccuracy, inaccuracy)
            );

            Vec3 direction = target.subtract(randomPos).normalize();

            spellEntity.shoot(direction.x, direction.y, direction.z, getFinalStat(spellLevel, getStat(SPEED)), Math.max(5 - spellLevel, 0));

            serverLevel.addFreshEntity(spellEntity);
            serverLevel.playSound(null, player.getX(), player.getY(0.5), player.getZ(), BrutalitySounds.BASS_BOP.get(), SoundSource.AMBIENT,
                    1.5F - ((float) Math.min(spellLevel, 10) / 10), Mth.nextFloat(player.getRandom(), 0.7F, 1.2F));
        }
        return true;
    }
}
