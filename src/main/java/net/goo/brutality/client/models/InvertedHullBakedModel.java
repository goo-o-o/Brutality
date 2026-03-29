package net.goo.brutality.client.models;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InvertedHullBakedModel extends BakedModelWrapper<BakedModel> {
    private final float thickness;

    public InvertedHullBakedModel(BakedModel original, float thickness) {
        super(original);
        this.thickness = thickness;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        List<BakedQuad> originalQuads = this.originalModel.getQuads(state, side, rand);
        List<BakedQuad> thickenedQuads = new ArrayList<>();

        for (BakedQuad quad : originalQuads) {
            thickenedQuads.addAll(thickenQuad(quad, this.thickness));
        }

        return thickenedQuads;
    }

    private Vector3f getPos(BakedQuad quad, int index) {
        int[] vertexData = quad.getVertices();
        int offset = index * 8; // Stride is 8
        float x = Float.intBitsToFloat(vertexData[offset]);
        float y = Float.intBitsToFloat(vertexData[offset + 1]);
        float z = Float.intBitsToFloat(vertexData[offset + 2]);
        return new Vector3f(x, y, z);
    }

    private List<BakedQuad> thickenQuad(BakedQuad quad, float scale) {
        List<BakedQuad> results = new ArrayList<>();

        // 1. Get original vertex positions
        Vector3f[] verts = {getPos(quad, 0), getPos(quad, 1), getPos(quad, 2), getPos(quad, 3)};

        // 2. Get the face normal (to push the outline slightly away from the item)
        Direction dir = quad.getDirection();
        Vector3f normal = new Vector3f(dir.getStepX(), dir.getStepY(), dir.getStepZ()).mul(scale);

        // 3. Get the 4 Cardinal directions (diagonals) from your VertexHelper logic
        Vector3f[] cardinalDirs = getFaceCardinalDirs(verts, scale);

        if (cardinalDirs != null) {
            for (Vector3f cardDir : cardinalDirs) {
                Vector3f[] movedVerts = new Vector3f[4];
                for (int i = 0; i < 4; i++) {
                    // Apply the exact logic: vert + normal + cardinalShift
                    movedVerts[i] = new Vector3f(verts[i]).add(normal).add(cardDir);
                }

                // 4. Create the quad with flipped winding (3, 2, 1, 0)
                results.add(createInvertedQuad(quad, movedVerts));
            }
        }

        return results;
    }

    private Vector3f[] getFaceCardinalDirs(Vector3f[] quadVerts, float scale) {
        Vector3f center = new Vector3f();
        for (Vector3f vert : quadVerts) center.add(vert);
        center.div(4.0f);

        Vector3f corner1 = new Vector3f(quadVerts[0]).sub(center);
        Vector3f corner2 = new Vector3f(quadVerts[1]).sub(center);

        Vector3f side1 = new Vector3f(corner1).add(corner2).normalize();
        Vector3f side2 = new Vector3f(corner1).sub(corner2).normalize();

        Vector3f localDiagonal = new Vector3f(side1).add(side2).mul(scale);
        Vector3f otherLocal = new Vector3f(localDiagonal).reflect(side2);

        return new Vector3f[]{
                new Vector3f(localDiagonal),
                new Vector3f(otherLocal),
                localDiagonal.mul(-1),
                otherLocal.mul(-1)
        };
    }

    private BakedQuad createInvertedQuad(BakedQuad original, Vector3f[] newPos) {
        int[] vertexData = new int[32];
        for (int i = 0; i < 4; i++) {
            int dest = i * 8;
            int src = 3 - i; // Flip winding order

            vertexData[dest] = Float.floatToRawIntBits(newPos[src].x());
            vertexData[dest + 1] = Float.floatToRawIntBits(newPos[src].y());
            vertexData[dest + 2] = Float.floatToRawIntBits(newPos[src].z());
            vertexData[dest + 3] = 0xFF000000; // Force Black
            vertexData[dest + 4] = original.getVertices()[src * 8 + 4]; // U
            vertexData[dest + 5] = original.getVertices()[src * 8 + 5]; // V
        }
        return new BakedQuad(vertexData, original.getTintIndex(), original.getDirection().getOpposite(), original.getSprite(), false);
    }
}