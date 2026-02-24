package net.goo.brutality.common.item.weapon.tome;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.registry.BrutalityAttributes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.UUID;

public class CelestiaTome extends BaseMagicTome {


    UUID CELESTIA_SCHOOL_BOOST_UUID = UUID.fromString("e0ff2fb1-3857-4508-be0f-ed20beadcef8");

    public CelestiaTome(Rarity rarity, int baseSpellSlots, int baseAugmentSlots) {
        super(rarity, baseSpellSlots, baseAugmentSlots);
    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);
            builder.put(
                    BrutalityAttributes.CELESTIA_SCHOOL_LEVEL.get(),
                    new AttributeModifier(
                            CELESTIA_SCHOOL_BOOST_UUID,
                            "Celestia School bonus",
                            1,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }
        return modifiers;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        super.registerControllers(controllers);

        controllers.add(new AnimationController<>(this, null, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }
}