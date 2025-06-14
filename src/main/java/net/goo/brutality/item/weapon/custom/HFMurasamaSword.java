package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityAutoFullbrightItemNoDepthRenderer;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.particle.custom.flat.MurasamaSlash;
import net.goo.brutality.registry.ModParticles;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HFMurasamaSword extends BrutalitySwordItem {
    private final RandomSource random = RandomSource.create();

    public HFMurasamaSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, rarity, descriptionComponents);
    }


    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityAutoFullbrightItemNoDepthRenderer.class);
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
        if (!(pLivingEntity instanceof ServerPlayer pPlayer)) {
            super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
            return;
        }

        if (!(pLevel instanceof ServerLevel serverLevel)) {
            super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
            return;
        }

        for (int i = 0; i < 10; i++)
            serverLevel.sendParticles(pPlayer, new MurasamaSlash.ParticleData(false, 0, 0, 0,
                    pPlayer.getId(), Mth.nextFloat(random, 3f, 12f),
                    random.nextIntBetweenInclusive(-90, 90),
                    random.nextIntBetweenInclusive(-90, 90),
                    random.nextIntBetweenInclusive(-90, 90)), false, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), 1, 0, 0, 0, 0);


        pPlayer.getCooldowns().addCooldown(pStack.getItem(), 60);


        pPlayer.level().playSound(null, pPlayer.getOnPos(), ModUtils.getRandomSound(ModSounds.MURASAMA), SoundSource.PLAYERS, 1, ModUtils.nextFloatBetweenInclusive(random, 0.8F, 1F));

        List<LivingEntity> nearbyEntities = serverLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, pPlayer, pPlayer.getBoundingBox().inflate(7));

        for (LivingEntity entity : nearbyEntities) {
            entity.hurt(entity.damageSources().playerAttack(pPlayer), 10);
            serverLevel.sendParticles(pPlayer, ModUtils.getRandomParticle(ModParticles.MURASAMA_PARTICLE), true,
                    entity.getX(), entity.getY() + entity.getBbHeight(), entity.getZ(), 2, 1, 1, 1, 0);
        }


        pStack.hurtAndBreak(nearbyEntities.size(), pPlayer, player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker.level() instanceof ServerLevel serverLevel) {
            if (pAttacker instanceof ServerPlayer player) {
                if (!ModList.get().isLoaded("bettercombat")) {
                    serverLevel.playSound(null, player.getOnPos(), ModUtils.getRandomSound(ModSounds.MURASAMA), SoundSource.PLAYERS, 1,
                            ModUtils.nextFloatBetweenInclusive(random, 0.8F, 1F));
                }

                List<LivingEntity> nearbyEntities = serverLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, pAttacker, pAttacker.getBoundingBox().inflate(2.5, 0, 2.5));

                for (LivingEntity entity : nearbyEntities)
                    entity.hurt(entity.damageSources().playerAttack(player), 5);

                for (int i = 0; i < 3; i++) {
                    serverLevel.sendParticles(player, ModUtils.getRandomParticle(ModParticles.MURASAMA_PARTICLE), true,
                            pTarget.getX(), pTarget.getY() + pTarget.getBbHeight() / 2, pTarget.getZ(),
                            3, 1, 1, 1, 0);
                }
            }
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (entity instanceof ServerPlayer player) {
                List<LivingEntity> nearbyEntities = serverLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, player, player.getBoundingBox().inflate(2.5, 0, 2.5));


                for (LivingEntity nearbyEntity : nearbyEntities) {
                    nearbyEntity.hurt(nearbyEntity.damageSources().playerAttack(player), 5 * player.getAttackStrengthScale(1));
                    nearbyEntity.hurt(nearbyEntity.damageSources().magic(), 5 * player.getAttackStrengthScale(1));

                    for (int i = 0; i < 3; i++) {
                        serverLevel.sendParticles(player, ModUtils.getRandomParticle(ModParticles.MURASAMA_PARTICLE), true,
                                nearbyEntity.getX(), nearbyEntity.getY() + nearbyEntity.getBbHeight() / 2, nearbyEntity.getZ(),
                                3, 1, 1, 1, 0);
                    }
                }
            }
        }
        return super.onEntitySwing(stack, entity);
    }
}
