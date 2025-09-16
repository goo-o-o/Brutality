package net.goo.brutality.magic.spells.umbrancy;

import net.goo.brutality.entity.spells.umbrancy.PiercingMoonlight;
import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;

import java.util.List;

import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.AOE;
import static net.goo.brutality.magic.IBrutalitySpell.SpellCategory.INSTANT;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.PIERCE;
import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class PiercingMoonlightSpell extends BrutalitySpell {


    public PiercingMoonlightSpell() {
        super(MagicSchool.UMBRANCY,
                List.of(INSTANT, AOE),
                "piercing_moonlight",
                40, 3, 50, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(PIERCE, 100, 0, null, null),
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 50, 5, 50F, 100F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 2F;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
            PiercingMoonlight piercingMoonlight = new PiercingMoonlight(BrutalityModEntities.PIERCING_MOONLIGHT_ENTITY.get(), player.level());
            piercingMoonlight.setOwner(player);
            piercingMoonlight.setPos(player.getEyePosition().add(player.getLookAngle().scale(3.0)));
            piercingMoonlight.setSpellLevel(spellLevel);
            piercingMoonlight.setYRot(player.getYRot());
            piercingMoonlight.setXRot(player.getXRot());
            player.level().addFreshEntity(piercingMoonlight);

            float range = getFinalStat(spellLevel, getStat(RANGE));
            int pierce = (int) getFinalStat(spellLevel, getStat(PIERCE));
            float damage = getFinalDamage(player, spellLevel);

            ModUtils.RayData<LivingEntity> rayData = ModUtils.getEntitiesInRay(LivingEntity.class, player, range,
                    ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, 0.0625F, e -> e != player, pierce,
                    null
            );

            piercingMoonlight.setDataMaxLength(rayData.distance() - 2);
            int nightMult = player.level().isNight() ? 2 : 1;
            rayData.entityList().forEach(e -> e.hurt(e.damageSources().indirectMagic(player, null), (e.distanceTo(player) > 9.5 && e.distanceTo(player) < 10.5) ? damage * 3 * nightMult : damage * nightMult));

        return true;

    }
}
