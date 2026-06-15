package net.charonus2012.orrery.worldgen;

import net.charonus2012.orrery.OrreryMod;
import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class OrreryPlacedFeatures {

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        var configuredLookup = context.lookup(Registries.CONFIGURED_FEATURE);

        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planet = planetEntry.getKey();

            for (var blockEntry : planetEntry.getValue().entrySet()) {
                String blockKey = blockEntry.getKey();
                if (!blockKey.endsWith("_ore")) continue;

                String oreType = blockKey.replace("_ore", "");
                String featureName = "ore_" + planet + "_" + oreType;

                // Create keys with proper mod namespace identifiers
                ResourceKey<ConfiguredFeature<?, ?>> configuredKey = OrreryConfiguredFeatures.registerKey(featureName);
                ResourceKey<PlacedFeature> placedKey = registerKey(featureName);

                Holder<ConfiguredFeature<?, ?>> configuredHolder = configuredLookup.getOrThrow(configuredKey);

                // --- LOWERED UNIFORM DEPTH CONFIGURATION ---
                // Pushes generation limits down near the bottom of the world boundaries
                int minY, maxY, veinSize, count;

                // Define settings dynamically by ore tier to reduce manual copy-pasting
                if (oreType.equals("coal") || oreType.equals("zinc")) {
                    minY = 40; maxY = 120; veinSize = 9; count = 8; // Common Tier
                } else if (oreType.equals("tungsten") || oreType.equals("diamond")) {
                    minY = -64; maxY = 20; veinSize = 4; count = 3;  // Rare Tier
                } else {
                    minY = 0; maxY = 64; veinSize = 6; count = 6;   // Standard Tier
                }

                context.register(placedKey, new PlacedFeature(
                        configuredHolder,
                        List.of(
                                CountPlacement.of(count),
                                InSquarePlacement.spread(),
                                HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)),
                                BiomeFilter.biome()
                        )
                ));
            }
        }
    }

    // Inside net.charonus2012.orrery.worldgen.OrreryPlacedFeatures
    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(OrreryMod.MOD_ID, name));
    }
}
