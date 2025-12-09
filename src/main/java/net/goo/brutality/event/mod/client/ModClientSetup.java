package net.goo.brutality.event.mod.client;

import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.goo.brutality.block.custom.DustbinBlock;
import net.goo.brutality.config.BrutalityClientConfig;
import net.goo.brutality.datagen.ingredients.AnySharpnessBookIngredient;
import net.goo.brutality.gui.RageMeter;
import net.goo.brutality.gui.screen.FilingCabinetScreen;
import net.goo.brutality.registry.BrutalityMenuTypes;
import net.goo.brutality.registry.BrutalityModBlocks;
import net.goo.brutality.registry.BrutalityModItems;
import net.goo.brutality.util.BetterCombatIntegration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.concurrent.CompletableFuture;

import static net.goo.brutality.Brutality.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientSetup {
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
    public static void onTextureStitchPost(TextureStitchEvent event) {
        System.out.println(event.getAtlas().location());


//        for (ModRarities.RarityData data : ModRarities.RarityData.values()) {
//            String name = data.name().toLowerCase(Locale.ROOT);
//            ResourceLocation id = new ResourceLocation(Brutality.MOD_ID, name); // → brutality:fabled
//
//            data.default_sprite = atlas.getSprite(id);
//            data.open_sprite   = atlas.getSprite(id.withSuffix("_open"));
//        }
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, reg -> {
            CraftingHelper.register(
                    AnySharpnessBookIngredient.Serializer.ID,
                    AnySharpnessBookIngredient.Serializer.INSTANCE
            );
        });
    }

    @SubscribeEvent
    public static void registerBlockColorHandlersEvent(RegisterColorHandlersEvent.Block event) {
        event.register((state, world, pos, tintIndex) -> (world != null && pos != null)
                ? BiomeColors.getAverageWaterColor(world, pos) : 4159204, BrutalityModBlocks.PUDDLE.get());
    }

    @SubscribeEvent
    public static void registerItemColorHandlersEvent(RegisterColorHandlersEvent.Item event) {
        event.register((itemStack, tintIndex) -> 4159204, BrutalityModBlocks.PUDDLE.get());
    }

    public static final ResourceLocation GUI_ATLAS = ResourceLocation.fromNamespaceAndPath(MOD_ID, "gui");

    @SubscribeEvent
    public static  void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new ReloadableResourceManager() {
            @Override
            public Identifier getFabricId() { return GUI_ATLAS; }
            @Override
            public CompletableFuture<Void> reload(Preparer preparer, ResourceManager rm, ProfilerFiller gf, ProfilerFiller gg, IStage stage, Set<Identifier> set) {
                return CompletableFuture.runAsync(() -> ((TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(GUI_ATLAS)).reload(rm));
            }
        });
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (ModList.get().isLoaded("bettercombat")) {
            BetterCombatIntegration.register();
        }


        event.enqueueWork(() -> {
            TextureManager tm = Minecraft.getInstance().getTextureManager();
            TextureAtlas atlas = new TextureAtlas(GUI_ATLAS);
            tm.register(GUI_ATLAS, atlas);  // this makes it load the json above
        });

        event.enqueueWork(DustbinBlock::bootStrap);

        event.enqueueWork(() -> ItemProperties.register(BrutalityModItems.DULL_KNIFE_DAGGER.get(), ResourceLocation.parse("texture"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getInt("texture"))));


        event.enqueueWork(() -> ItemProperties.register(BrutalityModItems.DEATHSAW.get(), ResourceLocation.parse("texture"),
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
