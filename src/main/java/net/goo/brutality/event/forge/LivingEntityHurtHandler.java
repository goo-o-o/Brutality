package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.item.generic.augments.BrutalitySealAugmentItem;
import net.goo.brutality.common.item.weapon.sword.RoyalGuardianSword;
import net.goo.brutality.common.magic.spells.celestia.HolyMantleSpell;
import net.goo.brutality.common.mob_effect.Blockchained;
import net.goo.brutality.common.mob_effect.SadEffect;
import net.goo.brutality.util.AugmentHelper;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.build_archetypes.RageHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class LivingEntityHurtHandler {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity victim = event.getEntity();
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        float amount = event.getAmount();

        amount = onLivingHurt(event, source, victim, amount);

        if (attacker instanceof LivingEntity livingAttacker) {
            amount = onLivingHurtByLiving(victim, livingAttacker, source, amount);
            if (attacker instanceof Player attackerPlayer) {
                amount = onLivingHurtByPlayer(victim, attackerPlayer, source, amount);
            }
        }

        if (victim instanceof Player victimPlayer) {
            // Victim is a Player
            amount = onPlayerHurt(victimPlayer, source, amount);

            if (attacker instanceof LivingEntity livingAttacker) {
                amount = onPlayerHurtByLiving(victimPlayer, livingAttacker, source, amount);
                if (attacker instanceof Player attackerPlayer) {
                    amount = onPlayerHurtByPlayer(victimPlayer, attackerPlayer, source, amount);
                }
            }
        }
        event.setAmount(amount);
    }

    // --- UNIVERSAL ---

    private static float onLivingHurt(LivingHurtEvent event, DamageSource source, LivingEntity victim, float amount) {
        amount = AttributeCalculationHelper.handleDamageTaken(amount, victim);
        HolyMantleSpell.processHurt(event, victim, amount);
        handleArmorSealsHurt(victim, source, amount);
        amount = Blockchained.handleHurt(victim, amount);
        amount = applyOnWearerHurt(victim, source, amount);
        return amount;
    }


    // --- 2. EVERYTIME A PLAYER GETS HURT ---
    private static float onPlayerHurt(Player victim, DamageSource source, float amount) {
        amount = SadEffect.processHurt(victim, amount);
        amount = RoyalGuardianSword.processHurt(victim, amount);
        RageHelper.processDamageDealtAndTaken(victim, amount); // Victim gains rage from any damage
        return amount;
    }

    // --- 3. EVERYTIME A MOB GETS HURT FROM ANOTHER MOB ---
    private static float onLivingHurtByLiving(LivingEntity victim, LivingEntity attacker, DamageSource source, float amount) {
        amount = applyOnWearerHit(attacker, victim, source, amount);
        handleArmorSealsHurtByEntity(victim, attacker, source, amount);
        return amount;
    }

    // --- 4. EVERYTIME A MOB GETS HURT BY A PLAYER ---
    private static float onLivingHurtByPlayer(LivingEntity victim, Player attacker, DamageSource source, float amount) {
        AttributeCalculationHelper.handleOmnivamp(amount, attacker);
        RageHelper.processDamageDealtAndTaken(attacker, amount); // Attacker gains rage from dealing damage
        return amount;
    }

    // --- 5. EVERYTIME A PLAYER GETS HURT BY ANOTHER MOB ---
    private static float onPlayerHurtByLiving(Player victim, LivingEntity attacker, DamageSource source, float amount) {
        return amount;
    }

    // --- 6. EVERYTIME A PLAYER GETS HURT BY ANOTHER PLAYER ---
    private static float onPlayerHurtByPlayer(Player victim, Player attacker, DamageSource source, float amount) {
        return amount;
    }

    // --- REUSABLE HELPERS ---

    private static float applyOnWearerHurt(LivingEntity victim, DamageSource source, float amount) {
        float current = amount;
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(victim).resolve();
        if (opt.isPresent()) {
            for (SlotResult result : opt.get().findCurios(s -> s.getItem() instanceof BrutalityCurioItem)) {
                current = ((BrutalityCurioItem) result.stack().getItem()).onWearerHurt(victim, result.stack(), source, current);
            }
        }
        return current;
    }

    private static float applyOnWearerHit(LivingEntity attacker, LivingEntity victim, DamageSource source, float amount) {
        float current = amount;
        Optional<ICuriosItemHandler> opt = CuriosApi.getCuriosInventory(attacker).resolve();
        if (opt.isPresent()) {
            for (SlotResult result : opt.get().findCurios(s -> s.getItem() instanceof BrutalityCurioItem)) {
                current = ((BrutalityCurioItem) result.stack().getItem()).onWearerHit(attacker, result.stack(), victim, source, current);
            }
        }
        return current;
    }

    private static void handleArmorSealsHurtByEntity(LivingEntity victim, LivingEntity attacker, DamageSource source, float amount) {
        victim.getArmorSlots().forEach(armor -> AugmentHelper.getAugmentCounts(armor).forEach((brutalityAugmentItem, integer) -> {
            if (brutalityAugmentItem instanceof BrutalitySealAugmentItem augmentItem) {
                augmentItem.onHurtByEntity(attacker, victim, source, amount, integer);
            }
        }));
    }

    private static void handleArmorSealsHurt(LivingEntity victim, DamageSource source, float amount) {
        victim.getArmorSlots().forEach(armor -> AugmentHelper.getAugmentCounts(armor).forEach((brutalityAugmentItem, integer) -> {
            if (brutalityAugmentItem instanceof BrutalitySealAugmentItem augmentItem) {
                augmentItem.onHurt(victim, source, amount, integer);
            }
        }));
    }
}