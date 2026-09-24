package com.alcatrazescapee.primalwinter.mixin;

import com.alcatrazescapee.primalwinter.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.npc.CatSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatSpawner.class)
public abstract class CatSpawnerMixin {
    @Inject(method = "spawnInHut", at = @At("HEAD"), cancellable = true)
    private void primalwinter$noCatSpawnInHut(ServerLevel serverLevel, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (ServerConfig.isWinterDimension(serverLevel.dimension())) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "spawnInVillage", at = @At("HEAD"), cancellable = true)
    private void primalwinter$noCatSpawnInVillage(ServerLevel serverLevel, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (ServerConfig.isWinterDimension(serverLevel.dimension())) {
            cir.setReturnValue(0);
        }
    }
}