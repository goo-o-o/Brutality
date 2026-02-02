package net.goo.brutality.common.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.common.entity.projectile.generic.AbyssProjectile;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.registry.BrutalityEntities;
import net.goo.brutality.util.ProjectileHelper;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.UUID;

public class ChallengerDeepSword extends BrutalitySwordItem {
    public ChallengerDeepSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }


    UUID CHALLENGER_DEEP_RANGE__UUID = UUID.fromString("869fb9a7-237b-4b26-9f03-c04dcecea752");

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);


        if (slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(modifiers);

            builder.put(
                    ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(
                            CHALLENGER_DEEP_RANGE__UUID,
                            "Reach bonus",
                            4,
                            AttributeModifier.Operation.ADDITION
                    )
            );

            return builder.build();
        }

        return modifiers;
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!ModList.get().isLoaded("bettercombat"))
            if (entity instanceof Player player) {
                performChallengerDeepAttack(stack, player);
            }
        return super.onEntitySwing(stack, entity);
    }

    public void performChallengerDeepAttack(ItemStack stack, Player player) {
        Level level = player.level();
        if (player.getCooldowns().isOnCooldown(stack.getItem())) return;
//        if (level.isClientSide()) {
//            for (int i = 1; i <= 360; i += 30)
//                PacketHandler.sendToServer(new ServerboundShootProjectilePacket(BrutalityModEntities.ABYSS_PROJECTILE.getId(), 0.5F, false, 0F, i));
//        } else {
        for (int i = 1; i <= 360; i += 30) {
            ProjectileHelper.shootProjectile(() ->
                    new AbyssProjectile(BrutalityEntities.ABYSS_PROJECTILE.get(), level), player, level, 0.5F, false, 0F, i);
        }
//        }
        player.getCooldowns().addCooldown(stack.getItem(), 10);
    }

}
