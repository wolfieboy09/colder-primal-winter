package com.alcatrazescapee.primalwinter.client;

@FunctionalInterface
public interface FogDensityCallback {
    void accept(float nearPlane, float farPlane);
}