package sad.ami.postalis.api.system.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoModel;
import sad.ami.postalis.api.system.geo.GeoModelManager;
import sad.ami.postalis.api.system.geo.modeldata.FaceNormal;
import sad.ami.postalis.api.system.geo.modeldata.VertexPos;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;

import java.util.List;

public class GeoBlockRenderer implements BlockEntityRenderer<HeavensForgeBlockEntity> {
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json");
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("postalis", "textures/block/texture.png");

    public GeoBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HeavensForgeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        GeoModel model = GeoModelManager.CACHE.get(MODEL);

        if (model == null || model.minecraft_geometry.isEmpty())
            return;

        var geo = model.minecraft_geometry.getFirst();

        poseStack.pushPose();

        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(1f / 16f, 1f / 16f, 1f / 16f);

        for (var bone : geo.bones)
            for (var cube : bone.cubes)
                drawCube(poseStack, bufferSource.getBuffer(RenderType.entityCutout(TEXTURE)), cube, geo.description.texture_width, geo.description.texture_height, packedOverlay);

        poseStack.popPose();
    }

    private void drawCube(PoseStack poseStack, VertexConsumer buffer, GeoModel.Cube cube, int texWidth, int texHeight, int overlay) {
        float ox = cube.origin.get(0);
        float oy = cube.origin.get(1);
        float oz = cube.origin.get(2);

        float sx = cube.size.get(0);
        float sy = cube.size.get(1);
        float sz = cube.size.get(2);

        for (int face = 0; face < 6; face++)
            drawFace(buffer, poseStack.last().pose(), VertexPos.generateCubeVertices(ox, oy, oz, sx, sy, sz), face, FaceNormal.values()[face].vec(), cube.uv_faces, texWidth, texHeight, overlay);
    }

    private void drawFace(VertexConsumer buffer, Matrix4f pose, List<VertexPos> positions, int faceIndex, float[] normal, GeoModel.FaceUV faces, int texWidth, int texHeight, int overlay) {
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

            buffer.addVertex(v.x(), v.y(), v.z(), 0xFFFFFFFF, tex[0], tex[1], overlay, LightTexture.FULL_BRIGHT, normal[0], normal[1], normal[2]);
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