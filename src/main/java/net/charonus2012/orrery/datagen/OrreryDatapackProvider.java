package net.charonus2012.orrery.datagen;

import net.charonus2012.orrery.OrreryMod;
import net.charonus2012.orrery.worldgen.OrreryBiomeModifiers;
import net.charonus2012.orrery.worldgen.OrreryConfiguredFeatures;
import net.charonus2012.orrery.worldgen.OrreryPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OrreryDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, OrreryConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, OrreryPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, OrreryBiomeModifiers::bootstrap);

    public OrreryDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(OrreryMod.MOD_ID));
    }
}
