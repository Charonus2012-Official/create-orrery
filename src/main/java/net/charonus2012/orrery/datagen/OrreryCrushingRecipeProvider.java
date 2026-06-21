package net.charonus2012.orrery.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryCrushingRecipeProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public OrreryCrushingRecipeProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipe");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();

        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planetName = planetEntry.getKey();
            Item stone = planetEntry.getValue().get("stone").get().asItem();

            for (var entry : planetEntry.getValue().entrySet()) {
                String key = entry.getKey();
                if (!key.endsWith("_ore")) continue;

                String oreType = key.replace("_ore", "");
                Block oreBlock = entry.getValue().get();

                futures.add(saveCrushingRecipe(output, planetName, oreType, oreBlock, stone));
            }
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private CompletableFuture<?> saveCrushingRecipe(CachedOutput output, String planetName, String oreType, Block oreBlock, Item stone) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "create:crushing");

        JsonArray ingredients = new JsonArray();
        JsonObject ingredient = new JsonObject();
        ingredient.addProperty("item", BuiltInRegistries.BLOCK.getKey(oreBlock).toString());
        ingredients.add(ingredient);
        json.add("ingredients", ingredients);

        JsonArray results = new JsonArray();

        CrushingInfo info = getCrushingInfo(oreType);
        if (info == null) return CompletableFuture.completedFuture(null);

        // Primary result
        results.add(createResult(info.item, info.count, 1.0f));
        // Bonus 75%
        results.add(createResult(info.item, 1, 0.75f));
        // Experience nugget 50%
        results.add(createResult(ResourceLocation.fromNamespaceAndPath("create", "experience_nugget"), 1, 0.5f));
        // Planet stone 12%
        results.add(createResult(BuiltInRegistries.ITEM.getKey(stone), 1, 0.12f));

        json.add("results", results);
        json.addProperty("processingTime", 250);

        Path path = pathProvider.json(ResourceLocation.fromNamespaceAndPath(MOD_ID, "crushing/" + planetName + "_" + oreType));
        return DataProvider.saveStable(output, json, path);
    }

    private JsonObject createResult(ResourceLocation item, int count, float chance) {
        JsonObject res = new JsonObject();
        res.addProperty("id", item.toString());
        if (count > 1) res.addProperty("count", count);
        if (chance < 1.0f) res.addProperty("chance", chance);
        return res;
    }

    private JsonObject createResult(Item item, int count, float chance) {
        return createResult(BuiltInRegistries.ITEM.getKey(item), count, chance);
    }

    private CrushingInfo getCrushingInfo(String oreType) {
        return switch (oreType) {
            case "iron" -> new CrushingInfo(ResourceLocation.fromNamespaceAndPath("create", "crushed_raw_iron"), 1);
            case "gold" -> new CrushingInfo(ResourceLocation.fromNamespaceAndPath("create", "crushed_raw_gold"), 1);
            case "copper" -> new CrushingInfo(ResourceLocation.fromNamespaceAndPath("create", "crushed_raw_copper"), 2);
            case "zinc" -> new CrushingInfo(ResourceLocation.fromNamespaceAndPath("create", "crushed_raw_zinc"), 1);
            case "tungsten" -> new CrushingInfo(ResourceLocation.fromNamespaceAndPath("northstar", "crushed_raw_tungsten"), 1);
            case "titanium" -> new CrushingInfo(ResourceLocation.fromNamespaceAndPath("northstar", "raw_titanium"), 1);
            case "redstone" -> new CrushingInfo(BuiltInRegistries.ITEM.getKey(Items.REDSTONE), 7);
            case "lapis" -> new CrushingInfo(BuiltInRegistries.ITEM.getKey(Items.LAPIS_LAZULI), 7);
            case "coal" -> new CrushingInfo(BuiltInRegistries.ITEM.getKey(Items.COAL), 1);
            case "diamond" -> new CrushingInfo(BuiltInRegistries.ITEM.getKey(Items.DIAMOND), 1);
            case "emerald" -> new CrushingInfo(BuiltInRegistries.ITEM.getKey(Items.EMERALD), 1);
            case "quartz" -> new CrushingInfo(BuiltInRegistries.ITEM.getKey(Items.QUARTZ), 1);
            case "glowstone" -> new CrushingInfo(ResourceLocation.fromNamespaceAndPath("northstar", "raw_glowstone_ore"), 2);
            default -> null;
        };
    }

    private record CrushingInfo(ResourceLocation item, int count) {}

    @Override
    public String getName() {
        return "Orrery Crushing Recipes";
    }
}
