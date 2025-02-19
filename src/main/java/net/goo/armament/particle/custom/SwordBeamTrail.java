package net.goo.armament.particle.custom;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.armament.Armament;
import net.goo.armament.particle.AbstractLightTrailParticle;
import net.goo.armament.registry.ModParticles;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

public class SwordBeamTrail extends AbstractLightTrailParticle {

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(Armament.MOD_ID, "textures/particle/generic_trail_particle.png");

    private final int EntityId;
    private final float width;
    private final float height;
    private final float initialYRot;
    private final float rotateByAge;

    public SwordBeamTrail(ClientLevel world, double x, double y, double z, float r, float g, float b, float width, float height, int EntityId, int initialYRot) {
        super(world, x, y, z, 0, 0, 0,r,g,b);
        this.EntityId = EntityId;
        this.initialYRot = initialYRot;
        this.gravity = 0;
        this.lifetime = 160 + this.random.nextInt(40);
        rotateByAge = (10 + random.nextFloat() * 10F) * (random.nextBoolean() ? -1F : 1F);
        this.width = width;
        this.height = height;
        Vec3 vec3 = getEntityPosition();
        this.x = this.xo = vec3.x;
        this.y = this.yo = vec3.y;
        this.z = this.zo = vec3.z;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
    }

    public Vec3 getEntityPosition(){
        Entity from = this.getFromEntity();
        if(from != null){
            Vec3 movement = getRenderedDirection();

            return from.position().add(movement.scale(-1F));
        }
        return new Vec3(this.x, this.y, this.z);
    }

    public Vec3 getRenderedDirection() {
        Entity owner = this.getFromEntity();
        if (owner != null) {
            Vec3 movement = owner.getDeltaMovement();
            if (movement.lengthSqr() < 0.0001) { // Small value to account for floating point errors
                return new Vec3(0, 0, 0); // Not moving
            }
            return movement.normalize();
        }
        return new Vec3(this.x, this.y, this.z);
    }

    public Entity getFromEntity() {
        return EntityId == -1 ? null : level.getEntity(EntityId);
    }

    public void tick() {
        super.tick();
        float fade = 1F - age / (float) lifetime;
        this.trailA = fade * 2F;
        Vec3 vec3 = getOrbitPosition();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;

        Entity from = this.getFromEntity();
        if(from == null){
            remove();
        }

    }

    public Vec3 getOrbitPosition(){
        Vec3 dinoPos = getEntityPosition();
        Vec3 vec3 = new Vec3(0, height, width).yRot((float)Math.toRadians(initialYRot + rotateByAge * age));
        return dinoPos.add(vec3);
    }

    public int sampleCount() {
        return 4;
    }

    public int sampleStep() {
        return 1;
    }

    @Override
    public float getTrailHeight() {
        return 1F;
    }

    @Override
    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class OrbFactory implements ParticleProvider<SwordBeamTrail.OrbData> {

        @Override
        public Particle createParticle(SwordBeamTrail.OrbData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SwordBeamTrail particle;
            particle = new SwordBeamTrail(worldIn, x, y, z, typeIn.getR(), typeIn.getG(), typeIn.getB(), typeIn.getWidth(), typeIn.getHeight(),typeIn.getEntityID(), typeIn.getInitialYRot());

            return particle;
        }
    }


    public static class OrbData implements ParticleOptions {
        public static final Deserializer<SwordBeamTrail.OrbData> DESERIALIZER = new Deserializer<SwordBeamTrail.OrbData>() {
            public SwordBeamTrail.OrbData fromCommand(ParticleType<SwordBeamTrail.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
                reader.expect(' ');
                float r = reader.readFloat();
                reader.expect(' ');
                float g = reader.readFloat();
                reader.expect(' ');
                float b = reader.readFloat();
                reader.expect(' ');
                float width = reader.readFloat();
                reader.expect(' ');
                float height = reader.readFloat();
                reader.expect(' ');
                int EntityId = reader.readInt();
                reader.expect(' ');
                int initialYRot = reader.readInt();
                return new SwordBeamTrail.OrbData(r, g, b,width,height, EntityId, initialYRot);
            }

            public SwordBeamTrail.OrbData fromNetwork(ParticleType<SwordBeamTrail.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new SwordBeamTrail.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),buffer.readInt(), buffer.readInt());
            }
        };

        private final float r, g, b, width, height;
        private final int entityID, initialYRot;

        public OrbData(float r, float g, float b, float width, float height, int entityID, int initialYRot) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.width = width;
            this.height = height;
            this.entityID = entityID;
            this.initialYRot = initialYRot;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeFloat(this.width);
            buffer.writeFloat(this.height);
            buffer.writeInt(this.entityID);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b,this.width,this.height,this.entityID);
        }

        @Override
        public ParticleType<SwordBeamTrail.OrbData> getType() {
            return ModParticles.SWORD_BEAM_TRAIL_PARTICLE.get();
        }

        @OnlyIn(Dist.CLIENT)
        public float getR() {
            return this.r;
        }

        @OnlyIn(Dist.CLIENT)
        public float getG() {
            return this.g;
        }

        @OnlyIn(Dist.CLIENT)
        public float getB() {
            return this.b;
        }

        @OnlyIn(Dist.CLIENT)
        public float getWidth() {
            return this.width;
        }

        @OnlyIn(Dist.CLIENT)
        public float getHeight() {
            return this.height;
        }

        @OnlyIn(Dist.CLIENT)
        public int getEntityID() {
            return this.entityID;
        }

        @OnlyIn(Dist.CLIENT)
        public int getInitialYRot() {
            return this.initialYRot;
        }

        @OnlyIn(Dist.CLIENT)
        public Entity getOwner() {
            return this.getOwner();
        }

        public static Codec<SwordBeamTrail.OrbData> CODEC(ParticleType<SwordBeamTrail.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(SwordBeamTrail.OrbData::getR),
                            Codec.FLOAT.fieldOf("g").forGetter(SwordBeamTrail.OrbData::getG),
                            Codec.FLOAT.fieldOf("b").forGetter(SwordBeamTrail.OrbData::getB),
                            Codec.FLOAT.fieldOf("width").forGetter(SwordBeamTrail.OrbData::getWidth),
                            Codec.FLOAT.fieldOf("height").forGetter(SwordBeamTrail.OrbData::getHeight),
                            Codec.INT.fieldOf("entityID").forGetter(SwordBeamTrail.OrbData::getEntityID),
                            Codec.INT.fieldOf("initialYRot").forGetter(SwordBeamTrail.OrbData::getInitialYRot)
                    ).apply(codecBuilder, SwordBeamTrail.OrbData::new)
            );
        }
    }

}