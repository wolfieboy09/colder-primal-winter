package com.alcatrazescapee.primalwinter.packet;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public record ConfigPacket(Collection<ResourceKey<Level>> winterDimensions) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ConfigPacket> TYPE = new CustomPacketPayload.Type<>(PrimalWinter.id("config"));

    public static final StreamCodec<ByteBuf, ConfigPacket> STREAM_CODEC = ResourceKey.streamCodec(Registries.DIMENSION)
            .<Collection<ResourceKey<Level>>>apply(ByteBufCodecs.collection(ArrayList::new))
            .map(ConfigPacket::new, ConfigPacket::winterDimensions);

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
