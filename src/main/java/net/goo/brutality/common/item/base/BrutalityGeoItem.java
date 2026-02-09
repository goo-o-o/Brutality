package net.goo.brutality.common.item.base;

import com.mojang.blaze3d.platform.InputConstants;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.util.ModResources;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public interface BrutalityGeoItem extends GeoItem, ModResources {
    default String getRegistryName() {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey((Item) this)).getPath().toLowerCase(Locale.ROOT);
    }

    BrutalityCategories.AttackType getAttackType();

    UUID BASE_STUN_CHANCE_UUID = UUID.fromString("6d3d3787-e06f-4111-b03f-aed7c9317416");


    BrutalityCategories category();

    default String model(@Nullable ItemStack stack) {
        return null;
    }

    default String texture(@Nullable ItemStack stack) {
        return null;
    }

    default String animation(@Nullable ItemStack stack) {
        return null;
    }

    default String getCategoryAsString() {
        return category().toString().toLowerCase(Locale.ROOT);
    }

    GeoAnimatable cacheItem();


    default void brutalityTooltipHandler(ItemStack stack, List<Component> pTooltipComponents, List<ItemDescriptionComponent> descriptionComponents, Rarity rarity) {
        String identifier = getRegistryName();
        for (ItemDescriptionComponent descriptionComponent : descriptionComponents) {

            String componentLower = descriptionComponent.type().toString().toLowerCase(Locale.ROOT);
            if (!descriptionComponent.type().equals(ItemDescriptionComponent.ItemDescriptionComponents.LORE)) {
                pTooltipComponents.add(Component.translatable(
                        Brutality.MOD_ID + ".description.type." + componentLower));
            }

            for (int i = 1; i <= descriptionComponent.lines(); i++) {
                pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + "." + componentLower + "." + i));
            }

            if (descriptionComponent.cooldownTicks() != null) {
                int totalSecs = (int) (descriptionComponent.cooldownTicks() / 20F);
                int hours = (int) (totalSecs / 3600F);
                int minutes = (int) ((totalSecs % 3600) / 60F);
                int seconds = totalSecs % 60;

                StringBuilder formatted = new StringBuilder();
                boolean first = true;
                if (hours > 0) {
                    formatted.append(hours).append(" hour");
                    first = false;
                }
                if (minutes > 0) {
                    if (!first) formatted.append(" ");
                    formatted.append(minutes).append(" minute");
                    first = false;
                }
                if (seconds > 0 || (hours == 0 && minutes == 0)) {
                    if (!first) formatted.append(" ");
                    formatted.append(seconds).append(" second");
                }


                MutableComponent component = Component.literal(" -" + formatted + " ")
                        .append(Component.translatable("message." + Brutality.MOD_ID + ".cooldown"));

                pTooltipComponents.add(component.withStyle(ChatFormatting.DARK_AQUA));
            }

            if (descriptionComponent.keySupplier() != null) {
                InputConstants.Key key = descriptionComponent.keySupplier().get();
                if (key != null) {
                    MutableComponent component = Component.literal("(")
                            .append(Component.translatable("key.brutality.current_keybind"))
                            .append(": ")
                            .append(key.getDisplayName())
                            .append(")");
                    pTooltipComponents.add(component.withStyle(ChatFormatting.GRAY));

                }
            }


            if (!descriptionComponent.equals(descriptionComponents.get(descriptionComponents.size() - 1)))
                pTooltipComponents.add(Component.empty());
        }

        if (stack.isEnchanted() && ((6 & ItemStack.TooltipPart.ENCHANTMENTS.getMask()) == 0)) {
            pTooltipComponents.add(Component.empty());
        }
    }

    /**
     * Provides a hook to modify outgoing melee damage based on a specific weapon or item held by a Player.
     * <p>
     * <b>Advantages:</b>
     * This method is specifically designed for "True Melee" interactions. Unlike general attribute
     * modifiers, this logic only triggers when the {@link Player} actively swings the {@link ItemStack}.
     * This prevents unintended damage scaling from indirect sources, such as pets (e.g., Wolves)
     * or projectiles, even if the player is holding the item.
     * </p>
     * * <p><b>Implementation Note:</b>
     * This is called in {@link net.goo.brutality.mixin.mixins.PlayerMixin}
     * </p>
     *
     * @param attacker The {@link Player} initiating the attack.
     * @param victim   The {@link LivingEntity} receiving the hit.
     * @param weapon   The {@link ItemStack} currently being used to strike.
     * @param source   The {@link DamageSource} of the attack, useful for checking damage types.
     * @param amount   The current damage value before this modification.
     * @return The modified damage value. By default, returns the {@code amount} unchanged.
     */
    default float hurtEnemyModifiable(Player attacker, LivingEntity victim, ItemStack weapon, DamageSource source, float amount) {
        return amount;
    }

}
