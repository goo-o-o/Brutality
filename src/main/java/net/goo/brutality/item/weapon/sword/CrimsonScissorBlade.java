package net.goo.brutality.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.entity.projectile.generic.Bloodslash;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.registry.ModAttributes;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.UUID;

public class CrimsonScissorBlade extends BrutalitySwordItem {

    public CrimsonScissorBlade(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    UUID CSB_OMNIVAMP_UUID = UUID.fromString("12fbfbd2-98df-4b10-ad86-52b74a69f74a");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> original = super.getAttributeModifiers(slot, stack);

        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> newAttributes = ImmutableMultimap.builder();
            newAttributes.putAll(original);
            newAttributes.put(ModAttributes.OMNIVAMP.get(),
                    new AttributeModifier(CSB_OMNIVAMP_UUID, "Omnivamp buff", 0.15, AttributeModifier.Operation.MULTIPLY_BASE));

            return newAttributes.build();
        }

        return super.getAttributeModifiers(slot, stack);
    }

    public void performBloodSlash(Player player) {
        Level level = player.level();
        Bloodslash bloodslash = new Bloodslash(BrutalityModEntities.BLOODSLASH.get(), level);
        Vec3 loc = player.getEyePosition();
        bloodslash.shootFromRotation(player, player.getXRot(), player.getYRot(), 0, 1.5F, 0);
        bloodslash.setPos(loc);
        bloodslash.setOwner(player);
        level.addFreshEntity(bloodslash);
        player.level().playSound(null, player.blockPosition(), BrutalityModSounds.BLOOD_MAGIC_MISSILE.get(), SoundSource.PLAYERS, 1, Mth.nextFloat(player.getRandom(), 0.8F, 1.2F));
        float health = Mth.clamp(player.getHealth() - 4, 1, player.getMaxHealth());
        player.setHealth(health);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!ModList.get().isLoaded("bettercombat") && entity instanceof Player player) {
            if (player.getAttackStrengthScale(0) >= 1)
                performBloodSlash(player);
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        for (int i = 0; i < 10; i++) {
            pTarget.level().addParticle(BrutalityModParticles.BLOOD_PARTICLE.get(), pTarget.getX(), pTarget.getY(0.5), pTarget.getZ(), 0, 0, 0);
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
