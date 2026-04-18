package net.goo.brutality.common.item.generic.coins;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class UrielsPenny extends BrutalityCoinItem {


    public UrielsPenny(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties, cooldownTime, descriptionComponents);
    }

    /**
     * @return
     */
    @Override
    protected float getBasePixelDiameter() {
        return 12;
    }

    /**
     * Triggered when a coin flip lands on heads. Provides the player and the item stack involved in the flip.
     *
     * @param player   The {@link Player} who initiated the coin flip. This provides information such as player state and context.
     * @param stack    The {@link ItemStack} representing the coin used in the flip. Contains details like the item's properties and state.
     * @param location
     */
    @Override
    public void onHeads(Player player, ItemStack stack, Vec3 location) {
        playBuffSounds(player);
        player.heal(6);
        List<MobEffectInstance> harmfulEffects = player.getActiveEffects().stream()
                .filter(instance -> instance.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                .toList();

        if (!harmfulEffects.isEmpty()) {
            MobEffectInstance toRemove = harmfulEffects.get(player.getRandom().nextInt(harmfulEffects.size()));
            player.removeEffect(toRemove.getEffect());
        }
    }

    /**
     * Triggered when the Brutality Coin lands on tails after being used.
     * This method is abstract and must be implemented to define the specific behavior
     * that occurs when the tails side is the result of a coin flip.
     *
     * @param player   The {@link Player} who initiated the coin flip.
     * @param stack    The {@link ItemStack} representing the Brutality Coin item being used.
     * @param location
     */
    @Override
    public void onTails(Player player, ItemStack stack, Vec3 location) {
        playDebuffSounds(player);
    }

}
