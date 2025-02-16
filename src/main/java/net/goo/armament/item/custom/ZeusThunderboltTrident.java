package net.goo.armament.item.custom;

import com.google.common.collect.ImmutableMultimap;
import net.goo.armament.Armament;
import net.goo.armament.client.item.ArmaGeoItem;
import net.goo.armament.entity.projectile.ThunderboltProjectile;
import net.goo.armament.item.ArmaTridentItem;
import net.goo.armament.item.ModItemCategories;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.GrindstoneEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ZeusThunderboltTrident extends ArmaTridentItem implements Vanishable, ArmaGeoItem {
    private static final UUID SPEED_BOOST_UUID = UUID.fromString("f9d0a647-4999-4637-b4a0-7f768a65b5db");  // Unique UUID for speed boost modifier

    public ZeusThunderboltTrident(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pProperties, identifier, category, rarity, abilityCount);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(SPEED_BOOST_UUID, "Tool modifier", 2, AttributeModifier.Operation.ADDITION));
        this.colors = new int[][]{{255, 215, 86}, {164, 92, 0}};
    }

    public ModItemCategories getCategory() {
        return category;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        EnchantmentHelper.setEnchantments(Map.of(Enchantments.LOYALTY, 5, Enchantments.INFINITY_ARROWS, 1), stack);
        return stack;
    }

    @SubscribeEvent
    public static void onGrindstoneUse(GrindstoneEvent.OnPlaceItem event) {
        ItemStack bottomItem = event.getBottomItem();
        ItemStack topItem = event.getTopItem();
        ItemStack targetStack = bottomItem.isEmpty() ? topItem : bottomItem;

        if (targetStack.getItem() == net.goo.armament.registry.ModItems.ZEUS_THUNDERBOLT_TRIDENT.get()) {
            ItemStack resultStack = targetStack.copy();

            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(resultStack);
            enchantments = enchantments.entrySet().stream().filter(entry ->
                            entry.getKey().isCurse() || entry.getKey() == Enchantments.LOYALTY || entry.getKey() == Enchantments.INFINITY_ARROWS)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            enchantments.put(Enchantments.LOYALTY, 5);
            enchantments.put(Enchantments.INFINITY_ARROWS, 1);
            EnchantmentHelper.setEnchantments(enchantments, resultStack);
            event.setXp(0);
            event.setOutput(resultStack);
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (pPlayer.isCrouching()) {
            pPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 2), pPlayer);
            pPlayer.getCooldowns().addCooldown(itemstack.getItem(), 400);
            return InteractionResultHolder.fail(itemstack);
        } else if (!isUnbreakable()) {
            if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
                return InteractionResultHolder.fail(itemstack);
            } else if (EnchantmentHelper.getRiptide(itemstack) > 0 && !pPlayer.isInWaterOrRain()) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                pPlayer.startUsingItem(pHand);
                return InteractionResultHolder.consume(itemstack);
            }
        } else {
            if (EnchantmentHelper.getRiptide(itemstack) > 0 && !pPlayer.isInWaterOrRain()) {
                return InteractionResultHolder.fail(itemstack);
            } else {
                pPlayer.startUsingItem(pHand);
                return InteractionResultHolder.consume(itemstack);
            }
        }
    }

    @Override
    public boolean isInfinite() {
        return true;
    }

    @Override
    public boolean isUnbreakable() {
        return true;
    }

    @Override
    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
        int j = EnchantmentHelper.getRiptide(pStack);

        pStack.hurtAndBreak(1, player, (consumer) -> {
            consumer.broadcastBreakEvent(player.getUsedItemHand());
        });
        if (j == 0) {
            ThunderboltProjectile thrownEntity = new ThunderboltProjectile(pLevel, player, pStack);
            thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel() + (float) j * 0.5F, 1.0F);
            if (player.getAbilities().instabuild) {
                thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            pLevel.addFreshEntity(thrownEntity);
            pLevel.playSound(null, thrownEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player.getAbilities().instabuild && !isInfinite()) {
                player.getInventory().removeItem(pStack);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle")))
        );
    }
}
