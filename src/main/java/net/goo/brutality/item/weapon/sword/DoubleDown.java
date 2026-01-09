package net.goo.brutality.item.weapon.sword;

import com.google.common.collect.Multimap;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DoubleDown extends BrutalitySwordItem {


    public DoubleDown(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }


    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume(itemstack);
        }
    }


    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
        if (pLivingEntity instanceof Player player && !pLevel.isClientSide()) {
            ItemStack randomSword = getRandomSword(player);
            randomSword.getOrCreateTag().putBoolean("fromDoubleDown", true);
            player.getInventory().setItem(player.getInventory().selected, randomSword);
        }

    }



    private Map<Item, Double> calculateSwordRarity(Player player) {
        int luckAmplifier = player.hasEffect(MobEffects.LUCK)
                ? Objects.requireNonNull(player.getEffect(MobEffects.LUCK)).getAmplifier() + 1 : 0;

        double luckFactor = 0.5 + (luckAmplifier * 2);

        List<SwordData> swordData = getSortedSwordData();
        if (swordData.isEmpty()) {
            return Map.of(BrutalityModItems.DOUBLE_DOWN.get(), 100.0);
        }

        double[] baseWeights = swordData.stream()
                .mapToDouble(data -> 1.0 / data.eDps)
                .toArray();

        double[] adjustedWeights = new double[baseWeights.length];
        double weightSum = 0;

        for (int i = 0; i < baseWeights.length; i++) {
            double positionFactor = 1.0 - (i / (double) baseWeights.length);
            adjustedWeights[i] = baseWeights[i] * Math.pow(luckFactor, 3 * positionFactor);
            weightSum += adjustedWeights[i];
        }

        Map<Item, Double> rarityMap = new LinkedHashMap<>();
        for (int i = 0; i < swordData.size(); i++) {
            double percentage = (adjustedWeights[i] / weightSum) * 100;
            rarityMap.put(swordData.get(i).item, percentage);
        }

//        normalizePercentages(rarityMap);
//        System.out.println(rarityMap);
        return rarityMap;
    }


    private List<SwordData> getSortedSwordData() {
        return ForgeRegistries.ITEMS.getValues().stream()
                .filter(sword -> sword instanceof SwordItem || sword.getDefaultInstance().is(ItemTags.SWORDS))
                .map(this::getSwordData)
                .filter(data -> data.eDps > 0)
                .sorted(Comparator.comparingDouble(SwordData::eDps).reversed())
                .toList();
    }

    private SwordData getSwordData(Item sword) {

        Multimap<Attribute, AttributeModifier> attributes = sword.getAttributeModifiers(EquipmentSlot.MAINHAND, sword.getDefaultInstance());
        double damage = 0;
        double speed = 0;

        Collection<AttributeModifier> damageModifiers = attributes.get(Attributes.ATTACK_DAMAGE);
        Collection<AttributeModifier> speedModifiers = attributes.get(Attributes.ATTACK_SPEED);
        if (!speedModifiers.isEmpty()) {
            damage = damageModifiers.iterator().next().getAmount(); // Convert to positive
        }

        if (!speedModifiers.isEmpty()) {
            speed = -speedModifiers.iterator().next().getAmount(); // Convert to positive
        }

        double eDps = damage * speed; // Effective DPS

        return new SwordData(sword, eDps);
    }

    private record SwordData(Item item, double eDps) {
    }

    public ItemStack getRandomSword(Player player) {
        Map<Item, Double> rarityMap = calculateSwordRarity(player);
        double roll = player.getRandom().nextDouble() * 100;

        double cumulative = 0;
        for (Map.Entry<Item, Double> entry : rarityMap.entrySet()) {
            cumulative += entry.getValue();
            if (roll <= cumulative) {
                return entry.getKey().getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
    }

}
