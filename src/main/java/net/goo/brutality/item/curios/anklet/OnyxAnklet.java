package net.goo.brutality.item.curios.anklet;

import net.goo.brutality.item.base.BrutalityAnkletItem;
import net.goo.brutality.util.helpers.tooltip.ItemDescriptionComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OnyxAnklet extends BrutalityAnkletItem {


    public OnyxAnklet(Rarity rarity, List<ItemDescriptionComponent> descriptionComponents) {
        super(rarity, descriptionComponents);
    }

    @Override
    public void onDodgeServer(LivingEntity dodger, DamageSource source, float damage, ItemStack stack) {
        Level level = dodger.level();
        if (dodger instanceof Player player && !player.getCooldowns().isOnCooldown(this)) {

            if (!level.isClientSide()) {
                double d0 = dodger.getX();
                double d1 = dodger.getY();
                double d2 = dodger.getZ();

                for (int i = 0; i < 16; ++i) {
                    double d3 = dodger.getX() + (dodger.getRandom().nextDouble() - 0.5D) * 16.0D;
                    double d4 = Mth.clamp(dodger.getY() + (double) (dodger.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMinBuildHeight() + ((ServerLevel) level).getLogicalHeight() - 1);
                    double d5 = dodger.getZ() + (dodger.getRandom().nextDouble() - 0.5D) * 16.0D;
                    if (dodger.isPassenger()) {
                        dodger.stopRiding();
                    }

                    Vec3 vec3 = dodger.position();
                    level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(dodger));
                    if (dodger.randomTeleport(d3, d4, d5, true)) {
                        level.playSound(null, d0, d1, d2, SoundEvents.CHORUS_FRUIT_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                        dodger.playSound(SoundEvents.CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                        player.getCooldowns().addCooldown(this, 60);
                        break;
                    }
                }

            }
        }

    }
}
