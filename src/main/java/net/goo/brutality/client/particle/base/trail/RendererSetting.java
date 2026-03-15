

package net.goo.brutality.client.particle.base.trail;


import com.lowdragmc.photon.client.gameobject.emitter.data.ToggleGroup;
import com.lowdragmc.photon.client.gameobject.particle.TileParticle;
import net.minecraft.client.Camera;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.function.TriFunction;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class RendererSetting {

    protected Layer layer;

    protected boolean bloomEffect;

    protected int bloomColor;

    protected final Cull cull;

    public RendererSetting() {
        this.layer = Layer.TRANSLUCENT;
        this.bloomEffect = false;
        this.bloomColor = -1;
        this.cull = new Cull();
    }

    public Layer getLayer() {
        return this.layer;
    }

    public boolean isBloomEffect() {
        return this.bloomEffect;
    }

    public Cull getCull() {
        return this.cull;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public void setBloomEffect(boolean bloomEffect) {
        this.bloomEffect = bloomEffect;
    }

    public void setBloomColor(int bloomColor) {
        this.bloomColor = bloomColor;
    }

    public int getBloomColor() {
        return this.bloomColor;
    }

    public enum Layer {
        OPAQUE, TRANSLUCENT
    }

    public static class Cull extends ToggleGroup {

        protected Vector3f from = new Vector3f(-0.5F, -0.5F, -0.5F);
        protected Vector3f to = new Vector3f(0.5F, 0.5F, 0.5F);

        public AABB getCullAABB(Emitter particle, float partialTicks) {
            Vector3f pos = particle.transform().position();
            return (new AABB(this.from.x, this.from.y, this.from.z, this.to.x, this.to.y, this.to.z)).move(pos.x, pos.y, pos.z);
        }

        public void setFrom(Vector3f from) {
            this.from = from;
        }

        public Vector3f getFrom() {
            return this.from;
        }

        public void setTo(Vector3f to) {
            this.to = to;
        }

        public Vector3f getTo() {
            return this.to;
        }
    }

    public static class Particle extends RendererSetting {
        protected Mode renderMode;
        protected boolean shade;
        protected boolean useBlockUV;

        public Particle() {
            this.renderMode = RendererSetting.Particle.Mode.Billboard;
            this.shade = true;
            this.useBlockUV = true;
        }



        public Mode getRenderMode() {
            return this.renderMode;
        }

        public boolean isShade() {
            return this.shade;
        }

        public boolean isUseBlockUV() {
            return this.useBlockUV;
        }

        public void setRenderMode(Mode renderMode) {
            this.renderMode = renderMode;
        }


        public void setShade(boolean shade) {
            this.shade = shade;
        }

        public void setUseBlockUV(boolean useBlockUV) {
            this.useBlockUV = useBlockUV;
        }

        public static enum Mode {
            Billboard((p, c, t) -> c.rotation()),
            Horizontal(0.0F, 90.0F),
            Vertical(0.0F, 0.0F),
            VerticalBillboard((p, c, t) -> {
                Quaternionf quaternion = new Quaternionf();
                quaternion.rotateY((float)Math.toRadians(-c.getYRot()));
                return quaternion;
            }),
            Model((p, c, t) -> new Quaternionf());

            public final TriFunction<TileParticle, Camera, Float, Quaternionf> quaternion;

            Mode(TriFunction<TileParticle, Camera, Float, Quaternionf> quaternion) {
                this.quaternion = quaternion;
            }

            Mode(Quaternionf quaternion) {
                this.quaternion = (p, c, t) -> quaternion;
            }

            Mode(float yRot, float xRot) {
                Quaternionf quaternion = new Quaternionf();
                quaternion.rotateY((float)Math.toRadians(-yRot));
                quaternion.rotateX((float)Math.toRadians(xRot));
                this.quaternion = (p, c, t) -> quaternion;
            }
        }
    }
}
