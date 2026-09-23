package com.alcatrazescapee.primalwinter;

import com.alcatrazescapee.primalwinter.client.ReloadableLevelRenderer;
import com.alcatrazescapee.primalwinter.config.ClientConfig;
import com.alcatrazescapee.primalwinter.config.CommonConfig;
import com.alcatrazescapee.primalwinter.config.ServerConfig;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterAmbience;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterBiomeModifier;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterBlocks;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterFeatures;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterItemGroups;
import com.alcatrazescapee.primalwinter.util.ConfigPacket;
import com.alcatrazescapee.primalwinter.util.WinterBiomes;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@Mod(PrimalWinter.MOD_ID)
@EventBusSubscriber
public class PrimalWinter {
    public static final String MOD_ID = "primalwinter";
    public static final Logger LOGGER = LogUtils.getLogger();

    public PrimalWinter(IEventBus bus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);
        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);

        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        PrimalWinterBlocks.BLOCKS.register(bus);
        PrimalWinterBlocks.ITEMS.register(bus);
        PrimalWinterItemGroups.TABS.register(bus);
        PrimalWinterFeatures.FEATURES.register(bus);
        PrimalWinterAmbience.PARTICLE_TYPES.register(bus);
        PrimalWinterAmbience.SOUND_EVENTS.register(bus);
        PrimalWinterBiomeModifier.BIOME_MODIFIERS.register(bus);
    }

    public static ResourceLocation id(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            // Sync the server's winter dimension list to the client, since server configs are never loaded client-side
            PacketDistributor.sendToPlayer(player, new ConfigPacket(ServerConfig.winterDimensions()));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        WinterBiomes.load(event.getServer());
    }

    @SubscribeEvent
    public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(ModList.get().getModFileById(MOD_ID).versionString());
        registrar.playToClient(ConfigPacket.TYPE, ConfigPacket.CODEC, (packet, context) -> {
            ServerConfig.onClientSync(packet.winterDimensions());
            if (FMLEnvironment.dist.isClient()) {
                ((ReloadableLevelRenderer) Minecraft.getInstance().levelRenderer).primalWinter$reload();
            }
        });
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != ServerConfig.SPEC) return;
        ServerConfig.onReload();
        if (FMLEnvironment.dist.isClient()) {
            ((ReloadableLevelRenderer) Minecraft.getInstance().levelRenderer).primalWinter$reload();
        }
    }
}