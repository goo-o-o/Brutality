package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.particle.providers.FlatParticleData;
import net.goo.brutality.registry.BrutalityModParticles;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MurasamaSword extends BrutalitySwordItem {
    private final RandomSource random = RandomSource.create();

    public MurasamaSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
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

        for (int i = 0; i < 10; i++) {
            FlatParticleData<?> data = new FlatParticleData<>(BrutalityModParticles.MURASAMA_SLASH_PARTICLE.get(),
                    Mth.nextFloat(random, 3F, 12F),
                    random.nextIntBetweenInclusive(-90, 90),
                    random.nextIntBetweenInclusive(-90, 90),
                    random.nextIntBetweenInclusive(-90, 90)
            );
//            PacketHandler.sendToServer(new ClientboundParticlePacket(data, true, (float) pPlayer.getX(), (float) pPlayer.getY(), (float) pPlayer.getZ(), 3.5F, 3.5F, 3.5F, 0, 0, 0, 1));
            ModUtils.sendParticles(serverLevel, data, true, pPlayer,
                    3.5, 3.5, 3.5, 1, 0);
        }

        pPlayer.getCooldowns().addCooldown(pStack.getItem(), 60);


        pPlayer.level().playSound(null, pPlayer.getOnPos(), ModUtils.getRandomSound(BrutalityModSounds.MURASAMA), SoundSource.PLAYERS, 1,
                Mth.nextFloat(random, 0.8F, 1F));

        List<LivingEntity> nearbyEntities = serverLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, pPlayer, pPlayer.getBoundingBox().inflate(7));

        for (LivingEntity entity : nearbyEntities) {
            entity.hurt(entity.damageSources().playerAttack(pPlayer), 10);
            ModUtils.sendParticles(serverLevel, ModUtils.getRandomParticle(BrutalityModParticles.MURASAMA_PARTICLE), true,
                    entity, 0, 0, 0, 3, 0);
        }


        pStack.hurtAndBreak(nearbyEntities.size(), pPlayer, player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        super.releaseUsing(pStack, pLevel, pLivingEntity, pTimeCharged);
    }


    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker.level() instanceof ServerLevel serverLevel) {
            if (pAttacker instanceof ServerPlayer player) {
                if (!ModList.get().isLoaded("bettercombat")) {
                    serverLevel.playSound(null, player.getOnPos(), ModUtils.getRandomSound(BrutalityModSounds.MURASAMA), SoundSource.PLAYERS, 1,
                            Mth.nextFloat(random, 0.8F, 1F));
                }

                List<LivingEntity> nearbyEntities = serverLevel.getNearbyEntities(LivingEntity.class, TargetingConditions.DEFAULT, pAttacker, pAttacker.getBoundingBox().inflate(2.5, 0, 2.5));

                for (LivingEntity entity : nearbyEntities) {
                    entity.hurt(entity.damageSources().playerAttack(player), 5);

                    ModUtils.sendParticles(serverLevel, ModUtils.getRandomParticle(BrutalityModParticles.MURASAMA_PARTICLE), true,
                            entity, 0, 0, 0, 3, 0);
                }
            }
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }


    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (entity instanceof ServerPlayer player) {
                List<LivingEntity> nearbyEntities = serverLevel.getNearbyEntities(LivingEntity.class,
                        TargetingConditions.DEFAULT.selector(e -> !(e instanceof Animal || e instanceof AmbientCreature)), player, player.getBoundingBox().inflate(2.5, 0, 2.5));


                for (LivingEntity nearbyEntity : nearbyEntities) {
                    nearbyEntity.hurt(nearbyEntity.damageSources().playerAttack(player), 5 * player.getAttackStrengthScale(1));
                    nearbyEntity.hurt(nearbyEntity.damageSources().magic(), 5 * player.getAttackStrengthScale(1));

                    ModUtils.sendParticles(serverLevel, ModUtils.getRandomParticle(BrutalityModParticles.MURASAMA_PARTICLE), true,
                            entity, 0, 0, 0, 3, 0);
                }
            }
        }
        return super.onEntitySwing(stack, entity);
    }
}
