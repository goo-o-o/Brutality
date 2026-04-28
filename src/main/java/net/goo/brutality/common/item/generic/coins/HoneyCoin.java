package net.goo.brutality.common.item.generic.coins;

import com.github.stephengold.joltjni.BodyInterface;
import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.registry.BrutalityEffects;
import net.goo.brutality.common.velthoric.CoinContactListener;
import net.goo.brutality.common.velthoric.bodies.CoinRigidBody;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.xmx.velthoric.physics.body.type.VxBody;

import javax.annotation.Nullable;
import java.util.List;

public class HoneyCoin extends BrutalityCoinItem {


    public HoneyCoin(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties, cooldownTime, descriptionComponents);
        bcs.setRestitution(0);
        bcs.setFriction(1);
    }

    @Override
    protected float getBasePixelDiameter() {
        return 12;
    }

    @Override
    public void onHeads(Player player, ItemStack stack, Vec3 location) {
        playBuffSounds(player);
        for (LivingEntity nearbyEntity : player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, new AABB(location, location).inflate(5))) {
            nearbyEntity.addEffect(new MobEffectInstance(BrutalityEffects.SLICKED.get(), 200));
        }
    }

    @Override
    public void onTails(Player player, ItemStack stack, Vec3 location) {
        playDebuffSounds(player);
        player.addEffect(new MobEffectInstance(BrutalityEffects.SLICKED.get(), 200));
    }

    @Override
    public void onCollide(CoinRigidBody coinBody, ItemStack stack, @Nullable VxBody other, CoinContactListener.CollisionType type) {
        // 1. Get the BodyInterface from the physics system
        if (type != CoinContactListener.CollisionType.STATIC) return;
        BodyInterface bi = coinBody.getPhysicsWorld().getPhysicsSystem().getBodyInterface();

        // 2. Get the Jolt BodyID from your rigid body wrapper
        int joltId = coinBody.getBodyId();

        // 3. Deactivate the body to "freeze" it in place
        bi.deactivateBody(joltId);
    }
}
