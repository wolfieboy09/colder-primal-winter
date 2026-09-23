package com.alcatrazescapee.primalwinter.registries;

import com.alcatrazescapee.primalwinter.PrimalWinter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class PrimalWinterItemGroups {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PrimalWinter.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = TABS.register("items", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(PrimalWinterBlocks.SNOWY_DIRT.get()))
            .title(Component.translatable("primalwinter.items"))
            .displayItems((params, output) -> {
                PrimalWinterBlocks.ITEMS.getEntries().forEach(item -> output.accept(item.get()));
            })
            .build());
}
