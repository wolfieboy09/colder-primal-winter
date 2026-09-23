package com.alcatrazescapee.primalwinter.content.block;

import com.alcatrazescapee.primalwinter.registries.PrimalWinterBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SnowySugarCaneBlock extends SugarCaneBlock {
    public SnowySugarCaneBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(PrimalWinterBlockTags.SNOWY_SUGAR_CANE_SURVIVES_ON);
    }
}
