package net.goo.brutality.client.renderers.shaders.outline;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.goo.brutality.Brutality;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class MaxSwordOutlineShader extends OutlineShader {

    public static final Object2LongOpenHashMap<UUID> START_TIMES = new Object2LongOpenHashMap<>();
    public static void startAttack(Player player) {
        START_TIMES.put(player.getUUID(), player.level().getGameTime());
    }

    @Override
    public ResourceLocation getShaderLocation() {
        return ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "shaders/post/max_sword_item_outline_post.json");
    }

    @Override
    public void setUniforms(PostPass instance) {
        super.setUniforms(instance); // gets all the base uniforms
        instance.getEffect().setSampler("OutlineDepthSampler", getSilhouetteTarget()::getDepthTextureId);
    }
}
