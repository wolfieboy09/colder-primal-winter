package com.alcatrazescapee.primalwinter.registries;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PrimalWinterAmbience {
    // Particles
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, PrimalWinter.MOD_ID);

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SNOW = PARTICLE_TYPES.register("snow", () -> new SimpleParticleType(false));

    // Sounds
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PrimalWinter.MOD_ID);
    public static final DeferredHolder<SoundEvent, SoundEvent> WIND = SOUND_EVENTS.register("wind", () -> SoundEvent.createVariableRangeEvent(PrimalWinter.id("wind")));
}
