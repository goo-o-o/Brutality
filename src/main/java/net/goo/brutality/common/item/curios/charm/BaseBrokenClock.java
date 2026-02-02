package net.goo.brutality.common.item.curios.charm;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class BaseBrokenClock extends BrutalityCurioItem {
    private static final float TIMEKEEPERS_CLOCK_DEBUFF_DURATION = 0.75F, TCOFT_DEBUFF_DURATION = 0.65F, TCOFT_BUFF_DURATION = 1.25F;

    private static final Supplier<List<Pair<Item, Float>>> CHANCES = Suppliers.memoize(() -> List.of(
            Pair.of(BrutalityItems.THE_CLOCK_OF_FROZEN_TIME.get(), 0.6F),
            Pair.of(BrutalityItems.TIMEKEEPERS_CLOCK.get(), 0.5F),
            Pair.of(BrutalityItems.SUNDERED_CLOCK.get(), 0.35F),
            Pair.of(BrutalityItems.SHATTERED_CLOCK.get(), 0.25F),
            Pair.of(BrutalityItems.BROKEN_CLOCK.get(), 0.25F)
    ));

    public BaseBrokenClock(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity()).map(handler ->
                handler.isEquipped(itemStack -> itemStack.getItem() instanceof BaseBrokenClock)
        ).orElse(super.canEquip(slotContext, stack));
    }


    /**
     * Intercepts status effect ticking to potentially freeze duration.
     * Uses a prioritized list to ensure the best equipped clock takes precedence.
     */
    public static void pauseTickDown(LivingEntity livingEntity, MobEffectInstance effect, CallbackInfoReturnable<Boolean> cir) {
        if (effect.isInfiniteDuration()) return;

        CuriosApi.getCuriosInventory(livingEntity).ifPresent(handler -> {
            for (Pair<Item, Float> entry : CHANCES.get()) {
                Item clock = entry.getFirst();

                if (handler.isEquipped(clock)) {
                    // We found the highest priority clock equipped, so we roll once and stop looking.
                    if (livingEntity.getRandom().nextFloat() < entry.getSecond()) {

                        // Specific logic: Broken Clock is unstable (pauses everything), others only pause buffs.
                        boolean isBroken = clock == BrutalityItems.BROKEN_CLOCK.get();
                        if (isBroken || effect.getEffect().isBeneficial()) {
                            cir.setReturnValue(true);
                        }
                    }
                    // Break the loop regardless of the roll outcome because we found the highest tier item.
                    break;
                }
            }
        });
    }

    /**
     * Processes and modifies the duration of a MobEffectInstance based on equipped time-related gear.
     * <p>
     * This logic handles scaling for both beneficial and harmful effects.
     * {@link BrutalityItems#THE_CLOCK_OF_FROZEN_TIME} affects both categories, while the {@link BrutalityItems#TIMEKEEPERS_CLOCK}
     * specifically targets debuffs.
     * </p>
     *
     * @param entity         The entity receiving the effect.
     * @param effectInstance The original effect instance being applied.
     * @return A new {@link MobEffectInstance} with adjusted duration, or {@code null} if no modification is needed.
     */
    @Nullable
    public static MobEffectInstance getModifiedTimeEffect(LivingEntity entity, MobEffectInstance effectInstance) {
        return CuriosApi.getCuriosInventory(entity).resolve().map(handler -> {
            MobEffect effect = effectInstance.getEffect();
            MobEffectCategory category = effect.getCategory();
            int originalDuration = effectInstance.getDuration();
            double multiplier;

            // Condition 1: The Clock of Frozen Time (Affects Buffs and Debuffs)
            if (handler.isEquipped(BrutalityItems.THE_CLOCK_OF_FROZEN_TIME.get())) {
                multiplier = (category == MobEffectCategory.HARMFUL) ? TCOFT_DEBUFF_DURATION : TCOFT_BUFF_DURATION;
            }
            // Condition 2: Timekeeper's Clock (Affects only Debuffs)
            else if (category == MobEffectCategory.HARMFUL && handler.isEquipped(BrutalityItems.TIMEKEEPERS_CLOCK.get())) {
                multiplier = TIMEKEEPERS_CLOCK_DEBUFF_DURATION;
            } else {
                return null; // No relevant gear equipped
            }

            // Apply scaling and ensure duration is at least 1 tick
            int newDuration = Math.max((int) (originalDuration * multiplier), 1);

            // Return a copy with the new duration
            return new MobEffectInstance(
                    effect,
                    newDuration,
                    effectInstance.getAmplifier(),
                    effectInstance.isAmbient(),
                    effectInstance.isVisible(),
                    effectInstance.showIcon()
            );
        }).orElse(effectInstance);
    }
}
