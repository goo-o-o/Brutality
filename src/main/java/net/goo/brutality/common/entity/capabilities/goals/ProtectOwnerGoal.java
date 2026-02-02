package net.goo.brutality.common.entity.capabilities.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;

public class ProtectOwnerGoal extends NearestAttackableTargetGoal<LivingEntity> {
    private final Mob entity;
    private final Player player;

    public ProtectOwnerGoal(Mob mob, Player player) {
        super(mob, LivingEntity.class, 10, true, false, null);
        this.entity = mob;
        this.player = player;
    }

    @Override
    public boolean canUse() {
        LivingEntity attacker = player.getLastHurtByMob();

        if (attacker == null) {
            return false; // Player is not under attack
        }

        this.targetConditions = TargetingConditions.forCombat()
                .range(getFollowDistance())
                .selector(target -> entity.hasLineOfSight(attacker));

        // Call the parent method to check if there's a valid target
        return super.canUse();
    }

}