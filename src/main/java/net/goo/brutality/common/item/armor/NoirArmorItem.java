package net.goo.brutality.common.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.base.BrutalityArmorItem;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class NoirArmorItem extends BrutalityArmorItem {

    public NoirArmorItem(ArmorMaterial pMaterial, Type pType, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, rarity, descriptionComponents);
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        if (pEquipmentSlot == type.getSlot()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            UUID modifierUUID = UUID.nameUUIDFromBytes((this.getType().getName() + this.getMaterial() + pEquipmentSlot).getBytes());
            builder.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
            builder.put(BrutalityAttributes.STEALTH.get(), new AttributeModifier(modifierUUID, "Stealth buff", 0.25F, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(pEquipmentSlot);
    }


}
