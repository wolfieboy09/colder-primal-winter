package com.alcatrazescapee.primalwinter.client;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import com.alcatrazescapee.primalwinter.config.CommonConfig;
import com.alcatrazescapee.primalwinter.config.ServerConfig;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import static com.alcatrazescapee.primalwinter.registries.PrimalWinterBlocks.SNOWY_SUGAR_CANE;

@EventBusSubscriber(modid = PrimalWinter.MOD_ID, value = Dist.CLIENT)
public final class ClientEventHandler {
    private static final int DEFAULT_FOG_COLOR_DAY = 0xbfbfd8;
    private static final int DEFAULT_FOG_COLOR_NIGHT = 0x0c0c19;

    private static float prevFogDensity = -1f;
    private static long prevFogTick = -1L;

    private static int fogColorDayCache = DEFAULT_FOG_COLOR_DAY;
    private static int fogColorNightCache = DEFAULT_FOG_COLOR_NIGHT;

    private ClientEventHandler() {}

    public static void updateColorCaches() {
        fogColorDayCache = parseColor(CommonConfig.fogColorDayValue, DEFAULT_FOG_COLOR_DAY);
        fogColorNightCache = parseColor(CommonConfig.fogColorNightValue, DEFAULT_FOG_COLOR_NIGHT);
    }

    private static int parseColor(ModConfigSpec.ConfigValue<String> value, int fallback) {
        try {
            return Integer.parseUnsignedInt(value.get(), 16);
        }
        catch (RuntimeException e) {
            return fallback;
        }
    }

    @SubscribeEvent
    public static void onComputeFogColor(ViewportEvent.ComputeFogColor event) {
        renderFogColors(event.getCamera(), (float) event.getPartialTick(), (red, green, blue) -> {
            event.setRed(red);
            event.setGreen(green);
            event.setBlue(blue);
        });
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        renderFogDensity(event.getCamera(), (nearPlane, farPlane) -> {
            event.scaleNearPlaneDistance(nearPlane);
            event.scaleFarPlaneDistance(farPlane);
            event.setCanceled(true);
        });
    }

    private static void renderFogColors(Camera camera, float partialTick, FogColorCallback callback) {
        if (camera.getEntity() instanceof Player player && camera.getFluidInCamera() == FogType.NONE && prevFogDensity > 0f) {
            // Calculate color based on time of day
            final float angle = player.level().getSunAngle(partialTick);
            final float height = Mth.cos(angle);
            final float delta = Mth.clamp((height + 0.4f) / 0.8f, 0, 1);

            final int colorDay = fogColorDayCache;
            final int colorNight = fogColorNightCache;
            final float red = ((colorDay >> 16) & 0xFF) * delta + ((colorNight >> 16) & 0xFF) * (1 - delta);
            final float green = ((colorDay >> 8) & 0xFF) * delta + ((colorNight >> 8) & 0xFF) * (1 - delta);
            final float blue = (colorDay & 0xFF) * delta + (colorNight & 0xFF) * (1 - delta);

            callback.accept(red / 255f, green / 255f, blue / 255f);
        }
    }

    private static void renderFogDensity(Camera camera, FogDensityCallback callback) {
        if (camera.getEntity() instanceof Player player) {
            final long thisTick = Util.getMillis();
            final boolean firstTick = prevFogTick == -1;
            final float deltaTick = firstTick ? 1e10f : (thisTick - prevFogTick) * 0.00005f;

            prevFogTick = thisTick;

            float expectedFogDensity = 0f;

            final Level level = player.level();
            if (ServerConfig.isWinterDimension(level.dimension())) {
                final int light = level.getBrightness(LightLayer.SKY, BlockPos.containing(player.getEyePosition()));
                expectedFogDensity = Mth.clampedMap(light, 0f, 15f, 0f, 1f);
            }

            // Scale the output by the render distance, so changes to the render distance don't
            // visually affect the fog depth
            final float renderDistanceAdjustment = (12f * 16f) / Minecraft.getInstance().gameRenderer.getRenderDistance();

            // Smoothly interpolate fog towards the expected value - increasing faster than it decreases
            if (expectedFogDensity > prevFogDensity) {
                prevFogDensity = Math.min(prevFogDensity + 4f * deltaTick, expectedFogDensity);
            }
            else if (expectedFogDensity < prevFogDensity) {
                prevFogDensity = Math.max(prevFogDensity - deltaTick, expectedFogDensity);
            }

            if (camera.getFluidInCamera() != FogType.NONE) {
                prevFogDensity = -1; // Immediately cancel fog if there's another fog effect going on
                prevFogTick = -1;
            }

            if (prevFogDensity > 0) {
                final float scaledDelta = 1 - (1 - prevFogDensity) * (1 - prevFogDensity);
                final float fogDensity = (float) CommonConfig.fogDensity.getAsDouble();
                final float farPlaneScale = Mth.lerp(scaledDelta, 1f, fogDensity) * renderDistanceAdjustment;
                final float nearPlaneScale = Mth.lerp(scaledDelta, 1f, 0.3f * fogDensity) * renderDistanceAdjustment;
                callback.accept(nearPlaneScale, farPlaneScale);
            }
        }
    }
}