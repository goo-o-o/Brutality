//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.goo.brutality.util.particle;


import com.lowdragmc.lowdraglib.gui.editor.annotation.ConfigSetter;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public final class Transform {
    private Vector3f localPosition = new Vector3f();
    private Quaternionf localRotation = new Quaternionf();
    private Vector3f localScale = new Vector3f(1.0F, 1.0F, 1.0F);
    @Nullable
    private Transform parent;
    private List<Transform> children = new ArrayList<>();
    @NotNull
    public final ISceneObject sceneObject;
    @Nullable
    private Vector3f position = null;
    @Nullable
    private Quaternionf rotation = null;
    @Nullable
    private Vector3f scale = null;
    @Nullable
    private Matrix4f localTransformMatrix = null;
    @Nullable
    private Matrix4f worldToLocalMatrix = null;
    private Matrix4f localToWorldMatrix = null;

    public Transform(ISceneObject sceneObject) {
        this.sceneObject = sceneObject;
    }

    // Simplified parenting: just use references, no UUID lookups
    public void parent(@Nullable Transform newParent) {
        if (this.parent != newParent) {
            if (this.parent != null) this.parent.children.remove(this);
            this.parent = newParent;
            if (newParent != null) newParent.children.add(this);
            onTransformChanged();
        }
    }

    private void onTransformChanged() {
        this.position = null;
        this.rotation = null;
        this.localToWorldMatrix = null;
        this.worldToLocalMatrix = null;
        for (Transform child : children) child.onTransformChanged();
        this.sceneObject.onTransformChanged();
    }

    public Matrix4f localTransformMatrix() {
        if (this.localTransformMatrix == null) {
            this.localTransformMatrix = (new Matrix4f()).translate(this.localPosition).rotate(this.localRotation).scale(this.localScale);
        }

        return this.localTransformMatrix;
    }

    public Matrix4f localToWorldMatrix() {
        if (this.localToWorldMatrix == null) {
            this.localToWorldMatrix = this.parent == null ? this.localTransformMatrix() : (new Matrix4f(this.parent.localToWorldMatrix())).mul(this.localTransformMatrix());
        }

        return this.localToWorldMatrix;
    }

    public Matrix4f worldToLocalMatrix() {
        if (this.worldToLocalMatrix == null) {
            this.worldToLocalMatrix = this.localToWorldMatrix().invert(new Matrix4f());
        }

        return this.worldToLocalMatrix;
    }

    public Transform set(Transform transform) {
        return this.set(transform, false);
    }

    public Transform set(Transform transform, boolean local) {
        if (local) {
            this.localPosition(transform.localPosition());
            this.localRotation(transform.localRotation());
            this.localScale(transform.localScale());
        } else {
            this.position(transform.position());
            this.rotation(transform.rotation());
            this.scale(transform.scale());
        }

        return this;
    }

    public Vector3f position() {
        if (this.position == null) {
            this.position = this.parent == null ? this.localPosition : this.parent.localToWorldMatrix().transformPosition(new Vector3f(this.localPosition));
        }

        return new Vector3f(this.position);
    }

    public void position(Vector3f position) {
        this.onTransformChanged();
        this.position = new Vector3f(position);
        if (this.parent == null) {
            this.localPosition = new Vector3f(position);
        } else {
            this.localPosition = this.parent.worldToLocalMatrix().transformPosition(new Vector3f(position));
        }

    }

    public void localPosition(Vector3f localPosition) {
        this.localPosition = localPosition;
        this.onTransformChanged();
    }

    public Quaternionf rotation() {
        if (this.rotation == null) {
            this.rotation = this.parent == null ? this.localRotation : this.parent.rotation().mul(this.localRotation);
        }

        return new Quaternionf(this.rotation);
    }

    public void rotation(Quaternionf rotation) {
        this.onTransformChanged();
        this.rotation = new Quaternionf(rotation);
        if (this.parent == null) {
            this.localRotation = new Quaternionf(rotation);
        } else {
            this.localRotation = this.parent.rotation().invert().mul(rotation);
        }

    }

    @ConfigSetter(
            field = "localRotation"
    )
    public void localRotation(Quaternionf localRotation) {
        this.localRotation = localRotation;
        this.onTransformChanged();
    }

    public Vector3f scale() {
        if (this.scale == null) {
            this.scale = this.parent == null ? this.localScale : (new Vector3f(this.localScale)).mul(this.parent.scale());
        }

        return new Vector3f(this.scale);
    }

    public void scale(Vector3f scale) {
        this.onTransformChanged();
        this.scale = new Vector3f(scale);
        if (this.parent == null) {
            this.localScale = new Vector3f(scale);
        } else {
            this.localScale = (new Vector3f(scale)).div(this.parent.scale());
        }

    }

    public void localScale(Vector3f localScale) {
        this.localScale = localScale;
        this.onTransformChanged();
    }


    public Vector3f localPosition() {
        return this.localPosition;
    }

    public Quaternionf localRotation() {
        return this.localRotation;
    }

    public Vector3f localScale() {
        return this.localScale;
    }

    @Nullable
    public Transform parent() {
        return this.parent;
    }

    public List<Transform> children() {
        return this.children;
    }

    public ISceneObject sceneObject() {
        return sceneObject;
    }
}
