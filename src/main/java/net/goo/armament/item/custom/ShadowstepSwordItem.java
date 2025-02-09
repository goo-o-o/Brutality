package net.goo.armament.item.custom;

import net.goo.armament.item.ModItemCategories;
import net.goo.armament.client.event.item.renderer.ShadowstepSwordItemRenderer;
import net.goo.armament.particle.ModParticles;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static net.goo.armament.util.ModResources.FANTASY;

public class ShadowstepSwordItem extends SwordItem implements GeoItem {
    AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private final ModItemCategories category;

    public ShadowstepSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, ModItemCategories category) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.category = category;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    int[] color1 = new int[]{65, 0, 125};
    int[] color2 = new int[]{25, 25, 25};

    public ModItemCategories getCategory() {
        return category;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.shadowstep.desc.1", false, null, color2));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.shadowstep.desc.2", false, null, color1));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.shadowstep.desc.3", false, null, color2));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.tooltipHelper("item.armament.shadowstep", false, FANTASY, color1, color2);
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ShadowstepSwordItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    renderer = new ShadowstepSwordItemRenderer();
                }
                return this.renderer;
            }
        });
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

        boolean isBehind = targetDeg > (90 + 15) && targetDeg < (270 - 15);

        float baseDamage = 3 * player.getAttackStrengthScale(1F);
        float damageMult = 5;

        if (isBehind) {
            double d0 = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
            double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
            if (player.level() instanceof ServerLevel) {
            ((ServerLevel)player.level()).sendParticles(ModParticles.VOID_SWEEP_PARTICLE.get(), entity.getX() + d0, entity.getY(0.5D), entity.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
            }
            entity.hurt(entity.damageSources().playerAttack(player), baseDamage * damageMult);
        } else {
            entity.hurt(entity.damageSources().playerAttack(player), baseDamage);
        }
        return true;
    }
}
