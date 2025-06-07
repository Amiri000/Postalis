package sad.ami.postalis.api.system.geo.manage;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sad.ami.postalis.api.system.geo.util.FaceNormal;
import sad.ami.postalis.api.system.geo.util.VertexPos;

import java.util.List;

public interface IGeoRenderer {
    default void drawModel(PoseStack poseStack, VertexConsumer buffer, GeoModel model, int overlay, int packedLight) {
        GeoModel.Geometry geometry = model.minecraft_geometry.getFirst();

        int texWidth = geometry.description.texture_width;
        int texHeight = geometry.description.texture_height;

        for (GeoModel.Bone bone : geometry.bones)
            drawBone(poseStack, buffer, bone, texWidth, texHeight, overlay, packedLight);
    }

    default void drawBone(PoseStack poseStack, VertexConsumer buffer, GeoModel.Bone bone, int texWidth, int texHeight, int overlay, int packedLight) {
        poseStack.pushPose();

        if (bone.pivot != null && bone.pivot.size() == 3) {
            float pivotX = -bone.pivot.get(0);
            float pivotY = bone.pivot.get(1);
            float pivotZ = bone.pivot.get(2);

            poseStack.translate(pivotX, pivotY, pivotZ);

            if (bone.rotation != null && bone.rotation.size() == 3) {
                float rx = bone.rotation.get(0);
                float ry = bone.rotation.get(1);
                float rz = bone.rotation.get(2);

                if (rz != 0)
                    poseStack.mulPose(Axis.ZP.rotationDegrees(rz));

                if (ry != 0)
                    poseStack.mulPose(Axis.YP.rotationDegrees(-ry));

                if (rx != 0)
                    poseStack.mulPose(Axis.XP.rotationDegrees(-rx));
            }

            poseStack.translate(-pivotX, -pivotY, -pivotZ);
        }

        if (bone.cubes != null)
            for (GeoModel.Cube cube : bone.cubes)
                drawCube(poseStack, buffer, cube, List.of(0f, 0f, 0f), texWidth, texHeight, overlay, packedLight);

        poseStack.popPose();
    }

    default void drawCube(PoseStack poseStack, VertexConsumer buffer, GeoModel.Cube cube, List<Float> visibleOffset, int texWidth, int texHeight, int overlay, int packedLight) {
        float ox = cube.origin.get(0);
        float oy = cube.origin.get(1);
        float oz = cube.origin.get(2);

        float sx = cube.size.get(0);
        float sy = cube.size.get(1);
        float sz = cube.size.get(2);

        float inflate = cube.inflate != null ? cube.inflate : 0f;
        boolean mirror = cube.mirror != null && cube.mirror;

        ox -= inflate;
        oy -= inflate;
        oz -= inflate;
        sx += inflate * 2;
        sy += inflate * 2;
        sz += inflate * 2;

        // Flip X to match Minecraft's coordinate system
        ox = -ox - sx;

        if (mirror) {
            ox = ox + sx;
            sx = -sx;
        }

        poseStack.pushPose();

        if (visibleOffset != null && visibleOffset.size() == 3)
            poseStack.translate(visibleOffset.get(0), visibleOffset.get(1), visibleOffset.get(2));

        if (cube.rotation != null && cube.rotation.size() == 3 && cube.pivot != null && cube.pivot.size() == 3) {
            float pivotX = -cube.pivot.get(0);
            float pivotY = cube.pivot.get(1);
            float pivotZ = cube.pivot.get(2);

            poseStack.translate(pivotX, pivotY, pivotZ);

            float rx = cube.rotation.get(0);
            float ry = cube.rotation.get(1);
            float rz = cube.rotation.get(2);

            if (rz != 0)
                poseStack.mulPose(Axis.ZP.rotationDegrees(rz));

            if (ry != 0)
                poseStack.mulPose(Axis.YP.rotationDegrees(-ry));

            if (rx != 0)
                poseStack.mulPose(Axis.XP.rotationDegrees(-rx));

            poseStack.translate(-pivotX, -pivotY, -pivotZ);
        }

        for (int face = 0; face < 6; face++)
            drawFace(buffer, poseStack.last().pose(), VertexPos.generateCubeVertices(ox, oy, oz, sx, sy, sz), face,
                    FaceNormal.values()[face].getVector(), cube.uv_faces, texWidth, texHeight, overlay, packedLight);

        poseStack.popPose();
    }

    private void drawFace(VertexConsumer buffer, Matrix4f pose, List<VertexPos> positions, int faceIndex, float[] normal, GeoModel.FaceUV faces, int texWidth, int texHeight, int overlay, int packedLight) {
        int vertexStart = faceIndex * 4;

        float[] uv = getUV(faces, faceIndex);
        float[] size = getUVSize(faces, faceIndex);

        float u0 = uv[0] / texWidth;
        float v0 = uv[1] / texHeight;
        float u1 = (uv[0] + size[0]) / texWidth;
        float v1 = (uv[1] + size[1]) / texHeight;

        float[][] uvCords = {{u1, v0}, {u0, v0}, {u0, v1}, {u1, v1}};

        for (int j = 0; j < 4; j++) {
            VertexPos pos = positions.get(vertexStart + j);
            float[] tex = uvCords[j];
            Vector4f v = pos.toVec4f().mul(pose);

            buffer.addVertex(v.x(), v.y(), v.z(), 0xFFFFFFFF, tex[0], tex[1], overlay, packedLight, normal[0], normal[1], normal[2]);
        }
    }

    private float[] getUV(GeoModel.FaceUV faces, int face) {
        return switch (face) {
            case 0 -> toArray(faces.north.uv);
            case 1 -> toArray(faces.south.uv);
            case 2 -> toArray(faces.west.uv);
            case 3 -> toArray(faces.east.uv);
            case 4 -> toArray(faces.up.uv);
            case 5 -> toArray(faces.down.uv);
            default -> throw new IllegalArgumentException("Invalid face index: " + face);
        };
    }

    private float[] getUVSize(GeoModel.FaceUV faces, int face) {
        return switch (face) {
            case 0 -> toArray(faces.north.uv_size);
            case 1 -> toArray(faces.south.uv_size);
            case 2 -> toArray(faces.west.uv_size);
            case 3 -> toArray(faces.east.uv_size);
            case 4 -> toArray(faces.up.uv_size);
            case 5 -> toArray(faces.down.uv_size);
            default -> throw new IllegalArgumentException("Invalid face index: " + face);
        };
    }

    private float[] toArray(List<Float> list) {
        return new float[]{list.get(0), list.get(1)};
    }
}
