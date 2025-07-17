package net.goo.brutality.item.curios;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.util.RenderUtils;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class CosineCharmCurio extends BrutalityCurioItem {
    public CosineCharmCurio(Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pProperties, identifier, rarity, descriptionComponents);
    }

    public static float getCurrentBonus() {
        return Mth.cos((float) (RenderUtils.getCurrentTick() * 0.1f)) * 0.25F + 0.125F;
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return CuriosApi.getCuriosInventory(slotContext.entity())
                .map(handler ->
                        handler.findFirstCurio(ModItems.SCIENTIFIC_CALCULATOR_BELT.get()).isPresent()
                )
                .orElse(false);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeed != null) {
                // Remove old modifier (if exists)
                attackSpeed.removeModifier(COSINE_CHARM_AS_UUID);

                // Add new modifier with dynamic value
                attackSpeed.addTransientModifier(
                        new AttributeModifier(
                                COSINE_CHARM_AS_UUID,
                                "Temporary Speed Bonus",
                                getCurrentBonus(),
                                AttributeModifier.Operation.MULTIPLY_TOTAL
                        )
                );
            }
        }
    }

    protected static final UUID COSINE_CHARM_AS_UUID = UUID.fromString("d11f8d34-2c5d-4fdc-880a-7a72500ba3e4");

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeed != null) {
                attackSpeed.removeModifier(COSINE_CHARM_AS_UUID);
            }
        }
    }


    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));
        float value = getCurrentBonus() * 100;

        String formattedValue = value % 1 == 0 ?
                String.format("%.0f", value) :
                String.format("%.1f", value);

        tooltip.add(Component.literal((value > 0 ? "+" : "") + formattedValue + "% ").append(Component.translatable("attribute.name.generic.attack_speed"))
                .withStyle(value > 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }
}
