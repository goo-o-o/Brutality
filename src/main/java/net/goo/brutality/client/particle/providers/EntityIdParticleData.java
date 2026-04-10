package net.goo.brutality.client.particle.providers;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class EntityIdParticleData<T extends ParticleOptions> implements ParticleOptions {
    public static final Codec<EntityIdParticleData<?>> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.PARTICLE_TYPES.getCodec().fieldOf("type").forGetter(data -> data.type),
                    Codec.INT.fieldOf("entityId").forGetter(data -> data.entityId)
            ).apply(instance,
                    (type, entityId) ->
                            new EntityIdParticleData<>((ParticleType<?>) type, entityId))
    );

    protected final ParticleType<T> type;
    protected final int entityId;

    public EntityIdParticleData(ParticleType<T> type, int entityId) {
        this.type = type;
        this.entityId = entityId;
    }


    @Override
    public @NotNull ParticleType<T> getType() {
        return type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format("%s %d",
                ForgeRegistries.PARTICLE_TYPES.getKey(getType()), entityId);
    }

    public static final Deserializer<EntityIdParticleData<?>> DESERIALIZER = new Deserializer<>() {
        @Override
        public @NotNull EntityIdParticleData<?> fromCommand(@NotNull ParticleType<EntityIdParticleData<?>> type, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            int entityId = reader.readInt();
            return new EntityIdParticleData<>(type, entityId);
        }

        @Override
        public @NotNull EntityIdParticleData<?> fromNetwork(@NotNull ParticleType<EntityIdParticleData<?>> type, FriendlyByteBuf buf) {
            int entityId = buf.readInt();

            return new EntityIdParticleData<>(type, entityId);
        }
    };


    public int getEntityId() {
        return entityId;
    }
}