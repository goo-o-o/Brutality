package net.goo.brutality.item.weapon.custom;

import net.goo.brutality.Brutality;
import net.goo.brutality.client.renderers.item.BrutalityEmissiveItemRenderer;
import net.goo.brutality.item.base.BrutalitySwordItem;
import net.goo.brutality.registry.ModItems;
import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tier;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib.core.animation.AnimatableManager;

import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TruthseekerSword extends BrutalitySwordItem {

    public TruthseekerSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, String identifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, identifier, rarity, descriptionComponents);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public <R extends BlockEntityWithoutLevelRenderer> void initGeo(Consumer<IClientItemExtensions> consumer, Class<R> rendererClass) {
        super.initGeo(consumer, BrutalityEmissiveItemRenderer.class);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onXPChange(PlayerXpEvent.XpChange event) {
        Player player = event.getEntity();

        final float EXPERIENCE_BOOST = 2.0F;
        if (player.getMainHandItem().getItem() == ModItems.TRUTHSEEKER_SWORD.get()) {
            int originalXP = event.getAmount();
            int boostedXP = (int) (originalXP * EXPERIENCE_BOOST);
            event.setAmount(boostedXP);
        }
    }

}
