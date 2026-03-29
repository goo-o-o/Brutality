package net.goo.brutality.client.renderers.shaders;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.NotNull;
public class FixedColorVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;
    private final int r, g, b, a;

    public FixedColorVertexConsumer(VertexConsumer delegate, int argb) {
        this.delegate = delegate;
        this.a = FastColor.ARGB32.alpha(argb);
        this.r = FastColor.ARGB32.red(argb);
        this.g = FastColor.ARGB32.green(argb);
        this.b = FastColor.ARGB32.blue(argb);
        // embeddium incompat
        //        delegate.defaultColor(r, g, b, a);
    }

    @Override
    public @NotNull VertexConsumer color(int red, int green, int blue, int alpha) {
        return delegate.color(r, g, b, a);
    }

    @Override
    public @NotNull VertexConsumer color(int argb) {
        return delegate.color(r, g, b, a);
    }

    @Override public @NotNull VertexConsumer vertex(double x, double y, double z) { return delegate.vertex(x, y, z); }
    @Override public @NotNull VertexConsumer uv(float u, float v) { return delegate.uv(u, v); }
    @Override public @NotNull VertexConsumer overlayCoords(int u, int v) { return delegate.overlayCoords(u, v); }
    @Override public @NotNull VertexConsumer uv2(int u, int v) { return delegate.uv2(u, v); }
    @Override public @NotNull VertexConsumer normal(float x, float y, float z) { return delegate.normal(x, y, z); }
    @Override public void endVertex() { delegate.endVertex(); }
    @Override public void defaultColor(int r, int g, int b, int a) { /* ignore — we set our own */ }
    @Override public void unsetDefaultColor() { /* ignore */ }
}