package net.charonus2012.orrery.util;

import net.charonus2012.orrery.OrreryMod;
import net.charonus2012.orrery.block.OrreryBlocks;
import net.charonus2012.orrery.item.OrreryItems;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class OrreryTags {
    public static class Blocks {
        public static Map<String, TagKey<Block>> PLANET_ORE_TAGS = new HashMap<>();

        static {
            for (String PlanetName : OrreryBlocks.PLANET_BLOCKS.keySet()) {
                PLANET_ORE_TAGS.put(PlanetName, createTag(PlanetName + "_ores"));
            }
        }

        public static final TagKey<Block> COAL_ORES = createTag("coal_ores");
        public static final TagKey<Block> COPPER_ORES = createTag("copper_ores");
        public static final TagKey<Block> DIAMOND_ORES = createTag("diamond_ores");
        public static final TagKey<Block> EMERALD_ORES = createTag("emerald_ores");
        public static final TagKey<Block> GLOWSTONE_ORES = createTag("glowstone_ores");
        public static final TagKey<Block> GOLD_ORES = createTag("gold_ores");
        public static final TagKey<Block> IRON_ORES = createTag("iron_ores");
        public static final TagKey<Block> LAPIS_ORES = createTag("lapis_ores");
        public static final TagKey<Block> QUARTZ_ORES = createTag("quartz_ores");
        public static final TagKey<Block> REDSTONE_ORES = createTag("redstone_ores");
        public static final TagKey<Block> TITANIUM_ORES = createTag("titanium_ores");
        public static final TagKey<Block> TUNGSTEN_ORES = createTag("tungsten_ores");
        public static final TagKey<Block> ZINC_ORES = createTag("zinc_ores");

        public static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(OrreryMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static Map<String, TagKey<Item>> PLANET_ORE_TAGS = new HashMap<>();

        static {
            for (String PlanetName : OrreryBlocks.PLANET_BLOCKS.keySet()) {
                PLANET_ORE_TAGS.put(PlanetName, createTag(PlanetName + "_ores"));
            }
        }
        public static final TagKey<Item> COAL_ORES = createTag("coal_ores");
        public static final TagKey<Item> COPPER_ORES = createTag("copper_ores");
        public static final TagKey<Item> DIAMOND_ORES = createTag("diamond_ores");
        public static final TagKey<Item> EMERALD_ORES = createTag("emerald_ores");
        public static final TagKey<Item> GLOWSTONE_ORES = createTag("glowstone_ores");
        public static final TagKey<Item> GOLD_ORES = createTag("gold_ores");
        public static final TagKey<Item> IRON_ORES = createTag("iron_ores");
        public static final TagKey<Item> LAPIS_ORES = createTag("lapis_ores");
        public static final TagKey<Item> QUARTZ_ORES = createTag("quartz_ores");
        public static final TagKey<Item> REDSTONE_ORES = createTag("redstone_ores");
        public static final TagKey<Item> TITANIUM_ORES = createTag("titanium_ores");
        public static final TagKey<Item> TUNGSTEN_ORES = createTag("tungsten_ores");
        public static final TagKey<Item> ZINC_ORES = createTag("zinc_ores");

        public static TagKey<Item> createTag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(OrreryMod.MOD_ID, name));
        }
    }
}
