package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;

public class PortableMiningRig extends BrutalityCurioItem {
    public PortableMiningRig(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public static final String END_COIN = "endCoin", NETHER_COIN = "netherCoin";

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!(livingEntity.level() instanceof ServerLevel serverLevel)) return;
            if (livingEntity.tickCount % 20 != 0) return;
            CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
                handler.findFirstCurio(BrutalityModItems.CRYPTO_WALLET.get()).ifPresent(slotResult -> {
                            ItemStack cryptoWalletStack = slotResult.stack();
                            RandomSource random = serverLevel.getRandom();
                            if (random.nextIntBetweenInclusive(0, 100) < 3) {
                                float baseReward = Mth.nextFloat(random, 0, 0.005F); // Higher initial reward
                                float cap = 25.0F;
                                float power = 2.0F;

                                float currentEndCoins = cryptoWalletStack.getOrCreateTag().getFloat(END_COIN);
                                float logFraction = (float) (Math.log(currentEndCoins + 1) / Math.log(cap + 1));
                                float adjustedReward = baseReward * (1 - (float) Math.pow(logFraction, power));

                                cryptoWalletStack.getOrCreateTag().putFloat(END_COIN, currentEndCoins + adjustedReward);
                            }
                            if (random.nextIntBetweenInclusive(0, 100) < 2) {
                                float baseReward = Mth.nextFloat(random, 0, 0.001F); // Higher initial reward
                                float cap = 1.5F;
                                float power = 2.0F;

                                float currentNetherCoins = cryptoWalletStack.getOrCreateTag().getFloat(NETHER_COIN);
                                float logFraction = (float) (Math.log(currentNetherCoins + 1) / Math.log(cap + 1));
                                float adjustedReward = baseReward * (1 - (float) Math.pow(logFraction, power));

                                cryptoWalletStack.getOrCreateTag().putFloat(NETHER_COIN, currentNetherCoins + adjustedReward);
                            }
                        }
                );
            });
        }
    }

}

