package net.goo.brutality.item.base;

import net.goo.brutality.entity.base.BrutalityArrow;
import net.goo.brutality.entity.projectile.arrow.LightArrow;
import net.goo.brutality.event.mod.client.BrutalityModItemRenderManager;
import net.goo.brutality.item.BrutalityCategories;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;

import java.util.List;
import java.util.function.Consumer;

public class BrutalityBowItem extends BowItem implements BrutalityGeoItem {
    public String identifier;
    public Rarity rarity;
    private final List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents;

    public BrutalityBowItem(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(new Item.Properties().stacksTo(1));
        this.rarity = rarity;
        this.descriptionComponents = descriptionComponents;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return this.rarity;
    }

    @Override
    public @NotNull Component getName(ItemStack pStack) {
        return brutalityNameHandler(pStack);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityHoverTextHandler(pTooltipComponents, descriptionComponents, rarity);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.BOW;
    }

    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    protected boolean requiresArrows() {
        return true;
    }

    protected float getArrowBaseDamage() {
        return 1;
    }

    protected int getFullDrawTicks() {
        return 20;
    }


    protected float getPowerMultiplier() {
        return 3;
    }


    public static float getPowerForTime(int pCharge, float fullDrawTicks) {
        float f = (float) pCharge / fullDrawTicks;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BrutalityModItemRenderManager.createRenderer(BrutalityBowItem.this);
            }
        });
    }


    @Override
    public void releaseUsing(ItemStack bowStack, Level world, LivingEntity shooter, int timeLeft) {
        if (shooter instanceof Player player) {
            boolean infiniteArrows = player.getAbilities().instabuild
                    || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowStack) > 0
                    || !requiresArrows();

            ItemStack arrowStack = player.getProjectile(bowStack);

            int chargeTime = this.getUseDuration(bowStack) - timeLeft;
            chargeTime = ForgeEventFactory.onArrowLoose(bowStack, world, player, chargeTime, !arrowStack.isEmpty() || infiniteArrows);
            if (chargeTime < 0) return;

            if (!arrowStack.isEmpty() || infiniteArrows) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float drawPower = getPowerForTime(chargeTime, getFullDrawTicks());
                if (drawPower >= 0.1F) {
                    boolean isInfiniteArrow = player.getAbilities().instabuild
                            || (arrowStack.getItem() instanceof ArrowItem && ((ArrowItem) arrowStack.getItem()).isInfinite(arrowStack, bowStack, player));

                    if (!world.isClientSide) {
                        AbstractArrow arrowEntity = new LightArrow(BrutalityModEntities.LIGHT_ARROW.get(), player, world);
                        arrowEntity.setOwner(player);
                        arrowEntity.setBaseDamage(getArrowBaseDamage());
                        arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, drawPower * getPowerMultiplier(), 0F);

                        if (drawPower == 1.0F) {
                            arrowEntity.setCritArrow(true);
                            List<LivingEntity> nearbyEntities = world.getNearbyEntities(
                                    LivingEntity.class,
                                    TargetingConditions.DEFAULT,
                                    null,
                                    player.getBoundingBox().inflate(3)
                            );

                            for (LivingEntity entity : nearbyEntities) {
                                entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0), entity);
                            }
                        }

                        int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bowStack);
                        if (powerLevel > 0) {
                            arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (powerLevel * 0.5D + 0.5D));
                        }

                        int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bowStack);
                        if (punchLevel > 0) {
                            arrowEntity.setKnockback(punchLevel);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bowStack) > 0) {
                            arrowEntity.setSecondsOnFire(100);
                        }

                        bowStack.hurtAndBreak(1, player, (playerEntity) -> {
                            playerEntity.broadcastBreakEvent(player.getUsedItemHand());
                        });

                        if (isInfiniteArrow || (player.getAbilities().instabuild
                                && (arrowStack.is(Items.SPECTRAL_ARROW) || arrowStack.is(Items.TIPPED_ARROW)))) {
                            arrowEntity.pickup = BrutalityArrow.Pickup.CREATIVE_ONLY;
                        }

                        world.addFreshEntity(arrowEntity);
                    }

                    world.playSound(null, player.getX(), player.getY(), player.getZ(),
                            BrutalityModSounds.WINGS_FLAP.get(), SoundSource.PLAYERS, 1.0F,
                            1.0F / (world.getRandom().nextFloat() * 0.4F + 1.2F) + drawPower * 0.5F);

                    if (!isInfiniteArrow && !player.getAbilities().instabuild) {
                        arrowStack.shrink(1);
                        if (arrowStack.isEmpty()) {
                            player.getInventory().removeItem(arrowStack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }


}
