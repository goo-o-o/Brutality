package net.goo.brutality.client.particle.base.trail;

import net.goo.brutality.util.particle.ISceneObject;
import net.goo.brutality.util.particle.Transform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public interface IFXObject extends ISceneObject {
    String getName();

    void setName(String name);

    Level getLevel();

    void setLevel(Level level);

    boolean isAlive();

    boolean isVisible();

    void setVisible(boolean visible);

    void setEffect(IEffect effect);

    IEffect getEffect();

    void remove(boolean force);

    default void emit(IEffect effect, @Nullable Vector3f pos, @Nullable Quaternionf rot) {
        setEffect(effect);
        setLevel(effect.getLevel());

        if (pos != null) transform().position(pos);
        if (rot != null) transform().rotation(rot);

        if (this instanceof Particle p) {
            Minecraft.getInstance().particleEngine.add(p);
        }
    }

    Transform transform();
}