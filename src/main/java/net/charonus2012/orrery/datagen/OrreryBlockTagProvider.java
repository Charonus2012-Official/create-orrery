package net.charonus2012.orrery.datagen;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.charonus2012.orrery.util.OrreryTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryBlockTagProvider extends BlockTagsProvider {

    public OrreryBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        List<String> basic = List.of("coal", "copper", "titanium", "glowstone", "tungsten");
        List<String> stone = List.of("iron", "lapis", "redstone");

        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planetName = planetEntry.getKey();
            for (var entry : planetEntry.getValue().entrySet()) {
                String key = entry.getKey();
                Block block = entry.getValue().get();
                LOGGER.info(key);

                if (key.endsWith("_ore")) {
                    String oreType = key.replace("_ore", "");
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                    tag(Tags.Blocks.ORES).add(block);

                    tag(OrreryTags.Blocks.createTag(planetName + "_ores")).add(block);
                    tag(OrreryTags.Blocks.createTag(oreType + "_ores")).add(block);

                    if (!basic.contains(oreType)) {
                        if (stone.contains(oreType)) {
                            tag(BlockTags.NEEDS_STONE_TOOL).add(block);
                        } else {
                            tag(BlockTags.NEEDS_IRON_TOOL).add(block);
                        }
                    }

                    // Map to common ore-type tags, e.g. c:ores/zinc
                    tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "ores/" + oreType)))
                            .add(block);
                } else if (key.endsWith("sand")) {
                    tag(BlockTags.MINEABLE_WITH_SHOVEL).add(block);
                } else {
                    tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
                }
            }
        }
    }
}
