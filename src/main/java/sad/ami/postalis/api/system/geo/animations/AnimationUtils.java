package sad.ami.postalis.api.system.geo.animations;

import java.util.List;

public class AnimationUtils {
    public static float[] interpolate(GeoAnimationContainer.KeyframeList keyframes, float time, float animationLength, boolean loop) {
        if (keyframes == null || keyframes.keyframes.isEmpty())
            return null;

        var frames = keyframes.keyframes;

        if (frames.size() == 1)
            return toArray(frames.getFirst().value);

        if (loop)
            time = time % animationLength;
        else if (time > animationLength)
            time = animationLength;

        for (int i = 0; i < frames.size() - 1; i++) {
            GeoAnimationContainer.Keyframe kf1 = frames.get(i);
            GeoAnimationContainer.Keyframe kf2 = frames.get(i + 1);

            if (time >= kf1.time && time <= kf2.time) {
                float progress = (time - kf1.time) / (kf2.time - kf1.time);

                return lerp(kf1.value, kf2.value, progress);
            }
        }

        return toArray(frames.getLast().value);
    }

    private static float[] lerp(List<Float> from, List<Float> to, float t) {
        float[] result = new float[from.size()];

        for (int i = 0; i < result.length; i++)
            result[i] = from.get(i) + (to.get(i) - from.get(i)) * t;

        return result;
    }

    private static float[] toArray(List<Float> list) {
        float[] result = new float[list.size()];

        for (int i = 0; i < list.size(); i++)
            result[i] = list.get(i);

        return result;
    }
}