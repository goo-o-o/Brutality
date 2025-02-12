package net.goo.armament.item;

import net.goo.armament.util.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

public class ArmaTridentItem extends TridentItem implements ArmaGeoItem {
    public boolean isRiptideEnabled;
    public String identifier;
    public ModItemCategories category;
    protected int[][] colors;
    public float launchVel;
    public boolean isInfinite;

    public ArmaTridentItem(Properties pProperties, String identifier, ModItemCategories category) {
        super(pProperties);
        this.category = category;
        this.identifier = identifier;
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.tooltipHelper("item.armament." + identifier, false, getFontFromCategory(category), colors);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.1", false, null, colors[1]));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.2", false, null, colors[0]));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament." + identifier + ".desc.3", false, null, colors[1]));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public String geoIdentifier() {
        return this.identifier;
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    public void setRiptideEnabled(Boolean enabled) {
        this.isRiptideEnabled = enabled;
    }

    public void setInfinite(Boolean enabled) {
        this.isInfinite = enabled;
    }

    public void setLaunchVel(float launchVel) {
        this.launchVel = launchVel;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int useDuration = this.getUseDuration(pStack) - pTimeLeft;
            if (useDuration >= 10) {
                int riptideLevel = EnchantmentHelper.getRiptide(pStack);
                if (riptideLevel <= 0 || player.isInWaterOrRain()) {
                    if (!pLevel.isClientSide) {
                        pStack.hurtAndBreak(1, player, (p_43388_) -> p_43388_.broadcastBreakEvent(pEntityLiving.getUsedItemHand()));
                        if (riptideLevel == 0) {
                            launchProjectile(pLevel, player, pStack);
                        }
                    }

                    player.awardStat(Stats.ITEM_USED.get(this));
                    if (riptideLevel > 0 && this.isRiptideEnabled) {
                        float yRot = player.getYRot();
                        float xRot = player.getXRot();
                        float $$10 = -Mth.sin(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
                        float $$11 = -Mth.sin(xRot * ((float) Math.PI / 180F));
                        float $$12 = Mth.cos(yRot * ((float) Math.PI / 180F)) * Mth.cos(xRot * ((float) Math.PI / 180F));
                        float $$13 = Mth.sqrt($$10 * $$10 + $$11 * $$11 + $$12 * $$12);
                        float $$14 = 3.0F * ((1.0F + (float) riptideLevel) / 4.0F);
                        $$10 *= $$14 / $$13;
                        $$11 *= $$14 / $$13;
                        $$12 *= $$14 / $$13;
                        player.push((double) $$10, (double) $$11, (double) $$12);
                        player.startAutoSpinAttack(20);
                        if (player.onGround()) {
                            float $$15 = 1.1999999F;
                            player.move(MoverType.SELF, new Vec3((double) 0.0F, (double) 1.1999999F, (double) 0.0F));
                        }

                        SoundEvent $$16;
                        if (riptideLevel >= 3) {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_3;
                        } else if (riptideLevel == 2) {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_2;
                        } else {
                            $$16 = SoundEvents.TRIDENT_RIPTIDE_1;
                        }

                        pLevel.playSound((Player) null, player, $$16, SoundSource.PLAYERS, 1.0F, 1.0F);
                    }

                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack $$3 = pPlayer.getItemInHand(pHand);
        if ($$3.getDamageValue() >= $$3.getMaxDamage() - 1) {
            return InteractionResultHolder.fail($$3);
        } else if (EnchantmentHelper.getRiptide($$3) > 0 && !pPlayer.isInWaterOrRain() && this.isRiptideEnabled) {
            return InteractionResultHolder.fail($$3);
        } else {
            pPlayer.startUsingItem(pHand);
            return InteractionResultHolder.consume($$3);
        }
    }

    public void launchProjectile(Level pLevel, Player player, ItemStack pStack) {
        int riptideLevel = EnchantmentHelper.getRiptide(pStack);
        ThrownTrident thrownEntity = new ThrownTrident(pLevel, player, pStack);
        thrownEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, launchVel + (isRiptideEnabled ? (float) riptideLevel : 0) * 0.5F, 1.0F);
        if (player.getAbilities().instabuild) {
            thrownEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }

        pLevel.addFreshEntity(thrownEntity);
        pLevel.playSound((Player) null, thrownEntity, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
        if (!player.getAbilities().instabuild || !this.isInfinite) {
            player.getInventory().removeItem(pStack);
        }
    }

}
