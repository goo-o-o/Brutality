package net.goo.brutality.item.curios.charm;

import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityCurioItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

import static net.goo.brutality.item.curios.charm.PortableMiningRig.END_COIN;
import static net.goo.brutality.item.curios.charm.PortableMiningRig.NETHER_COIN;

public class CryptoWallet extends BrutalityCurioItem {
    public CryptoWallet(Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.CurioType.CHARM;
    }

    UUID CRYPTO_CHARM_AD_UUID = UUID.fromString("a0dcfef5-8322-4542-a40a-ce5c7f3bb236");
    UUID CRYPTO_CHARM_AS_UUID = UUID.fromString("99e26a45-2d98-4064-b934-2faa1e6dcae3");

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            CuriosApi.getCuriosInventory(player).ifPresent(handler -> {
                AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
                if (attackDamage != null) {
                    attackDamage.removeModifier(CRYPTO_CHARM_AD_UUID);

                    attackDamage.addTransientModifier(
                            new AttributeModifier(
                                    CRYPTO_CHARM_AD_UUID,
                                    "Temporary AD Bonus",
                                    stack.getOrCreateTag().getFloat(END_COIN),
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }


                AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);

                if (attackSpeed != null) {


                    // Remove old modifier (if exists)
                    attackSpeed.removeModifier(CRYPTO_CHARM_AS_UUID);

                    // Add new modifier with dynamic value
                    attackSpeed.addTransientModifier(
                            new AttributeModifier(
                                    CRYPTO_CHARM_AS_UUID,
                                    "Temporary Speed Bonus",
                                    stack.getOrCreateTag().getFloat(NETHER_COIN),
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }

            });
        }
    }

    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() instanceof Player player) {
            AttributeInstance attackSpeed = player.getAttribute(Attributes.ATTACK_SPEED);
            if (attackSpeed != null) {
                attackSpeed.removeModifier(CRYPTO_CHARM_AS_UUID);
            }
            AttributeInstance attackDamage = player.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attackDamage != null) {
                attackDamage.removeModifier(CRYPTO_CHARM_AD_UUID);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        tooltip.add(Component.empty());
        tooltip.add(Component.translatable("curios.modifiers.charm").withStyle(ChatFormatting.GOLD));

        float endCoin = stack.getOrCreateTag().getFloat(END_COIN);

        tooltip.add(Component.literal((endCoin >= 0 ? "+" : "") + endCoin + " ").append(Component.translatable("attribute.name.generic.attack_damage"))
                .withStyle(ChatFormatting.DARK_RED));


        float netherCoin = stack.getOrCreateTag().getFloat(NETHER_COIN);

        tooltip.add(Component.literal((netherCoin >= 0 ? "+" : "") + netherCoin + " ").append(Component.translatable("attribute.name.generic.attack_speed"))
                .withStyle(ChatFormatting.DARK_PURPLE));

    }

}

