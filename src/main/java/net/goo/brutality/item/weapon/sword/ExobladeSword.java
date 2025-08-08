package net.goo.brutality.item.weapon.sword;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.projectile.trident.ExobladeBeam;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sShootProjectilePacket;
import net.goo.brutality.registry.BrutalityModEntities;
import net.goo.brutality.registry.BrutalityModSounds;
import net.goo.brutality.util.ModUtils;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.ProjectileHelper;
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
public class ExobladeSword extends BrutalitySwordItem {


    public ExobladeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.ItemDescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.level().isClientSide && !ModList.get().isLoaded("bettercombat")) {
            performExobladeBeam(stack, player);
        }
        entity.level().playSound(null, entity.blockPosition(), ModUtils.getRandomSound(BrutalityModSounds.EXOBLADE), SoundSource.PLAYERS, 1, Mth.nextFloat(entity.level().getRandom(), 0.5F, 1.25F));

        return super.onEntitySwing(stack, entity);
    }


    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public void performExobladeBeam(ItemStack stack, Player player) {
        Level level = player.level();
        Item item = stack.getItem();
        if (!player.getCooldowns().isOnCooldown(item)) {
            player.getCooldowns().addCooldown(item, 5);
            if (level.isClientSide()) {
                for (int i = 0; i < 4; i++)
                    PacketHandler.sendToServer(new c2sShootProjectilePacket(BrutalityModEntities.EXOBLADE_BEAM.getId(), 2F, true, 1.5F, 0));
            } else {
                for (int i = 0; i < 4; i++)
                    ProjectileHelper.shootProjectile(() -> new ExobladeBeam(BrutalityModEntities.EXOBLADE_BEAM.get(), level), player, level, 2F, true, 1.5F, 0);
            }
        }
    }


}
