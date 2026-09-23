package com.alcatrazescapee.primalwinter.data;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

import static com.alcatrazescapee.primalwinter.registries.PrimalWinterFeatures.Keys.*;
import static net.minecraft.world.level.levelgen.placement.BiomeFilter.biome;
import static net.minecraft.world.level.levelgen.placement.HeightmapPlacement.onHeightmap;
import static net.minecraft.world.level.levelgen.placement.InSquarePlacement.spread;
import static net.minecraft.world.level.levelgen.placement.RarityFilter.onAverageOnceEvery;

public class BuiltinPlacedFeatures {
    private static ResourceKey<PlacedFeature> key(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, PrimalWinter.id(id));
    }

    final BootstrapContext<PlacedFeature> context;
    final HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures;

    public BuiltinPlacedFeatures(BootstrapContext<PlacedFeature> context) {
        this.context = context;
        this.configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        register(FREEZE_TOP_LAYER, PrimalWinterFeatures.FREEZE_TOP_LAYER);
        register(ICE_PATCH, PrimalWinterFeatures.ICE_PATCH,
                spread(),
                onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                biome());
        register(ICE_SPIKES, PrimalWinterFeatures.ICE_SPIKES,
                onAverageOnceEvery(8),
                spread(),
                onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                biome());
        register(POWDER_SNOW_PATCH, PrimalWinterFeatures.POWDER_SNOW_PATCH,
                spread(),
                onHeightmap(Heightmap.Types.MOTION_BLOCKING),
                biome());
        register(SNOW_PATCH, PrimalWinterFeatures.SNOW_PATCH,
                spread(),
                onHeightmap(Heightmap.Types.OCEAN_FLOOR),
                biome());
    }

    void register(ResourceKey<PlacedFeature> key, ResourceKey<ConfiguredFeature<?, ?>> feature, PlacementModifier... placements) {
        context.register(key, new PlacedFeature(configuredFeatures.getOrThrow(feature), List.of(placements)));
    }
}
