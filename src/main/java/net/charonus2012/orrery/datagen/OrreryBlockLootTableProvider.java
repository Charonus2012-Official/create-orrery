package net.charonus2012.orrery.datagen;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;

public class OrreryBlockLootTableProvider extends BlockLootSubProvider {
    protected OrreryBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            for (var entry : planetEntry.getValue().entrySet()) {
                String key = entry.getKey();
                Block block = entry.getValue().get();

                if (key.endsWith("_ore")) {
                    String oreType = key.replace("_ore", "");
                    List<String> northstar_blocks = List.of("titanium", "tungsten", "glowstone");
                    List<String> create_blocks = List.of("zinc");
                    List<String> no_raw = List.of("diamond", "redstone", "quartz", "coal");
                    Item drop;
                    int min = 1;
                    int max = 1;

                    if (northstar_blocks.contains(oreType)) {
                        drop = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("northstar", "raw_" + key));
                    } else if (create_blocks.contains(oreType)) {
                        drop = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "raw_" + oreType));
                    } else if (no_raw.contains(oreType)) {
                        drop = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", oreType));
                        if (oreType.equals("redstone")) {
                            min = 4;
                            max = 5;
                        }
                    } else if (oreType.equals("lapis")) {
                        drop = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", oreType + "_lazuli"));
                        min = 4;
                        max = 9;
                    } else {
                        drop = BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("minecraft", "raw_" + oreType));
                        if (oreType.equals("copper")) {
                            min = 2;
                            max = 5;
                        }
                    }
                    int finalMin = min;
                    int finalMax = max;
                    add(block, b -> createMultipleOreDrops(block, drop, finalMin, finalMax));
                } else {
                    dropSelf(block);
                }
            }
        }
    }

    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return OrreryBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
