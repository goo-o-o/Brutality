package net.goo.brutality.common.item.curios.hands;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.util.build_archetypes.CoinHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;

import java.util.List;

public class HandOfMidas extends BrutalityCurioItem {

    public HandOfMidas(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static void activeAbility(Player player, float cdMult) {
        List<ItemStack> coinStacks = CoinHelper.getCoinsInInventoryOffCd(player);
        for (ItemStack stack : coinStacks) {
            BrutalityCoinItem coinItem = ((BrutalityCoinItem) stack.getItem());
            if (player.level() instanceof ServerLevel serverLevel) {
                coinItem.playCoinTossSounds(player, serverLevel);
                player.swing(InteractionHand.MAIN_HAND, true);
                VxPhysicsWorld world = VxPhysicsWorld.get(serverLevel.dimension());

                if (world != null && world.isRunning()) {
                    world.execute(() -> CoinHelper.spawnAndLaunchCoin(coinItem, player, stack, world, coinStacks.size() * 0.05F + 0.2F));

                    player.getCooldowns().addCooldown(coinItem, (int) (coinItem.getCooldownTime(player) * cdMult));
                }
            }
        }
    }
}
