package net.goo.brutality.item.curios.charm;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.goo.brutality.item.curios.base.BaseCharmCurio;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;

public class Envy extends BaseCharmCurio {

    public Envy(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }


    private static float getNewBonus(LivingEntity wearer) {
        Player nearestValidPlayer =
                wearer.level().getNearestPlayer(wearer.getX(), wearer.getY(), wearer.getZ(), 10, e ->
                        e instanceof Player p && !p.isCreative() && !p.isSpectator() && p != wearer &&
                                !CuriosApi.getCuriosInventory(p).map(handler ->
                                        handler.isEquipped(BrutalityModItems.ENVY.get())).orElse(false));
        if (nearestValidPlayer == null) return 0;

        AttributeInstance maxHealthAttr = wearer.getAttribute(Attributes.MAX_HEALTH);
        float playerHealthNoEnvy = (float) wearer.getAttributeBaseValue(Attributes.MAX_HEALTH);
        if (maxHealthAttr != null)
            for (AttributeModifier modifier : maxHealthAttr.getModifiers()) {
                if (!modifier.getId().equals(ENVY_HP_UUID)) {
                    playerHealthNoEnvy += (float) modifier.getAmount();
                }
            }

        return Math.max((nearestValidPlayer.getMaxHealth() - playerHealthNoEnvy) / 2, 0);
    }

    private static final UUID ENVY_HP_UUID = UUID.fromString("0a98b1be-25b9-4c38-8e4e-762979e8a1a3");

    private static final Object2FloatOpenHashMap<UUID> OLD_BONUS_MAP = new Object2FloatOpenHashMap<>();

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (slotContext.entity() != null) {
            LivingEntity livingEntity = slotContext.entity();
            if (!livingEntity.level().isClientSide() && livingEntity.tickCount % 10 == 0) {
                AttributeInstance maxHealth = livingEntity.getAttribute(Attributes.MAX_HEALTH);
                float newBonus = getNewBonus(livingEntity);

                UUID uuid = livingEntity.getUUID();
                float oldBonus = OLD_BONUS_MAP.getOrDefault(uuid, 0);
                if (maxHealth != null && oldBonus != newBonus) {
                    OLD_BONUS_MAP.put(uuid, newBonus);
                    maxHealth.removeModifier(ENVY_HP_UUID);
                    maxHealth.addTransientModifier(
                            new AttributeModifier(
                                    ENVY_HP_UUID,
                                    "HP Bonus",
                                    newBonus,
                                    AttributeModifier.Operation.ADDITION
                            )
                    );
                }
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        if (slotContext.entity() != null) OLD_BONUS_MAP.removeFloat(slotContext.entity().getUUID());
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (slotContext.entity() != null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
            builder.put(Attributes.MAX_HEALTH, new AttributeModifier(ENVY_HP_UUID, "HP Buff", getNewBonus(slotContext.entity()), AttributeModifier.Operation.ADDITION));

            return builder.build();
        }
        return super.getAttributeModifiers(slotContext, uuid, stack);
    }
}
