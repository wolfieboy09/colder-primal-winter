package com.alcatrazescapee.primalwinter.mixin.client;

import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.neoforged.neoforge.client.extensions.IDimensionSpecialEffectsExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DimensionSpecialEffects.class)
public abstract class DimensionSpecialEffectsMixin implements IDimensionSpecialEffectsExtension {
}
