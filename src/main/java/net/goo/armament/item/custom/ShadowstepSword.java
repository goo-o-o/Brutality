package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.registry.ModParticles;
import net.goo.armament.util.ModUtils;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.EnumSet;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ShadowstepSword extends ArmaSwordItem {
    public ShadowstepSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = SHADOWSTEP_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        Entity entity = ModUtils.getEntityPlayerLookingAt(pPlayer, 75);
        teleportBehindEntity(pLevel, pPlayer, entity, pUsedHand);

        return super.use(pLevel, pPlayer, pUsedHand);
    }


    private void teleportBehindEntity(Level pLevel, Player pPlayer, Entity entity, InteractionHand pUsedHand) {
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

                pLevel.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1F, 1F);
                entity.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);


            } else {
                pPlayer.displayClientMessage(Component.translatable("item.armament." + identifier + ".invalid").withStyle(Style.EMPTY.withColor(ModUtils.rgbToInt(new int[]{200, 50, 50}))), true);
            }
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {

        float baseDamage = 3 * player.getAttackStrengthScale(1F);
        float damageMult = 5;

        if (isPlayerBehind(player, entity)) {
            double d0 = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
            double d1 = Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
            if (player.level() instanceof ServerLevel) {
                ((ServerLevel)player.level()).sendParticles(ModParticles.SHADOW_SWEEP_PARTICLE.get(), player.getX() + d0, player.getY(0.5D), player.getZ() + d1, 0, d0, 0.0D, d1, 0.0D);
            }
            entity.hurt(entity.damageSources().playerAttack(player), baseDamage * damageMult);
        } else {
            entity.hurt(entity.damageSources().playerAttack(player), baseDamage);
        }
        return true;
    }

    private boolean isPlayerBehind(Player player, Entity entity) {
        Vec3 playerPos = player.getPosition(1.0F);
        Vec3 entityPos = entity.getPosition(1.0F);

        Vec3 targetVec = entityPos.subtract(playerPos);
        float targetDeg = (float) Math.toDegrees(Math.atan2(targetVec.x, targetVec.z)) + 180;

        return targetDeg > (90 + 15) && targetDeg < (270 - 15);
    }

}
