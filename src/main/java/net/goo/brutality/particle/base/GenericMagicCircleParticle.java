//package net.goo.brutality.particle.base;
//
//import com.mojang.brigadier.StringReader;
//import com.mojang.brigadier.exceptions.CommandSyntaxException;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import net.goo.brutality.registry.BrutalityModParticles;
//import net.minecraft.client.multiplayer.ClientLevel;
//import net.minecraft.client.particle.Particle;
//import net.minecraft.client.particle.ParticleProvider;
//import net.minecraft.client.particle.SpriteSet;
//import net.minecraft.core.particles.ParticleOptions;
//import net.minecraft.core.particles.ParticleType;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraftforge.registries.ForgeRegistries;
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Locale;
//
//public class GenericMagicCircleParticle extends FlatParticleWithData {
//    public int EntityId;
//    public float rotX, rotY, rotZ, growthProgress = 0, growthSpeed = 1, growthDuration = 0.5F;
//    public long lastUpdateTime = System.currentTimeMillis(); // Tracks the last update time
//    public boolean rotateLeft;
//
//    protected GenericMagicCircleParticle(ClientLevel level, double x, double y, double z, SpriteSet spriteSet, int EntityId, float size, float rotX, float rotY, float rotZ) {
//        super(level, x, y, z, spriteSet, EntityId, size, rotX, rotY, rotZ);
//        this.EntityId = EntityId;
//        this.friction = 0.75F;
//        this.quadSize = size;
//        this.lifetime = 100;
//        this.pickSprite(spriteSet);
//    }
//
//
//    public record ParticleData(int entityId, float size, float rotX, float rotY, float rotZ) implements ParticleOptions {
//        public static final Deserializer<ParticleData> DESERIALIZER = new Deserializer<>() {
//            public ParticleData fromCommand(ParticleType<ParticleData> type, StringReader reader) throws CommandSyntaxException {
//                reader.expect(' ');
//                int entityId = reader.readInt();
//                reader.expect(' ');
//                float size = reader.readFloat();
//                reader.expect(' ');
//                float rotX = reader.readFloat();
//                reader.expect(' ');
//                float rotY = reader.readFloat();
//                reader.expect(' ');
//                float rotZ = reader.readFloat();
//                return new ParticleData(entityId, size, rotX, rotY, rotZ);
//            }
//
//            public ParticleData fromNetwork(ParticleType<ParticleData> type, FriendlyByteBuf buffer) {
//                return new ParticleData(buffer.readInt(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
//            }
//        };
//
//        @Override
//        public void writeToNetwork(FriendlyByteBuf buffer) {
//            buffer.writeInt(entityId);
//            buffer.writeFloat(size);
//            buffer.writeFloat(rotX);
//            buffer.writeFloat(rotY);
//            buffer.writeFloat(rotZ);
//        }
//
//        @Override
//        public @NotNull String writeToString() {
//            return String.format(Locale.ROOT, "%s %d %.2f",
//                    ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
//                    entityId, size, rotX, rotY, rotZ);
//        }
//
//        @Override
//        public ParticleType<ParticleData> getType() {
//            return BrutalityModParticles.GENERIC_MAGIC_CIRCLE_PARTICLE.get();
//        }
//
//        public static Codec<ParticleData> CODEC(ParticleType<ParticleData> type) {
//            return RecordCodecBuilder.create(instance ->
//                    instance.group(
//                            Codec.INT.fieldOf("entityId").forGetter(ParticleData::entityId),
//                            Codec.FLOAT.fieldOf("size").forGetter(ParticleData::size),
//                            Codec.FLOAT.fieldOf("rotX").forGetter(ParticleData::rotX),
//                            Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotY),
//                            Codec.FLOAT.fieldOf("rotY").forGetter(ParticleData::rotZ)
//                    ).apply(instance, ParticleData::new)
//            );
//        }
//    }
//
//    public static class Provider implements ParticleProvider<ParticleData> {
//        private final SpriteSet spriteSet;
//
//        public Provider(SpriteSet spriteSet) {
//            this.spriteSet = spriteSet;
//        }
//
//        @Override
//        public Particle createParticle(ParticleData data, ClientLevel level,
//                                       double x, double y, double z,
//                                       double xSpeed, double ySpeed, double zSpeed) {
//            return new GenericMagicCircleParticle(level, x, y, z,
//                    this.spriteSet,
//                    data.entityId(),
//                    data.size(),
//                    data.rotX(),
//                    data.rotY(),
//                    data.rotZ()
//            );
//
//        }
//    }
//
//}