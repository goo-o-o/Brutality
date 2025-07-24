//package net.goo.brutality.item.weapon.sword;
//
//import net.goo.brutality.Brutality;
//import net.goo.brutality.item.base.BrutalitySwordItem;
//import net.goo.brutality.registry.BrutalityModItems;
//import net.goo.brutality.util.helpers.BrutalityTooltipHelper;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.Rarity;
//import net.minecraft.world.item.Tier;
//import net.minecraftforge.event.entity.player.PlayerXpEvent;
//import net.minecraftforge.eventbus.api.EventPriority;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//
//import java.util.List;
//
//@Mod.EventBusSubscriber(modid = Brutality.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
//public class TruthseekerSword extends BrutalitySwordItem {
//
//
//    public TruthseekerSword(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Rarity rarity, List<BrutalityTooltipHelper.DescriptionComponent> descriptionComponents) {
//        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, rarity, descriptionComponents);
//    }
//
//    @Override
//    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//
//    }
//
////    @Override
////    public <T extends Item & BrutalityGeoItem> void configureLayers(BrutalityItemRenderer<T> renderer) {
////        super.configureLayers(renderer);
////        renderer.addRenderLayer(new AutoGlowingGeoLayer<>(renderer));
////    }
//
//    @SubscribeEvent(priority = EventPriority.NORMAL)
//    public static void onXPChange(PlayerXpEvent.XpChange event) {
//        Player player = event.getEntity();
//
//        final float EXPERIENCE_BOOST = 2.0F;
//        if (player.getMainHandItem().getItem() == BrutalityModItems.TRUTHSEEKER_SWORD.get()) {
//            int originalXP = event.getAmount();
//            int boostedXP = (int) (originalXP * EXPERIENCE_BOOST);
//            event.setAmount(boostedXP);
//        }
//    }
//
//}
