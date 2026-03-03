package net.goo.brutality.client.particle.providers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.Queue;

/***
 * This class is based of LightningParticleOptions Code from Goety:
 <a href="https://github.com/Polarice3/Goety-2/blob/1.20/src/main/java/com/Polarice3/Goety/client/particles/LightningParticleOptions.java">...</a>
 * which is based off Botania.
 */
public class ChainLightningParticleData {
    private final BoltRenderInfo renderInfo;
    private final Vec3 start, end;
    private final int segments;
    private final int count;
    private final float size;
    private final int lifespan;

    private final SpawnFunction spawnFunction;
    private final FadeFunction fadeFunction;

    public ChainLightningParticleData(Vec3 start, Vec3 end) {
        this(BoltRenderInfo.DEFAULT, start, end);
    }

    public ChainLightningParticleData(Vec3 start, Vec3 end, int lifespan) {
        this(BoltRenderInfo.DEFAULT, start, end, lifespan, (int) (Math.sqrt(start.distanceTo(end) * 100)));
    }

    public ChainLightningParticleData(BoltRenderInfo info, Vec3 start, Vec3 end) {
        this(info, start, end, 30);
    }

    public ChainLightningParticleData(BoltRenderInfo info, Vec3 start, Vec3 end, int lifespan) {
        this(info, start, end, lifespan, (int) (Math.sqrt(start.distanceTo(end) * 100)));
    }

    public ChainLightningParticleData(BoltRenderInfo info, Vec3 start, Vec3 end, int lifespan, int segments) {
        this(info, start, end, segments, 1, 0.1F, lifespan, SpawnFunction.delay(60), FadeFunction.fade(0.5F));
    }

    public ChainLightningParticleData(BoltRenderInfo info, Vec3 start, Vec3 end, int segments,
                                      int count,
                                      float size,
                                      int lifespan,
                                      SpawnFunction spawnFunction,
                                      FadeFunction fadeFunction) {
        this.renderInfo = info;
        this.start = start;
        this.end = end;
        this.segments = segments;
        this.count = count;
        this.size = size;
        this.lifespan = lifespan;
        this.spawnFunction = spawnFunction;
        this.fadeFunction = fadeFunction;
    }


    public ChainLightningParticleData count(int count) {
        return new ChainLightningParticleData(
                this.renderInfo, this.start, this.end, this.segments,
                count,
                this.size, this.lifespan, this.spawnFunction, this.fadeFunction);
    }

    public ChainLightningParticleData size(float size) {
        return new ChainLightningParticleData(
                this.renderInfo, this.start, this.end, this.segments, this.count,
                size,
                this.lifespan, this.spawnFunction, this.fadeFunction);
    }

    public ChainLightningParticleData spawn(SpawnFunction spawnFunction) {
        return new ChainLightningParticleData(
                this.renderInfo, this.start, this.end, this.segments, this.count,
                this.size, this.lifespan,
                spawnFunction,
                this.fadeFunction);
    }

    public ChainLightningParticleData fade(FadeFunction fadeFunction) {
        return new ChainLightningParticleData(
                this.renderInfo, this.start, this.end, this.segments, this.count,
                this.size, this.lifespan, this.spawnFunction,
                fadeFunction);
    }

    public ChainLightningParticleData lifespan(int lifespan) {
        return new ChainLightningParticleData(
                this.renderInfo, this.start, this.end, this.segments, this.count, this.size,
                lifespan,
                this.spawnFunction, this.fadeFunction);
    }

    public int getLifespan() {
        return lifespan;
    }

    public SpawnFunction getSpawnFunction() {
        return spawnFunction;
    }

    public FadeFunction getFadeFunction() {
        return fadeFunction;
    }

    public Color[] getColors() {
        return renderInfo.colors;
    }

    public List<BoltQuads> generate() {
        Random random = new Random();
        List<BoltQuads> quads = new ArrayList<>();
        Vec3 diff = end.subtract(start);

        float totalDistance = (float) diff.length();

        for (int i = 0; i < count; i++) {
            Queue<BoltInstructions> drawQueue = new ArrayDeque<>();
            drawQueue.add(new BoltInstructions(start, 0, Vec3.ZERO, null, false));

            while (!drawQueue.isEmpty()) {
                BoltInstructions data = drawQueue.poll();
                Vec3 perpendicularDist = data.perpendicularDist();
                float progress = data.progress() + (1F / segments) * (1 - renderInfo.parallelNoise + random.nextFloat() * renderInfo.parallelNoise * 2);
                Vec3 segmentEnd;
                float segmentDiffScale = renderInfo.spreadFunction.getMaxSpread(progress);

                if (progress >= 1 && segmentDiffScale <= 0) {
                    segmentEnd = end;
                } else {
                    float maxDiff = renderInfo.spreadFactor * segmentDiffScale * totalDistance;
                    Vec3 randVec = findRandomOrthogonalVector(diff, random);
                    double rand = renderInfo.randomFunction.getRandom(random);
                    perpendicularDist = renderInfo.segmentSpreader.getSegmentAdd(perpendicularDist, randVec, maxDiff, segmentDiffScale, progress, rand);
                    segmentEnd = start.add(diff.scale(progress)).add(perpendicularDist);
                }
                float boltSize = size * (0.5F + (1 - progress) * 0.5F);
                Pair<BoltQuads, QuadCache> quadData = createQuads(data.cache(), data.start(), segmentEnd, boltSize);
                quads.add(quadData.getFirst());


                if (progress >= 1) {
                    break;
                } else if (!data.isBranch()) {
                    drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getSecond(), false));
                } else if (random.nextFloat() < renderInfo.branchContinuationFactor) {
                    drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getSecond(), true));
                }

                while (random.nextFloat() < renderInfo.branchInitiationFactor * (1 - progress)) {
                    drawQueue.add(new BoltInstructions(segmentEnd, progress, perpendicularDist, quadData.getSecond(), true));
                }

            }

        }
        return quads;
    }

    private static Vec3 findRandomOrthogonalVector(Vec3 vec, Random random) {
        Vec3 newVec = new Vec3(-0.5 + random.nextDouble(), -0.5 + random.nextDouble(), -0.5 + random.nextDouble());
        return vec.cross(newVec);
    }

    private Pair<BoltQuads, QuadCache> createQuads(@Nullable QuadCache cache, Vec3 startPos, Vec3 end, float size) {
        Vec3 diff = end.subtract(startPos);
        Vec3 rightAdd = diff.cross(new Vec3(0.5, 0.5, 0.5)).normalize().scale(size);
        Vec3 backAdd = diff.cross(rightAdd).normalize().scale(size);
        Vec3 rightAddSplit = rightAdd.scale(0.5F);

        Vec3 start, startRight, startBack;
        if (cache != null) {
            start = cache.prevEnd();
            startRight = cache.prevEndRight();
            startBack = cache.prevEndBack();
        } else {
            start = startPos;
            startRight = start.add(rightAdd);
            startBack = start.add(rightAddSplit).add(backAdd);
        }
        Vec3 endRight = end.add(rightAdd);
        Vec3 endBack = end.add(rightAddSplit).add(backAdd);

        BoltQuads quads = new BoltQuads();
        quads.addQuad(start, end, endRight, startRight);
        quads.addQuad(startRight, endRight, end, start);

        quads.addQuad(startRight, endRight, endBack, startBack);
        quads.addQuad(startBack, endBack, endRight, startRight);

        return Pair.of(quads, new QuadCache(end, endRight, endBack));
    }

    private record QuadCache(Vec3 prevEnd, Vec3 prevEndRight, Vec3 prevEndBack) {
    }

    private record BoltInstructions(Vec3 start, float progress,
                                    Vec3 perpendicularDist,
                                    @Nullable QuadCache cache, boolean isBranch) {
    }

    public static class BoltQuads {

        private final List<Vec3> vecs = new ArrayList<>();

        protected void addQuad(Vec3... quadVecs) {
            vecs.addAll(Arrays.asList(quadVecs));
        }

        public List<Vec3> getVecs() {
            return vecs;
        }
    }

    public interface SpreadFunction {
        SpreadFunction LINEAR_ASCENT = progress -> progress;
        SpreadFunction LINEAR_ASCENT_DESCENT = progress -> (progress - Math.max(0, 2 * progress - 1)) / 0.5F;
        SpreadFunction SINE = progress -> (float) Math.sin(Math.PI * progress);

        float getMaxSpread(float progress);
    }

    public interface RandomFunction {
        RandomFunction UNIFORM = Random::nextFloat;
        RandomFunction GAUSSIAN = rand -> (float) rand.nextGaussian();

        float getRandom(Random rand);
    }


    public interface SegmentSpreader {

        SegmentSpreader NO_MEMORY = (perpendicularDist, randVec, maxDiff, scale, progress, rand) -> randVec.scale(maxDiff * rand);

        static SegmentSpreader memory(float memoryFactor) {
            return (perpendicularDist, randVec, maxDiff, spreadScale, progress, rand) -> {
                double nextDiff = maxDiff * (1 - memoryFactor) * rand;
                Vec3 cur = randVec.scale(nextDiff);
                perpendicularDist = perpendicularDist.add(cur);
                double length = perpendicularDist.length();
                if (length > maxDiff) {
                    perpendicularDist = perpendicularDist.scale(maxDiff / length);
                }
                return perpendicularDist.add(cur);
            };
        }

        Vec3 getSegmentAdd(Vec3 perpendicularDist, Vec3 randVec, float maxDiff, float scale, float progress, double rand);
    }

    public interface SpawnFunction {

        SpawnFunction NO_DELAY = rand -> Pair.of(0F, 0F);
        SpawnFunction CONSECUTIVE = new SpawnFunction() {
            @Override
            public Pair<Float, Float> getSpawnDelayBounds(Random rand) {
                return Pair.of(0F, 0F);
            }

            @Override
            public boolean isConsecutive() {
                return true;
            }
        };

        static SpawnFunction delay(float delay) {
            return rand -> Pair.of(delay, delay);
        }

        static SpawnFunction noise(float delay, float noise) {
            return rand -> Pair.of(delay - noise, delay + noise);
        }

        Pair<Float, Float> getSpawnDelayBounds(Random rand);

        default float getSpawnDelay(Random rand) {
            Pair<Float, Float> bounds = getSpawnDelayBounds(rand);
            return bounds.getFirst() + (bounds.getSecond() - bounds.getFirst()) * rand.nextFloat();
        }

        default boolean isConsecutive() {
            return false;
        }
    }

    public interface FadeFunction {

        FadeFunction NONE = (totalBolts, lifeScale) -> Pair.of(0, totalBolts);

        static FadeFunction fade(float fade) {
            return (totalBolts, lifeScale) -> {
                int start = lifeScale > (1 - fade) ? (int) (totalBolts * (lifeScale - (1 - fade)) / fade) : 0;
                int end = lifeScale < fade ? (int) (totalBolts * (lifeScale / fade)) : totalBolts;
                return Pair.of(start, end);
            };
        }

        Pair<Integer, Integer> getRenderBounds(int totalBolts, float lifeScale);
    }

    public static class BoltRenderInfo {

        public static final BoltRenderInfo DEFAULT = defaultConfig();

        /**
         * Represents the noise level of the bolt's parallel segments or strands,
         * influencing how much random deviation is applied along the bolt's direction.
         *
         * This parameter is used to adjust the randomness in the parallel offset of
         * the visual representation of the bolt. A higher value of {@code parallelNoise}
         * will result in a more irregular or erratic appearance of the bolt.
         *
         * Typically passed to the {@link BoltRenderInfo#noise(float, float)} method during configuration.
         */
        private final float parallelNoise;

        /**
         * Represents the factor determining the degree of randomization or deviation from a central
         * axis in the rendering of a bolt. Higher values of {@code spreadFactor} lead to a wider
         * and more erratic spread of the bolt's segments, while lower values result in a more focused,
         * linear appearance.
         *
         * Typically used in conjunction with {@link SpreadFunction} or related methods to algorithmically
         * control the bolt's visual dispersion.
         */
        private final float spreadFactor;

        /**
         * The factor used in determining the likelihood or frequency of branches initiating
         * from a primary lightning bolt segment in the rendering process.
         *
         * This value controls the visual complexity of the bolt by influencing how often
         * branches appear alongside the main bolt structure. It is utilized in conjunction with
         * the {@link BoltRenderInfo#branching(float, float)} method to configure branching behavior.
         *
         * A higher value results in more frequent branching, while a lower value
         * results in fewer or no branches. Must be a positive floating-point value.
         */
        private final float branchInitiationFactor;
        /**
         * Factor defining the probability of a branch in the bolt continuing beyond its initiation.
         *
         * <p>This value is used during the generation of branching structures in lightning or other
         * particle effects simulated by the {@link BoltRenderInfo} class. The {@code branchContinuationFactor}
         * influences the likelihood of a branch extending further after it has been initiated.</p>
         *
         * <p>A higher value increases the tendency for branches to propagate, while a lower value results
         * in shorter or fewer continuations. Combined with {@code branchInitiationFactor}, it dictates the
         * branching dynamics during particle rendering.</p*/
        private final float branchContinuationFactor;

        /**
         * An array of {@link Color} objects representing the defined color scheme for
         * rendering the bolt effect. The colors are used sequentially or interpolated
         * depending on the implementation in rendering methods.
         *
         * <p>This is a configuration parameter used to customize the color appearance
         * of bolts when rendering effects, such as in {@link BoltRenderInfo#shock(Color)}
         * or {@link BoltRenderInfo#thunderBolt(Color)}.</p>
         *
         * <p>Immutable since it is marked {@code private final}.</p>
         */
        private final Color[] colors;

        /**
         * A private final field representing an instance of {@link RandomFunction}, which is used
         * to provide randomness with a custom randomization logic.
         *
         * <p>The {@link RandomFunction} interface defines a method {@code getRandom(Random rand)}
         * that generates a float value based on a given {@link Random} instance. Common implementations
         * include uniform and Gaussian randomness, as defined by {@link RandomFunction#UNIFORM} and
         * {@link RandomFunction#GAUSSIAN}.
         *
         * <p>This field is utilized in the {@link BoltRenderInfo} class to introduce variability
         * in rendering parameters like noise, branching, or spread, depending on the specific
         * functionality set in the context of the object.
         */
        private final RandomFunction randomFunction;
        /**
         * A {@link SpreadFunction} implementation used by {@code BoltRenderInfo} to calculate
         * the maximum spread of bolt segments based on a specified progression value.
         *
         * <p>This function primarily determines how the spread factor scales with progression.
         * The spread behavior can vary based on the specific logic of the {@link SpreadFunction}
         * implementation, such as {@link SpreadFunction#LINEAR_ASCENT},
         * {@link SpreadFunction#LINEAR_ASCENT_DESCENT}, or {@link SpreadFunction#SINE}.
         *
         * <p>It is passed as a parameter to {@link BoltRenderInfo#BoltRenderInfo(float, float, float, float, RandomFunction, SpreadFunction, SegmentSpreader, Color...)}
         * and modifies the rendering logic by influencing segment spread calculations.
         *
         * @see SpreadFunction
         */
        private final SpreadFunction spreadFunction;
        /**
         * Defines customization for the spread of individual segments in a bolt effect.
         * The {@code segmentSpreader} is responsible for determining the additional displacement
         * applied to segments of the bolt as they are generated, introducing randomness or structured
         * behavior.
         *
         * <p>This variable delegates its behavior to an implementation of {@link SegmentSpreader}, such
         * as {@link SegmentSpreader#NO_MEMORY} or a custom configuration using
         * {@link SegmentSpreader#memory(float)}. The exact spread logic is defined via the
         * {@link SegmentSpreader#getSegmentAdd} method within the implementation.</p>
         *
         * <p>The spread logic may consider parameters*/
        private final SegmentSpreader segmentSpreader;

        private BoltRenderInfo(
                float parallelNoise,
                float spreadFactor,
                float branchInitiationFactor,
                float branchContinuationFactor,
                RandomFunction randomFunction, SpreadFunction spreadFunction, SegmentSpreader segmentSpreader, Color... colors) {
            this.parallelNoise = parallelNoise;
            this.spreadFactor = spreadFactor;
            this.branchInitiationFactor = branchInitiationFactor;
            this.branchContinuationFactor = branchContinuationFactor;
            this.colors = colors;
            this.randomFunction = randomFunction;
            this.spreadFunction = spreadFunction;
            this.segmentSpreader = segmentSpreader;
        }

        private static BoltRenderInfo defaultConfig() {
            return new BoltRenderInfo(
                    0.1F, 0.1F, 0.0F, 0.0F,
                    RandomFunction.GAUSSIAN, SpreadFunction.SINE, SegmentSpreader.NO_MEMORY, new Color(177,
                            171,
                            241,
                            204)
            );
        }

        public static BoltRenderInfo shock(Color color){
            return new BoltRenderInfo(
                    0.1F, 0.1F, 0.0F, 0.0F,
                    RandomFunction.GAUSSIAN, SpreadFunction.SINE, SegmentSpreader.NO_MEMORY, color
            );
        }

        public static BoltRenderInfo thunderBolt(Color color){
            return new BoltRenderInfo(
                    0.1F, 0.1F, 0.0F, 0.0F,
                    RandomFunction.GAUSSIAN, SpreadFunction.LINEAR_ASCENT, SegmentSpreader.NO_MEMORY, color
            );
        }

        public BoltRenderInfo noise(float parallelNoise, float spreadFactor) {
            return new BoltRenderInfo(
                    parallelNoise,
                    spreadFactor,
                    this.branchInitiationFactor,
                    this.branchContinuationFactor,
                    this.randomFunction, this.spreadFunction, this.segmentSpreader, this.colors
            );
        }

        public BoltRenderInfo branching(float branchInitiationFactor, float branchContinuationFactor) {
            return new BoltRenderInfo(
                    this.parallelNoise,
                    this.spreadFactor,
                    branchInitiationFactor,
                    branchContinuationFactor,
                    this.randomFunction, this.spreadFunction, this.segmentSpreader, this.colors
            );
        }

        public BoltRenderInfo spreader(SegmentSpreader segmentSpreader) {
            return new BoltRenderInfo(
                    this.parallelNoise,
                    this.spreadFactor,
                    this.branchInitiationFactor,
                    this.branchContinuationFactor,
                    this.randomFunction, this.spreadFunction, segmentSpreader, this.colors
            );
        }

        public BoltRenderInfo randomFunction(RandomFunction randomFunction) {
            return new BoltRenderInfo(
                    this.parallelNoise,
                    this.spreadFactor,
                    this.branchInitiationFactor,
                    this.branchContinuationFactor,
                    randomFunction, this.spreadFunction, this.segmentSpreader, this.colors
            );
        }

        public BoltRenderInfo spreadFunction(SpreadFunction spreadFunction) {
            return new BoltRenderInfo(
                    this.parallelNoise,
                    this.spreadFactor,
                    this.branchInitiationFactor,
                    this.branchContinuationFactor,
                    this.randomFunction, spreadFunction, this.segmentSpreader, this.colors
            );
        }

        public BoltRenderInfo color(Color... color) {
            return new BoltRenderInfo(
                    this.parallelNoise,
                    this.spreadFactor,
                    this.branchInitiationFactor,
                    this.branchContinuationFactor,
                    this.randomFunction, this.spreadFunction, this.segmentSpreader, color
            );
        }
    }
}
