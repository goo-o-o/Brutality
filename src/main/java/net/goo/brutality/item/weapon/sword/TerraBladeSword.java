package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.entity.custom.projectile.beam.TerraBeam;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.network.PacketHandler;
import net.goo.brutality.network.c2sShootProjectilePacket;
import net.goo.brutality.registry.ModEntities;
import net.goo.brutality.registry.ModSounds;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.goo.brutality.util.helpers.ProjectileHelper;
import net.minecraft.sounds.SoundSource;
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
public class TerraBladeSword extends BrutalitySwordItem {


    public TerraBladeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.level().isClientSide && !ModList.get().isLoaded("bettercombat")) {
            performTerraBeam(stack, player);
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
            level.playSound(player, player.getOnPos(), ModSounds.TERRA_BLADE_USE.get(), SoundSource.PLAYERS);
            player.getCooldowns().addCooldown(item, 5);
            if (level.isClientSide()) {
                PacketHandler.sendToServer(new c2sShootProjectilePacket(ModEntities.TERRA_BEAM.getId(), 2.5F, false, 0F));
            } else {
                ProjectileHelper.shootProjectile(() -> new TerraBeam(ModEntities.TERRA_BEAM.get(), level), player, level, 3.5F, false, 0F);
            }
        }
    }


}
