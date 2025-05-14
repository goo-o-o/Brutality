package net.goo.armament.item.weapon.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.renderers.item.ArmaAutoGlowingItemRenderer;
import net.goo.armament.entity.base.ArmaArrow;
import net.goo.armament.entity.custom.arrow.LightArrow;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.weapon.base.ArmaBowItem;
import net.goo.armament.registry.ModParticles;
import net.goo.armament.registry.ModSounds;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class ProvidenceBow extends ArmaBowItem {
    private static final float fullDrawTicks = 30F;

    public ProvidenceBow(Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pProperties, identifier, category, rarity, abilityCount);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @SubscribeEvent
    public static void onFOVModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();

        if (player.isUsingItem()) {
            ItemStack itemStack = player.getUseItem();
            if (itemStack.getItem() instanceof ProvidenceBow) {
                float pullProgress = getPowerForTime(player.getTicksUsingItem(), fullDrawTicks);
                float FOVModifier = 1.0F - pullProgress * 0.5F;
                event.setNewFovModifier(event.getFovModifier() * FOVModifier);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, pStack) > 0;
            ItemStack itemstack = player.getProjectile(pStack);

            int i = this.getUseDuration(pStack) - pTimeLeft;
            i = ForgeEventFactory.onArrowLoose(pStack, pLevel, player, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getPowerForTime(i, fullDrawTicks);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, pStack, player));
                    if (!pLevel.isClientSide) {
                        ArmaArrow abstractarrow = new LightArrow(player.getX(), player.getEyeY(), player.getZ(), pLevel);
                        abstractarrow.setOwner(player);
                        abstractarrow.setBaseDamage(1);
                        abstractarrow.setPierceLevel(((byte) 10));
                        abstractarrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * 3.0F, 0F);
                        if (f == 1.0F) {
                            abstractarrow.setCritArrow(true);
                            ((ServerLevel) pLevel).sendParticles(ModParticles.HEAL_WAVE_PARTICLE.get(), player.getX(), player.getY() + player.getBbHeight() / 3, player.getZ(), 1, 0, 0, 0, 0);
                            List<LivingEntity> nearbyEntities = pLevel.getNearbyEntities(
                                    LivingEntity.class,
                                    TargetingConditions.DEFAULT,
                                    null,
                                    player.getBoundingBox().inflate(3)
                            );

                            for (LivingEntity entity : nearbyEntities) {
                                entity.addEffect(new MobEffectInstance(MobEffects.HEAL, 1, 0), entity);
                            }
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, pStack);
                        if (j > 0) {
                            abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, pStack);
                        if (k > 0) {
                            abstractarrow.setKnockback(k);
                        }

                        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack) > 0) {
                            abstractarrow.setSecondsOnFire(100);
                        }

                        pStack.hurtAndBreak(1, player, (p_289501_) -> {
                            p_289501_.broadcastBreakEvent(player.getUsedItemHand());
                        });
                        if (flag1 || player.getAbilities().instabuild && (itemstack.is(Items.SPECTRAL_ARROW) || itemstack.is(Items.TIPPED_ARROW))) {
                            abstractarrow.pickup = ArmaArrow.Pickup.CREATIVE_ONLY;
                        }

                        pLevel.addFreshEntity(abstractarrow);
                    }

                    pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), ModSounds.WINGS_FLAP.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !player.getAbilities().instabuild) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            player.getInventory().removeItem(itemstack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }


        if (pLevel instanceof ServerLevel serverLevel)
            triggerAnim(pEntityLiving, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "release");
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> state.setAndContinue(RawAnimation.begin().thenPlay("idle")))
                .triggerableAnim("draw", RawAnimation.begin().thenPlay("draw"))
                .triggerableAnim("release", RawAnimation.begin().thenPlay("release").thenPlay("idle"))
                .triggerableAnim("idle", RawAnimation.begin().thenPlay("idle"))
        );
    }


    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if (player.level() instanceof ServerLevel serverLevel)
            triggerAnim(player, GeoItem.getOrAssignId(item, serverLevel), "controller", "idle");
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pIsSelected && pLevel instanceof ServerLevel serverLevel && pEntity instanceof LivingEntity livingEntity) {
            triggerAnim(livingEntity, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "idle");
        }
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        boolean flag = !pPlayer.getProjectile(itemstack).isEmpty();

        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, flag);
        if (ret != null) return ret;

        if (!pPlayer.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            pPlayer.startUsingItem(pHand);
            if (pLevel instanceof ServerLevel serverLevel)
                triggerAnim(pPlayer, GeoItem.getOrAssignId(itemstack, serverLevel), "controller", "draw");
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaAutoGlowingItemRenderer.class);
    }

}
