package net.goo.brutality.common.item.curios.charm;

import net.goo.brutality.common.config.BrutalityCommonConfig;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.nbt.CompoundTag;
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
        LivingEntity entity = slotContext.entity();
        if (entity == null || entity.level().isClientSide || entity.tickCount % 20 != 0) return;

        CuriosApi.getCuriosInventory(entity).ifPresent(handler -> {
            handler.findFirstCurio(BrutalityItems.CRYPTO_WALLET.get()).ifPresent(slotResult -> {
                ItemStack wallet = slotResult.stack();
                RandomSource random = entity.level().getRandom();
                CompoundTag tag = wallet.getOrCreateTag();

                // Config & Factors
                double genFactor = BrutalityCommonConfig.PORTABLE_MINING_RIG_GENERATION_FACTOR.get();

                // --- End Coin Mining ---
                if (shouldMine(random, 3, genFactor)) {
                    float current = tag.getFloat(END_COIN);
                    float max = BrutalityCommonConfig.MAXIMUM_END_COINS.get().floatValue();

                    if (current < max) {
                        tag.putFloat(END_COIN, current + calculateReward(random, current, max, 0.005F, genFactor));
                    }
                }

                // --- Nether Coin Mining ---
                if (shouldMine(random, 2, genFactor)) {
                    float current = tag.getFloat(NETHER_COIN);
                    float max = BrutalityCommonConfig.MAXIMUM_NETHER_COINS.get().floatValue();

                    if (current < max) {
                        tag.putFloat(NETHER_COIN, current + calculateReward(random, current, max, 0.001F, genFactor));
                    }
                }
            });
        });
    }

    /**
     * Calculates a reward with diminishing returns using a logarithmic curve.
     * As 'current' approaches 'max', the reward scales down towards zero.
     */
    private float calculateReward(RandomSource random, float current, float max, float baseMax, double factor) {
        float baseReward = Mth.nextFloat(random, 0, baseMax) * (float) factor;
        float power = 2.0F;

        // Logarithmic fraction prevents flat linear drop-off, making early mining feel better
        float logFraction = (float) (Math.log(current + 1) / Math.log(max + 1));
        float multiplier = 1 - (float) Math.pow(logFraction, power);

        return Math.max(0, baseReward * multiplier);
    }

    /**
     * Determines if a mining "tick" succeeds based on a base percent and a generation factor.
     */
    private boolean shouldMine(RandomSource random, int basePercent, double factor) {
        return random.nextFloat() < (basePercent / 100f) * factor;
    }

}

