package sad.ami.postalis.api.system.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import sad.ami.postalis.Postalis;
import sad.ami.postalis.api.system.geo.GeoModel;
import sad.ami.postalis.api.system.geo.GeoModelManager;
import sad.ami.postalis.block.block_entity.HeavensForgeBlockEntity;

import java.util.List;

public class GeoBlockRenderer implements BlockEntityRenderer<HeavensForgeBlockEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("postalis", "textures/block/texture.png");

    public GeoBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HeavensForgeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        GeoModel model = GeoModelManager.CACHE.get(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json"));

        if (model == null || model.minecraft_geometry == null || model.minecraft_geometry.isEmpty()) return;

        GeoModel.Geometry geo = model.minecraft_geometry.getFirst();
        var buffer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));

        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(1f / 16f, 1f / 16f, 1f / 16f);

        for (var bone : geo.bones) {
            for (var cube : bone.cubes) {
                drawCube(poseStack, buffer, cube, geo.description.texture_width, geo.description.texture_height, LightTexture.FULL_BRIGHT, packedOverlay);
            }
        }

        poseStack.popPose();
    }

    private void drawCube(PoseStack poseStack, VertexConsumer buffer, GeoModel.Cube cube, int texWidth, int texHeight, int light, int overlay) {
        float ox = cube.origin.get(0);
        float oy = cube.origin.get(1);
        float oz = cube.origin.get(2);

        float sx = cube.size.get(0);
        float sy = cube.size.get(1);
        float sz = cube.size.get(2);

        float x1 = ox, x2 = ox + sx;
        float y1 = oy, y2 = oy + sy;
        float z1 = oz, z2 = oz + sz;

        int color = 0xFFFFFFFF;
        Matrix4f pose = poseStack.last().pose();
        Matrix3f normal = poseStack.last().normal();

        float[][] positions = {
                {x1, y2, z1}, {x2, y2, z1}, {x2, y1, z1}, {x1, y1, z1}, // front
                {x2, y2, z2}, {x1, y2, z2}, {x1, y1, z2}, {x2, y1, z2}, // back
                {x1, y2, z2}, {x1, y2, z1}, {x1, y1, z1}, {x1, y1, z2}, // left
                {x2, y2, z1}, {x2, y2, z2}, {x2, y1, z2}, {x2, y1, z1}, // right
                {x1, y2, z2}, {x2, y2, z2}, {x2, y2, z1}, {x1, y2, z1}, // top
                {x1, y1, z1}, {x2, y1, z1}, {x2, y1, z2}, {x1, y1, z2}, // bottom
        };

        float[][] normals = {
                {0, 0, -1}, {0, 0, 1}, {-1, 0, 0}, {1, 0, 0}, {0, 1, 0}, {0, -1, 0}
        };

        GeoModel.FaceUV faces = cube.uv_faces;
        List<List<Float>> uvs = List.of(
                faces.north.uv,
                faces.south.uv,
                faces.west.uv,
                faces.east.uv,
                faces.up.uv,
                faces.down.uv
        );

        List<List<Float>> sizes = List.of(
                faces.north.uv_size,
                faces.south.uv_size,
                faces.west.uv_size,
                faces.east.uv_size,
                faces.up.uv_size,
                faces.down.uv_size
        );
        for (int face = 0; face < 6; face++) {
            float[] norm = normals[face];
            int i = face * 4;

            List<Float> uv = uvs.get(face);
            List<Float> uvSize = sizes.get(face);
            float u0 = uv.get(0) / texWidth;
            float v0 = uv.get(1) / texHeight;
            float u1 = (uv.get(0) + uvSize.get(0)) / texWidth;
            float v1 = (uv.get(1) + uvSize.get(1)) / texHeight;

            float[][] uvCoords = {
                    {u0, v0}, {u1, v0}, {u1, v1}, {u0, v1}
            };

            for (int j = 0; j < 4; j++) {
                float[] pos = positions[i + j];
                float[] tex = uvCoords[j];

                Vector4f v = new Vector4f(pos[0], pos[1], pos[2], 1.0f);
                v.mul(pose);

                buffer.addVertex(
                        v.x(), v.y(), v.z(),
                        color,
                        tex[0], tex[1],
                        overlay,
                        light,
                        norm[0], norm[1], norm[2]
                );
            }
        }
    }
}