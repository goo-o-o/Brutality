package net.goo.brutality.item.armor;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalityArmorItem;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.UUID;

public class TerraArmorItem extends BrutalityArmorItem {


    public TerraArmorItem(ArmorMaterial pMaterial, Type pType, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pMaterial, pType, rarity, descriptionComponents);
    }


    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot pEquipmentSlot) {
        if (pEquipmentSlot == type.getSlot()) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            UUID modifierUUID = UUID.nameUUIDFromBytes((this.getType().getName() + this.getMaterial() + pEquipmentSlot).getBytes());
            builder.putAll(super.getDefaultAttributeModifiers(pEquipmentSlot));
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(modifierUUID, "KB Resist buff", 0.25F, AttributeModifier.Operation.MULTIPLY_TOTAL));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
