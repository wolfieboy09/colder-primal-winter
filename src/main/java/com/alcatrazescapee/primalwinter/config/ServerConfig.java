package com.alcatrazescapee.primalwinter.config;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue enableWeatherCommand;
    public static final ModConfigSpec.BooleanValue enableSnowAccumulationDuringWorldgen;
    public static final ModConfigSpec.BooleanValue enableSnowAccumulationDuringWeather;

    static {
        enableWeatherCommand = BUILDER
                .comment("", " Should the vanilla /weather be disabled?")
                .translation(key("enableWeatherCommand"))
                .gameRestart()
                .define("enableWeatherCommand", false);

        enableSnowAccumulationDuringWorldgen = BUILDER
                .comment(
                        "",
                        " If true, snow will be layered higher than one layer during world generation.",
                        "",
                        " Note: due to snow layers being > 1 block tall, this tends to prevent most passive (and hostile) mob spawning on the surface, since there are no places to spawn."
                )
                .translation(key("enableSnowAccumulationDuringWorldgen"))
                .define("enableSnowAccumulationDuringWorldgen", false);

        enableSnowAccumulationDuringWeather = BUILDER
                .comment("", "If true, snow will be layered higher than one layer during weather (snow).")
                .translation(key("enableSnowAccumulationDuringWeather"))
                .define("enableSnowAccumulationDuringWeather", true);

        SPEC = BUILDER.build();
    }

    private static String key(String path) {
        return PrimalWinter.MOD_ID + ".config." + path;
    }
}
