package com.alcatrazescapee.primalwinter.platform.client;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Function;

@FunctionalInterface
public interface ParticleProviderCallback {
    <T extends ParticleOptions> void accept(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider);
}
