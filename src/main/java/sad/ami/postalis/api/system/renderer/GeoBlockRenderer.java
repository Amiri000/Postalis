package sad.ami.postalis.api.system.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public class GeoBlockRenderer implements BlockEntityRenderer<HeavensForgeBlockEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("postalis", "textures/block/test_texture.png");
    private static final ResourceLocation MODEL = ResourceLocation.fromNamespaceAndPath("postalis", "geo/test.geo.json");

    public GeoBlockRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(HeavensForgeBlockEntity be, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        GeoModel model = GeoModelManager.CACHE.get(ResourceLocation.fromNamespaceAndPath(Postalis.MODID, "geo/test_model.geo.json"));

        if (model == null || model.minecraft_geometry == null || model.minecraft_geometry.isEmpty()) {
            System.err.println(model ==null);
            return;
        }

        GeoModel.Geometry geo = model.minecraft_geometry.get(0);

        var buffer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        poseStack.pushPose();

        for (var bone : geo.bones) {
            for (var cube : bone.cubes) {
                drawCube(poseStack, buffer, cube, packedLight, packedOverlay);
            }
        }

        poseStack.popPose();
    }

    private void drawCube(PoseStack poseStack, VertexConsumer buffer, GeoModel.Cube cube, int light, int overlay) {
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

        // Helper
        final float[][] normals = {
                {0, 0, -1}, // front
                {0, 0, 1}, // back
                {-1, 0, 0}, // left
                {1, 0, 0}, // right
                {0, 1, 0}, // top
                {0, -1, 0}, // bottom
        };

        float[][][] faces = {
                // FRONT
                {{x1, y1, z1}, {0, 1}}, {{x2, y1, z1}, {1, 1}}, {{x2, y2, z1}, {1, 0}}, {{x1, y2, z1}, {0, 0}},
                // BACK
                {{x2, y1, z2}, {0, 1}}, {{x1, y1, z2}, {1, 1}}, {{x1, y2, z2}, {1, 0}}, {{x2, y2, z2}, {0, 0}},
                // LEFT
                {{x1, y1, z2}, {0, 1}}, {{x1, y1, z1}, {1, 1}}, {{x1, y2, z1}, {1, 0}}, {{x1, y2, z2}, {0, 0}},
                // RIGHT
                {{x2, y1, z1}, {0, 1}}, {{x2, y1, z2}, {1, 1}}, {{x2, y2, z2}, {1, 0}}, {{x2, y2, z1}, {0, 0}},
                // TOP
                {{x1, y2, z1}, {0, 1}}, {{x2, y2, z1}, {1, 1}}, {{x2, y2, z2}, {1, 0}}, {{x1, y2, z2}, {0, 0}},
                // BOTTOM
                {{x1, y1, z2}, {0, 1}}, {{x2, y1, z2}, {1, 1}}, {{x2, y1, z1}, {1, 0}}, {{x1, y1, z1}, {0, 0}},
        };

        for (int face = 0; face < 6; face++) {
            float[] normalVec = normals[face];
            for (int i = 0; i < 4; i++) {
                float[] vertex = faces[face * 4 + i][0];
                float[] uv = faces[face * 4 + i][1];

                Vector4f v = new Vector4f(vertex[0], vertex[1], vertex[2], 1.0f);
                v.mul(pose);

                buffer.addVertex(
                        v.x(), v.y(), v.z(),
                        color,
                        uv[0], uv[1],
                        overlay,
                        light,
                        normalVec[0], normalVec[1], normalVec[2]
                );
            }
        }
    }

}
