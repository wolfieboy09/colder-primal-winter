package com.alcatrazescapee.primalwinter.registries;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public final class PrimalWinterEntityTypeTags {
    public static final TagKey<EntityType<?>> SPAWNABLE = TagKey.create(Registries.ENTITY_TYPE, PrimalWinter.id("spawnable"));

    private PrimalWinterEntityTypeTags() {}
}
