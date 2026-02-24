package net.goo.brutality.client.renderers.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Error404BakedModel implements net.minecraftforge.client.model.IDynamicBakedModel {
    private final BakedModel original;

    public Error404BakedModel(BakedModel original) {
        this.original = original;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, net.minecraftforge.client.model.data.ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> originalQuads = original.getQuads(state, side, rand, data, renderType);
        List<BakedQuad> modifiedQuads = new ArrayList<>();

        TextureAtlasSprite missing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(MissingTextureAtlasSprite.getLocation());

        for (BakedQuad quad : originalQuads) {
            modifiedQuads.add(remapQuad(quad, missing));
        }
        return modifiedQuads;
    }

    private BakedQuad remapQuad(BakedQuad quad, TextureAtlasSprite newSprite) {
        int[] vertices = quad.getVertices().clone(); // Clone so we don't break the original model
        for (int i = 0; i < 4; i++) {
            int offset = i * 8;
            // Map the 4 vertices to the 4 corners of the missing texture
            vertices[offset + 4] = Float.floatToRawIntBits((i == 0 || i == 3) ? newSprite.getU0() : newSprite.getU1());
            vertices[offset + 5] = Float.floatToRawIntBits((i == 0 || i == 1) ? newSprite.getV0() : newSprite.getV1());
        }
        return new BakedQuad(vertices, quad.getTintIndex(), quad.getDirection(), newSprite, quad.isShade());
    }

    // Delegate all other methods to original
    @Override public boolean useAmbientOcclusion() { return original.useAmbientOcclusion(); }
    @Override public boolean isGui3d() { return original.isGui3d(); }
    @Override public boolean usesBlockLight() { return original.usesBlockLight(); }
    @Override public boolean isCustomRenderer() { return original.isCustomRenderer(); }
    @Override public TextureAtlasSprite getParticleIcon() { return original.getParticleIcon(); }
    @Override public ItemTransforms getTransforms() { return original.getTransforms(); }
    @Override public ItemOverrides getOverrides() { return ItemOverrides.EMPTY; }
}