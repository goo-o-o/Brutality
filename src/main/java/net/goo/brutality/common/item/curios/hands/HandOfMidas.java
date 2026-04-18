package net.goo.brutality.common.item.curios.hands;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HandOfMidas extends BrutalityCurioItem {

    public HandOfMidas(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static void activeAbility(Player player, float cdMult) {
        Set<ItemStack> coinStacks = new HashSet<>(player.getInventory().items);

        for (ItemStack stack : coinStacks) {
            if (stack.getItem() instanceof BrutalityCoinItem coinItem) {

                if (!player.getCooldowns().isOnCooldown(coinItem)) {
                    if (player.level() instanceof ServerLevel serverLevel) {
                        coinItem.playCoinTossSounds(player, serverLevel);
                        player.swing(InteractionHand.MAIN_HAND, true);
                        VxPhysicsWorld world = VxPhysicsWorld.get(serverLevel.dimension());

                        if (world != null && world.isRunning()) {
                            world.execute(() -> coinItem.spawnAndLaunchCoin(player, stack, world, 180));

                            player.getCooldowns().addCooldown(coinItem, (int) (coinItem.cooldownTime * cdMult));
                        }
                    }
                }
            }
        }
    }
}
