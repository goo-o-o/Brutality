package net.goo.brutality.item.curios.necklace;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BloodHowlPendant extends BrutalityCurioItem {


    public BloodHowlPendant(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.NECKLACE;
    }

    UUID BLOOD_HOWL_RAGE_GAIN_UUID = UUID.fromString("9f300d11-6844-4a87-8350-eb0cee580300");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance rageGainAttr = player.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
            if (rageGainAttr != null) {

                if (player.getHealth() / player.getMaxHealth() < 0.5F) {

                    // Remove old modifier (if exists)
                    rageGainAttr.removeModifier(BLOOD_HOWL_RAGE_GAIN_UUID);

                    // Add new modifier with dynamic value
                    rageGainAttr.addTransientModifier(
                            new AttributeModifier(
                                    BLOOD_HOWL_RAGE_GAIN_UUID,
                                    "Temporary Speed Bonus",
                                    0.5F,
                                    AttributeModifier.Operation.MULTIPLY_TOTAL
                            )
                    );
                } else {
                    rageGainAttr.removeModifier(BLOOD_HOWL_RAGE_GAIN_UUID);
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance rageGainAttr = player.getAttribute(ModAttributes.RAGE_GAIN_MULTIPLIER.get());
            if (rageGainAttr != null) {
                rageGainAttr.removeModifier(BLOOD_HOWL_RAGE_GAIN_UUID);
            }
        }
    }
}
