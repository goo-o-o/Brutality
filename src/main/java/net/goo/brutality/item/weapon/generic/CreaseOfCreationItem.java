package net.goo.brutality.item.weapon.generic;

import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.item.base.BrutalityGenericItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.s2cSyncCapabilitiesPacket;
import net.goo.brutality.particle.providers.TrailParticleData;
import net.goo.brutality.registry.BrutalityCapabilities;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static net.goo.brutality.util.helpers.EnchantmentHelper.restrictEnchants;

public class CreaseOfCreationItem extends BrutalityGenericItem {


    public CreaseOfCreationItem(Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 6000;
    }

    @Override
    public @NotNull ItemStack getDefaultInstance() {
        ItemStack stack = new ItemStack(this);
        stack.enchant(Enchantments.KNOCKBACK, 4);
        return stack;
    }

    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(
            Enchantments.KNOCKBACK
    );

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return restrictEnchants(book, ALLOWED_ENCHANTMENTS);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return ALLOWED_ENCHANTMENTS.contains(enchantment);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel instanceof ServerLevel serverLevel) {
            pPlayer.startUsingItem(pUsedHand);
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);

            List<Entity> targets = ModUtils.getEntitiesInRay(Entity.class, pPlayer, 30, ClipContext.Fluid.NONE, ClipContext.Block.OUTLINE,
                    1F, entity -> entity != pPlayer, 10, null).entityList();

            Entity target = targets.isEmpty() ? null : targets.get(0);

            triggerAnim(pPlayer, GeoItem.getOrAssignId(stack, serverLevel), "controller_2", "use");

            if (target != null) {
                if (target instanceof Player player) {
                    if (player.isHolding(this)) {
                        return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
                    }
                }
                stack.getOrCreateTag().putInt("target", target.getId());
            }

        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state ->
                state.setAndContinue(RawAnimation.begin().thenPlay("idle")))
        );

        controllers.add(new AnimationController<>(this, "controller_2", state -> null).
                triggerableAnim("use", RawAnimation.begin().thenPlay("use")));

    }

    public static void handleCreaseOfCreation(Player player) {
        player.getMainHandItem().getOrCreateTag().remove("target");
        player.getOffhandItem().getOrCreateTag().remove("target");
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof CreaseOfCreationItem item) {
                long geoId = GeoItem.getOrAssignId(stack, serverLevel);
                item.stopTriggeredAnim(player, geoId, "controller_2", "use");
            }
        }

    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            stopTriggeredAnim(player, GeoItem.getOrAssignId(item, serverLevel), "controller_2", "use");

            int targetId = item.getOrCreateTag().getInt("target");
            Entity target = serverLevel.getEntity(targetId);

            if (target != null) {
                target.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP).ifPresent(cap -> {
                    cap.setShouldRotate(false);
                    PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(targetId, target));
                });
            }
            item.getOrCreateTag().remove("target");
        }
        return true;
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {

        if (pLevel instanceof ServerLevel serverLevel) {
            int targetId = pStack.getOrCreateTag().getInt("target");
            Entity target = pLevel.getEntity(targetId);
            pStack.getOrCreateTag().remove("target");
            stopTriggeredAnim(pLivingEntity, GeoItem.getOrAssignId(pStack, serverLevel), "controller_2", "use");

            if (target != null) {

                DelayedTaskScheduler.queueServerWork(30, () ->
                        target.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP).ifPresent(cap -> {
                            cap.setShouldRotate(false);
                            PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(targetId, target));
                        }));

                if (!pLivingEntity.isShiftKeyDown()) return;

                Vec3 lookAng = pLivingEntity.getLookAngle().scale(2);

                target.push(lookAng.x, lookAng.y, lookAng.z);

                if (target instanceof ServerPlayer playerTarget)
                    playerTarget.connection.send(new ClientboundSetEntityMotionPacket(playerTarget));

                if (pLivingEntity instanceof Player)
                    pLevel.playSound(null, pLivingEntity.getOnPos(), BrutalityModSounds.BASS_BOOM.get(), SoundSource.AMBIENT);
            }
        }

    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void onUseTick(Level level, LivingEntity player, ItemStack stack, int remainingUseDuration) {
        if (!(player instanceof Player)) return;
        if (level.isClientSide()) return;

        int targetId = stack.getOrCreateTag().getInt("target");

        Entity target = level.getEntity(targetId);
        if (target == null) return;


        target.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP).ifPresent(cap -> {
            if (!cap.isShouldRotate()) {
                cap.setShouldRotate(true);
//                System.out.println("set should rotate");
                PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(targetId, target));
            }
        });


        Vec3 lookVec = player.getViewVector(1.0F);
        Vec3 holdPos = player.getEyePosition().add(lookVec.scale(7));

        Vec3 direction = holdPos.subtract(target.position().x, target.position().y + target.getBbHeight() / 2, target.position().z);

        double distance = direction.length();
        float normalized = (float) (Math.min(distance, 25) / 25);

        Vec3 force = direction.normalize().scale(normalized * 5);

        Vec3 currentMotion = target.getDeltaMovement();
        float lerpFactor = 0.5F;
        Vec3 newMotion = new Vec3(
                Mth.lerp(lerpFactor, currentMotion.x, force.x),
                Mth.lerp(lerpFactor, currentMotion.y, force.y),
                Mth.lerp(lerpFactor, currentMotion.z, force.z)
        );

        target.hasImpulse = true;
        target.setDeltaMovement(newMotion);

        stack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(Objects.requireNonNull(stack.getEquipmentSlot())));
        if (target instanceof LivingEntity) {
            if ((target.horizontalCollision || target.verticalCollision) && target.getDeltaMovement().lengthSqr() > 0.25) {
                target.hurt(player.damageSources().flyIntoWall(), ((float) target.getDeltaMovement().lengthSqr() * 10));
            }
            if (target instanceof ServerPlayer playerTarget) {
                playerTarget.connection.send(new ClientboundSetEntityMotionPacket(playerTarget));
            }
        }
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pLevel instanceof ServerLevel serverLevel)
            if (pIsSelected) {
                if (serverLevel.getGameTime() % 20 == 0) {


                    Vec3 viewVec = pEntity.getViewVector(1F).normalize().scale(1.5F);

                    serverLevel.sendParticles((new TrailParticleData(BrutalityModParticles.CREASE_OF_CREATION_PARTICLE.get(),
                            0.5F, 0.3F, 1f, 1, 0.5F, pEntity.getId(), 20)),
                            pEntity.getX() - viewVec.x, pEntity.getY() + pEntity.getBbHeight() / 2, pEntity.getZ() - viewVec.y, 1, 0.5, 0.5, 0.5, 0);
                }
            } else {
                if (pStack.getOrCreateTag().contains("target")) {

                    int targetId = pStack.getOrCreateTag().getInt("target");
                    pStack.getOrCreateTag().remove("target");

                    Entity target = pLevel.getEntity(targetId);
                    if (target == null) return;

                    DelayedTaskScheduler.queueServerWork(30, () ->
                            target.getCapability(BrutalityCapabilities.ENTITY_SHOULD_ROTATE_CAP).ifPresent(cap -> {
                                cap.setShouldRotate(false);
                                PacketHandler.sendToAllClients(new s2cSyncCapabilitiesPacket(targetId, target));
                            }));
                }
            }
    }
}


