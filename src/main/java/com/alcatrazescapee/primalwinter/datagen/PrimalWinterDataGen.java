package com.alcatrazescapee.primalwinter.datagen;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = PrimalWinter.MOD_ID)
public class PrimalWinterDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

    }
}
