package net.goo.brutality.item.curios;

import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
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

public class SineCharmCurio extends BrutalityCurioItem {
    public SineCharmCurio(Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pProperties, identifier, rarity, descriptionComponents);
    }

    public static float getCurrentDisplayValue() {
        return Mth.sin((float) (RenderUtils.getCurrentTick() * 0.1f)) * 5f + 2.5f;
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
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                attackDamage.setBaseValue(getCurrentDisplayValue());
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());

        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));
        float value = getCurrentDisplayValue();

        String formattedValue = value % 1 == 0 ?
                String.format("%.0f", value) :
                String.format("%.1f", value);

        tooltip.add(Component.literal((value >= 0 ? "+" : "") + formattedValue + " ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(value >= 0 ? ChatFormatting.BLUE : ChatFormatting.RED));

    }

}
