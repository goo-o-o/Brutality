package net.goo.brutality.magic.spells.cosmic;

import net.goo.brutality.magic.BrutalitySpell;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.mcreator.terramity.init.TerramityModParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.Vec3;

import java.util.List;

import static net.goo.brutality.util.helpers.BrutalityTooltipHelper.SpellStatComponents.*;

public class GravitokinesisSpell extends BrutalitySpell {


    public GravitokinesisSpell() {
        super(MagicSchool.COSMIC,
                List.of(SpellCategory.INSTANT, SpellCategory.TARGET, SpellCategory.UTILITY),
                "gravitokinesis",
                40, 0, 160, 0, 1, List.of(
                        new BrutalityTooltipHelper.SpellStatComponent(RANGE, 5, 2, 5F, 100F),
                        new BrutalityTooltipHelper.SpellStatComponent(SIZE, 1, 0.5F, 1F, 5F),
                        new BrutalityTooltipHelper.SpellStatComponent(PIERCE, 3, 1, 1F, null)
                ));
    }

    @Override
    public int getCooldownLevelScaling() {
        return -5;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        float radius = getFinalStat(spellLevel, getStat(SIZE));
        float baseRange = getFinalStat(spellLevel, getStat(RANGE));
        float pierceCap = getFinalStat(spellLevel, getStat(PIERCE));

        if (player.level() instanceof ServerLevel serverLevel) {
            ModUtils.RayData<LivingEntity> entitiesInRay =
                    ModUtils.getEntitiesInRay(LivingEntity.class, player, baseRange, ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE, radius,
                            e -> e != player, ((int) pierceCap), null);

            List<LivingEntity> targets = entitiesInRay.entityList();

            Vec3 pushVec = player.getLookAngle().scale(1 + spellLevel / 3F);
            Vec3 pullVec = pushVec.scale(-1);
            if (player.isShiftKeyDown()) {
                targets.forEach(target -> {
                    target.push(pushVec.x(), pushVec.y(), pushVec.z());
                    serverLevel.sendParticles(TerramityModParticleTypes.COSMIC_BOOM.get(), target.getX(), target.getY(0.5), target.getZ(),
                            1, 0.1, 0.1 ,0.1, 0);
                    if (target instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                    }
                });
                player.push(pullVec.x(), pullVec.y() + 0.25F, pullVec.z());
                ((ServerPlayer) player).connection.send(new ClientboundSetEntityMotionPacket(player));

            } else {
                targets.forEach(target -> {
                    target.push(pullVec.x(), pullVec.y(), pullVec.z());
                    serverLevel.sendParticles(TerramityModParticleTypes.COSMIC_BOOM.get(), target.getX(), target.getY(0.5), target.getZ(),
                            1, 0.1, 0.1 ,0.1, 0);
                    if (target instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(target));
                    }
                });

            }
        }
        return true;
    }

}
