package net.goo.brutality.common.item.weapon.sword.max;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.client.particle.base.trail.IEffect;
import net.goo.brutality.client.particle.base.trail.IFXObject;
import net.goo.brutality.client.particle.base.trail.TrailEmitter;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.joml.Quaternionf;

import java.util.List;

import static net.goo.brutality.common.registry.BrutalityAttributes.BASE_ENTITY_RANGE_UUID;

public class Maxim extends BrutalitySwordItem {
    protected int rangeBonus;

    public Maxim(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
        this.rangeBonus = 0;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 7200;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide()) {
            spawnTestTrail((ClientLevel) pLevel, pPlayer);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ENTITY_RANGE_UUID, "Range Buff", this.rangeBonus, AttributeModifier.Operation.ADDITION));
        builder.putAll(super.getAttributeModifiers(slot, stack));
        return builder.build();
    }

    public void spawnTestTrail(ClientLevel level, Player player) {
        // 1. Create the Emitter (The Shell)
        TrailEmitter emitter = new TrailEmitter(level, player.getX(), player.getY(), player.getZ());

        // 2. Create the Effect (The Brain)
        // You can use your FXProjectEffect or a simple anonymous class for testing
        IEffect myEffect = new IEffect() {
            @Override
            public Level getLevel() {
                return level;
            }

            @Override
            public void updateFXObjectTick(IFXObject fxObject) {
                // Make the trail follow the player
                fxObject.transform().position(player.position().toVector3f());

                // Optional: Auto-kill after 100 ticks
                if (player.tickCount % 100 == 0) fxObject.remove(true);
            }
        };

        // 3. Ignite!
        // This adds it to the Minecraft ParticleEngine automatically
        emitter.emit(myEffect, player.position().toVector3f(), new Quaternionf());
    }
}
