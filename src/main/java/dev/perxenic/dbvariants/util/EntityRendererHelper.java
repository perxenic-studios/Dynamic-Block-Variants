package dev.perxenic.dbvariants.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class EntityRendererHelper {
    public static void addVertex(
            VertexConsumer vc,
            PoseStack poseStack,
            Vector3f normal,
            Vector3f pos,
            float u,
            float v,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        vc.addVertex(poseStack.last(), pos)
                .setColor(color)
                .setUv(u, v)
                .setOverlay(packedOverlay)
                .setLight(packedLight)
                .setNormal(poseStack.last(), normal.x, normal.y, normal.z);
    }

    public static void drawXQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(1f, 0f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x,
                uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.x),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, size.x),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawNegXQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(-1f, 0f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x,
                uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, size.x),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.x),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawYQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(0f, 1f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.y),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, size.y),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawNegYQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(0f, -1f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, size.y),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.y),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawZQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(0f, 0f, -1f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, size.y, 0),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawNegZQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(0f, 0f, 1f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, size.y, 0),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
    }

    /**
     * Draws a cube where UVs are determined by position in block
     * @param vc Vertex consumer to draw to
     * @param stack Pose stack with transforms
     * @param start Start coordinate
     * @param size Cube size
     * @param packedOverlay Overlay integer
     * @param packedLight Light integer
     * @param disabledSides Array of disabled sides (right, left, bottom, top, back, front)
     */
    public static void drawInsetCube(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector3f size,
            int packedOverlay,
            int packedLight,
            boolean[] disabledSides
    ) {
        // Right
        if (!disabledSides[0]) {
            drawXQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.z, size.y),
                    new Vector2f(start.z, 16f - start.y - size.y),
                    16f,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Left
        if (!disabledSides[1]) {
            drawNegXQuad(
                    vc, stack,
                    new Vector3f(start).add(size.x, 0f, 0f),
                    new Vector2f(size.z, size.y),
                    new Vector2f(start.z, 16f - start.y - size.y),
                    16f,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Bottom
        if (!disabledSides[2]) {
            drawNegYQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.x, size.z),
                    new Vector2f(start.x, 16f - start.z - size.z),
                    16f,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Top
        if (!disabledSides[3]) {
            drawYQuad(
                    vc, stack,
                    new Vector3f(start).add(0f, size.y, 0f),
                    new Vector2f(size.x, size.z),
                    new Vector2f(start.x, 16f - start.z - size.z),
                    16f,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Back
        if (!disabledSides[4]) {
            drawZQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.x, size.y),
                    new Vector2f(start.x, 16f - start.y - size.y),
                    16f,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Front
        if (!disabledSides[5]) {
            drawNegZQuad(
                    vc, stack,
                    new Vector3f(start).add(0f, 0f, size.z),
                    new Vector2f(size.x, size.y),
                    new Vector2f(start.x, 16f - start.y - size.y),
                    16f,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }
    }

    /**
     * Draws a cube where UVs have the typical arrangement
     * @param vc Vertex consumer to draw to
     * @param stack Pose stack with transforms
     * @param start Start coordinate
     * @param size Cube size
     * @param uvOffset Start position in texture
     * @param relTexSize Texture size relative to 1/16th standard texture (pixel)
     * @param packedOverlay Overlay integer
     * @param packedLight Light integer
     * @param disabledSides Array of disabled sides (right, left, bottom, top, back, front)
     */
    public static void drawStandardCube(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector3f size,
            Vector2f uvOffset,
            float relTexSize,
            int packedOverlay,
            int packedLight,
            boolean[] disabledSides
    ) {
        // Right
        if (!disabledSides[0]) {
            drawXQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.z, size.y),
                    new Vector2f(uvOffset).add(0f, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Left
        if (!disabledSides[1]) {
            drawNegXQuad(
                    vc, stack,
                    new Vector3f(start).add(size.x, 0f, 0f),
                    new Vector2f(size.z, size.y),
                    new Vector2f(uvOffset).add(size.x + size.z, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Bottom
        if (!disabledSides[2]) {
            drawNegYQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.x, size.z),
                    new Vector2f(uvOffset).add(size.x+size.z,0f),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Top
        if (!disabledSides[3]) {
            drawYQuad(
                    vc, stack,
                    new Vector3f(start).add(0f, size.y, 0f),
                    new Vector2f(size.x, size.z),
                    new Vector2f(uvOffset).add(size.z, 0f),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Back
        if (!disabledSides[4]) {
            drawZQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.x, size.y),
                    new Vector2f(uvOffset).add(size.x + size.z * 2, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Front
        if (!disabledSides[5]) {
            drawNegZQuad(
                    vc, stack,
                    new Vector3f(start).add(0f, 0f, size.z),
                    new Vector2f(size.x, size.y),
                    new Vector2f(uvOffset).add(size.z, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }
    }

    public static void drawXFlipQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(1f, 0f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.x),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, size.x),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawNegXFlipQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(-1f, 0f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, size.x),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.x),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawYFlipQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(0f, 1f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.y),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, size.y),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawNegYFlipQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(0f, -1f, 0f);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, size.y),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, 0, size.y),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawZFlipQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        Vector3f normal = new Vector3f(0f, 0f, -1f);
        addVertex(
                vc, stack, normal,
                start, uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, size.y, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
    }

    public static void drawNegZFlipQuad(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector2f size,
            Vector2f uvOrigin,
            float relTexSize,
            int packedLight,
            int packedOverlay,
            int color
    ) {
        Vector3f normal = new Vector3f(0f, 0f, 1f);
        start = new Vector3f(start).div(16f);
        Vector2f uvSize = new Vector2f(size).div(relTexSize);
        size = new Vector2f(size).div(16f);
        uvOrigin = new Vector2f(uvOrigin).div(relTexSize);
        addVertex(
                vc, stack, normal,
                start,
                uvOrigin.x + uvSize.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, 0, 0),
                uvOrigin.x, uvOrigin.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(size.x, size.y, 0),
                uvOrigin.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
        addVertex(
                vc, stack, normal,
                new Vector3f(start).add(0, size.y, 0),
                uvOrigin.x + uvSize.x, uvOrigin.y + uvSize.y,
                packedLight, packedOverlay, color
        );
    }

    /**
     * Draws a cube where UVs are upside down (like a chest)
     * @param vc Vertex consumer to draw to
     * @param stack Pose stack with transforms
     * @param start Start coordinate
     * @param size Cube size
     * @param uvOffset Start position in texture
     * @param relTexSize Texture size relative to 1/16th standard texture (pixel)
     * @param packedOverlay Overlay integer
     * @param packedLight Light integer
     * @param disabledSides Array of disabled sides (right, left, bottom, top, back, front)
     */
    public static void drawFlippedCube(
            VertexConsumer vc,
            PoseStack stack,
            Vector3f start,
            Vector3f size,
            Vector2f uvOffset,
            float relTexSize,
            int packedOverlay,
            int packedLight,
            boolean[] disabledSides
    ) {
        // Right
        if (!disabledSides[0]) {
            drawXFlipQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.z, size.y),
                    new Vector2f(uvOffset).add(0f, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Left
        if (!disabledSides[1]) {
            drawNegXFlipQuad(
                    vc, stack,
                    new Vector3f(start).add(size.x, 0f, 0f),
                    new Vector2f(size.z, size.y),
                    new Vector2f(uvOffset).add(size.x + size.z, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Bottom
        if (!disabledSides[2]) {
            drawNegYFlipQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.x, size.z),
                    new Vector2f(uvOffset).add(size.z, 0f),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Top
        if (!disabledSides[3]) {
            drawYFlipQuad(
                    vc, stack,
                    new Vector3f(start).add(0f, size.y, 0f),
                    new Vector2f(size.x, size.z),
                    new Vector2f(uvOffset).add(size.x+size.z,0f),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Back
        if (!disabledSides[4]) {
            drawZFlipQuad(
                    vc, stack,
                    start,
                    new Vector2f(size.x, size.y),
                    new Vector2f(uvOffset).add(size.z, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }

        // Front
        if (!disabledSides[5]) {
            drawNegZFlipQuad(
                    vc, stack,
                    new Vector3f(start).add(0f, 0f, size.z),
                    new Vector2f(size.x, size.y),
                    new Vector2f(uvOffset).add(size.x + size.z * 2, size.z),
                    relTexSize,
                    packedLight, packedOverlay, 0xFFFFFFFF
            );
        }
    }
}
