package net.goo.brutality.common.item.base;

import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.util.ModResources;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import javax.annotation.Nullable;
import java.util.UUID;

public interface BrutalityGeoItem extends GeoItem, ModResources {

    int baseAugmentSlots = 1;

    BrutalityCategories.AttackType getAttackType();

    UUID BASE_STUN_CHANCE_UUID = UUID.fromString("6d3d3787-e06f-4111-b03f-aed7c9317416");


    default String model(@Nullable ItemStack stack) {
        return null;
    }

    default String texture(@Nullable ItemStack stack) {
        return null;
    }

    default String animation(@Nullable ItemStack stack) {
        return null;
    }

    GeoAnimatable cacheItem();


    /**
     * Provides a hook to modify outgoing melee damage based on a specific weapon or item held by a Player.
     * <p>
     * <b>Advantages:</b>
     * This method is specifically designed for "True Melee" interactions. Unlike general attribute
     * modifiers, this logic only triggers when the {@link Player} actively swings the {@link ItemStack}.
     * This prevents unintended damage scaling from indirect sources, such as pets (e.g., Wolves)
     * or projectiles, even if the player is holding the item.
     * </p>
     * <p><b>Implementation Note:</b>
     * This is called in {@link net.goo.brutality.mixin.mixins.PlayerMixin} <br>
     * Do not ever call {@link Entity#hurt(DamageSource, float)} with a playerAttack damage source, infinite recursion
     * </p>
     *
     * @param attacker The {@link Player} initiating the attack.
     * @param victim   The {@link LivingEntity} receiving the hit.
     * @param weapon   The {@link ItemStack} currently being used to strike.
     * @param amount   The current damage value before this modification.
     * @return The modified damage value. By default, returns the {@code amount} unchanged.
     */
    default float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, float amount) {
        return amount;
    }

}
