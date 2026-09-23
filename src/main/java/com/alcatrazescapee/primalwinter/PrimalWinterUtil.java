package com.alcatrazescapee.primalwinter;

import net.minecraft.world.level.block.state.BlockState;

public final class PrimalWinterUtil {
    private  PrimalWinterUtil() {}
    public static BlockState copyProperties(BlockState oldState, BlockState newState) {
        return newState.getBlock().withPropertiesOf(oldState);
    }
}
