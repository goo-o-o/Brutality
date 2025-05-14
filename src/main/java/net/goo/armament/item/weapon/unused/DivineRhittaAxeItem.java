package net.goo.armament.item.weapon.unused;

import net.goo.armament.entity.custom.CruelSunEntity;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.item.weapon.base.ArmaAxeItem;
import net.goo.armament.registry.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.core.animation.AnimatableManager;

public class DivineRhittaAxeItem extends ArmaAxeItem {

    public DivineRhittaAxeItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, String identifier, ModItemCategories category, Rarity rarity, int abilityCount) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties, identifier, category, rarity, abilityCount);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide && pUsedHand == InteractionHand.MAIN_HAND) {
            CruelSunEntity cruelSun = ModEntities.CRUEL_SUN_ENTITY.get().create(pLevel);
            pPlayer.getCooldowns().addCooldown(this, 400);
            if (cruelSun != null) {
                cruelSun.setPos(pPlayer.getX(), pPlayer.getY() + 5, pPlayer.getZ());
                pLevel.addFreshEntity(cruelSun);
            }
        }

        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }
}
