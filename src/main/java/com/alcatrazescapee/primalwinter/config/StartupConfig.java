package com.alcatrazescapee.primalwinter.config;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.packet.ConfigPacket;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class StartupConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<List<? extends String>> WINTER_DIMENSIONS;

    static {
        final ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        WINTER_DIMENSIONS = builder
                .comment(
                        "",
                        " A list of dimensions that will be modified by Primal Winter.",
                        " Dimensions on this list, and all biomes that they may generate, **will** be modified. Note that this means if a biome is generated in a winter dimension, and a non-winter dimension **that biome will be broken**."
                )
                .defineListAllowEmpty(
                        List.of("winterDimensions"),
                        () -> List.of(Level.OVERWORLD.location().toString()),
                        () -> "minecraft:overworld",
                        obj -> obj instanceof String s && ResourceLocation.tryParse(s) != null
                );

        builder.pop();

        SPEC = builder.build();
    }

    public static @NotNull Collection<ResourceKey<Level>> winterDimensions() {
        final List<ResourceKey<Level>> dimensions = new ArrayList<>();
        for (String raw : WINTER_DIMENSIONS.get()) {
            final ResourceLocation id = ResourceLocation.tryParse(raw);
            if (id != null) {
                dimensions.add(ResourceKey.create(Registries.DIMENSION, id));
            }
            else {
                PrimalWinter.LOGGER.warn("Invalid dimension id '{}' in startup config 'winterDimensions', skipping", raw);
            }
        }
        return dimensions;
    }

    public void syncTo(ServerPlayer player, ConfigPacket packet) {
        PacketDistributor.sendToPlayer(player, packet);
    }
}