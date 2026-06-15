package net.charonus2012.orrery.worldgen;

import net.charonus2012.orrery.OrreryMod;
import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class OrreryBiomeModifiers {
    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planet = planetEntry.getKey();

            // Attempts to find "northstar:eris", etc.
            ResourceKey<net.minecraft.world.level.biome.Biome> planetBiomeKey =
                    ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("orrery", planet + "_surface"));

            var biomeHolder = biomes.get(planetBiomeKey);
            if (biomeHolder.isEmpty()) continue; // Safely skip if the planet's dimension registry isn't loaded

            for (var blockEntry : planetEntry.getValue().entrySet()) {
                String blockKey = blockEntry.getKey();
                if (!blockKey.endsWith("_ore")) continue;

                String oreType = blockKey.replace("_ore", "");
                String featureName = "ore_" + planet + "_" + oreType;

                ResourceKey<PlacedFeature> placedKey = OrreryPlacedFeatures.registerKey(featureName);
                ResourceKey<BiomeModifier> modifierKey = registerKey("add_" + featureName);

                context.register(modifierKey, new BiomeModifiers.AddFeaturesBiomeModifier(
                        HolderSet.direct(biomeHolder.get()),
                        HolderSet.direct(placedFeatures.getOrThrow(placedKey)),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                ));
            }
        }
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(OrreryMod.MOD_ID, name));
    }
}
