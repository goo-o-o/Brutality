package net.goo.brutality.common.item.base;

import net.goo.brutality.common.entity.base.BrutalityArrow;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.event.mod.client.BrutalityModItemRenderManager;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

public class BrutalityBowItem extends BowItem implements BrutalityGeoItem {
    public String identifier;
    private final List<ItemDescriptionComponent> descriptionComponents;

    public BrutalityBowItem(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(new Item.Properties().stacksTo(1).rarity(rarity));
        this.descriptionComponents = descriptionComponents;
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.NONE;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityTooltipHandler(pStack, pTooltipComponents, descriptionComponents, getRarity(pStack));
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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

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

    protected float getPowerMultiplier() {
        return 3;
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

    protected EntityType<? extends AbstractArrow> getArrowEntity() {
        return EntityType.ARROW;
    }

    protected void onFullDraw(Player player, Level level) {
    }

    protected SoundEvent getShootSound() {
        return SoundEvents.ARROW_SHOOT;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack bowStack, @NotNull Level level, @NotNull LivingEntity shooter, int timeLeft) {
        if (shooter instanceof Player player) {

            ItemStack arrowStack = player.getProjectile(bowStack);
            boolean infiniteArrows = player.getAbilities().instabuild
                    || bowStack.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0
                    || (arrowStack.getItem() instanceof ArrowItem arrowItem && (arrowItem).isInfinite(arrowStack, bowStack, player))
                    || !requiresArrows();

            int chargeTime = this.getUseDuration(bowStack) - timeLeft;
            chargeTime = ForgeEventFactory.onArrowLoose(bowStack, level, player, chargeTime, !arrowStack.isEmpty() || infiniteArrows);
            if (chargeTime < 0) return;

            if (!arrowStack.isEmpty() || infiniteArrows) {
                if (arrowStack.isEmpty()) {
                    arrowStack = new ItemStack(Items.ARROW);
                }

                float drawPower = getPowerForTime(chargeTime);
                if (drawPower >= 0.1F) {


                    if (!level.isClientSide) {
                        AbstractArrow arrowEntity = getArrowEntity().create(level);
                        if (arrowEntity != null) {
                            arrowEntity.setOwner(player);
                            arrowEntity.setBaseDamage(getArrowBaseDamage());
                            arrowEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, drawPower * getPowerMultiplier(), 0F);

                            if (drawPower == 1.0F) {
                                arrowEntity.setCritArrow(true);
                                onFullDraw(player, level);
                            }

                            int powerLevel = bowStack.getEnchantmentLevel(Enchantments.POWER_ARROWS);
                            if (powerLevel > 0) {
                                arrowEntity.setBaseDamage(arrowEntity.getBaseDamage() + (powerLevel * 0.5D + 0.5D));
                            }

                            int punchLevel = bowStack.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
                            if (punchLevel > 0) {
                                arrowEntity.setKnockback(punchLevel);
                            }

                            if (bowStack.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
                                arrowEntity.setSecondsOnFire(100);
                            }

                            bowStack.hurtAndBreak(1, player, (playerEntity) -> {
                                playerEntity.broadcastBreakEvent(player.getUsedItemHand());
                            });

                            if (infiniteArrows || (player.getAbilities().instabuild
                                    && (arrowStack.is(Items.SPECTRAL_ARROW) || arrowStack.is(Items.TIPPED_ARROW)))) {
                                arrowEntity.pickup = BrutalityArrow.Pickup.CREATIVE_ONLY;
                            }

                            level.addFreshEntity(arrowEntity);
                        }
                    }

                    level.playSound(null, player.getX(), player.getY(), player.getZ(),
                            getShootSound(), SoundSource.PLAYERS, 1.0F,
                            1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + drawPower * 0.5F);

                    if (!infiniteArrows  && !player.getAbilities().instabuild) {
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
