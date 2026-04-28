package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.build_archetypes.CoinHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.xmx.velthoric.physics.world.VxPhysicsWorld;

import java.util.List;

public class SwallowedPenny extends BrutalityCurioItem {
    public SwallowedPenny(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public float onWearerHurt(LivingEntity wearer, ItemStack stack, DamageSource source, float amount) {
        if (wearer instanceof Player player) {
            if (amount >= 2 && !player.getCooldowns().isOnCooldown(BrutalityItems.SWALLOWED_PENNY.get())) {
                List<ItemStack> coinsInInventory = CoinHelper.getCoinsInInventory(player);
                ItemStack coin = coinsInInventory.get(player.getRandom().nextInt(coinsInInventory.size()));
                BrutalityCoinItem coinItem = ((BrutalityCoinItem) coin.getItem());
                if (player.level() instanceof ServerLevel serverLevel) {
                    coinItem.playCoinTossSounds(player, serverLevel);
                    player.swing(InteractionHand.MAIN_HAND, true);
                    VxPhysicsWorld world = VxPhysicsWorld.get(serverLevel.dimension());

                    if (world != null && world.isRunning()) {
                        world.execute(() -> CoinHelper.spawnAndLaunchCoin(coinItem, player, coin, world));

                    }

                }
                player.getCooldowns().addCooldown(BrutalityItems.SWALLOWED_PENNY.get(), 60);
            }
        }
        return super.onWearerHurt(wearer, stack, source, amount);
    }
}
