package com.alcatrazescapee.primalwinter.config;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue snowDensity;
    public static final ModConfigSpec.BooleanValue snowSounds;
    public static final ModConfigSpec.BooleanValue windSounds;

    public static ModConfigSpec SPEC;

    static {
        snowDensity = BUILDER
                .comment("", " How visually dense the snow weather effect is. Normally, vanilla sets this to 5 with fast graphics, and 10 with fancy graphics.")
                .translation(key("snowDensity"))
                .defineInRange("snowDensity", 15, 1, 15);

        snowSounds = BUILDER
                .comment("", " Enable snow (actually rain) weather sounds.")
                .translation(key("snowSounds"))
                .define("snowSounds", true);

        windSounds = BUILDER
                .comment("", "Enable wind / snow storm weather sounds.")
                .translation(key("windSounds"))
                .define("windSounds", true);

        SPEC = BUILDER.build();
    }

    private static String key(String path) {
        return PrimalWinter.MOD_ID + ".config." + path;
    }
}
