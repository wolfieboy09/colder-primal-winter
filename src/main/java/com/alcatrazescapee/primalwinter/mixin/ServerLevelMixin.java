package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.config.ServerConfig;
import com.alcatrazescapee.primalwinter.util.PrimalWinterUtil;
import com.alcatrazescapee.primalwinter.util.WinterWeatherSavedData;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin extends Level implements WorldGenLevel {
    @Shadow
    @Final
    private ServerLevelData serverLevelData;

    protected ServerLevelMixin(WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess, Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide, boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates) {
        super(levelData, dimension, registryAccess, dimensionTypeRegistration, profiler, isClientSide, isDebug, biomeZoomSeed, maxChainedNeighborUpdates);
    }

    @Inject(method = "tickChunk", at = @At("RETURN"))
    public void placeExtraSnow(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci) {
        PrimalWinterUtil.placeExtraSnow((ServerLevel) (Object) this, chunk);
    }

    @Inject(method = "advanceWeatherCycle", at = @At("HEAD"), cancellable = true)
    private void primalwinter$advanceWeatherCycle(CallbackInfo ci) {
        if (!ServerConfig.isWinterDimension(dimension()) || !ServerConfig.enableWeatherDrySpells.get()) {
            return;
        }

        ServerLevel level = (ServerLevel) (Object) this;
        if (!dimensionType().hasSkyLight()) {
            return;
        }
        if (!getGameRules().getBoolean(GameRules.RULE_WEATHER_CYCLE)) {
            getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(true, getServer());
        }

        WinterWeatherSavedData data = WinterWeatherSavedData.get(level);
        data.initializeIfNeeded(level);

        long currentDay = level.getDayTime() / 24000L;
        if (currentDay >= data.getNextSwitchDay()) {
            if (data.getPhase() == WinterWeatherSavedData.Phase.SNOW) {
                data.setPhase(WinterWeatherSavedData.Phase.DRY);
            } else {
                data.setPhase(WinterWeatherSavedData.Phase.SNOW);
            }
            data.randomizeNextSwitch(level);
        }

        if (data.getPhase() == WinterWeatherSavedData.Phase.SNOW) {
            if (!level.isRaining() || !level.isThundering()) {
                level.setWeatherParameters(0, 24000 * Math.max(1, ServerConfig.maxSnowDays.get()), true, true);
            }
        } else {
            if (level.isRaining() || level.isThundering()) {
                level.setWeatherParameters(24000 * Math.max(1, ServerConfig.maxDryDays.get()), 0, false, false);
            }
        }

        ci.cancel();
    }
}
