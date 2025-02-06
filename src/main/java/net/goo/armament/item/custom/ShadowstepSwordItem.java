package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.util.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class ShadowstepSwordItem extends SwordItem implements GeoItem {
    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public ShadowstepSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    int[] color1 = new int[]{65, 0, 125};
    int[] color2 = new int[]{25, 25, 25};

    private static final ResourceLocation ALAGARD = new ResourceLocation(Armament.MOD_ID, "alagard");

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.armament.shadowstep.desc.1").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(Component.translatable("item.armament.shadowstep.desc.2").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color1))));
        pTooltipComponents.add(Component.translatable("item.armament.shadowstep.desc.3").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(color2))));
        pTooltipComponents.add(Component.translatable("item.armament.shadowstep.desc.3").withStyle(Style.EMPTY.withFont(ALAGARD).withColor(ModUtils.rgbToInt(color1))));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }


    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.addColorGradientText((Component.translatable("item.armament.shadowstep")), color2, color1, color2).withStyle(Style.EMPTY.withBold(true));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Entity entity = ModUtils.getEntityPlayerLookingAt(pPlayer, 35);

        if (!pLevel.isClientSide && entity instanceof LivingEntity) {
            Item item = pPlayer.getItemInHand(pUsedHand).getItem();
            float distance = 2.0F; // Distance behind the entity
            Vec3 entityPos = entity.position();
            float entityYaw = entity.getYRot();

            pPlayer.getCooldowns().addCooldown(item, 60);

            double offsetX = -distance * Math.sin(Math.toRadians(entityYaw));
            double offsetZ = distance * Math.cos(Math.toRadians(entityYaw));

            Vec3 newPos = entityPos.subtract(offsetX, 0, offsetZ);
            BlockPos targetPos = new BlockPos(new Vec3i(((int) newPos.x), ((int) newPos.y), ((int) newPos.z)));

            if (pLevel.getBlockState(targetPos.above()).isAir() && pLevel.getBlockState(targetPos.below()).isSolid()) {
                pPlayer.teleportTo(Objects.requireNonNull(Objects.requireNonNull(pLevel.getServer()).getLevel(pPlayer.level().dimension())),
                        newPos.x, newPos.y, newPos.z,
                        EnumSet.allOf(RelativeMovement.class), entityYaw, pPlayer.getXRot());

                pLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F ,1F);
                entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);

            } else {
                pPlayer.displayClientMessage(Component.translatable("item.armament.shadowstep.invalid").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(new int[]{200, 50, 50}))), true);
            }

        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }


    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        Vec3 playerPos = player.getPosition(1.0F);
        Vec3 entityPos = entity.getPosition(1.0F);

        Vec3 targetVec = entityPos.subtract(playerPos);
        float targetDeg = (float) Math.toDegrees(Math.atan2(targetVec.x, targetVec.z)) + 180;
        player.sendSystemMessage(Component.literal("ANGLE: " + targetDeg));

        boolean isBehind = targetDeg > 90 && targetDeg < 270;

        float baseDamage = 5 * player.getAttackStrengthScale(1F);

        if (isBehind) {
            entity.hurt(entity.damageSources().playerAttack(player), baseDamage * 3F);
            player.sendSystemMessage(Component.literal("CRIT"));
        } else {
            entity.hurt(entity.damageSources().playerAttack(player), baseDamage);
            player.sendSystemMessage(Component.literal("NORMAL"));
        }
        return true;
    }
}
