package net.goo.brutality.common.magic.spells.brimwielder;

import net.goo.brutality.common.entity.spells.brimwielder.BrimspikeEntity;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

import static net.goo.brutality.util.tooltip.SpellTooltipRenderer.SpellStatComponentType.PIERCE;

public class BrimspikeSpell extends BrutalitySpell {


    public BrimspikeSpell() {
        super(MagicSchool.BRIMWIELDER, List.of(SpellCategory.CONTINUOUS, SpellCategory.AOE), "brimspike",
                10, 1, 100, 5, 1, List.of(
                        new SpellTooltipRenderer.SpellStatComponent(PIERCE, 1, 0, 1F, 1F)
                ));
    }

    @Override
    public float getDamageLevelScaling() {
        return 0.5F;
    }

    @Override
    public float getManaCostLevelScaling() {
        return 0.25F;
    }

    @Override
    public int getCooldownLevelScaling() {
        return 5;
    }


    @Override
    public boolean onCastTick(Player player, ItemStack stack, int spellLevel) {
        if (getSpellEntity(player.level(), player) instanceof BrimspikeEntity brimspikeEntity) {

            Vec3 targetPos = player.getEyePosition().add(player.getLookAngle().scale(5F));

            Vec3 currentPos = brimspikeEntity.position();
            Vector3f newPos = currentPos.lerp(targetPos, 0.2).toVector3f();

            Vec3 movement = new Vec3(newPos).subtract(currentPos);
            brimspikeEntity.setDeltaMovement(movement);
            String damage = String.format("%.2f", brimspikeEntity.getBaseDamage());
            player.displayClientMessage(Component.literal(damage), true);
        }
        return true;
    }

    @Override
    public boolean onStartCast(Player player, ItemStack stack, int spellLevel) {
        if (getSpellEntity(player.level(), player) == null) {
            BrimspikeEntity brimspikeEntity = new BrimspikeEntity(BrutalityEntities.BRIMSPIKE_ENTITY.get(), player.level());
            brimspikeEntity.setOwner(player);
            brimspikeEntity.setPos(player.getLookAngle().scale(3).add(player.getX(), player.getEyeY(), player.getZ()));
            brimspikeEntity.setYRot((float) (Math.PI / 2 - player.getViewYRot(1)));
            brimspikeEntity.setXRot((float) (Math.PI / 2 - player.getViewXRot(1)));
            brimspikeEntity.setSpellLevel(spellLevel);
            brimspikeEntity.setNoGravity(true);
            player.level().addFreshEntity(brimspikeEntity);
            return true;
        }
        player.displayClientMessage(Component.translatable("spell.brutality.brimspike.fail"), true);

        return false;
    }

    @Override
    public void onEndCast(Player player, ItemStack stack, int spellLevel) {
        if (getSpellEntity(player.level(), player) instanceof BrimspikeEntity brimspikeEntity) {
            brimspikeEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0,
                    Math.min(3.5F, ((stack.getUseDuration() - player.getUseItemRemainingTicks())) / 40F), 0);
        }
    }

    private Entity getSpellEntity(Level level, Player player) {
        for (BrimspikeEntity entity : level.getEntitiesOfClass(BrimspikeEntity.class, player.getBoundingBox().inflate(25F), e -> e.getOwner() == player)) {
            return entity;
        }

        return null;
    }


}
