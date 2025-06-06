package sad.ami.postalis.api.system.geo.util;

public enum FaceNormal {
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0),
    UP(0, 1, 0),
    DOWN(0, -1, 0);

    public final float[] vector;

    FaceNormal(float x, float y, float z) {
        this.vector = new float[]{x, y, z};
    }

    public float[] vec() {
        return vector;
    }
}