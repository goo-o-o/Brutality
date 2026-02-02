package net.goo.brutality.common.magic.spells.voidwalker;

import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityDamageTypes;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.ParticleHelper;
import net.goo.brutality.util.tooltip.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.tooltip.BrutalityTooltipHelper.SpellStatComponents.RANGE;

public class DematerializeSpell extends BrutalitySpell {


    public DematerializeSpell() {
        super(MagicSchool.VOIDWALKER,
                List.of(SpellCategory.INSTANT, SpellCategory.TARGET),
                "dematerialize",
                40, 7, 120, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 10, 2, 0F, 50F)
                ));
    }


    @Override
    public float getDamageLevelScaling() {
        return 3;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {

        Entity target = ModUtils.getEntityPlayerLookingAt(player, getFinalStat(spellLevel, getStat(RANGE)));
        if (!(target instanceof LivingEntity livingTarget)) {
            player.displayClientMessage(Component.translatable("spell.brutality.dematerialize.no_target_found"), true);
            return false;
        }
        if (livingTarget.getHealth() > getFinalDamage(player, spellLevel)) {
            player.displayClientMessage(Component.translatable("spell.brutality.dematerialize.too_much_health"), true);
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

                ParticleHelper.sendParticles(serverLevel, TerramityModParticleTypes.ANTIMATTER.get(), true, particlePos, 0, 0, 0, 0, 1);
            }
            ParticleHelper.sendParticles(serverLevel, TerramityModParticleTypes.ANTIMATTER.get(), true, target, target.getBbWidth() / 2,
                    target.getBbHeight() / 2, target.getBbWidth() / 2, (int) (target.getBoundingBox().getSize() * 10), 0);
        }

        if (livingTarget instanceof Player targetPlayer) {
            targetPlayer.hurt(BrutalityDamageTypes.dematerialize(targetPlayer), Integer.MAX_VALUE);
            targetPlayer.setHealth(0);

        } else {
            livingTarget.discard();
        }

        return true;
    }

}
