package com.alcatrazescapee.primalwinter.mixin;

import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin implements WorldGenLevel {
}
