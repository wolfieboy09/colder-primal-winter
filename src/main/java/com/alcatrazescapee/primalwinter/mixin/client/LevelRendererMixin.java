package com.alcatrazescapee.primalwinter.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements ResourceManagerReloadListener, AutoCloseable {
    @Shadow @Final
    private static ResourceLocation SNOW_LOCATION;

    @Shadow
    private ClientLevel level;
    @Shadow @Final private Minecraft minecraft;
    @Shadow private int ticks;
    @Shadow private int rainSoundTime;

    @Shadow @Final private float[] rainSizeX;
    @Shadow @Final private float[] rainSizeZ;

    @Unique private int primalWinter$windSoundTime;
    @Unique
    private boolean primalWinter$isWinterDimension;
}
