package net.goo.brutality.common.magic.spells.celestia;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.ParticleHelper;
import net.goo.brutality.util.tooltip.SpellTooltips;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.common.magic.IBrutalitySpell.SpellCategory.*;
import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.DURATION;
import static net.goo.brutality.util.tooltip.SpellTooltips.SpellStatComponents.RANGE;

public class LightBindingSpell extends BrutalitySpell {


    public LightBindingSpell() {
        super(MagicSchool.CELESTIA,
                List.of(INSTANT, TARGET, DEBUFF),
                "light_binding",
                40, 0, 200, 0, 1, List.of(
                        new SpellTooltips.SpellStatComponent(RANGE, 10, 4, 0F, 40F),
                        new SpellTooltips.SpellStatComponent(DURATION, 20, 10, 0F, 100F)
                ));
    }


    @Override
    public float getManaCostLevelScaling() {
        return 10;
    }


    @Override
    public float getDamageLevelScaling() {
        return 0;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        SpellTooltips.SpellStatComponent range = getStat(RANGE);
        SpellTooltips.SpellStatComponent duration = getStat(DURATION);

        Entity target = ModUtils.getEntityPlayerLookingAt(player, getFinalStat(spellLevel, range));
        if (!(target instanceof LivingEntity livingTarget)) {
            player.displayClientMessage(Component.translatable("spell.brutality.dematerialize.no_target_found"), true);
            return false;
        }

        if (livingTarget.isSpectator() || (livingTarget instanceof Player targetPlayer && targetPlayer.isCreative())) {
            player.displayClientMessage(Component.translatable("spell.brutality.dematerialize.invalid_target"), true);
            return false;
        }

        Vec3 startPos = player.getPosition(1).add(0, player.getBbHeight() / 2, 0);
        Vec3 endPos = target.getPosition(1).add(0, target.getBbHeight() / 2, 0);

        Vec3 direction = startPos.vectorTo(endPos);
        double distance = direction.length();
        Vec3 unitDir = direction.normalize();

        if (player.level() instanceof ServerLevel serverLevel) {
            for (double d = 0; d < distance; d += 0.5F) {
                Vec3 intervalPos = new Vec3(unitDir.toVector3f()).scale(d);
                Vec3 particlePos = startPos.add(intervalPos);

                ParticleHelper.sendParticles(serverLevel, TerramityModParticleTypes.HOLY_GLINT.get(), true, particlePos, 0, 0, 0, 0, 1);
            }
            ParticleHelper.sendParticles(serverLevel, TerramityModParticleTypes.HOLY_GLINT.get(), true, target, target.getBbWidth() / 2,
                    target.getBbHeight() / 2, target.getBbWidth() / 2, (int) (target.getBoundingBox().getSize() * 10), 0);
        }

        livingTarget.addEffect(new MobEffectInstance(BrutalityEffects.LIGHT_BOUND.get(), (int) getFinalStat(spellLevel, duration), 0));

        return true;
    }

}
