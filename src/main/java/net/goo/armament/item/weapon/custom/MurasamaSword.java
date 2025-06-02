package net.goo.armament.item.weapon.custom;

import net.goo.armament.Armament;
import net.goo.armament.client.renderers.item.ArmaAutoFullbrightItemRenderer;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.s2cEnhancedExactParticlePacket;
import net.goo.armament.particle.custom.flat.MurasamaSlash;
import net.goo.armament.registry.ModParticles;
import net.goo.armament.registry.ModSounds;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MurasamaSword extends ArmaSwordItem {
    private final RandomSource random = RandomSource.create();

    public MurasamaSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, ArmaAutoFullbrightItemRenderer.class);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);
        return InteractionResultHolder.consume(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
        if (!(pLivingEntity instanceof Player pPlayer)) {
            super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
            return;
        }

        // Calculate charge ratio (0.0 to 1.0)
        double chargeTime = getUseDuration(pStack) - pTimeCharged;
        double chargeRatio = Math.min(chargeTime / 40.0, 1.0);

        // Calculate size multiplier (2.5 to 10.0)
        double sizeMultiplier = 2.5 + (7.5 * chargeRatio);

        // Calculate proportional damage (health-based scaling)
        float damageToPlayer = (float) (sizeMultiplier); // Reduced self-damage factor
        if (damageToPlayer >= pPlayer.getHealth()) {
            damageToPlayer = pPlayer.getHealth() - 1; // Leave player with at least 0.5 hearts
        }

        if (pPlayer.getHealth() <= 1) return;
        // Apply self-damage
        pPlayer.hurt(pPlayer.damageSources().generic(), damageToPlayer);
        pPlayer.getCooldowns().addCooldown(pStack.getItem(), 60);

        if (!(pLevel instanceof ServerLevel serverLevel)) {
            super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
            return;
        }


        pPlayer.level().playSound(null, pPlayer.getOnPos(), ModUtils.getRandomSound(ModSounds.MURASAMA), SoundSource.PLAYERS, 1, ModUtils.nextFloatBetweenInclusive(random, 0.8F, 1F));

        PacketHandler.sendToAllClients(new s2cEnhancedExactParticlePacket(
                pPlayer.getX(),
                pPlayer.getY() + pPlayer.getBbHeight() / 3,
                pPlayer.getZ(),
                0, 0, 0,
                new MurasamaSlash.ParticleData(pPlayer.getId(), (float) sizeMultiplier,
                        serverLevel.random.nextInt(-10, 11),
                        serverLevel.random.nextInt(-10, 11),
                        serverLevel.random.nextInt(-10, 11)
                )

        ));

        pStack.hurtAndBreak(10, pPlayer, player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        float baseDamage = 5.0f;
        float chargedDamage = baseDamage * (1.0f + (float) chargeRatio); // 5-10 damage range

        // Find and damage nearby entities
        double hitRadius = sizeMultiplier * 0.9;
        List<LivingEntity> entities = serverLevel.getNearbyEntities(
                LivingEntity.class,
                TargetingConditions.DEFAULT,
                pPlayer,
                pPlayer.getBoundingBox().inflate(hitRadius, hitRadius * 0.5, hitRadius)
        );

        for (LivingEntity entity : entities) {

            entity.hurt(entity.damageSources().indirectMagic(pPlayer, entity), chargedDamage);
            serverLevel.sendParticles(
                    ModParticles.MURASAMA_PARTICLE.get(),
                    entity.getX(),
                    entity.getY() + entity.getBbHeight() / 2,
                    entity.getZ(),
                    20, 0.5, 0.5, 0.5, 0
            );
        }

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {

        if (entity instanceof Player player)
            player.level().playSound(null, player.getOnPos(), ModUtils.getRandomSound(ModSounds.MURASAMA), SoundSource.PLAYERS, 1, ModUtils.nextFloatBetweenInclusive(random, 0.8F, 1F));
        return super.onEntitySwing(stack, entity);
    }
}
