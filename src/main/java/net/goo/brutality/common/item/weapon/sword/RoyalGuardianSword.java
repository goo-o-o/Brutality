package net.goo.brutality.common.item.weapon.sword;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.goo.brutality.Brutality;
import net.goo.brutality.client.particle.providers.WaveParticleData;
import net.goo.brutality.client.player_animation.AnimationHelper;
import net.goo.brutality.common.item.base.BrutalitySwordItem;
import net.goo.brutality.common.network.PacketHandler;
import net.goo.brutality.common.network.serverbound.ServerboundStartPlayerAnimationPacket;
import net.goo.brutality.common.network.serverbound.ServerboundStopPlayerAnimationPacket;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.common.registry.BrutalityParticles;
import net.goo.brutality.common.registry.BrutalitySounds;
import net.goo.brutality.event.forge.DelayedTaskScheduler;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.math.phys.hitboxes.OrientedBoundingBox;
import net.goo.brutality.util.tooltip.ItemDescriptionComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.goo.brutality.common.registry.BrutalityAttributes.BASE_ENTITY_RANGE_UUID;

public class RoyalGuardianSword extends BrutalitySwordItem {
    public static final OrientedBoundingBox DISPLAY_HITBOX = new OrientedBoundingBox(Vec3.ZERO, new Vec3(3F, 5, 20).scale(0.5F), 0, 0, 0);
    public static final Vec3 OFFSET = new Vec3(0, -1, 1.5 + DISPLAY_HITBOX.halfExtents.z);
    private static final ResourceLocation OVERHEAD_SWORD_POSE = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "overhead_sword_pose");
    private static final ResourceLocation OVERHEAD_SWORD_DOWNSWING = ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "overhead_sword_downswing");
    private static final int MAX_CHARGE_TICKS = 100;
    private static final int YELLOW = FastColor.ARGB32.color(255, 253, 245, 95);
    private static final int TRANSPARENT = FastColor.ARGB32.color(0, 253, 245, 95);

    public RoyalGuardianSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    private static OrientedBoundingBox getHitbox(Player player) {
        return (OrientedBoundingBox) DISPLAY_HITBOX.inWorld(player, OFFSET, 0, player.getYRot());
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderDisplayHitbox(Player player, PoseStack poseStack) {
        float percent = getChargePercent(player);
        int bottomColor = FastColor.ARGB32.lerp(percent, TRANSPARENT, YELLOW);
        OrientedBoundingBox.renderGradientSides(getHitbox(player), poseStack, bottomColor, TRANSPARENT);

    }

    public static float processHurt(Player victim, float amount) {
        return victim.getUseItem().is(BrutalityItems.ROYAL_GUARDIAN_SWORD.get()) ? amount * 0.25F : amount;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 10000;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != EquipmentSlot.MAINHAND) return super.getAttributeModifiers(slot, stack);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(ForgeMod.ENTITY_REACH.get(), new AttributeModifier(BASE_ENTITY_RANGE_UUID, "Range Buff", 14, AttributeModifier.Operation.ADDITION));
        builder.putAll(super.getAttributeModifiers(slot, stack));
        return builder.build();
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }


    public static float getChargePercent(Player player) {
        ItemStack stack = player.getUseItem();
        if (!stack.is(BrutalityItems.ROYAL_GUARDIAN_SWORD.get())) return 0.0F;
        return Math.min(1.0F, player.getTicksUsingItem() / (float) MAX_CHARGE_TICKS);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pUsedHand != InteractionHand.MAIN_HAND || !pPlayer.onGround())
            return InteractionResultHolder.fail(pPlayer.getItemInHand(pUsedHand));
        pPlayer.startUsingItem(pUsedHand);

        AnimationHelper.playAnimation(pPlayer, OVERHEAD_SWORD_POSE, false, 0.2F, 5);
        PacketHandler.sendToServer(new ServerboundStartPlayerAnimationPacket(pPlayer.getUUID(), OVERHEAD_SWORD_POSE, false, 0.2F, 5));

        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        pLivingEntity.setYBodyRot(pLivingEntity.getYHeadRot());
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (pLivingEntity instanceof Player pPlayer) {
            if (getChargePercent(pPlayer) < 1) {
                AnimationHelper.stopAnimation(pPlayer, 5);
                PacketHandler.sendToServer(new ServerboundStopPlayerAnimationPacket(pPlayer.getUUID(), 5));
            } else {
                DelayedTaskScheduler.queueCommonWork(pLevel, 4, () -> {
                    float attackDamage = (float) pPlayer.getAttributeValue(Attributes.ATTACK_DAMAGE);

                    pLevel.playSound(null, pPlayer.blockPosition(), BrutalitySounds.METEOR_CRASH.get(), SoundSource.PLAYERS);
                    OrientedBoundingBox box = getHitbox(pPlayer);

                    box.findEntitiesHit(pPlayer, LivingEntity.class).forEach(e ->
                            e.hurt(e.damageSources().playerAttack(pPlayer), attackDamage * 5));

                    WaveParticleData<?> waveParticleData = new WaveParticleData<>(BrutalityParticles.SHOCKWAVE.get(), attackDamage * 0.25F, 10);

                    if (pLevel instanceof ServerLevel serverLevel) {

                        serverLevel.sendParticles(
                                waveParticleData, box.center.x, pPlayer.getY(0.15), box.center.z, 1,
                                0, 0, 0,
                                0
                        );

                        ModUtils.applyWaveEffect(serverLevel, box.center.x, pPlayer.getY(0.15), box.center.z, LivingEntity.class, waveParticleData, null, e -> {
                            e.invulnerableTime = 0;
                            e.hurt(e.damageSources().playerAttack(pPlayer), attackDamage);
                            Vec3 fromCenter = e.position().subtract(box.center);
                            e.push(fromCenter.x, 0.2, fromCenter.z);
                        });

                    }

                });
                AnimationHelper.playAnimation(pPlayer, OVERHEAD_SWORD_DOWNSWING, false, 1, 0);
                PacketHandler.sendToServer(new ServerboundStartPlayerAnimationPacket(pPlayer.getUUID(), OVERHEAD_SWORD_DOWNSWING, false, 1, 0));
            }
        }
    }


}
