package com.alcatrazescapee.primalwinter.datagen.content;

import com.alcatrazescapee.primalwinter.registries.PrimalWinterBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Strippable;

public class BuiltinDatamapProvider extends DataMapProvider {
    public BuiltinDatamapProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), event.getLookupProvider());
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.STRIPPABLES)
                .replace(false)
                .add(PrimalWinterBlocks.SNOWY_OAK_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.OAK_LOG), false)
                .add(PrimalWinterBlocks.SNOWY_BIRCH_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.BIRCH_LOG), false)
                .add(PrimalWinterBlocks.SNOWY_SPRUCE_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.SPRUCE_LOG), false)
                .add(PrimalWinterBlocks.SNOWY_JUNGLE_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.JUNGLE_LOG), false)
                .add(PrimalWinterBlocks.SNOWY_DARK_OAK_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.OAK_LOG), false)
                .add(PrimalWinterBlocks.SNOWY_ACACIA_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.ACACIA_LOG), false)
                .add(PrimalWinterBlocks.SNOWY_CHERRY_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.CHERRY_LOG), false)
                .add(PrimalWinterBlocks.SNOWY_MANGROVE_LOG.get().builtInRegistryHolder(), new Strippable(Blocks.MANGROVE_LOG), false);
    }
}
