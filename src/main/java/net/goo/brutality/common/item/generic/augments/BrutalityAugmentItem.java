package net.goo.brutality.common.item.generic.augments;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.attribute.AttributeContainer;
import net.goo.brutality.util.attribute.SlottedAttributeContainer;
import net.goo.brutality.util.item.ItemCategoryUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static net.minecraft.world.item.ItemStack.ATTRIBUTE_MODIFIER_FORMAT;

public abstract class BrutalityAugmentItem extends Item {
    private int passiveLines = 0;
    public final BrutalityCategories[] itemTypes;

    public BrutalityAugmentItem(Properties pProperties, BrutalityCategories... itemTypes) {
        super(pProperties);
        this.itemTypes = itemTypes;
    }

    public BrutalityAugmentItem withPassiveLines(int passiveLines) {
        this.passiveLines = passiveLines;
        return this;
    }


    protected boolean shouldShowSection() {
        return !this.attributeTemplates.isEmpty() || passiveLines > 0;
    }

    protected abstract void addCustomTooltipLines(List<Component> components);

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
        pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + ".augment_item.description.1"));

        int hideFlags = this.getHideFlags(pStack);
        if (shouldShowInTooltip(hideFlags, ItemStack.TooltipPart.MODIFIERS)) {
            Minecraft mc = Minecraft.getInstance();
            Player pPlayer = mc.player;

            if (itemTypes.length == 0) {
                appendTooltipModifiers("item", pPlayer, pStack, pTooltipComponents);
            } else {
                for (BrutalityCategories type : itemTypes) {
                    appendTooltipModifiers(type.toString(), pPlayer, pStack, pTooltipComponents);
                }
            }
        }
    }

    private void appendTooltipModifiers(String type, Player pPlayer, ItemStack pStack, List<Component> pTooltipComponents) {
        List<AttributeContainer> allTemplates = new ArrayList<>(this.attributeTemplates);

        // Add Augment templates
        List<BrutalityAugmentItem> augments = AugmentHelper.getAugmentsFromItem(pStack);
        for (BrutalityAugmentItem augment : augments) {
            allTemplates.addAll(augment.attributeTemplates);
        }

        if (shouldShowSection()) {
            pTooltipComponents.add(CommonComponents.EMPTY);
            pTooltipComponents.add(Component.translatable("item.modifiers." + type.toLowerCase(Locale.ROOT))
                    .withStyle(ChatFormatting.GRAY));

            for (int i = 1; i <= passiveLines; i++) {
                pTooltipComponents.add(Component.translatable("item." + Brutality.MOD_ID + "." + pStack.getItem() + ".passive." + i));
            }

            addCustomTooltipLines(pTooltipComponents);

            for (AttributeContainer holder : allTemplates) {
                Attribute attribute = holder.attribute();
                double amount = holder.value();
                AttributeModifier.Operation operation = holder.operation();


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

                if (amount > 0.0D) {
                    pTooltipComponents.add(Component.translatable("attribute.modifier.plus." + operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.BLUE));
                } else if (amount < 0.0D) {
                    displayAmount *= -1.0D;
                    pTooltipComponents.add(Component.translatable("attribute.modifier.take." + operation.toValue(), ATTRIBUTE_MODIFIER_FORMAT.format(displayAmount), Component.translatable(attribute.getDescriptionId())).withStyle(ChatFormatting.RED));
                }
            }
        }
    }

    public BrutalityAugmentItem withAttributes(AttributeContainer... attributes) {
        this.attributeTemplates = List.of(attributes);
        return this;
    }

    // for stuff like adding slots or something
    public void onAddedToItem(ItemStack parent) {
    }

    public static void addAugmentAttributeModifiers(ItemStack stack, EquipmentSlot pSlot, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir) {
        List<BrutalityAugmentItem> augments = AugmentHelper.getAugmentsFromItem(stack);

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(cir.getReturnValue());
        ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (itemName != null) {
            UUID baseUuid = UUID.nameUUIDFromBytes(itemName.toString().getBytes());

            for (int i = 0; i < augments.size(); i++) {
                BrutalityAugmentItem augmentItem = augments.get(i);
                for (int j = 0; j < augmentItem.attributeTemplates.size(); j++) {

                    AttributeContainer holder = augmentItem.attributeTemplates.get(j);
                    boolean shouldAdd = false;

                    if (ItemCategoryUtils.isWeapon(stack) || ItemCategoryUtils.isTool(stack)) {
                        shouldAdd = pSlot == EquipmentSlot.MAINHAND || pSlot == EquipmentSlot.OFFHAND;
                    } else if (ItemCategoryUtils.isHelmet(stack)) {
                        shouldAdd = pSlot == EquipmentSlot.HEAD;
                    } else if (ItemCategoryUtils.isChestplate(stack)) {
                        shouldAdd = pSlot == EquipmentSlot.CHEST;
                    } else if (ItemCategoryUtils.isLeggings(stack)) {
                        shouldAdd = pSlot == EquipmentSlot.LEGS;
                    } else if (ItemCategoryUtils.isBoots(stack)) {
                        shouldAdd = pSlot == EquipmentSlot.FEET;
                    }

                    if (holder instanceof SlottedAttributeContainer slotted) {
                        for (EquipmentSlot slot : slotted.slots()) {
                            if (slot == pSlot) {
                                shouldAdd = true;
                                break;
                            }
                        }
                    }


                    if (shouldAdd) {

                        UUID attributeUUID = new UUID(
                                baseUuid.getMostSignificantBits() ^ holder.attribute().getDescriptionId().hashCode(),
                                baseUuid.getLeastSignificantBits() ^ i ^ j
                        );

                        AttributeModifier modifier = holder.createModifier(attributeUUID);
                        builder.put(holder.attribute(), modifier);
                    }
                }
            }
            cir.setReturnValue(builder.build());
        }
    }
}
