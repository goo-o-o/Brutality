package net.goo.brutality.event.mod.client;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.goo.brutality.gui.screen.FilingCabinetScreen;
import net.goo.brutality.registry.BrutalityMenuTypes;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.BetterCombatIntegration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static net.goo.brutality.Brutality.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientSetup {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatIntegration.register();
        }



        event.enqueueWork(() -> ItemProperties.register(BrutalityModItems.DULL_KNIFE_DAGGER.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));
        event.enqueueWork(() -> ItemProperties.register(BrutalityModItems.DARKIN_SCYTHE.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));
        event.enqueueWork(() -> ItemProperties.register(BrutalityModItems.STYROFOAM_CUP.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));
        event.enqueueWork(() -> ItemProperties.register(BrutalityModItems.MUG.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));


        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ResourceLocation.fromNamespaceAndPath(MOD_ID, "animation"), 42,
                (player) -> new ModifierLayer<>());

        event.enqueueWork(() -> {
            ItemProperties.register(BrutalityModItems.PROVIDENCE.get(), ResourceLocation.parse("pull"), (stack, level, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                }
                return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            });

            ItemProperties.register(BrutalityModItems.PROVIDENCE.get(), ResourceLocation.parse("pulling"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

            MenuScreens.register(BrutalityMenuTypes.FILING_CABINET_MENU.get(), FilingCabinetScreen::new);
        });


    }

}
