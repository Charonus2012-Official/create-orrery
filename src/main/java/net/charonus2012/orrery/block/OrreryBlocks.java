package net.charonus2012.orrery.block;

import net.charonus2012.orrery.item.OrreryItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryBlocks {

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);

    public static final Map<String, Map<String, DeferredBlock<Block>>> PLANET_BLOCKS = new HashMap<>();

    public static void registerPlanetBlockSet(String planetName, List<String> ores) {
        Map<String, DeferredBlock<Block>> blocks = new HashMap<>();

        blocks.put("stone", registerBlock(planetName + "_stone",
                () -> new Block(BlockBehaviour.Properties.of().strength(1.5f, 6.0f).requiresCorrectToolForDrops())));

        blocks.put("sand", registerBlock(planetName + "_sand",
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SAND).strength(0.5f))));

        for (String ore : ores) {
            String key = ore + "_ore";
            blocks.put(key, registerBlock(planetName + "_" + key,
                    () -> new Block(BlockBehaviour.Properties.of().strength(3f).requiresCorrectToolForDrops())));
        }

        PLANET_BLOCKS.put(planetName, blocks);
    }

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        OrreryItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
