package com.alcatrazescapee.primalwinter.config;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ServerConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> WINTER_DIMENSIONS;

    private static Set<ResourceKey<Level>> winterDimensionsCache;
    private static boolean frozen = false;

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

        WINTER_DIMENSIONS = BUILDER
                .comment(
                        "",
                        " A list of dimensions that will be modified by Primal Winter.",
                        " Dimensions on this list, and all biomes that they may generate, **will** be modified."
                )
                .worldRestart()
                .defineListAllowEmpty(
                        List.of("winterDimensions"),
                        () -> List.of(Level.OVERWORLD.location().toString()),
                        () -> "minecraft:overworld",
                        obj -> obj instanceof String s && ResourceLocation.tryParse(s) != null
                );

        SPEC = BUILDER.build();
    }

    private static String key(String path) {
        return PrimalWinter.MOD_ID + ".config." + path;
    }

    public static Set<ResourceKey<Level>> winterDimensions() {
        if (winterDimensionsCache == null) {
            winterDimensionsCache = parseAndFreeze();
        }
        return winterDimensionsCache;
    }

    public static boolean isWinterDimension(ResourceKey<Level> dimension) {
        return winterDimensions().contains(dimension);
    }

    public static void onReload() {
        if (frozen) {
            PrimalWinter.LOGGER.warn("Ignoring change to 'winterDimensions': frozen for this session, reconnect/restart to apply.");
            return;
        }
        winterDimensionsCache = parseAndFreeze();
    }

    public static void reset() {
        winterDimensionsCache = null;
        frozen = false;
    }

    private static Set<ResourceKey<Level>> parseAndFreeze() {
        final Set<ResourceKey<Level>> dimensions = new HashSet<>();
        for (String raw : WINTER_DIMENSIONS.get()) {
            final ResourceLocation id = ResourceLocation.tryParse(raw);
            if (id != null) dimensions.add(ResourceKey.create(Registries.DIMENSION, id));
            else PrimalWinter.LOGGER.warn("Invalid dimension id '{}' in server config 'winterDimensions', skipping", raw);
        }
        frozen = true;
        return ImmutableSet.copyOf(dimensions);
    }
}
