package net.charonus2012.orrery.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryItemModelProvider extends ItemModelProvider {

    public OrreryItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (var planetEntry : net.charonus2012.orrery.block.OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            for (var entry : planetEntry.getValue().entrySet()) {
                String blockName = entry.getValue().get().builtInRegistryHolder().key().location().getPath();
                withExistingParent(blockName, modLoc("block/" + blockName));
            }
        }
    }
}
