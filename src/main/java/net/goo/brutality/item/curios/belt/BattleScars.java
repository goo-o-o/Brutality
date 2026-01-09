package net.goo.brutality.item.curios.belt;

import net.goo.brutality.item.curios.BrutalityCurioItem;
import net.goo.brutality.registry.BrutalityModAttributes;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class BattleScars extends BrutalityCurioItem {


    public BattleScars(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    UUID BATTLE_SCARS_RAGE_GAIN_UUID = UUID.fromString("551269f9-4bd5-4a74-a03d-55be778f39bc");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance rageGainAttr = player.getAttribute(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get());
            if (rageGainAttr != null) {
                List<MobEffectInstance> harmfulEffects = player.getActiveEffects().stream().toList().stream()
                        .filter(mobEffectInstance -> mobEffectInstance.getEffect().getCategory() == MobEffectCategory.HARMFUL).toList();


                if (!harmfulEffects.isEmpty()) {
                    rageGainAttr.removeModifier(BATTLE_SCARS_RAGE_GAIN_UUID);

                    rageGainAttr.addTransientModifier(
                            new AttributeModifier(
                                    BATTLE_SCARS_RAGE_GAIN_UUID,
                                    "Temporary Speed Bonus",
                                    harmfulEffects.size() * 0.15F,
                                    AttributeModifier.Operation.MULTIPLY_TOTAL
                            )
                    );
                } else {
                    rageGainAttr.removeModifier(BATTLE_SCARS_RAGE_GAIN_UUID);
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        AttributeInstance rageGainAttr = slotContext.entity().getAttribute(BrutalityModAttributes.DAMAGE_TO_RAGE_RATIO.get());
        if (rageGainAttr != null) {
            rageGainAttr.removeModifier(BATTLE_SCARS_RAGE_GAIN_UUID);
        }
    }
}
