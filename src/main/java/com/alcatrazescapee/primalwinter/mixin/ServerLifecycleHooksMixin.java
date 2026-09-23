package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.config.ServerConfig;
import com.alcatrazescapee.primalwinter.util.PrimalWinterUtil;
import com.alcatrazescapee.primalwinter.util.WinterBiomes;
import net.minecraft.server.MinecraftServer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mixin(ServerLifecycleHooks.class)
public abstract class ServerLifecycleHooksMixin {

    /**
     * @see WinterBiomes#load(MinecraftServer)
     */
    @Inject(
            method = "handleServerAboutToStart",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/server/ServerLifecycleHooks;runModifiers(Lnet/minecraft/server/MinecraftServer;)V",
                    remap = false
            ),
            cancellable = true,
            remap = false
    )
    private static void primalWinter$loadWinterBiomesBeforeModifiers(MinecraftServer server, CallbackInfo ci) {
        NeoForge.EVENT_BUS.post(new ServerAboutToStartEvent(server)); // Event first (TerraBlender listens here)
        WinterBiomes.load(server);                                   // Primal Winter second, now sees TerraBlender's biomes
        //ServerLifecycleHooks.runModifiers(server);                   // Biome modifiers third
        ci.cancel();                                                 // Rest of the original method is replicated above
    }
}