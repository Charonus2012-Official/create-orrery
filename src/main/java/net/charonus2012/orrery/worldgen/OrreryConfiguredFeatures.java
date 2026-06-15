package net.charonus2012.orrery.worldgen;

import net.charonus2012.orrery.OrreryMod;
import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.List;

import static net.charonus2012.orrery.OrreryMod.LOGGER;

public class OrreryConfiguredFeatures {
    private static final int VEIN_SIZE = 9;
    private static final float DISCARD_CHANCE_ON_AIR_EXPOSURE = 0.0f;

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planet = planetEntry.getKey();
            Block stoneBlock = planetEntry.getValue().get("stone").get();
            RuleTest replaceTarget = new BlockMatchTest(stoneBlock);

            for (var blockEntry : planetEntry.getValue().entrySet()) {
                String blockKey = blockEntry.getKey();
                if (!blockKey.endsWith("_ore")) continue;
                LOGGER.info("Configured Feature: " + blockKey);

                String oreType = blockKey.replace("_ore", "");
                // Produces: ore_eris_copper, ore_eris_zinc, etc.
                String featureName = "ore_" + planet + "_" + oreType;
                Block oreBlock = blockEntry.getValue().get();

                ResourceKey<ConfiguredFeature<?, ?>> key = registerKey(featureName);

                context.register(key, new ConfiguredFeature<>(Feature.ORE,
                        new OreConfiguration(
                                List.of(OreConfiguration.target(replaceTarget, oreBlock.defaultBlockState())),
                                VEIN_SIZE,
                                DISCARD_CHANCE_ON_AIR_EXPOSURE
                        )
                ));
            }
        }
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE,
                ResourceLocation.fromNamespaceAndPath(OrreryMod.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
