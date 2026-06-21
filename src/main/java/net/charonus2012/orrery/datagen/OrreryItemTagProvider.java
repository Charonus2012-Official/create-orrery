package net.charonus2012.orrery.datagen;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.charonus2012.orrery.util.OrreryTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryItemTagProvider extends ItemTagsProvider {
    public OrreryItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planetName = planetEntry.getKey();
            for (var entry : planetEntry.getValue().entrySet()) {
                String key = entry.getKey();
                Block block = entry.getValue().get();

                if (key.endsWith("_ore")) {
                    String oreType = key.replace("_ore", "");
                    tag(OrreryTags.Items.createTag(planetName + "_ores")).add(block.asItem());
                    tag(OrreryTags.Items.createTag(oreType + "_ores")).add(block.asItem());
                }
            }
        }
    }
}
