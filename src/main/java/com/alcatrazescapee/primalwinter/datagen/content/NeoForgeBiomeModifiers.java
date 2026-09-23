package com.alcatrazescapee.primalwinter.datagen.content;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterFeatures;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterBiomeModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class NeoForgeBiomeModifiers {
    final HolderGetter<PlacedFeature> placedFeatures;

    public NeoForgeBiomeModifiers(BootstrapContext<BiomeModifier> context) {
        this.placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        context.register(
                ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, PrimalWinter.id("instance")),
                new PrimalWinterBiomeModifier.Instance(
                        HolderSet.direct(
                                placedFeature(PrimalWinterFeatures.Keys.ICE_SPIKES),
                                placedFeature(PrimalWinterFeatures.Keys.ICE_PATCH),
                                placedFeature(PrimalWinterFeatures.Keys.SNOW_PATCH),
                                placedFeature(PrimalWinterFeatures.Keys.POWDER_SNOW_PATCH)
                        ),
                        HolderSet.direct(
                                placedFeature(PrimalWinterFeatures.Keys.FREEZE_TOP_LAYER)
                        )
                )
        );
    }

    Holder<PlacedFeature> placedFeature(ResourceKey<PlacedFeature> key) {
        return placedFeatures.getOrThrow(key);
    }
}