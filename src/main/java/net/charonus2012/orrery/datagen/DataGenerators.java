package net.charonus2012.orrery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

@EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(event.includeClient(),
                new OrreryBlockStateProvider(output, existingFileHelper));

        generator.addProvider(event.includeClient(),
                new OrreryLanguageProvider(output));

        OrreryBlockTagProvider blockTagProvider = generator.addProvider(event.includeServer(),
                new OrreryBlockTagProvider(output, event.getLookupProvider(), existingFileHelper));

        generator.addProvider(event.includeServer(),
                new OrreryItemTagProvider(output, event.getLookupProvider(), blockTagProvider.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeClient(),
                new OrreryItemModelProvider(output, existingFileHelper));

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(OrreryBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        generator.addProvider(event.includeServer(), new OrreryDatapackProvider(output, lookupProvider));

        generator.addProvider(event.includeServer(), new OrreryRecipeProvider(output, lookupProvider));

        generator.addProvider(event.includeServer(), new OrreryCrushingRecipeProvider(output));
    }
}
