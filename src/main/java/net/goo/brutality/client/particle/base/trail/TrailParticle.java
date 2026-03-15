//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.client.particle.base.trail;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import net.goo.brutality.client.renderers.BrutalityPhotonParticleRenderType;
import net.goo.brutality.util.particle.BrutalityShaders;
import net.goo.brutality.util.particle.ShaderProgram;
import net.goo.brutality.util.particle.ShaderSSBO;
import net.goo.brutality.util.particle.Shaders;
import net.goo.brutality.util.render.PhotonShaderUtils;
import net.minecraft.client.Camera;
import net.minecraft.core.BlockPos;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;


@OnlyIn(Dist.CLIENT)
public class TrailParticle implements IParticle {
    @Nullable
    private static FloatBuffer inputBuffer;
    @Nullable
    private static ShaderSSBO inputSSBO;
    @Nullable
    private static ShaderSSBO outputSSBO;
    private static Integer atomicCounterBuffer;
    protected float r = 1.0F;
    protected float g = 1.0F;
    protected float b = 1.0F;
    protected float a = 1.0F;
    protected float ro = 1.0F;
    protected float go = 1.0F;
    protected float bo = 1.0F;
    protected float ao = 1.0F;
    protected int light = -1;
    protected boolean dieWhenAllTailsRemoved = true;
    protected int delay;
    protected boolean isRemoved;
    protected Runnable onUpdate;
    protected Float2ObjectFunction<Vector3f> headPositionSupplier;
    protected Supplier<Float> lifetimeSupplier;
    protected Supplier<Float> widthMultiplier;
    protected Float2ObjectFunction<Vector4f> colorMultiplier;
    protected TailArray rawTails = new TailArray(this);
    protected TailArray tails = new TailArray(this);
    protected TrailConfig config;
    protected IParticleEmitter emitter;
    protected ConcurrentHashMap<Object, Float> memRandom = new ConcurrentHashMap<>();
    public RandomSource randomSource;

    public TrailParticle(IParticleEmitter emitter, TrailConfig config, RandomSource randomSource) {
        this.emitter = emitter;
        this.config = config;
        this.randomSource = randomSource;
        this.headPositionSupplier = (t) -> emitter.transform().position();
        this.setup();
    }

    public void setup() {
        this.setDelay(this.config.getStartDelay());
        this.lifetimeSupplier = () -> (float)this.config.getTime();
        this.update();
        this.updateOrigin();
        this.tails.clear();
        this.rawTails.clear();
    }

    public BrutalityPhotonParticleRenderType getRenderType() {
        return this.config.particleRenderType;
    }

    public boolean isAlive() {
        if (!this.isRemoved) {
            return true;
        } else {
            return this.dieWhenAllTailsRemoved && !this.tails.isEmpty();
        }
    }

    public float getT() {
        return this.emitter.getT();
    }

    public float getT(float partialTicks) {
        return this.emitter.getT(partialTicks);
    }

    public float getMemRandom(Object object) {
        return this.getMemRandom(object, RandomSource::nextFloat);
    }

    public float getMemRandom(Object object, Function<RandomSource, Float> randomFunc) {
        Float value = (Float)this.memRandom.get(object);
        return value == null ? (Float)this.memRandom.computeIfAbsent(object, (o) -> (Float)randomFunc.apply(this.randomSource)) : value;
    }

    public Vector4f getRealColor(float partialTicks) {
        Vector4f emitterColor = this.emitter.getRGBAColor();
        float a = Mth.lerp(partialTicks, this.ao, this.a);
        float r = Mth.lerp(partialTicks, this.ro, this.r);
        float g = Mth.lerp(partialTicks, this.go, this.g);
        float b = Mth.lerp(partialTicks, this.bo, this.b);
        if (this.colorMultiplier != null) {
            Vector4f color = this.colorMultiplier.get(partialTicks);
            r *= color.x();
            g *= color.y();
            b *= color.z();
            a *= color.w();
        }

        return emitterColor.mul(r, g, b, a);
    }

    public int getRealLight(float partialTicks) {
        if (this.config.renderer.isBloomEffect()) {
            return 15728880;
        } else {
            return this.config.lights.isEnable() ? this.config.lights.getLight(this, partialTicks) : this.light;
        }
    }

    public int getLightColor() {
        Vector3f pos = this.getHeadPosition();
        BlockPos blockPos = new BlockPos((int)pos.x, (int)pos.y, (int)pos.z);
        return this.emitter.getLightColor(blockPos);
    }

    public Vector3f getHeadPosition() {
        return this.getHeadPosition(0.0F);
    }

    public Vector3f getHeadPosition(float partialTicks) {
        return this.headPositionSupplier.get(partialTicks);
    }

    public void tick() {
        if (this.delay > 0) {
            --this.delay;
        } else {
            this.updateOrigin();
            this.update();
        }
    }

    protected void updateOrigin() {
        this.ro = this.r;
        this.go = this.g;
        this.bo = this.b;
        this.ao = this.a;
    }

    protected void update() {
        this.updateChanges();
        if (this.onUpdate != null) {
            this.onUpdate.run();
        }

    }

    protected void updateChanges() {
        boolean changed = this.updateTails();
        if (changed) {
            this.updateRawTailsProperties();
            if (this.config.isSmoothInterpolation()) {
                this.tails = this.generateSmoothPath(this.rawTails, this.config.getMinVertexDistance());
            } else {
                this.tails = this.rawTails;
            }
        }

        this.updateLight();
    }

    protected boolean updateTails() {
        boolean tailChanged = false;

        for(int i = 0; i < this.rawTails.size(); ++i) {
            this.rawTails.lifeTime[i]--;
        }

        TailArray newRawTails = new TailArray(this);

        for(int i = 0; i < this.rawTails.size(); ++i) {
            if (this.rawTails.lifeTime[i] > 0.0F) {
                this.rawTails.copyTailTo(newRawTails, i);
            } else {
                tailChanged = true;
            }
        }

        this.rawTails = newRawTails;
        if (!this.isRemoved()) {
            Vector3f headPos = this.getHeadPosition();
            boolean shouldAdd = true;
            if (!this.rawTails.isEmpty()) {
                Vector3f last = this.rawTails.getPosition(this.rawTails.size() - 1);
                if (headPos.distanceSquared(last) < this.config.getMinVertexDistance() * this.config.getMinVertexDistance()) {
                    shouldAdd = false;
                }
            }

            if (shouldAdd) {
                Tail newTail = new Tail(headPos, (Float)this.lifetimeSupplier.get());
                this.rawTails.add(newTail);
                tailChanged = true;
            }
        }

        return tailChanged;
    }

    public TailArray generateSmoothPath(TailArray vertices, float distance) {
        if (vertices.size() <= 2) {
            return vertices;
        } else {
            TailArray smoothPath = new TailArray(this);
            float minDistanceSq = Math.max(distance, 0.05F) * Math.max(distance, 0.05F);
            Vector3f prevDir = null;

            for(int i = 0; i < vertices.size() - 1; ++i) {
                Vector3f curr = vertices.getPosition(i);
                Vector3f next = vertices.getPosition(i + 1);
                Vector3f dir = (new Vector3f(next)).sub(curr).normalize();
                smoothPath.add(vertices.copyAsTail(i));
                if (prevDir == null || !(dir.dot(prevDir) > 0.99F)) {
                    float distSq = curr.distanceSquared(next);
                    if (distSq > minDistanceSq) {
                        int steps = (int) (distSq / minDistanceSq);

                        for (int j = 1; j < steps; ++j) {
                            float t = (float) j / (float) steps;
                            Vector3f interpPos = catmullRomInterpolate(vertices.getPosition(Math.max(i - 1, 0)), curr, next, vertices.getPosition(Math.min(i + 2, vertices.size() - 1)), t);
                            Tail interp = new Tail(interpPos, vertices.lifeTime[i]);
                            interp.color = vertices.getColor(i);
                            interp.width = vertices.getWidth(i);
                            smoothPath.add(interp);
                        }
                    }

                }
                prevDir = dir;
            }

            smoothPath.add(vertices.copyAsTail(vertices.size() - 1));
            return smoothPath;
        }
    }

    public LinkedList<Tail> generateSmoothPathFromShader(LinkedList<Tail> vertices, float distance) {
        if (vertices.size() <= 2) {
            return vertices;
        } else {
            float minDistance = Math.max(distance, 0.05F);
            LinkedList<Tail> smoothPath = new LinkedList<>();
            if (this.config.isCalculateSmoothByShader() && Shaders.supportComputeShader() && Shaders.supportSSBO()) {
                ShaderProgram program = BrutalityShaders.getCatmullRomProgram();
                int VERTEX_SIZE = 36;
                if (inputSSBO == null) {
                    inputSSBO = new ShaderSSBO();
                    inputSSBO.createBufferData(VERTEX_SIZE * 512, 35044);
                    int inputBlockIndex = GL43.glGetProgramResourceIndex(program.programId, 37606, "InputVertices");
                    inputSSBO.bindToShader(program.programId, inputBlockIndex, 0);
                    inputSSBO.bindIndex(0);
                }

                if (inputBuffer == null) {
                    inputBuffer = BufferUtils.createFloatBuffer(VERTEX_SIZE * 512);
                }

                if (outputSSBO == null) {
                    outputSSBO = new ShaderSSBO();
                    outputSSBO.createBufferData(VERTEX_SIZE * 1024, 35050);
                    int outputBlockIndex = GL43.glGetProgramResourceIndex(program.programId, 37606, "OutputVertices");
                    outputSSBO.bindToShader(program.programId, outputBlockIndex, 1);
                    outputSSBO.bindIndex(1);
                }

                if (atomicCounterBuffer == null) {
                    atomicCounterBuffer = GL43.glGenBuffers();
                    GL43.glBindBuffer(37568, atomicCounterBuffer);
                    GL43.glBufferData(37568, 4L, 35050);
                    GL43.glBindBufferBase(37568, 2, atomicCounterBuffer);
                }

                program.use((uniform) -> {
                    uniform.glUniform1F("minDistance", minDistance);
                    uniform.glUniform1I("inputCount", vertices.size());
                });

                for(Tail vertex : vertices) {
                    inputBuffer.put(vertex.position.x).put(vertex.position.y).put(vertex.position.z);
                    inputBuffer.put(vertex.color.x).put(vertex.color.y).put(vertex.color.z).put(vertex.color.w);
                    inputBuffer.put(vertex.lifeTime).put(vertex.width);
                }

                inputSSBO.bufferSubData(0L, inputBuffer);
                int numGroups = (int)Math.ceil((float)vertices.size() / 256.0F);
                GL43.glDispatchCompute(numGroups, 1, 1);
                GL43.glMemoryBarrier(12288);
                GL43.glBindBuffer(37568, atomicCounterBuffer);
                IntBuffer outputCountBuffer = GL43.glMapBuffer(37568, 35000).asIntBuffer();
                int outputCount = outputCountBuffer.get();
                GL43.glUnmapBuffer(37568);
                outputSSBO.bindBuffer();
                FloatBuffer smoothVertexBuffer = GL43.glMapBuffer(37074, 35000).asFloatBuffer();

                for(int i = 0; i < outputCount; ++i) {
                    Vector3f pos = new Vector3f(smoothVertexBuffer.get(), smoothVertexBuffer.get(), smoothVertexBuffer.get());
                    Vector4f color = new Vector4f(smoothVertexBuffer.get(), smoothVertexBuffer.get(), smoothVertexBuffer.get(), smoothVertexBuffer.get());
                    float lifeTime = smoothVertexBuffer.get();
                    float width = smoothVertexBuffer.get();
                    Tail tail = new Tail(pos, lifeTime);
                    tail.color = color;
                    tail.width = width;
                    smoothPath.add(tail);
                }

                GL43.glUnmapBuffer(37074);
            }
            return smoothPath;
        }
    }

    public static Vector3f catmullRomInterpolate(Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3, float t) {
        float t2 = t * t;
        float t3 = t2 * t;
        Vector3f result = new Vector3f();
        result.x = 0.5F * (2.0F * p1.x + (-p0.x + p2.x) * t + (2.0F * p0.x - 5.0F * p1.x + 4.0F * p2.x - p3.x) * t2 + (-p0.x + 3.0F * p1.x - 3.0F * p2.x + p3.x) * t3);
        result.y = 0.5F * (2.0F * p1.y + (-p0.y + p2.y) * t + (2.0F * p0.y - 5.0F * p1.y + 4.0F * p2.y - p3.y) * t2 + (-p0.y + 3.0F * p1.y - 3.0F * p2.y + p3.y) * t3);
        result.z = 0.5F * (2.0F * p1.z + (-p0.z + p2.z) * t + (2.0F * p0.z - 5.0F * p1.z + 4.0F * p2.z - p3.z) * t2 + (-p0.z + 3.0F * p1.z - 3.0F * p2.z + p3.z) * t3);
        return result;
    }

    protected void updateRawTailsProperties() {
        if (this.rawTails.size() > 1) {
            for(int i = 0; i < this.rawTails.size(); ++i) {
                this.updateRawTrailProperties(i, this.rawTails.size());
            }
        } else if (this.rawTails.size() == 1) {
            this.updateRawTrailProperties(0, 2);
        }

    }

    protected void updateRawTrailProperties(int tailIndex, int tailsSize) {
        float t = (float)tailIndex / (float)(tailsSize - 1);
        int colorInt = this.config.getColorOverTrail().get(t, () -> this.getMemRandom("trails-colorOverTrail")).intValue();
        this.rawTails.colorR[tailIndex] = FastColor.ARGB32.red(colorInt);
        this.rawTails.colorG[tailIndex] = FastColor.ARGB32.green(colorInt);
        this.rawTails.colorB[tailIndex] = FastColor.ARGB32.blue(colorInt);
        this.rawTails.colorA[tailIndex] = FastColor.ARGB32.alpha(colorInt);
        float widthValue = this.config.getWidthOverTrail().get(t, () -> this.getMemRandom("trails-widthOverTrail")).floatValue();
        if (this.widthMultiplier != null) {
            widthValue *= this.widthMultiplier.get();
        }

        this.rawTails.width[tailIndex] = widthValue;
    }

    protected void updateLight() {
        if (!this.config.lights.isEnable() && !this.config.renderer.isBloomEffect()) {
            this.light = this.getLightColor();
        }
    }

    @Override
    public void render(@Nonnull VertexConsumer pBuffer, Camera pRenderInfo, float partialTicks) {
        if (this.delay <= 0 && this.emitter.isVisible()) {
            if (!RenderSystem.isOnRenderThread()) return;

            // 1. Get the correct FBO ID based on whether Shaders are on
            // We use Translucent because Trails are usually transparent/blended
            int fboId = PhotonShaderUtils.getTranslucentFrameBufferID();

            // 2. Check completeness of the CORRECT buffer
            if (GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE) {
                // If Oculus hasn't initialized the buffer yet, skip to avoid spam
                return;
            }

            // 3. Bind the FBO Oculus expects
            GlStateManager._glBindFramebuffer(GL30.GL_FRAMEBUFFER, fboId);

            // 4. Draw your tails
            this.tails.renderInternal(
                    pBuffer,
                    partialTicks,
                    pRenderInfo.getPosition().toVector3f(),
                    this.getRealColor(partialTicks),
                    this.getRealLight(partialTicks)
            );
        }
    }

    public Vector4f getUVs(int tailIndex, int size, float partialTicks) {
        UVMode uvMode = this.config.getUvMode();
        float u0;
        float u1;
        float v0;
        float v1;
        if (uvMode == TrailParticle.UVMode.STRETCH) {
            u0 = (float)tailIndex / ((float)size - 1.0F);
            u1 = ((float)tailIndex + 1.0F) / ((float)size - 1.0F);
            v0 = 0.0F;
            v1 = 1.0F;
            if (this.config.uvAnimation.isEnable()) {
                Vector4f uvs = this.config.uvAnimation.getUVs(this, partialTicks);
                float x = uvs.x;
                float y = uvs.y;
                float w = uvs.z - uvs.x;
                float h = uvs.w - uvs.y;
                u0 = x + w * u0;
                v0 = y + h * v0;
                u1 = x + w * u1;
                v1 = y + h * v1;
            }
        } else if (this.config.uvAnimation.isEnable()) {
            Vector4f uvs = this.config.uvAnimation.getUVs(this, partialTicks);
            u0 = uvs.x();
            v0 = uvs.y();
            u1 = uvs.z();
            v1 = uvs.w();
        } else {
            u0 = 0.0F;
            v0 = 0.0F;
            u1 = 1.0F;
            v1 = 1.0F;
        }

        return new Vector4f(u0, v0, u1, v1);
    }

    public void setDieWhenAllTailsRemoved(boolean dieWhenAllTailsRemoved) {
        this.dieWhenAllTailsRemoved = dieWhenAllTailsRemoved;
    }

    public boolean isDieWhenAllTailsRemoved() {
        return this.dieWhenAllTailsRemoved;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }

    public boolean isRemoved() {
        return this.isRemoved;
    }

    public Runnable getOnUpdate() {
        return this.onUpdate;
    }

    public void setOnUpdate(Runnable onUpdate) {
        this.onUpdate = onUpdate;
    }

    public Float2ObjectFunction<Vector3f> getHeadPositionSupplier() {
        return this.headPositionSupplier;
    }

    public void setHeadPositionSupplier(Float2ObjectFunction<Vector3f> headPositionSupplier) {
        this.headPositionSupplier = headPositionSupplier;
    }

    public Supplier<Float> getLifetimeSupplier() {
        return this.lifetimeSupplier;
    }

    public void setLifetimeSupplier(Supplier<Float> lifetimeSupplier) {
        this.lifetimeSupplier = lifetimeSupplier;
    }

    public Supplier<Float> getWidthMultiplier() {
        return this.widthMultiplier;
    }

    public void setWidthMultiplier(Supplier<Float> widthMultiplier) {
        this.widthMultiplier = widthMultiplier;
    }

    public Float2ObjectFunction<Vector4f> getColorMultiplier() {
        return this.colorMultiplier;
    }

    public void setColorMultiplier(Float2ObjectFunction<Vector4f> colorMultiplier) {
        this.colorMultiplier = colorMultiplier;
    }

    public TailArray getRawTails() {
        return this.rawTails;
    }

    public TailArray getTails() {
        return this.tails;
    }

    public IParticleEmitter getEmitter() {
        return this.emitter;
    }

    public ConcurrentHashMap<Object, Float> getMemRandom() {
        return this.memRandom;
    }

    public RandomSource getRandomSource() {
        return this.randomSource;
    }

    public enum UVMode {
        STRETCH,
        TILE
    }

    public class TailArray {
        private int size = 0;
        private int capacity = 16;
        private float[] posX;
        private float[] posY;
        private float[] posZ;
        private float[] colorR;
        private float[] colorG;
        private float[] colorB;
        private float[] colorA;
        private float[] width;
        private float[] lifeTime;

        public TailArray(TrailParticle this$0) {
            this.posX = new float[this.capacity];
            this.posY = new float[this.capacity];
            this.posZ = new float[this.capacity];
            this.colorR = new float[this.capacity];
            this.colorG = new float[this.capacity];
            this.colorB = new float[this.capacity];
            this.colorA = new float[this.capacity];
            this.width = new float[this.capacity];
            this.lifeTime = new float[this.capacity];
        }

        public void clear() {
            this.size = 0;
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public void add(Tail tail) {
            this.ensureCapacity();
            this.posX[this.size] = tail.position.x;
            this.posY[this.size] = tail.position.y;
            this.posZ[this.size] = tail.position.z;
            this.colorR[this.size] = tail.color.x;
            this.colorG[this.size] = tail.color.y;
            this.colorB[this.size] = tail.color.z;
            this.colorA[this.size] = tail.color.w;
            this.width[this.size] = tail.width;
            this.lifeTime[this.size] = tail.lifeTime;
            ++this.size;
        }

        private void removeLast() {
            if (this.size > 0) {
                --this.size;
            }

        }

        public Vector3f getPosition(int index) {
            return new Vector3f(this.posX[index], this.posY[index], this.posZ[index]);
        }

        public Vector4f getColor(int index) {
            return new Vector4f(this.colorR[index], this.colorG[index], this.colorB[index], this.colorA[index]);
        }

        public float getWidth(int index) {
            return this.width[index];
        }

        public float getLifeTime(int index) {
            return this.lifeTime[index];
        }

        private void ensureCapacity() {
            if (this.size >= this.capacity) {
                int newCapacity = this.capacity * 2;
                this.posX = Arrays.copyOf(this.posX, newCapacity);
                this.posY = Arrays.copyOf(this.posY, newCapacity);
                this.posZ = Arrays.copyOf(this.posZ, newCapacity);
                this.colorR = Arrays.copyOf(this.colorR, newCapacity);
                this.colorG = Arrays.copyOf(this.colorG, newCapacity);
                this.colorB = Arrays.copyOf(this.colorB, newCapacity);
                this.colorA = Arrays.copyOf(this.colorA, newCapacity);
                this.width = Arrays.copyOf(this.width, newCapacity);
                this.lifeTime = Arrays.copyOf(this.lifeTime, newCapacity);
                this.capacity = newCapacity;
            }

        }

        private void copyTailTo(TailArray dest, int index) {
            dest.ensureCapacity();
            dest.posX[dest.size()] = this.posX[index];
            dest.posY[dest.size()] = this.posY[index];
            dest.posZ[dest.size()] = this.posZ[index];
            dest.colorR[dest.size()] = this.colorR[index];
            dest.colorG[dest.size()] = this.colorG[index];
            dest.colorB[dest.size()] = this.colorB[index];
            dest.colorA[dest.size()] = this.colorA[index];
            dest.width[dest.size()] = this.width[index];
            dest.lifeTime[dest.size()] = this.lifeTime[index];
            ++dest.size;
        }

        private Tail copyAsTail(int index) {
            Vector3f pos = this.getPosition(index);
            Tail tail = new Tail(pos, this.lifeTime[index]);
            tail.color = this.getColor(index);
            tail.width = this.width[index];
            return tail;
        }

        public void renderInternal(VertexConsumer buffer, float partialTicks, Vector3f cameraPos, Vector4f color, int light) {
            Vector3f lastNormal = null;
            Vector3f lastUp = null;
            Vector3f headPos = TrailParticle.this.getHeadPosition(partialTicks);
            boolean pushHead = true;
            int tailSize = TrailParticle.this.tails.size();
            if (tailSize > 0) {
                Vector3f lastPos = TrailParticle.this.tails.getPosition(tailSize - 1);
                if (lastPos.equals(headPos)) {
                    pushHead = false;
                }
            }

            if (pushHead) {
                Tail headTail = new Tail(headPos, 100.0F);
                headTail.previous = tailSize > 1 ? this.copyAsTail(tailSize - 1) : null;
                headTail.updateData();
                TrailParticle.this.tails.add(headTail);
            }

            for(int i = 0; i < this.size - 1; ++i) {
                if (!(this.lifeTime[i] - partialTicks <= 0.0F) && !(this.lifeTime[i + 1] - partialTicks <= 0.0F)) {
                    Vector3f tailPos = new Vector3f(this.posX[i], this.posY[i], this.posZ[i]);
                    Vector3f curr = new Vector3f(this.posX[i], this.posY[i], this.posZ[i]);
                    Vector3f next = new Vector3f(this.posX[i + 1], this.posY[i + 1], this.posZ[i + 1]);
                    Vector3f vec = (new Vector3f(next)).sub(curr);
                    Vector3f toTail = (new Vector3f(curr)).sub(cameraPos);
                    Vector3f normal = vec.cross(toTail).normalize();
                    if (lastNormal == null) {
                        lastNormal = normal;
                    }

                    Vector3f avgNormal = (new Vector3f(lastNormal)).add(normal).div(2.0F);
                    Vector3f up = (new Vector3f(tailPos)).add((new Vector3f(avgNormal)).mul(this.width[i])).sub(cameraPos);
                    Vector3f down = (new Vector3f(tailPos)).add((new Vector3f(avgNormal)).mul(-this.width[i])).sub(cameraPos);
                    float ta = color.w() * this.colorA[i];
                    float tr = color.x() * this.colorR[i];
                    float tg = color.y() * this.colorG[i];
                    float tb = color.z() * this.colorB[i];
                    Vector4f uvs = TrailParticle.this.getUVs(i, this.size, partialTicks);
                    float u0 = uvs.x();
                    float u1 = uvs.z();
                    float v0 = uvs.y();
                    float v1 = uvs.w();
                    if (lastUp == null) {
                        this.pushVertex(buffer, light, up, tr, tg, tb, ta, u0, v0);
                        this.pushVertex(buffer, light, up, tr, tg, tb, ta, u0, v0);
                    }

                    this.pushVertex(buffer, light, up, tr, tg, tb, ta, u0, v0);
                    this.pushVertex(buffer, light, down, tr, tg, tb, ta, u0, v1);
                    lastUp = up;
                    lastNormal = normal;
                }
            }

            int headIndex = this.size - 1;
            if (headIndex > 0 && lastNormal != null) {
                Vector3f head = this.getPosition(headIndex);
                Vector3f up = (new Vector3f(head)).add((new Vector3f(lastNormal)).mul(this.width[headIndex])).sub(cameraPos);
                Vector3f down = (new Vector3f(head)).add((new Vector3f(lastNormal)).mul(-this.width[headIndex])).sub(cameraPos);
                float ta = color.w() * this.colorA[headIndex];
                float tr = color.x() * this.colorR[headIndex];
                float tg = color.y() * this.colorG[headIndex];
                float tb = color.z() * this.colorB[headIndex];
                Vector4f uvs = TrailParticle.this.getUVs(headIndex - 1, this.size, partialTicks);
                float u0 = uvs.x();
                float u1 = uvs.z();
                float v0 = uvs.y();
                float v1 = uvs.w();
                this.pushVertex(buffer, light, up, tr, tg, tb, ta, u1, v0);
                this.pushVertex(buffer, light, down, tr, tg, tb, ta, u1, v1);
                lastUp = up;
            }

            if (lastUp != null) {
                this.pushVertex(buffer, light, lastUp, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
                this.pushVertex(buffer, light, lastUp, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            }

            if (pushHead) {
                TrailParticle.this.tails.removeLast();
            }

        }

        private void pushVertex(VertexConsumer buffer, int light, Vector3f pos, float r, float g, float b, float a, float u, float v) {
            buffer.vertex(pos.x, pos.y, pos.z).uv(u, v).color(r, g, b, a).uv2(light).endVertex();
        }
    }

    public static class Tail {
        public float lifeTime;
        public Vector3f position;
        public Vector4f color;
        public float width;
        public Tail previous;
        public Tail next;
        public float t;

        public Tail(Vector3f position, float lifeTime) {
            this.position = position;
            this.lifeTime = lifeTime;
            this.color = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.width = 0.2F;
        }

        public Tail(Vector3f position, Tail previous, Tail next, float t) {
            this.position = position;
            this.previous = previous;
            this.next = next;
            this.t = t;
            this.updateData();
        }

        public void updateData() {
            if (this.previous != null) {
                if (this.next == null) {
                    this.color = new Vector4f(this.previous.color);
                    this.width = this.previous.width;
                    this.lifeTime = this.previous.lifeTime;
                } else {
                    this.color = (new Vector4f(this.previous.color)).lerp(this.next.color, this.t);
                    this.width = Mth.lerp(this.t, this.previous.width, this.next.width);
                    this.lifeTime = Mth.lerp(this.t, this.previous.lifeTime, this.next.lifeTime);
                }

            }
        }

        public float distanceSquared(Tail next) {
            return this.position.distanceSquared(next.position);
        }
    }
}
