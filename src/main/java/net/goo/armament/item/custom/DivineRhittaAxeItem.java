package net.goo.armament.item.custom;

import net.goo.armament.client.item.renderer.DivineRhittaAxeItemRenderer;
import net.goo.armament.entity.custom.CruelSunEntity;
import net.goo.armament.item.ModItemCategories;
import net.goo.armament.registry.ModEntities;
import net.goo.armament.util.ModUtils;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

import static net.goo.armament.util.ModResources.FANTASY;

public class DivineRhittaAxeItem extends AxeItem implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    int[] color1 = new int[]{255, 253, 112};
    int[] color2 = new int[]{86, 73, 191};
    private final ModItemCategories category;

    public DivineRhittaAxeItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, ModItemCategories category) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        this.category = category;
    }

    public ModItemCategories getCategory() {
        return category;
    }


    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.divine_axe_rhitta.desc.1", false, null, color2));
        pTooltipComponents.add(Component.literal(""));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.divine_axe_rhitta.desc.2", false, null, color1));
        pTooltipComponents.add(ModUtils.tooltipHelper("item.armament.divine_axe_rhitta.desc.3", false, null, color2));

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return ModUtils.tooltipHelper("item.armament.divine_axe_rhitta", false, FANTASY, color1, color2);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private DivineRhittaAxeItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    renderer = new DivineRhittaAxeItemRenderer();
                }
                return this.renderer;
            }
        });
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
}
