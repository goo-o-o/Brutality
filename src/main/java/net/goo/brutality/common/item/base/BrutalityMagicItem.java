package net.goo.brutality.common.item.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.generic.BrutalityAugmentItem;
import net.goo.brutality.util.attribute.AttributeContainer;
import net.goo.brutality.util.magic.AugmentHelper;
import net.goo.brutality.util.magic.SpellStorage;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.goo.brutality.util.tooltip.MagicItemAugmentComponent;
import net.goo.brutality.util.tooltip.SpellTooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class BrutalityMagicItem extends BrutalityGenericItem {
    public int baseSpellSlots, baseAugmentSlots;
    public MagicItemType type;

    public enum MagicItemType {
        TOME,
        STAFF,
        WAND
    }

    public BrutalityMagicItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents, int baseSpellSlots, int baseAugmentSlots) {
        super(rarity, descriptionComponents);
        this.baseSpellSlots = baseSpellSlots;
        this.baseAugmentSlots = baseAugmentSlots;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            List<BrutalityAugmentItem> brutalityAugmentItems = AugmentHelper.getAugmentsFromItem(stack);

            ResourceLocation itemName = ForgeRegistries.ITEMS.getKey(stack.getItem());
            if (itemName != null) {
                UUID baseUuid = UUID.nameUUIDFromBytes(itemName.toString().getBytes());

                for (int i = 0; i < brutalityAugmentItems.size(); i++) {
                    BrutalityAugmentItem augmentItem = brutalityAugmentItems.get(i);
                    for (int j = 0; j < augmentItem.attributeTemplates.size(); j++) {

                        AttributeContainer holder = augmentItem.attributeTemplates.get(j);

                        UUID attributeUUID = new UUID(
                                baseUuid.getMostSignificantBits() ^ holder.attribute().getDescriptionId().hashCode(),
                                baseUuid.getLeastSignificantBits() ^ i ^ j
                        );

                        AttributeModifier modifier = holder.createModifier(attributeUUID);
                        builder.put(holder.attribute(), modifier);
                    }
                }
            }
            return builder.build();
        }

        return modifiers;
    }

    // instead of using getTooltipImage, we directly modify in the tooltip event so that we can easier control where it is located
    @OnlyIn(Dist.CLIENT)
    public static void renderAugmentSlots(RenderTooltipEvent.GatherComponents event) {
        Minecraft mc = Minecraft.getInstance();
        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof BrutalityMagicItem)) return;
        if (!InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue())) return;

        // 2. Find the insertion point
        int insertionIndex = -1;
        for (int i = 0; i < elements.size(); i++) {
            var either = elements.get(i);
            if (either.left().isPresent()) {
                // We look for the "Press Shift" text you added in appendHoverText
                // Or look for the start of modifiers
                String text = either.left().get().getString();

                // This is the most reliable way: insert right before the modifiers start
                // Note: Modifier headers often use translatable keys, so we check the raw content
                if (text.contains("item.modifiers") || text.contains("When in") || text.isEmpty() && i > elements.size() - 3) {
                    insertionIndex = i;
                    break;
                }
            }
        }

        // 3. Create and Inject the Component
        // If we didn't find a modifier header, insertionIndex remains -1, we'll just put it at the end
        if (insertionIndex == -1) {
            insertionIndex = elements.size();
        }

        var data = new MagicItemAugmentComponent.AugmentComponent(stack, AugmentHelper.getAugmentsFromItem(stack), ((BrutalityMagicItem) stack.getItem()).baseAugmentSlots);
        elements.add(insertionIndex, Either.right(data));
        elements.add(insertionIndex + 1, Either.left(Component.empty()));
    }

//    @Override
//    public Optional<TooltipComponent> getTooltipImage(ItemStack pStack) {
//        List<BrutalityAugmentItem> augments = AugmentHelper.getAugmentsFromItem(pStack);
//        return Optional.of(new MagicItemAugmentComponent.AugmentComponent(pStack, augments, this.baseAugmentSlots));
//    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        Minecraft mc = Minecraft.getInstance();
        boolean showExtendedView = InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keyShift.getKey().getValue());
        boolean showAllSpells = InputConstants.isKeyDown(mc.getWindow().getWindow(), mc.options.keySprint.getKey().getValue());

        List<SpellStorage.SpellEntry> spellEntries = SpellStorage.getSpells(stack);
        if (showAllSpells) {
            for (SpellStorage.SpellEntry spellEntry : spellEntries) {
                SpellTooltipRenderer.renderSpellEntry(spellEntry, tooltip, showExtendedView);
            }
        } else {
            SpellStorage.SpellEntry spellEntry = SpellStorage.getCurrentSpellEntry(stack);
            if (spellEntry != null) {
                SpellTooltipRenderer.renderSpellEntry(spellEntry, tooltip, showExtendedView);
            }
        }

        tooltip.add(Component.translatable("message." + Brutality.MOD_ID + ".press_for_more_info",
                Component.keybind(Minecraft.getInstance().options.keyShift.getName())));

        tooltip.add(Component.translatable("message." + Brutality.MOD_ID + ".press_to_show_all_spells",
                Component.keybind(Minecraft.getInstance().options.keySprint.getName()))

                .append(" [" + spellEntries.size() + "/" + SpellStorage.getSpellSlotCount(stack) + "]"));

    }

}
