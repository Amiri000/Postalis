package sad.ami.postalis.api.system.geo.animations;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class GeoAnimationContainer {
    public Map<String, AnimationClip> animations;

    public static class AnimationClip {
        @SerializedName("animation_length")
        public float animationLength;

        public boolean loop;

        public Map<String, AnimatedBone> bones;
    }

    public static class AnimatedBone {
        public KeyframeList rotation;
        public KeyframeList position;
        public KeyframeList scale;
    }

    public static class KeyframeList {
        public List<Keyframe> keyframes;
    }

    public static class Keyframe {
        public float time;

        public List<Float> value;

        public String interpolation;
    }
}