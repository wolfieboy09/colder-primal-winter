package com.alcatrazescapee.primalwinter.registries;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.util.WinterBiomes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.*;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public final class PrimalWinterBiomeModifier {
    public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, PrimalWinter.MOD_ID);
    public static final Supplier<MapCodec<? extends Instance>> CODEC = BIOME_MODIFIERS.register("instance", () -> RecordCodecBuilder.mapCodec(instance -> instance.group(
            PlacedFeature.LIST_CODEC.fieldOf("surface_structures").forGetter(c -> c.surfaceStructures),
            PlacedFeature.LIST_CODEC.fieldOf("top_layer_modification").forGetter(c -> c.topLayerModification)
    ).apply(instance, Instance::new)));

    public record Instance(
            HolderSet<PlacedFeature> surfaceStructures,
            HolderSet<PlacedFeature> topLayerModification
    )
            implements BiomeModifier {
        @Override
        public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
            if (biome.unwrapKey().filter(WinterBiomes::isWinterBiome).isEmpty() || phase != Phase.MODIFY) {
                return;
            }

            final ClimateSettingsBuilder climate = builder.getClimateSettings();
            climate.setHasPrecipitation(true);
            climate.setTemperature(-0.5f);
            climate.setTemperatureModifier(Biome.TemperatureModifier.NONE);

            builder.getSpecialEffects()
                    .waterColor(0x3938C9)
                    .waterFogColor(0x050533);

            final BiomeGenerationSettingsBuilder settings = builder.getGenerationSettings();
            for (Holder<PlacedFeature> feature : surfaceStructures) {
                settings.addFeature(GenerationStep.Decoration.SURFACE_STRUCTURES, feature);
            }

            settings.getFeatures(GenerationStep.Decoration.TOP_LAYER_MODIFICATION)
                    .removeIf(holder -> holder.unwrapKey().map(key -> key == MiscOverworldPlacements.FREEZE_TOP_LAYER).orElse(false));
            for (Holder<PlacedFeature> feature : topLayerModification) {
                settings.addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, feature);
            }

            final MobSpawnSettingsBuilder spawnSettings = builder.getMobSpawnSettings();

            record SnapshotEntry(MobCategory category, MobSpawnSettings.SpawnerData data) {}
            final List<SnapshotEntry> snapshot = new ArrayList<>();
            for (MobCategory category : MobCategory.values()) {
                for (MobSpawnSettings.SpawnerData data : spawnSettings.getSpawner(category)) {
                    snapshot.add(new SnapshotEntry(category, data));
                }
            }

            for (MobCategory category : MobCategory.values()) {
                spawnSettings.getSpawner(category).clear();
            }

            PrimalWinterFeatures.addSpawns(spawnSettings::addSpawn);

            for (SnapshotEntry entry : snapshot) {
                final EntityType<?> type = entry.data().type;
                if (type == EntityType.STRAY || type == EntityType.POLAR_BEAR) {
                    continue;
                }
                final Holder<EntityType<?>> holder = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(type);
                if (holder.is(PrimalWinterEntityTypeTags.SPAWNABLE)) {
                    // Check if already present to avoid duplicates
                    boolean exists = false;
                    for (MobSpawnSettings.SpawnerData existing : spawnSettings.getSpawner(entry.category())) {
                        if (existing.type == type) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        spawnSettings.addSpawn(entry.category(), entry.data());
                    }
                }
            }
        }

        @Override
        public MapCodec<? extends BiomeModifier> codec() {
            return CODEC.get();
        }
    }
}
