package net.goo.brutality.item.weapon.generic;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.projectile.ray.ExplosionRay;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.ModExplosionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;

import static net.goo.brutality.util.ModUtils.getBlockLookingAt;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class FirstExplosionStaff extends BrutalityGenericItem {
    private int randYaw;
    private int randPitch;
    private int circleCount = 0;
    private BlockPos targetBlockPos;
    private static final String SUCCESSFUL = "Successful";

    public FirstExplosionStaff(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }


    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateTag().putBoolean(SUCCESSFUL, true);
        return stack;
    }


    @SubscribeEvent
    public static void onFOVModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();

        if (player.isUsingItem()) {
            ItemStack itemStack = player.getUseItem();
            if (itemStack.getItem() instanceof FirstExplosionStaff) {
                event.setNewFovModifier(event.getFovModifier() * 1.5F);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack pStack = pPlayer.getItemInHand(pUsedHand);
        pPlayer.startUsingItem(pUsedHand);
        if (pLevel instanceof ServerLevel serverLevel) {
            triggerAnim(pPlayer, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "use");
            pStack.getOrCreateTag().putBoolean(SUCCESSFUL, true);
            targetBlockPos = getBlockLookingAt(pPlayer, false, 300);
            randPitch = pLevel.random.nextIntBetweenInclusive(-15, 16);
            randYaw = pLevel.random.nextIntBetweenInclusive(0, 361);

            for (int i = 0; i < 2; i++) {
//                PacketHandler.sendToServer(new ClientboundExactParticlePacket(
//                        pPlayer.getX(),
//                        pPlayer.getY() + 0.1,
//                        pPlayer.getZ(),
//                        new ExplosionMagicCircleParticle.ParticleData(
//                                pPlayer.getId(),
//                                1,
//                                0,
//                                0,
//                                0, i == 1, true
//                        )
//                ));

            }
        }


        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    private void spawnExplosionSpellMagicCircles(ServerLevel level, Player player, int step) {
        if (targetBlockPos == null) return;

        double pitchRad = Math.toRadians(randPitch);
        double yawRad = Math.toRadians(randYaw);

        Vec3 startPos = Vec3.atCenterOf(targetBlockPos);
        int baseHeight = 14;
        int circleStep = 8;

        int height = baseHeight + circleStep * step;
        double radius = height * Math.tan(pitchRad);
        double xOffset = radius * Math.sin(yawRad);
        double zOffset = radius * Math.cos(yawRad);

        Vec3 position = new Vec3(startPos.x + xOffset, startPos.y + height, startPos.z + zOffset);


//        PacketHandler.sendToNearbyClients(level, position.x, position.y, position.z, 128, new ClientboundParticlePacket(
//                new ExplosionMagicCircleParticle.ParticleData(
//                        player.getId(),
//                        level.random.nextIntBetweenInclusive(5, 50),
//                        randPitch,
//                        randYaw,
//                        0, level.random.nextBoolean(), false
//                ), true, (float) position.x, (float) position.y, (float) position.z,
//                0, 0, 0,
//                0, 0, 0,
//                1
//        ));

    }


    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        int useDuration = getUseDuration(pStack) - pRemainingUseDuration;
        String identifier = pStack.getDescriptionId();

        if (pLevel instanceof ServerLevel serverLevel && pLivingEntity instanceof Player player) {

            if (useDuration % 20 == 0) {
                if (circleCount < 10) {
                    circleCount++;
                    spawnExplosionSpellMagicCircles(serverLevel, player, circleCount);
                } else {
                    player.displayClientMessage(Component.translatable("item." + Brutality.MOD_ID + "." + identifier + ".maximum").withStyle(ChatFormatting.DARK_RED).withStyle(ChatFormatting.BOLD), true);
                }
            }

            pLivingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 5, 10, false, false));
        }
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {

        if (pLevel instanceof ServerLevel serverLevel && pEntity instanceof LivingEntity livingEntity) {
            if (!pStack.getOrCreateTag().getBoolean(SUCCESSFUL))
                triggerAnim(livingEntity, GeoItem.getOrAssignId(pStack, serverLevel), "controller", "release");

        }
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player pPlayer) {

            if (!pLevel.isClientSide()) {
                triggerAnim(pPlayer, GeoItem.getOrAssignId(pStack, ((ServerLevel) pLevel)), "controller", "release");
                if (pStack.getOrCreateTag().getBoolean(SUCCESSFUL)) {
                    int explosionSize = circleCount * 3;
                    int fatigueTime = 200 * explosionSize;

//                pPlayer.getCooldowns().addCooldown(pStack.getItem(), fatigueTime);
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, fatigueTime, 1));
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, fatigueTime, 1));
                    pPlayer.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, fatigueTime, 1));

//                    PacketHandler.sendToAllClients(new ClientboundEnhancedParticlePacket(
//                            targetBlockPos.getCenter().x(),
//                            targetBlockPos.getCenter().y(),
//                            targetBlockPos.getCenter().z(),
//                            new ExplosionAmbientParticle.ParticleData(
//                                    pPlayer.getId(),
//                                    explosionSize * 2F
//                            )
//                    ));


                    ExplosionRay explosionRay = new ExplosionRay(BrutalityModEntities.EXPLOSION_RAY.get(), pLevel);
                    explosionRay.setOwner(pPlayer.getUUID());
                    explosionRay.setPitch(randPitch);
                    explosionRay.setYaw(randYaw);
                    explosionRay.setCircleCount(circleCount);
                    explosionRay.setPos(targetBlockPos.getCenter().x, targetBlockPos.getY(), targetBlockPos.getCenter().z);
                    pLevel.addFreshEntity(explosionRay);
                    ModExplosionHelper.Server.addExplosion(pLevel, pPlayer, explosionRay.blockPosition(), explosionSize, 3);

                } else {
                    pLivingEntity.hurt(pLivingEntity.damageSources().indirectMagic(pLivingEntity, pLivingEntity), circleCount);
                }
                circleCount = 0;
            }
        }
    }


    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if (player.level() instanceof ServerLevel serverLevel)
            triggerAnim(player, GeoItem.getOrAssignId(item, serverLevel), "controller", "idle");
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> state.setAndContinue(RawAnimation.begin().thenPlay("idle")))
                .triggerableAnim("use", RawAnimation.begin().thenPlay("use").thenPlayAndHold("using"))
                .triggerableAnim("release", RawAnimation.begin().thenPlayAndHold("release").thenPlayAndHold("idle"))
        );
    }

//    @SubscribeEvent
//    public static void onOwnerDamaged(LivingHurtEvent event) {
//        LivingEntity entity = event.getEntity();
//        if (entity instanceof Player player) {
//            if (!player.level().isClientSide() && player.getUseItem().getItem() instanceof FirstExplosionStaff) {
//                if (player.isUsingItem()) {
//                    ItemStack pStack = player.getUseItem();
//                    player.getCooldowns().addCooldown(pStack.getItem(), 200);
//                    player.getUseItem().getOrCreateTag().putBoolean(SUCCESSFUL, false);
//                    player.stopUsingItem();
//                }
//            }
//        }
//    }
}
