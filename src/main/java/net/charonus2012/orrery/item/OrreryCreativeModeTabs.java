package net.charonus2012.orrery.item;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryCreativeModeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ORRERY_TAB =
            CREATIVE_MODE_TAB.register("orrery_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("creativetab.orrery.blocks"))
                    .icon(() -> new ItemStack(OrreryBlocks.PLANET_BLOCKS.get("pluto").get("redstone_ore").get()))
                    .displayItems((parameters, output) -> {
                        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
                            for (var blockEntry : planetEntry.getValue().entrySet()) {
                                output.accept(blockEntry.getValue().get());
                            }
                        }
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
