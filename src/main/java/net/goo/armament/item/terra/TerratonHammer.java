package net.goo.armament.item.terra;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.weapon.base.ArmaHammerItem;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class TerratonHammer extends ArmaHammerItem implements GeoItem {
    private static final UUID MOVEMENT_SPEED_MODIFIER_UUID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public TerratonHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.attributeModifiers = ImmutableMultimap.<Attribute, AttributeModifier>builder()
                .put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 20, AttributeModifier.Operation.ADDITION))
                .put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -2, AttributeModifier.Operation.ADDITION))
                .put(Attributes.MOVEMENT_SPEED, new AttributeModifier(MOVEMENT_SPEED_MODIFIER_UUID, "Tool modifier", -0.35, AttributeModifier.Operation.MULTIPLY_BASE))
                .build();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }


    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot equipmentSlot, ItemStack stack) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(equipmentSlot, stack);
    }

    @SubscribeEvent
    public static void onLivingKnockback(LivingKnockBackEvent event) {
        if (event.getEntity().getLastAttacker() instanceof Player player && player.getMainHandItem().getItem() instanceof TerratonHammer) {
            float kbMult = ((float) Math.pow(player.getAttackStrengthScale(0.5F), 3));

            event.setStrength((event.getOriginalStrength() * 10) * kbMult);

        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pIsSelected && pEntity instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 4, 8, false, false));
        }
    }
}
