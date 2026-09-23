package com.alcatrazescapee.primalwinter.mixin.client;

import com.alcatrazescapee.primalwinter.config.ServerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DimensionSpecialEffects.class)
public abstract class DimensionSpecialEffectsMixin implements IDimensionSpecialEffectsExtension {
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "getSunriseColor", at = @At("RETURN"), cancellable = true)
    private void noSunriseColor(float timeOfDay, float partialTicks, CallbackInfoReturnable<float[]> cir) {
        final float[] original = cir.getReturnValue();
        final Level level = Minecraft.getInstance().level;
        if (original != null && level != null && ServerConfig.isWinterDimension(level.dimension())) {
            cir.setReturnValue(null);
        }
    }
}
