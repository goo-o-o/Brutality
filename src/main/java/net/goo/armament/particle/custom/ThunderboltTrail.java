package net.goo.armament.particle.custom;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.goo.armament.Armament;
import net.goo.armament.particle.base.AbstractTrailParticle;
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

public class ThunderboltTrail extends AbstractTrailParticle {

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation(Armament.MOD_ID, "textures/particle/generic_trail_particle.png");

    private final int EntityId;

    public ThunderboltTrail(ClientLevel world, double x, double y, double z, float r, float g, float b, int EntityId) {
        super(world, x, y, z, 0, 0, 0,r,g,b);
        this.EntityId = EntityId;
        this.gravity = 0;
        this.lifetime = 200;
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
        this.trailA = 1F * fade;
        Vec3 vec3 = getEntityPosition();
        this.x = vec3.x;
        this.y = vec3.y;
        this.z = vec3.z;

        Entity from = this.getFromEntity();
        if(from == null){
            remove();
        }

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

    public int getLightColor(float f) {
        return 240;
    }

    @Override
    public ResourceLocation getTrailTexture() {
        return TRAIL_TEXTURE;
    }

    @OnlyIn(Dist.CLIENT)
    public static final class OrbFactory implements ParticleProvider<ThunderboltTrail.OrbData> {

        @Override
        public Particle createParticle(ThunderboltTrail.OrbData typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ThunderboltTrail particle;
            particle = new ThunderboltTrail(worldIn, x, y, z, typeIn.getR(), typeIn.getG(), typeIn.getB(),typeIn.getEntityID());

            return particle;
        }
    }


    public static class OrbData implements ParticleOptions {
        public static final Deserializer<ThunderboltTrail.OrbData> DESERIALIZER = new Deserializer<ThunderboltTrail.OrbData>() {
            public ThunderboltTrail.OrbData fromCommand(ParticleType<ThunderboltTrail.OrbData> particleTypeIn, StringReader reader) throws CommandSyntaxException {
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
                return new ThunderboltTrail.OrbData(r, g, b, EntityId);
            }

            public ThunderboltTrail.OrbData fromNetwork(ParticleType<ThunderboltTrail.OrbData> particleTypeIn, FriendlyByteBuf buffer) {
                return new ThunderboltTrail.OrbData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(),buffer.readInt());
            }
        };

        private final float r, g, b;
        private final int entityid;

        public OrbData(float r, float g, float b, int entityid) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.entityid = entityid;
        }

        @Override
        public void writeToNetwork(FriendlyByteBuf buffer) {
            buffer.writeFloat(this.r);
            buffer.writeFloat(this.g);
            buffer.writeFloat(this.b);
            buffer.writeInt(this.entityid);
        }

        @Override
        public String writeToString() {
            return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %d", BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
                    this.r, this.g, this.b, this.entityid);
        }

        @Override
        public ParticleType<ThunderboltTrail.OrbData> getType() {
            return ModParticles.THUNDERBOLT_TRAIL_PARTICLE.get();
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
        public int getEntityID() {
            return this.entityid;
        }

        @OnlyIn(Dist.CLIENT)
        public Entity getOwner() {
            return this.getOwner();
        }

        public static Codec<ThunderboltTrail.OrbData> CODEC(ParticleType<ThunderboltTrail.OrbData> particleType) {
            return RecordCodecBuilder.create((codecBuilder) -> codecBuilder.group(
                            Codec.FLOAT.fieldOf("r").forGetter(ThunderboltTrail.OrbData::getR),
                            Codec.FLOAT.fieldOf("g").forGetter(ThunderboltTrail.OrbData::getG),
                            Codec.FLOAT.fieldOf("b").forGetter(ThunderboltTrail.OrbData::getB),
                            Codec.INT.fieldOf("entityid").forGetter(ThunderboltTrail.OrbData::getEntityID)
                    ).apply(codecBuilder, ThunderboltTrail.OrbData::new)
            );
        }
    }

}