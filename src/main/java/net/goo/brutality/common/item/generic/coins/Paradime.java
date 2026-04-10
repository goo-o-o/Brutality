package net.goo.brutality.common.item.generic.coins;

import net.goo.brutality.common.item.base.BrutalityCoinItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class Paradime extends BrutalityCoinItem {


    public Paradime(Properties pProperties, int cooldownTime, List<ItemDescriptionComponent> descriptionComponents) {
        super(pProperties, cooldownTime, descriptionComponents);
    }

    /**
     * Triggered when a coin flip lands on heads. Provides the player and the item stack involved in the flip.
     *
     * @param player The {@link Player} who initiated the coin flip. This provides information such as player state and context.
     * @param stack  The {@link ItemStack} representing the coin used in the flip. Contains details like the item's properties and state.
     */
    @Override
    public void onHeads(Player player, ItemStack stack) {
        playBuffSounds(player);
        player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2));
    }

    /**
     * Triggered when the Brutality Coin lands on tails after being used.
     * This method is abstract and must be implemented to define the specific behavior
     * that occurs when the tails side is the result of a coin flip.
     *
     * @param player The {@link Player} who initiated the coin flip.
     * @param stack  The {@link ItemStack} representing the Brutality Coin item being used.
     */
    @Override
    public void onTails(Player player, ItemStack stack) {
        playDebuffSounds(player);
        player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 1));
    }

}
