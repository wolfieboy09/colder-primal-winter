package com.alcatrazescapee.primalwinter.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeItemAccessor {
    @Accessor("STRIPPABLES")
    static Map<Block, Block> accessor$getStrippables() {
        throw new AssertionError();
    }

    @Mutable
    @Accessor("STRIPPABLES")
    static void accessor$setStrippables(Map<Block, Block> strippables) {}
}
