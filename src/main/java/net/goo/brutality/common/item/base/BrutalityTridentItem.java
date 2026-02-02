package net.goo.brutality.common.item.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.goo.brutality.event.mod.client.BrutalityModItemRenderManager;
import net.goo.brutality.common.item.BrutalityCategories;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

import static net.goo.brutality.util.EnchantmentHelper.hasInfinity;

public class BrutalityTridentItem extends TridentItem implements BrutalityGeoItem {
    protected String identifier;
    protected Rarity rarity;
    protected List<ItemDescriptionComponent> descriptionComponents;
    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public BrutalityTridentItem(float attackDamageModifier, float attackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(new Item.Properties().defaultDurability(1561));

        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributes = ImmutableMultimap.builder();
        attributes.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", attackDamageModifier, AttributeModifier.Operation.ADDITION));
        attributes.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", attackSpeedModifier, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = attributes.build();

        this.rarity = rarity;
        this.descriptionComponents = descriptionComponents;
    }


    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.getDefaultAttributeModifiers(pEquipmentSlot);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return BrutalityModItemRenderManager.createRenderer(BrutalityTridentItem.this);
            }
        });
    }

    @Override
    public BrutalityCategories.AttackType getAttackType() {
        return BrutalityCategories.AttackType.PIERCE;
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
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        brutalityTooltipHandler(pStack, pTooltipComponents, descriptionComponents, rarity);
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


    @Override
    public BrutalityCategories category() {
        return BrutalityCategories.ItemType.TRIDENT;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    public boolean isRiptideEnabled() {
        return true;
    }


    public float getLaunchVel() {
        return 2.5F;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            if (i >= 10) {
                int j = EnchantmentHelper.getRiptide(pStack);
                if (j <= 0 || player.isInWaterOrRain()) {
                    if (!pLevel.isClientSide) {
                        pStack.hurtAndBreak(1, player, (consumer) -> {
                            consumer.broadcastBreakEvent(player.getUsedItemHand());
                        });

                        launchProjectile(pLevel, player, pStack);
                        if (!player.getAbilities().instabuild && !(hasInfinity(pStack) || isInnatelyInfinite())) {
                            if (pStack.getCount() > 1) {
                                pStack.shrink(1);
                            } else {
                                player.getInventory().removeItem(pStack);
                            }
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (j > 0) {
                        float f7 = player.getYRot();
                        float f = player.getXRot();
                        float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                        float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
                        float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                        float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                        float f5 = 3.0F * ((1.0F + (float) j) / 4.0F);
                        f1 *= f5 / f4;
                        f2 *= f5 / f4;
                        f3 *= f5 / f4;
                        player.push((double) f1, (double) f2, (double) f3);
                        player.startAutoSpinAttack(20);
                        if (player.onGround()) {
                            float f6 = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3(0.0D, f6, 0.0D));
                        }

                        SoundEvent soundevent;
                        if (j >= 3) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (j == 2) {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            soundevent = SoundEvents.TRIDENT_RIPTIDE_1;
                        }

                        pLevel.playSound(null, player, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                }
            }
        }
    }

    protected boolean isInnatelyInfinite() {
        return false;
    }

    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
        int j = EnchantmentHelper.getRiptide(pStack);

        if (j == 0) {
            ThrownTrident thrownEntity = new ThrownTrident(pLevel, player, pStack);
            thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, getLaunchVel() + (float) j * 0.5F, 1.0F);
            if (player.getAbilities().instabuild) {
                thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            pLevel.addFreshEntity(thrownEntity);
            pLevel.playSound(null, thrownEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);

        }
    }


    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
