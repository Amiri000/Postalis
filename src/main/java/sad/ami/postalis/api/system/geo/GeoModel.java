package sad.ami.postalis.api.system.geo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class GeoModel {
    @SerializedName("minecraft:geometry")
    public List<Geometry> minecraft_geometry;

    public static class Geometry {
        public Description description;
        public List<Bone> bones;
    }

    public static class Description {
        public String identifier;
        public int texture_width;
        public int texture_height;
        public float visible_bounds_width;
        public float visible_bounds_height;
        public List<Float> visible_bounds_offset;
    }

    public static class Bone {
        public String name;
        public List<Float> pivot;
        public List<Cube> cubes;
    }

    public static class Cube {
        public List<Float> origin;
        public List<Float> size;

        // Новое: поддержка UV в виде объекта с гранями
        @SerializedName("uv")
        public FaceUV uvFaces;
    }

    public static class FaceUV {
        public Face north;
        public Face east;
        public Face south;
        public Face west;
        public Face up;
        public Face down;
    }

    public static class Face {
        public List<Integer> uv;
        @SerializedName("uv_size")
        public List<Integer> uv_size;
    }
}