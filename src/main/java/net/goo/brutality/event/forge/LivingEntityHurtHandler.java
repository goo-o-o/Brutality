package net.goo.brutality.event.forge;

import net.goo.brutality.Brutality;
import net.goo.brutality.common.item.curios.BrutalityCurioItem;
import net.goo.brutality.common.magic.spells.celestia.HolyMantleSpell;
import net.goo.brutality.common.mob_effect.SadEffect;
import net.goo.brutality.util.build_archetypes.RageHelper;
import net.goo.brutality.util.attribute.AttributeCalculationHelper;
import net.goo.brutality.util.item.SealUtils;
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

        // 1. UNIVERSAL PRE-PROCESSING
        // Applies to everything (Attributes, Holy Mantle)
        amount = handleUniversalPreProcessing(event, victim, amount);

        // 2. IDENTITY-BASED DISPATCHING
        if (victim instanceof Player victimPlayer) {
            // Victim is a Player
            amount = handleEverytimePlayerHurt(victimPlayer, source, amount);

            if (attacker instanceof Player attackerPlayer) {
                amount = handleEverytimePlayerHurtByPlayer(victimPlayer, attackerPlayer, source, amount);
            } else if (attacker instanceof LivingEntity livingAttacker) {
                amount = handleEverytimePlayerHurtByMob(victimPlayer, livingAttacker, source, amount);
            }
        } else {
            // Victim is a Mob
            amount = handleEverytimeMobHurt(victim, source, amount);

            if (attacker instanceof Player attackerPlayer) {
                amount = handleEverytimeMobHurtByPlayer(victim, attackerPlayer, source, amount);
            } else if (attacker instanceof LivingEntity livingAttacker) {
                amount = handleEverytimeMobHurtByMob(victim, livingAttacker, source, amount);
            }
        }

        event.setAmount(amount);
    }

    // --- UNIVERSAL ---

    private static float handleUniversalPreProcessing(LivingHurtEvent event, LivingEntity victim, float amount) {
        amount = AttributeCalculationHelper.handleDamageTaken(amount, victim);
        HolyMantleSpell.processHurt(event, victim, amount);
        return amount;
    }

    // --- 1. EVERYTIME A MOB GETS HURT ---
    private static float handleEverytimeMobHurt(LivingEntity victim, DamageSource source, float amount) {
        return applyOnWearerHurt(victim, source, amount);
    }

    // --- 2. EVERYTIME A PLAYER GETS HURT ---
    private static float handleEverytimePlayerHurt(Player victim, DamageSource source, float amount) {
        amount = SadEffect.processHurt(victim, amount);
        amount = applyOnWearerHurt(victim, source, amount);
        RageHelper.processDamage(victim, amount); // Victim gains rage from any damage
        return amount;
    }

    // --- 3. EVERYTIME A MOB GETS HURT FROM ANOTHER MOB ---
    private static float handleEverytimeMobHurtByMob(LivingEntity victim, LivingEntity attacker, DamageSource source, float amount) {
        amount = applyOnWearerHit(attacker, victim, source, amount);
        handleArmorSeals(victim, attacker);
        return amount;
    }

    // --- 4. EVERYTIME A MOB GETS HURT BY A PLAYER ---
    private static float handleEverytimeMobHurtByPlayer(LivingEntity victim, Player attacker, DamageSource source, float amount) {
        amount = applyOnWearerHit(attacker, victim, source, amount);
        AttributeCalculationHelper.handleOmnivamp(amount, attacker);
        RageHelper.processDamage(attacker, amount); // Attacker gains rage from dealing damage
        handleArmorSeals(victim, attacker);
        return amount;
    }

    // --- 5. EVERYTIME A PLAYER GETS HURT BY ANOTHER MOB ---
    private static float handleEverytimePlayerHurtByMob(Player victim, LivingEntity attacker, DamageSource source, float amount) {
        amount = applyOnWearerHit(attacker, victim, source, amount);
        handleArmorSeals(victim, attacker);
        return amount;
    }

    // --- 6. EVERYTIME A PLAYER GETS HURT BY ANOTHER PLAYER ---
    private static float handleEverytimePlayerHurtByPlayer(Player victim, Player attacker, DamageSource source, float amount) {
        amount = applyOnWearerHit(attacker, victim, source, amount);
        AttributeCalculationHelper.handleOmnivamp(amount, attacker);
        RageHelper.processDamage(attacker, amount);
        handleArmorSeals(victim, attacker);
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

    private static void handleArmorSeals(LivingEntity victim, LivingEntity attacker) {
        victim.getArmorSlots().forEach(stack -> SealUtils.handleSealProcDefensive(victim.level(), attacker, victim, SealUtils.getSealType(stack)));
    }
}