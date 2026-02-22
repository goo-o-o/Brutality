package net.goo.brutality.mixin.mixins;

import net.goo.brutality.client.event.forge.ForgeClientPlayerStateHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(SimpleBakedModel.class)
public class SimpleBakedModelMixin {
    @Unique
// Wrap the WeakHashMap to make it thread-safe for background chunk worker threads
    private static final Map<List<BakedQuad>, List<BakedQuad>> brutality$CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    @Unique
    private static final ThreadLocal<Boolean> brutality$IS_TRANSFORMING = ThreadLocal.withInitial(() -> false);

    @Inject(method = "getQuads", at = @At("RETURN"), cancellable = true)
    private void inject404Quads(BlockState state, Direction side, RandomSource rand, CallbackInfoReturnable<List<BakedQuad>> cir) {
        if (ForgeClientPlayerStateHandler.ERROR_404_EQUIPPED && !brutality$IS_TRANSFORMING.get()) {
            List<BakedQuad> original = cir.getReturnValue();
            if (original == null || original.isEmpty()) return;

            brutality$IS_TRANSFORMING.set(true);
            try {
                // SynchronizedMap handles the thread safety, but we still use the cache
                // We use get() first to avoid unnecessary synchronization overhead once cached
                List<BakedQuad> glitched = brutality$CACHE.get(original);

                if (glitched == null) {
                    // Not in cache, let's build it (this part will be synchronized)
                    TextureAtlasSprite missing = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                            .apply(MissingTextureAtlasSprite.getLocation());

                    List<BakedQuad> glitchedList = new ArrayList<>(original.size());

                    for (BakedQuad quad : original) {
                        if (quad.getSprite() == missing) {
                            glitchedList.add(quad);
                            continue;
                        }

                        int[] vertexData = quad.getVertices().clone();
                        for (int i = 0; i < 4; i++) {
                            int offset = i * 8;
                            float u = (i == 0 || i == 1) ? missing.getU0() : missing.getU1();
                            float v = (i == 1 || i == 2) ? missing.getV1() : missing.getV0();
                            vertexData[offset + 4] = Float.floatToRawIntBits(u);
                            vertexData[offset + 5] = Float.floatToRawIntBits(v);
                        }
                        glitchedList.add(new BakedQuad(vertexData, quad.getTintIndex(), quad.getDirection(), quad.getSprite(), quad.isShade()));
                    }
                    glitched = Collections.unmodifiableList(glitchedList);
                    brutality$CACHE.put(original, glitched);
                }

                cir.setReturnValue(glitched);
            } finally {
                brutality$IS_TRANSFORMING.set(false);
            }
        }
    }
}