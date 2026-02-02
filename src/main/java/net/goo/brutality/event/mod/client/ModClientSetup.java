package net.goo.brutality.event.mod.client;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.goo.brutality.common.block.custom.DustbinBlock;
import net.goo.brutality.client.config.BrutalityClientConfig;
import net.goo.brutality.client.datagen.ingredients.AnySharpnessBookIngredient;
import net.goo.brutality.client.gui.meters.CooldownMeter;
import net.goo.brutality.client.gui.meters.ManaMeter;
import net.goo.brutality.client.gui.meters.RageMeter;
import net.goo.brutality.client.gui.SpellSelectionGui;
import net.goo.brutality.client.gui.screen.FilingCabinetScreen;
import net.goo.brutality.common.registry.BrutalityMenuTypes;
import net.goo.brutality.common.registry.BrutalityBlocks;
import net.goo.brutality.common.registry.BrutalityItems;
import net.goo.brutality.util.BetterCombatIntegration;
import net.goo.brutality.util.RarityBorderManager;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import static net.goo.brutality.Brutality.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientSetup {


    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("mana_meter", new ManaMeter());
        event.registerAboveAll("rage_meter", new RageMeter());
        event.registerAboveAll("ability_cooldown_meter", new CooldownMeter.AbilityCooldownMeter());
        event.registerAboveAll("armor_set_ability_cooldown_meter", new CooldownMeter.ArmorSetAbilityCooldownMeter());
        event.registerAboveAll("spell_selector", new SpellSelectionGui());
    }


    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == BrutalityClientConfig.SPEC) {
            RageMeter.updateConfig();
        }
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == BrutalityClientConfig.SPEC) {
            RageMeter.updateConfig();
        }
    }


    @SubscribeEvent
    public static void onAddReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new RarityBorderManager());
    }


    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, reg -> CraftingHelper.register(
                AnySharpnessBookIngredient.Serializer.ID,
                AnySharpnessBookIngredient.Serializer.INSTANCE
        ));
    }

    @SubscribeEvent
    public static void registerBlockColorHandlersEvent(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> (world != null && pos != null)
                ? BiomeColors.getAverageWaterColor(world, pos) : 4159204, BrutalityBlocks.PUDDLE.get());
    }

    @SubscribeEvent
    public static void registerItemColorHandlersEvent(RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, tintIndex) -> 4159204, BrutalityBlocks.PUDDLE.get());
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatIntegration.register();
        }


        event.enqueueWork(DustbinBlock::bootStrap);

        event.enqueueWork(() -> ItemProperties.register(BrutalityItems.DULL_KNIFE_DAGGER.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));


        event.enqueueWork(() -> ItemProperties.register(BrutalityItems.DEATHSAW.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));


        event.enqueueWork(() -> ItemProperties.register(BrutalityItems.DARKIN_SCYTHE.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));


        event.enqueueWork(() -> ItemProperties.register(BrutalityItems.STYROFOAM_CUP.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));


        event.enqueueWork(() -> ItemProperties.register(BrutalityItems.MUG.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));


        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(ResourceLocation.fromNamespaceAndPath(MOD_ID, "animation"), 42,
                (player) -> new ModifierLayer<>());

        event.enqueueWork(() -> {


            ItemProperties.register(BrutalityItems.PROVIDENCE.get(), ResourceLocation.parse("pull"), (stack, level, entity, seed) -> {
                if (entity == null) {
                    return 0.0F;
                }
                return entity.getUseItem() != stack ? 0.0F : (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / 20.0F;
            });

            ItemProperties.register(BrutalityItems.PROVIDENCE.get(), ResourceLocation.parse("pulling"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);

            MenuScreens.register(BrutalityMenuTypes.FILING_CABINET_MENU.get(), FilingCabinetScreen::new);
        });


    }

}
