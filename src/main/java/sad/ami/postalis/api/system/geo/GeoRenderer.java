package sad.ami.postalis.api.system.geo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sad.ami.postalis.api.system.geo.animations.AnimationUtils;
import sad.ami.postalis.api.system.geo.animations.GeoAnimationContainer;
import sad.ami.postalis.api.system.geo.manage.GeoModel;
import sad.ami.postalis.api.system.geo.util.FaceNormal;
import sad.ami.postalis.api.system.geo.util.VertexPos;

import java.util.List;

public class GeoRenderer implements IClientItemExtensions {
    public static final GeoRenderer INSTANCE = new GeoRenderer();

    public void drawModel(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, GeoModel model, int overlay, int packedLight) {
        this.drawModel(poseStack, buffer.getBuffer(RenderType.entityCutout(texture)), model, null, 0, overlay, packedLight);
    }

    public void drawModel(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation texture, GeoModel model, ItemDisplayContext context, int overlay, int packedLight) {
        poseStack.pushPose();

        if (context == ItemDisplayContext.GROUND) {
            var modifier = 1f / 60;

            poseStack.scale(modifier, modifier, modifier);
            poseStack.translate(30, 23, 30);
        }

        this.drawModel(poseStack, buffer.getBuffer(RenderType.entityCutout(texture)), model, null, 0, overlay, packedLight);

        poseStack.popPose();
    }

    public void drawModel(PoseStack poseStack, VertexConsumer consumer, GeoModel model, GeoAnimationContainer animation, float timeSeconds, int overlay, int packedLight) {
        GeoModel.Geometry geometry = model.minecraft_geometry.getFirst();

        int texWidth = geometry.description.texture_width;
        int texHeight = geometry.description.texture_height;

        for (GeoModel.Bone bone : geometry.bones)
            drawBone(poseStack, consumer, bone, texWidth, texHeight, overlay, packedLight, animation, timeSeconds);
    }

    private void drawBone(PoseStack poseStack, VertexConsumer consumer, GeoModel.Bone bone, int texWidth, int texHeight, int overlay, int packedLight, GeoAnimationContainer animation, float timeSeconds) {
        poseStack.pushPose();

        float pivotX = 0, pivotY = 0, pivotZ = 0;

        if (bone.pivot != null && bone.pivot.size() == 3) {
            pivotX = -bone.pivot.get(0);
            pivotY = bone.pivot.get(1);
            pivotZ = bone.pivot.get(2);
            poseStack.translate(pivotX, pivotY, pivotZ);
        }

        float rx = 0, ry = 0, rz = 0;
        if (bone.rotation != null && bone.rotation.size() == 3) {
            rx = bone.rotation.get(0);
            ry = bone.rotation.get(1);
            rz = bone.rotation.get(2);
        }

        float[] rotOut = new float[]{rx, ry, rz};
        applyBoneAnimation(poseStack, bone, animation, timeSeconds, rotOut);

        if (rotOut[2] != 0)
            poseStack.mulPose(Axis.ZP.rotationDegrees(rotOut[2]));

        if (rotOut[1] != 0)
            poseStack.mulPose(Axis.YP.rotationDegrees(-rotOut[1]));

        if (rotOut[0] != 0)
            poseStack.mulPose(Axis.XP.rotationDegrees(-rotOut[0]));

        poseStack.translate(-pivotX, -pivotY, -pivotZ);

        if (bone.cubes != null)
            for (GeoModel.Cube cube : bone.cubes)
                drawCube(poseStack, consumer, cube, List.of(0f, 0f, 0f), texWidth, texHeight, overlay, packedLight);

        poseStack.popPose();
    }

    private void drawCube(PoseStack poseStack, VertexConsumer consumer, GeoModel.Cube cube, List<Float> visibleOffset, int texWidth, int texHeight, int overlay, int packedLight) {
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
            drawFace(consumer, poseStack.last().pose(), VertexPos.generateCubeVertices(ox, oy, oz, sx, sy, sz), face,
                    FaceNormal.values()[face].getVector(), cube.uv_faces, texWidth, texHeight, overlay, packedLight);

        poseStack.popPose();
    }

    private void applyBoneAnimation(PoseStack poseStack, GeoModel.Bone bone, GeoAnimationContainer animation, float timeSeconds, float[] rotOut) {
        if (animation == null) return;

        var clip = animation.animations.get("idle_rotation");

        if (clip == null || !clip.bones.containsKey(bone.name))
            return;

        var animBone = clip.bones.get(bone.name);

        float[] rot = AnimationUtils.interpolate(animBone.rotation, timeSeconds, clip.animationLength, clip.loop);
        float[] pos = AnimationUtils.interpolate(animBone.position, timeSeconds, clip.animationLength, clip.loop);
        float[] scl = AnimationUtils.interpolate(animBone.scale, timeSeconds, clip.animationLength, clip.loop);

        if (rot != null) {
            rotOut[0] += rot[0];
            rotOut[1] += rot[1];
            rotOut[2] += rot[2];
        }

        if (pos != null)
            poseStack.translate(pos[0], pos[1], pos[2]);

        if (scl != null)
            poseStack.scale(scl[0], scl[1], scl[2]);
    }

    private void drawFace(VertexConsumer consumer, Matrix4f pose, List<VertexPos> positions, int faceIndex, float[] normal, GeoModel.FaceUV faces, int texWidth, int texHeight, int overlay, int packedLight) {
        int vertexStart = faceIndex * 4;

        float[] uv = getUV(faces, faceIndex);
        float[] size = getUVSize(faces, faceIndex);

        float u0 = uv[0] / texWidth;
        float v0 = uv[1] / texHeight;
        float u1 = (uv[0] + size[0]) / texWidth;
        float v1 = (uv[1] + size[1]) / texHeight;

        float[][] uvCords = {{u1, v0}, {u0, v0}, {u0, v1}, {u1, v1}};

        for (int j = 0; j < 4; j++) {
            float[] tex = uvCords[j];

            Vector4f v = positions.get(vertexStart + j).toVec4f().mul(pose);

            consumer.addVertex(v.x(), v.y(), v.z(), 0xFFFFFFFF, tex[0], tex[1], overlay, packedLight, normal[0], normal[1], normal[2]);
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
