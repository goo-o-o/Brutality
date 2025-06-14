package net.goo.brutality.event;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.weapon.custom.AtomicJudgementHammer;
import net.goo.brutality.item.weapon.custom.EventHorizonLance;
import net.goo.brutality.item.weapon.custom.TerratonHammer;
import net.goo.brutality.registry.ModMobEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class LivingEntityEventHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        Entity victim = event.getEntity();
        float damageTaken = event.getAmount();
        Level level = victim.level();
        float finalDamageDealt = -1;

        if (victim instanceof LivingEntity livingEntity) {
            if (livingEntity instanceof Player victimPlayer) {

                if (victimPlayer.hasEffect(ModMobEffects.SAD.get())) {
                    int foodLevel = victimPlayer.getFoodData().getFoodLevel();

                    if (foodLevel > 0) {
                        victimPlayer.getFoodData().setFoodLevel((int) (foodLevel - damageTaken / 2));
                        finalDamageDealt = (damageTaken / 2);
                    }
                }
            }


            if (livingEntity.hasEffect(ModMobEffects.STONEFORM.get())) {
                int amp = Objects.requireNonNull(livingEntity.getEffect(ModMobEffects.STONEFORM.get())).getAmplifier();
                finalDamageDealt = ((float) (damageTaken * (1 - 0.1 * amp)));
            }
        }

        event.setAmount(finalDamageDealt == -1 ? damageTaken : finalDamageDealt);
    }

    @SubscribeEvent
    public static void onLivingKnockback(LivingKnockBackEvent event) {
        LivingEntity victim = event.getEntity();
        Entity lastAttacker = victim.getLastAttacker();
        if (lastAttacker instanceof Player player) {
            Item item = player.getMainHandItem().getItem();

            if (item instanceof TerratonHammer) {
                float kbMult = ((float) Math.pow(player.getAttackStrengthScale(0.5F), 3));

                event.setStrength((event.getOriginalStrength() * 10) * kbMult);

            }

            if (item instanceof EventHorizonLance) {
                event.setCanceled(true);

                Vec3 direction = lastAttacker.getPosition(1f).subtract(victim.getPosition(1F)).normalize();

                victim.addDeltaMovement(direction.scale(1.2));

            }
        }

    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player && !player.level().isClientSide()) {


            if (player.getMainHandItem().getItem() instanceof AtomicJudgementHammer) {
                event.setDamageMultiplier(0.15F);
                if (event.getDistance() > 10 && !player.level().isClientSide()) {
                    AtomicJudgementHammer.doCustomExplosion(player.level(), player, player, player.getMainHandItem());
                }
            }


        }
    }

}
