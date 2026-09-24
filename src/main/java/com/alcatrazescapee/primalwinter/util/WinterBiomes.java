package com.alcatrazescapee.primalwinter.util;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.config.ServerConfig;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.Biome;

import java.util.Set;

public final class WinterBiomes {
    private static Set<ResourceKey<Biome>> winterBiomesView = Set.of();

    /**
     * Populates the set of winter biomes by scanning every dimension marked as a winter
     * dimension in {@link ServerConfig}, and collecting all biomes their biome source can generate.
     * <p>
     * Must run after {@link ServerConfig}'s dimension list is loaded, but before NeoForge's biome
     * modifiers run. NeoForge applies those modifiers in {@code ServerLifecycleHooks.handleServerAboutToStart()},
     * just before {@code ServerAboutToStartEvent} is posted, so this is injected at that point - see
     * {@code ServerLifecycleHooksMixin}. It does not reorder any of NeoForge's events.
     */
    public static void load(MinecraftServer server) {
        winterBiomesView = server.registryAccess()
                .registryOrThrow(Registries.LEVEL_STEM)
                .entrySet()
                .stream()
                .filter(e -> ServerConfig.isWinterDimension(ResourceKey.create(Registries.DIMENSION, e.getKey().location())))
                .flatMap(e -> e.getValue().generator().getBiomeSource().possibleBiomes().stream())
                .flatMap(holder -> holder.unwrapKey().stream())
                .collect(ImmutableSet.toImmutableSet());
        PrimalWinter.LOGGER.info("Loaded winter biomes={}", winterBiomesView.size());
    }

    /**
     * @return {@code true} if this is a winter biome. <strong>Server-side only</strong> - the
     * client has no registry access to compute this, so use {@link ServerConfig#isWinterDimension}
     * (which is synced) for any client-side dimension checks instead.
     */
    public static boolean isWinterBiome(ResourceKey<Biome> biome) {
        return winterBiomesView.contains(biome);
    }
}