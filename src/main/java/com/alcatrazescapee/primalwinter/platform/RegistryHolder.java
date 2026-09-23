package com.alcatrazescapee.primalwinter.platform;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public interface RegistryHolder<T> extends Supplier<T> {
    @Override
    T get();

    ResourceLocation id();
}
