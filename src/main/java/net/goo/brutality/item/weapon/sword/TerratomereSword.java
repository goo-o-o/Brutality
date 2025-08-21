package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sShootProjectilePacket;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID)
public class TerratomereSword extends BrutalitySwordItem {


    public TerratomereSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (!player.level().isClientSide && !ModList.get().isLoaded("bettercombat")) {
                performTerraBeam(stack, player);
            }
            player.level().playSound(null, player.getOnPos(),
                    ModUtils.getRandomSound(BrutalityModSounds.TERRATOMERE_SWING.get(), BrutalityModSounds.GENERIC_SLICE.get()), SoundSource.PLAYERS, 2, Mth.nextFloat(player.level().getRandom(), 0.6F, 1.2F));
        }
        return super.onEntitySwing(stack, entity);
    }


    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }


    public void performTerraBeam(ItemStack stack, Player player) {
        Level level = player.level();
        Item item = stack.getItem();
        if (!player.getCooldowns().isOnCooldown(item)) {
            player.getCooldowns().addCooldown(item, 5);
            if (level.isClientSide()) {
                PacketHandler.sendToServer(new c2sShootProjectilePacket(BrutalityModEntities.TERRA_BEAM.getId(), 0.45F, false, 0F, 0));
//                PacketHandler.sendToServer(new c2sParticlePacket(
//                        player.getX(), player.getY(), player.getZ(), 0.3f, 0.75f, 0.5f, 3, player.getId(), 15, 4, 0));
//                PacketHandler.sendToServer(new c2sParticlePacket(player.getX(), player.getY() + player.getBbHeight() / 2, player.getZ(), 2, 2, 2,
//                        new TrailParticleData(BrutalityModParticles.TERRATOMERE_PARTICLE.get(), Mth.nextFloat(level.random, 0.25f, 0.75F), 1F,
//                                Mth.nextFloat(level.random, 0.25f, 0.75F), 1F, 1.5F, player.getId(), 15), 4, 0));
//                PacketHandler.sendToServer(new c2sParticlePacket(player.getX(), player.getY(), player.getZ(), 0.5, 0.5 ,0.5, ParticleTypes.CLOUD, 10, 10));
//                PacketHandler.sendToServer(new c2sParticlePacket());

            } else {
//                ProjectileHelper.shootProjectile(() -> new TerraBeam(BrutalityModEntities.TERRA_BEAM.get(), level), player, level, 0.5F, false, 0F, 0);
//                ((ServerLevel) player.level()).sendParticles(new TrailParticleData(BrutalityModParticles.TERRATOMERE_PARTICLE.get(), 0.3F, 0.75F, 0.5F, 1F, 3, player.getId(), 15)
//                        , player.getX(), player.getY() + player.getBbHeight() / 2 ,  player.getZ(), 5, 2, 2, 2, 0);
            }
        }
    }


}
