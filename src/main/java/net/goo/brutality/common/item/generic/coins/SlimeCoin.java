package net.goo.brutality.common.item.generic.coins;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SlimeCoin extends BrutalityCoinItem {


    public SlimeCoin(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties, cooldownTime, descriptionComponents);
        bcs.setRestitution(0.5F);
    }

    @Override
    protected float getBasePixelDiameter() {
        return 14;
    }

    @Override
    public void onHeads(Player player, ItemStack stack, Vec3 location) {
        playBuffSounds(player);
        for (LivingEntity nearbyEntity : player.level().getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, new AABB(location, location).inflate(5))) {
            nearbyEntity.push(0, 2, 0);
        }
    }

    @Override
    public void onTails(Player player, ItemStack stack, Vec3 location) {
        playDebuffSounds(player);
        player.push(0, 2, 0);
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSetEntityMotionPacket(player));
        }
    }

}
