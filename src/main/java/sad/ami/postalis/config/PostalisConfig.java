package sad.ami.postalis.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class PostalisConfig {
    public static final ModConfigSpec CLIENT_SPEC;
    public static final PostalisConfig CONFIG;

    private final ModConfigSpec.BooleanValue visibleHint;

    public PostalisConfig(ModConfigSpec.Builder builder) {
        builder.push("General Settings");

        visibleHint = builder.comment("Hint for holding the key when opening the characteristics menu for the first time.")
                .define("visibleHint", true);

        builder.pop();
    }

    public static void disabledVisibleHint() {
        CONFIG.visibleHint.set(false);
        CLIENT_SPEC.save();
    }

    public static boolean isHintVisible() {
        return CONFIG.visibleHint.get();
    }

    static {
        var pair = new ModConfigSpec.Builder().configure(PostalisConfig::new);

        CONFIG = pair.getLeft();
        CLIENT_SPEC = pair.getRight();
    }
}
