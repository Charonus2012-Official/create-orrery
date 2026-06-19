package net.charonus2012.orrery;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.charonus2012.orrery.command.PlanetCommands;
import net.charonus2012.orrery.item.OrreryCreativeModeTabs;
import net.charonus2012.orrery.item.OrreryItems;
import net.charonus2012.orrery.planet.PlanetDataLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

import java.util.List;

@Mod(OrreryMod.MOD_ID)
public class OrreryMod {
    public static final String MOD_ID = "orrery";
    public static final Logger LOGGER = LogUtils.getLogger();



    public OrreryMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        // Planet BlockSets Definitions
        OrreryBlocks.registerPlanetBlockSet("pluto", List.of("coal", "copper", "diamond", "emerald", "glowstone", "gold", "iron", "lapis", "quartz", "redstone", "titanium", "tungsten", "zinc"));
        OrreryBlocks.registerPlanetBlockSet("eris", List.of("coal", "copper", "diamond", "emerald", "glowstone", "gold", "iron", "lapis", "quartz", "redstone", "titanium", "tungsten", "zinc"));

        OrreryBlocks.register(modEventBus);
        OrreryItems.register(modEventBus);
        OrreryCreativeModeTabs.register(modEventBus);

    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new PlanetDataLoader());
    }
    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        PlanetCommands.register(event.getDispatcher());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM ORRERY!");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO FROM ORRERY server starting");
    }
}
