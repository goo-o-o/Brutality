package net.goo.armament.item.custom;

import net.goo.armament.Armament;
import net.goo.armament.entity.custom.TerraBeam;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.base.ArmaSwordItem;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

@Mod.EventBusSubscriber(modid = Armament.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TerraBladeSword extends ArmaSwordItem {

    public TerraBladeSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
        this.colors = TERRA_BLADE_COLORS;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (entity instanceof Player player && !player.level().isClientSide) {
            Level level = player.level();
            Item item = stack.getItem();
            if (!player.getCooldowns().isOnCooldown(item)) {
                player.getCooldowns().addCooldown(item, 20);
                level.playSound(player, player.getOnPos(), ModSounds.TERRA_BLADE_USE.get(), SoundSource.PLAYERS);
                shootProjectile(() -> new TerraBeam(ModEntities.TERRA_BEAM.get(), level), player, level, 3.5F);
                stack.hurtAndBreak(1, player, pPlayer -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            }
        }
        return super.onEntitySwing(stack, entity);
    }
}
