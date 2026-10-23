package com.alcatrazescapee.primalwinter.client;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = PrimalWinter.MOD_ID, dist = Dist.CLIENT)
public class PrimalWinterClient {
    public PrimalWinterClient(IEventBus bus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    public static void reloadRenderer() {
        ((ReloadableLevelRenderer) Minecraft.getInstance().levelRenderer).primalWinter$reload();
    }
}
