package net.goo.brutality.common.item.generic;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityMagicItem;
import net.goo.brutality.common.magic.BrutalitySpell;
import net.goo.brutality.common.magic.IBrutalitySpell;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.attribute.AttributeContainer;
import net.goo.brutality.util.magic.AugmentHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public class BrutalityAugmentItem extends Item {
    public final BrutalityMagicItem.MagicItemType[] magicItemTypes;
    public int spellSlotBonus = 0;
    private int passiveLines = 0;

    public BrutalityAugmentItem(Properties pProperties, BrutalityMagicItem.MagicItemType... magicItemTypes) {
        super(pProperties);
        this.magicItemTypes = magicItemTypes;
    }

    public BrutalityAugmentItem withPassiveLines(int passiveLines) {
        this.passiveLines = passiveLines;
        return this;
    }

    public BrutalityAugmentItem withSpellSlotBonus(int spellSlotBonus) {
        this.spellSlotBonus = spellSlotBonus;
        return this;
    }

    public List<AttributeContainer> attributeTemplates = List.of();

    private static boolean shouldShowInTooltip(int pHideFlags, ItemStack.TooltipPart pPart) {
        return (pHideFlags & pPart.getMask()) == 0;
    }

    private int getHideFlags(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("HideFlags", 99) ? stack.getTag().getInt("HideFlags") : stack.getItem().getDefaultTooltipHideFlags(stack);
    }

    // We need to emulate attribute modifier tooltips here without actually adding modifiers, there are other ways to go about this such as
    // actually adding the modifiers, but then we lose the ability to do "When on Book" or "When on Staff"
    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        int hideFlags = this.getHideFlags(pStack);
        if (shouldShowInTooltip(hideFlags, ItemStack.TooltipPart.MODIFIERS)) {
            Minecraft mc = Minecraft.getInstance();
            Player pPlayer = mc.player;

            for (BrutalityMagicItem.MagicItemType type : magicItemTypes) {


                // 1. Collect all template containers
                List<AttributeContainer> allTemplates = new ArrayList<>(this.attributeTemplates);

                // Add Augment templates
                List<BrutalityAugmentItem> augments = AugmentHelper.getAugmentsFromItem(pStack);
                for (BrutalityAugmentItem augment : augments) {
                    allTemplates.addAll(augment.attributeTemplates);
                }

                if (!allTemplates.isEmpty() || spellSlotBonus != 0 || passiveLines > 0) {
                    pTooltipComponents.add(CommonComponents.EMPTY);
                    pTooltipComponents.add(Component.translatable("item.modifiers." + type.toString().toLowerCase(Locale.ROOT))
                            .withStyle(ChatFormatting.GRAY));

                    for (int i = 1; i <= passiveLines; i++) {
                        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + pStack.getItem() + ".passive." + i));
                    }

                    if (spellSlotBonus > 0) {
                        pTooltipComponents.add(Component.translatable("message.brutality.spell_slots", ("+" + spellSlotBonus)).withStyle(ChatFormatting.BLUE));
                    } else if (spellSlotBonus < 0) {
                        pTooltipComponents.add(Component.translatable("message.brutality.spell_slots", spellSlotBonus).withStyle(ChatFormatting.RED));
                    }

                    for (AttributeContainer holder : allTemplates) {
                        Attribute attribute = holder.attribute();
                        double amount = holder.value();
                        AttributeModifier.Operation operation = holder.operation();

                        // Specific logic for base attack values (Damage/Speed)
                        boolean isBaseValue = false;
                        // We check the attribute type since we don't have the UUID until creation
                        if (pPlayer != null) {
                            if (attribute.equals(Attributes.ATTACK_DAMAGE)) {
                                amount += pPlayer.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                                amount += EnchantmentHelper.getDamageBonus(pStack, MobType.UNDEFINED);
                                isBaseValue = true;
                            } else if (attribute.equals(Attributes.ATTACK_SPEED)) {
                                amount += pPlayer.getAttributeBaseValue(Attributes.ATTACK_SPEED);
                                isBaseValue = true;
                            }
                        }

                        // Handle RangedPercentage redirect
                        if (attribute instanceof BrutalityAttributes.RangedPercentageAttribute && operation == AttributeModifier.Operation.ADDITION) {
                            operation = AttributeModifier.Operation.MULTIPLY_BASE;
                        }

                        // Formatting the number
                        double displayAmount;
                        if (operation != AttributeModifier.Operation.MULTIPLY_BASE && operation != AttributeModifier.Operation.MULTIPLY_TOTAL) {
                            displayAmount = attribute.equals(Attributes.KNOCKBACK_RESISTANCE) ? amount * 10.0D : amount;
                        } else {
                            displayAmount = amount * 100.0D;
                        }

                        // Final Component building
                        if (isBaseValue) {
                            pTooltipComponents.add(CommonComponents.space().append(Component.translatable("attribute.modifier.equals." + operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), Component.translatable(attribute.getDescriptionId()))).withStyle(ChatFormatting.DARK_GREEN));
                        } else if (amount > 0.0D) {
                            pTooltipComponents.add(Component.translatable("attribute.modifier.plus." + operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.BLUE));
                        } else if (amount < 0.0D) {
                            displayAmount *= -1.0D;
                            pTooltipComponents.add(Component.translatable("attribute.modifier.take." + operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.RED));
                        }
                    }
                }
            }
        }
    }

    public BrutalityAugmentItem withAttributes(AttributeContainer... attributes) {
        this.attributeTemplates = List.of(attributes);
        return this;
    }

    // ran everytime a magic item is cast which has this item, ran after a spell is actually cast
    public void onAugmentedItemPostCast(Player caster, ItemStack parent, BrutalitySpell spell, int spellLevel, IBrutalitySpell.SpellCategory type) {
    }

    // ran everytime a magic item is cast which has this item, ran before a spell is actually cast, can also be used to modify spell level
    public int onAugmentedItemPreCast(Player caster, ItemStack parent, BrutalitySpell spell, int spellLevel, IBrutalitySpell.SpellCategory type) {
        return spellLevel;
    }

    // for stuff like adding slots or something
    public void onAddedToItem(ItemStack parent) {
    }
}
