package com.alcatrazescapee.primalwinter.platform.client;

import com.alcatrazescapee.primalwinter.platform.XPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

public interface XPlatformClient {
    XPlatformClient INSTANCE = XPlatform.find(XPlatformClient.class);

    void setRenderType(Block block, RenderType type);
}
