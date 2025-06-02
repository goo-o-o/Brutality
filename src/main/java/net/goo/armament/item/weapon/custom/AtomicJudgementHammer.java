package net.goo.armament.item.weapon.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.renderers.item.ArmaAutoGlowingItemRenderer;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaHammerItem;
import net.goo.armament.util.helpers.ModExplosionHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.function.Consumer;


@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AtomicJudgementHammer extends ArmaHammerItem {
    public AtomicJudgementHammer(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaAutoGlowingItemRenderer.class);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        Level level = player.level();

        if (!level.isClientSide() && entity instanceof LivingEntity livingEntity) {

            ModExplosionHelper.sendCustomExplode(EXPLOSION_TYPES.NUCLEAR, level, player,
                    livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight() / 5, livingEntity.getZ(),
                    player.getAttackStrengthScale(1F) * 7.5F, false, Level.ExplosionInteraction.BLOCK, true);
            stack.hurtAndBreak(5, livingEntity, e -> livingEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));

        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }


    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player && !player.level().isClientSide()) {
            if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof AtomicJudgementHammer) {
                event.setDamageMultiplier(0.15F);
                if (event.getDistance() > 10 && !entity.level().isClientSide()) {
                    ModExplosionHelper.sendCustomExplode(EXPLOSION_TYPES.NUCLEAR, player.level(), player, entity.getX(), entity.getY(), entity.getZ(), Math.min(event.getDistance() / 3, 5), false,
                            Level.ExplosionInteraction.BLOCK, true);
                }
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        Vec3 viewVec = pLivingEntity.getViewVector(1F).normalize();
        int charge = getUseDuration(pStack) - pTimeCharged;
        float powerForTime = getPowerForTime(charge);
        if (pLivingEntity.onGround()) {
            pLivingEntity.addDeltaMovement(viewVec.scale(powerForTime * -3F));
            if (pLivingEntity instanceof ServerPlayer player) {
                player.getCooldowns().addCooldown(pStack.getItem(), (int) (powerForTime * 100));
                player.connection.send(new ClientboundSetEntityMotionPacket(pLivingEntity));

            }
        }
        pStack.hurtAndBreak((int) (powerForTime * 20), pLivingEntity, livingEntity -> pLivingEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND));

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(stack);
    }


    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / 20.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }
}
