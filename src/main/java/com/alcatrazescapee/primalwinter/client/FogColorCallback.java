package com.alcatrazescapee.primalwinter.client;

@FunctionalInterface
public interface FogColorCallback {
    void accept(float red, float green, float blue);
}