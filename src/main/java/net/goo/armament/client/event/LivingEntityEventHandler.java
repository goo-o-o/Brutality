package net.goo.armament.client.event;

import net.goo.armament.Armament;
import net.goo.armament.item.noir.ShadowstepSword;
import net.goo.armament.item.weapon.custom.MurasamaSword;
import net.goo.armament.registry.ModEffects;
import net.goo.armament.util.ModUtils;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class LivingEntityEventHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();
        Entity victim = event.getEntity();

        if (event.getEntity().getLastAttacker() instanceof Player player) {
            if (player.getMainHandItem().getItem() instanceof ShadowstepSword) {
                if (ModUtils.isPlayerBehind(player, event.getEntity(), 30)) {
                    event.setAmount(event.getAmount() * 5);
                }
            }
        }

        if (source.is(DamageTypes.PLAYER_ATTACK) && attacker instanceof Player player) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof MurasamaSword) {
                event.getEntity().invulnerableTime = 0;
                event.getEntity().hurt(
                        event.getEntity().damageSources().indirectMagic(player, player),
                        event.getAmount() // Deal full base damage
                );
                event.setCanceled(true); // Cancel armor-reduced damage
            }
        }

        if (victim instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(ModEffects.STONEFORM.get())) {
                int amp = Objects.requireNonNull(livingEntity.getEffect(ModEffects.STONEFORM.get())).getAmplifier();
                event.setAmount((float) (event.getAmount() * (1 - 0.1 * amp)));
            }
        }
    }
}
