package net.goo.armament.item.noir;

import net.goo.armament.Armament;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.util.ModUtils;
import net.goo.armament.util.helpers.ModTooltipHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class ShadowstepDagger extends ArmaSwordItem {
    public ShadowstepDagger(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
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

                if (pLevel instanceof ServerLevel) {
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 5, 0), pPlayer);
                }
            } else {
                pPlayer.displayClientMessage(Component.translatable("item.armament." + identifier + ".invalid").withStyle(Style.EMPTY.withColor(ModTooltipHelper.rgbToInt(new int[]{200, 50, 50}))), true);
            }
        }
    }

}
