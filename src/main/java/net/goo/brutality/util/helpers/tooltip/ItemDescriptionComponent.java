package net.goo.brutality.util.helpers.tooltip;

import com.mojang.blaze3d.platform.InputConstants;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record ItemDescriptionComponent(
        ItemDescriptionComponents type,
        int lines,
        Integer cooldownTicks,
        @Nullable Supplier<InputConstants.Key> keySupplier  // Supplier, not direct Key
) {
    public ItemDescriptionComponent(ItemDescriptionComponents type, int lines) {
        this(type, lines, null, null);
    }

    public ItemDescriptionComponent(ItemDescriptionComponents type, int lines, int cooldownTicks) {
        this(type, lines, cooldownTicks, null);
    }

    public ItemDescriptionComponent(ItemDescriptionComponents type, int lines, Supplier<InputConstants.Key> keySupplier) {
        this(type, lines, null, keySupplier);
    }

    public enum ItemDescriptionComponents {
        ACTIVE, PASSIVE, FULL_SET_PASSIVE, FULL_SET_ACTIVE, ON_HIT, WHEN_THROWN, ON_SWING, ON_LEFT_CLICKING_ENTITY, ON_RIGHT_CLICK, ON_SHIFT_RIGHT_CLICK, LORE, ON_KILL, ON_SHOOT, ON_HOLD_RIGHT_CLICK, CHARM, DASH_ABILITY, ON_SUCCESSFUL_DODGE, MANA_COST
    }
}
