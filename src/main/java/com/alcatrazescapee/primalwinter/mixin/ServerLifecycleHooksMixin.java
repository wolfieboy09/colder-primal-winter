package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.config.ServerConfig;
import com.alcatrazescapee.primalwinter.util.WinterBiomes;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLifecycleHooks.class)
public abstract class ServerLifecycleHooksMixin {
    /**
     * NeoForge applies biome modifiers in {@code handleServerAboutToStart()}, immediately before dispatching
     * {@code ServerAboutToStartEvent}. The winter biomes must be known before those modifiers run (they check
     * {@link WinterBiomes#isWinterBiome} for every biome), and {@link ServerConfig}'s dimension list is loaded just
     * beforehand. So we inject the load right before {@code runModifiers()}, without reordering or cancelling any of
     * NeoForge's own dispatching.
     */
    @Inject(
            method = "handleServerAboutToStart",
            at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/server/ServerLifecycleHooks;runModifiers(Lnet/minecraft/server/MinecraftServer;)V", remap = false),
            remap = false
    )
    private static void loadWinterBiomesBeforeModifiers(MinecraftServer server, CallbackInfo ci) {
        WinterBiomes.load(server);
    }
}