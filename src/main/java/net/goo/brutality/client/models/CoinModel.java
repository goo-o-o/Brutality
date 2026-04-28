package net.goo.brutality.client.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.CompositeModel;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CoinModel implements IUnbakedGeometry<CoinModel> {

    private static final Logger LOGGER = LogUtils.getLogger();
    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker,
                           Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelState, ItemOverrides overrides,
                           ResourceLocation modelLocation) {


        LOGGER.info("CoinModel.bake called for: {}", modelLocation);
        LOGGER.info("Context transforms: {}", context.getTransforms());
        LOGGER.info("Use ambient occlusion: {}", context.useAmbientOcclusion());
        LOGGER.info("Is gui 3d: {}", context.isGui3d());
        LOGGER.info("Use block light: {}", context.useBlockLight());

        try {
            if (!context.hasMaterial("layer0") || !context.hasMaterial("layer1")) {
                LOGGER.error("Missing 'front' or 'back' texture!");
                return null;
            }

            Material frontMaterial = context.getMaterial("layer0");
            Material backMaterial = context.getMaterial("layer1");
            Material particleMaterial = context.hasMaterial("particle") ?
                    context.getMaterial("particle") : frontMaterial;

            TextureAtlasSprite frontSprite = spriteGetter.apply(frontMaterial);
            TextureAtlasSprite backSprite = spriteGetter.apply(backMaterial);
            TextureAtlasSprite particle = spriteGetter.apply(particleMaterial);

            var rootTransform = context.getRootTransform();
            if (!rootTransform.isIdentity())
                modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, rootTransform);

            var normalRenderTypes = new RenderTypeGroup(
                    RenderType.translucent(),
                    ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get()
            );

            CompositeModel.Baked.Builder builder = CompositeModel.Baked.builder(
                    context, particle, overrides, context.getTransforms()
            );

            // Generate elements for front sprite (will use for SOUTH and edge faces)
            var frontElements = UnbakedGeometryHelper.createUnbakedItemElements(
                    0, frontSprite.contents(), null
            );

            // Generate elements for back sprite (will use for NORTH faces only)
            var backElements = UnbakedGeometryHelper.createUnbakedItemElements(
                    0, backSprite.contents(), null
            );

            // Remove NORTH faces from front elements (keep SOUTH + edges)
            for (BlockElement element : frontElements) {
                element.faces.remove(Direction.NORTH);
            }

            // Remove everything except NORTH faces from back elements
            for (BlockElement element : backElements) {
                element.faces.keySet().removeIf(dir -> dir != Direction.NORTH);
            }

            // Bake both sets
            var frontQuads = UnbakedGeometryHelper.bakeElements(
                    frontElements, $ -> frontSprite, modelState, modelLocation
            );
            var backQuads = UnbakedGeometryHelper.bakeElements(
                    backElements, $ -> backSprite, modelState, modelLocation
            );


            List<BakedQuad> allQuads = new ArrayList<>();
            allQuads.addAll(frontQuads);
            allQuads.addAll(backQuads);

            builder.addQuads(normalRenderTypes, RenderTypeGroup.EMPTY, allQuads);

            return builder.build();
        } catch (Exception e) {
            return null;
        }
    }

    public static final class Loader implements IGeometryLoader<CoinModel> {
        public static final Loader INSTANCE = new Loader();
        private static final Logger LOGGER = LogUtils.getLogger();

        @Override
        public CoinModel read(JsonObject jsonObject, JsonDeserializationContext context) {
            LOGGER.info("CoinModel.Loader.read called with JSON: {}", jsonObject);
            return new CoinModel();
        }
    }
}