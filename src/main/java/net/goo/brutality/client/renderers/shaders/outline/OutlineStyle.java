package net.goo.brutality.client.renderers.shaders.outline;

import net.goo.brutality.client.renderers.shaders.PostShaderInstance;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public abstract class OutlineStyle {


    public PostShaderInstance shader;

    public final boolean shouldRender(ItemStack stack, @Nullable Player player) {
        return FastColor.ARGB32.alpha(getColor(stack,player)) > 0;
    }

    public final int defaultThickness;

    public OutlineStyle(int defaultThickness, PostShaderInstance shader) {
        this.defaultThickness = defaultThickness;
        this.shader = shader;
    }

    // Returns packed ARGB — alpha 0 means don't render
    public abstract int getColor(ItemStack stack, @Nullable Player player);

    // Optional: override thickness per-frame too
    public int getThickness(ItemStack stack, @Nullable Player player) {
        return defaultThickness;
    }
}
