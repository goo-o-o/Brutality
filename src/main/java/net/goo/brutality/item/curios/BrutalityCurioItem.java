package net.goo.brutality.item.curios;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.util.helpers.AttributeContainer;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;

public class BrutalityCurioItem extends BrutalityGenericItem implements ICurioItem {
    private List<AttributeContainer> attributeTemplates = List.of();

    public BrutalityCurioItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    public BrutalityCurioItem(Rarity rarity) {
        super(rarity);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (this.attributeTemplates.isEmpty()) {
            return ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack);
        }

        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for (int i = 0; i < this.attributeTemplates.size(); i++) {
            AttributeContainer holder = this.attributeTemplates.get(i);

            UUID attributeUUID = new UUID(
                    uuid.getMostSignificantBits() ^ holder.attribute().getDescriptionId().hashCode(),
                    uuid.getLeastSignificantBits() ^ i
            );
            AttributeModifier modifier = holder.createModifier(attributeUUID);

            builder.put(holder.attribute(), modifier);
        }

        return builder.build();
    }

    public BrutalityCurioItem withAttributes(AttributeContainer... attributes) {
        this.attributeTemplates = List.of(attributes);
        return this;
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.CURIO;
    }

    public boolean followBodyRotations() {
        return true;
    }
    public boolean followHeadRotations() {
        return true;
    }

    /**
     * Only works if the bone is within the chest slot, this is intentional
     */
    public boolean rotateIfSneaking() {
        return true;
    }
    public boolean translateIfSneaking() {
        return true;
    }
}
