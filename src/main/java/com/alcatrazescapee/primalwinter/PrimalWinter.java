package com.alcatrazescapee.primalwinter;

import com.alcatrazescapee.primalwinter.config.ClientConfig;
import com.alcatrazescapee.primalwinter.config.CommonConfig;
import com.alcatrazescapee.primalwinter.config.ServerConfig;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterFeatures;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterItemGroups;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(PrimalWinter.MOD_ID)
public class PrimalWinter {
    public static final String MOD_ID = "primalwinter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PrimalWinter(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        PrimalWinterBlocks.BLOCKS.register(bus);
        PrimalWinterBlocks.ITEMS.register(bus);
        PrimalWinterItemGroups.TABS.register(bus);
        PrimalWinterFeatures.FEATURES.register(bus);
        PrimalWinterAmbience.PARTICLE_TYPES.register(bus);
        PrimalWinterAmbience.SOUND_EVENTS.register(bus);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
