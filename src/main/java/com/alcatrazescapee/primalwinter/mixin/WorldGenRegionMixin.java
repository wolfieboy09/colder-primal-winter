package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.PrimalWinterUtil;
import com.alcatrazescapee.primalwinter.config.StartupConfig;
import com.alcatrazescapee.primalwinter.registries.PrimalWinterBlocks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Supplier;

@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin implements WorldGenLevel {
    @Shadow
    @Final
    private ServerLevel level;

    @ModifyVariable(method = "setBlock", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private BlockState replaceAllBlocksWithSnowyOnes(BlockState stateIn) {
        if (!StartupConfig.winterDimensions().contains(level.dimension())) {
            return stateIn;
        }
        final Supplier<? extends Block> block = PrimalWinterBlocks.SNOWY_DIRECT_REPLACEMENT_BLOCKS.get(stateIn.getBlock());
        if (block == null) {
            return stateIn;
        }
        final BlockState replacementState = block.get().defaultBlockState();
        return PrimalWinterUtil.copyProperties(stateIn, replacementState);
    }
}
