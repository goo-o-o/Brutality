package net.goo.brutality.common.magic.spells.umbrancy;

import net.goo.brutality.common.entity.spells.umbrancy.PiercingMoonlight;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.math.phys.hitboxes.HitboxUtils;
import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.AOE;
import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.INSTANT;
import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.RANGE;

public class PiercingMoonlightSpell extends BrutalitySpell {


    public PiercingMoonlightSpell() {
        super(MagicSchool.UMBRANCY,
                List.of(INSTANT, AOE),
                "piercing_moonlight",
                40, 3, 50, 0, 1, List.of(
//                        new BrutalityTooltipHelper.SpellStatComponent(PIERCE, 100, 0, null, null),
                        new SpellTooltipRenderer.SpellStatComponent(RANGE, 50, 5, 50F, 100F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 2F;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        int zOffset = 3;
        PiercingMoonlight piercingMoonlight = new PiercingMoonlight(BrutalityEntities.PIERCING_MOONLIGHT_ENTITY.get(), player.level());
        piercingMoonlight.setOwner(player);
        piercingMoonlight.setPos(player.getEyePosition().add(player.getLookAngle().scale(zOffset)));
        piercingMoonlight.setSpellLevel(spellLevel);
        piercingMoonlight.setYRot(player.getYRot());
        piercingMoonlight.setXRot(player.getXRot());
        player.level().addFreshEntity(piercingMoonlight);

        float range = getFinalStat(spellLevel, getStat(RANGE));
        float damage = getActualDamage(player, spellLevel);

        OrientedBoundingBox hitbox = new OrientedBoundingBox(Vec3.ZERO, new Vec3(0.0625F, 0.0625F, range).scale(0.5F), 0, 0, 0);
        Vec3 offset = new Vec3(0, 0, zOffset + hitbox.halfExtents.z);

        HitboxUtils.RayResult<LivingEntity> result = HitboxUtils.handleRay(player, LivingEntity.class, hitbox, offset, null, true);

        piercingMoonlight.setDataMaxLength(result.distance - zOffset);
        float actualDamage = player.level().isNight() ? 2 * damage : damage;
        result.entitiesHit.forEach(e -> e.hurt(e.damageSources().indirectMagic(player, null), actualDamage));

        return true;

    }
}
