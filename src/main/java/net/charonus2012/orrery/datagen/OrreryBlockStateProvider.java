package net.charonus2012.orrery.datagen;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.Map;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryBlockStateProvider extends BlockStateProvider {

    public OrreryBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planet = planetEntry.getKey();
            Map<String, DeferredBlock<Block>> blocks = planetEntry.getValue();

            // Stone & sand: simple cube_all using a base texture named after the block
            simpleBlockWithItem(blocks.get("stone").get(),
                    cubeAll(blocks.get("stone").get()));
            simpleBlockWithItem(blocks.get("sand").get(),
                    cubeAll(blocks.get("sand").get()));

            // Ores: cube_all + overlay
            for (var entry : blocks.entrySet()) {
                if (entry.getKey().endsWith("_ore")) {
                    String oreType = entry.getKey().replace("_ore", "");
                    Block oreBlock = entry.getValue().get();

                    ModelFile model = models().withExistingParent(
                                    BuiltInRegistries.BLOCK.getKey(oreBlock).getPath(),
                                    ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/ore_overlay"))
                            .texture("all", ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/" + planet + "_stone"))
                            .texture("overlay", ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/overlays/z_" + oreType + "_overlay"));

                    simpleBlockWithItem(oreBlock, model);
                }
            }
        }
    }
}
