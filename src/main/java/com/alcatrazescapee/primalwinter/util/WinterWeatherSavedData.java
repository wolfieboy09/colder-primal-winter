package com.alcatrazescapee.primalwinter.util;

import com.alcatrazescapee.primalwinter.config.ServerConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.Locale;

public class WinterWeatherSavedData extends SavedData {
    private static final String NAME = "primalwinter_weather";

    public enum Phase implements StringRepresentable {
        SNOW,
        DRY;

        public static final Codec<Phase> CODEC = StringRepresentable.fromEnum(Phase::values);

        @Override
        public String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }
    }

    private Phase phase = Phase.SNOW;
    private long nextSwitchDay = 0L;

    public static WinterWeatherSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(new Factory<>(WinterWeatherSavedData::new, WinterWeatherSavedData::load), NAME);
    }

    public static WinterWeatherSavedData load(CompoundTag tag, HolderLookup.Provider provider) {
        WinterWeatherSavedData data = new WinterWeatherSavedData();
        Phase.CODEC.parse(NbtOps.INSTANCE, tag.get("phase"))
                .result()
                .ifPresent(phase -> data.phase = phase);
        data.nextSwitchDay = tag.getLong("nextSwitch");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider provider) {
        Phase.CODEC.encodeStart(NbtOps.INSTANCE, phase)
                .result()
                .ifPresent(encoded -> tag.put("phase", encoded));
        tag.putLong("nextSwitch", nextSwitchDay);
        return tag;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
        setDirty();
    }

    public long getNextSwitchDay() {
        return nextSwitchDay;
    }

    public void setNextSwitchDay(long nextSwitchDay) {
        this.nextSwitchDay = nextSwitchDay;
        setDirty();
    }

    public void randomizeNextSwitch(ServerLevel level) {
        long currentDay = level.getDayTime() / 24000L;
        int days;
        if (phase == Phase.SNOW) {
            int min = ServerConfig.minSnowDays.get();
            int max = Math.max(min, ServerConfig.maxSnowDays.get());
            days = min + level.random.nextInt(max - min + 1);
        } else {
            int min = ServerConfig.minDryDays.get();
            int max = Math.max(min, ServerConfig.maxDryDays.get());
            days = min + level.random.nextInt(max - min + 1);
        }
        nextSwitchDay = currentDay + days;
        setDirty();
    }

    public void initializeIfNeeded(ServerLevel level) {
        if (nextSwitchDay == 0L) {
            phase = Phase.SNOW;
            randomizeNextSwitch(level);
            setDirty();
        }
    }
}