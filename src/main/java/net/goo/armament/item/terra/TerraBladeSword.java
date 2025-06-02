package net.goo.armament.item.terra;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.beam.TerraBeam;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.network.PacketHandler;
import net.goo.armament.network.c2sSwordBeamPacket;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.registry.ModSounds;
import net.goo.armament.util.helpers.ProjectileHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID)
public class TerraBladeSword extends ArmaSwordItem {

    public TerraBladeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.level().isClientSide && !BETTER_COMBAT_LOADED) {
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
//        if (!player.getCooldowns().isOnCooldown(item)) {
            level.playSound(player, player.getOnPos(), ModSounds.TERRA_BLADE_USE.get(), SoundSource.PLAYERS);
//            player.getCooldowns().addCooldown(item, 20);
            if (level.isClientSide()) {
                PacketHandler.sendToServer(new c2sSwordBeamPacket(BEAM_TYPES.TERRA));
            } else {
                ProjectileHelper.shootProjectile(() -> new TerraBeam(ModEntities.TERRA_BEAM.get(), level), player, level, 3.5F);
            }
//        }
    }


}
