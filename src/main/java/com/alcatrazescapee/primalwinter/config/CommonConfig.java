package com.alcatrazescapee.primalwinter.config;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class CommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue fogDensity;
    public static final ModConfigSpec.ConfigValue<String> fogColorDayValue;
    public static final ModConfigSpec.ConfigValue<String> fogColorNightValue;

    static {
        fogDensity = BUILDER
                .comment("", " How dense the fog effect during a snowstorm is.")
                .translation(key("fogDensity"))
                .defineInRange("fogDensity", 0.1, 0, 1);

        fogColorDayValue = BUILDER
                .comment("", " This is the fog color during the day. It must be an RGB hex string.")
                .translation(key("fogColorDay"))
                .define("fogColorDay", "bfbfd8", CommonConfig::isColor);

        fogColorNightValue = BUILDER
                .comment("", " This is the fog color during the night. It must be an RGB hex string.")
                .translation(key("fogColorNight"))
                .define("fogColorNight", "0c0c19", CommonConfig::isColor);

        SPEC = BUILDER.build();
    }

    private static String key(String path) {
        return PrimalWinter.MOD_ID + ".config." + path;
    }

    private static boolean isColor(Object o) {
        return o instanceof String s && s.matches("[0-9a-fA-F]{6}");
    }
}
