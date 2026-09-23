package com.alcatrazescapee.primalwinter;

import com.alcatrazescapee.primalwinter.registries.PrimalWinterBlocks;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(PrimalWinter.MOD_ID)
public class PrimalWinter {
    public static final String MOD_ID = "primalwinter";

    public PrimalWinter(IEventBus bus, ModContainer container) {
        PrimalWinterBlocks.BLOCKS.register(bus);
        PrimalWinterBlocks.ITEMS.register(bus);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
