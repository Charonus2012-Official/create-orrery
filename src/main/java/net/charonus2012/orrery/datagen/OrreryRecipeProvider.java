package net.charonus2012.orrery.datagen;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.charonus2012.orrery.util.OrreryTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryRecipeProvider extends RecipeProvider {
    public OrreryRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        java.util.Set<String> oreTypes = new java.util.HashSet<>();
        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.values()) {
            for (String key : planetEntry.keySet()) {
                if (key.endsWith("_ore")) {
                    oreTypes.add(key.replace("_ore", ""));
                }
            }
        }

        for (String oreType : oreTypes) {
            Item result = getSmeltResult(oreType);
            if (result == null) continue;

            TagKey<Item> oreTag = OrreryTags.Items.createTag(oreType + "_ores");

            SimpleCookingRecipeBuilder
                    .smelting(Ingredient.of(oreTag), RecipeCategory.MISC, result, 0.7f, 200)
                    .unlockedBy("has_ore", has(oreTag))
                    .save(output, ResourceLocation.fromNamespaceAndPath(MOD_ID, "smelting/" + oreType));

            SimpleCookingRecipeBuilder
                    .blasting(Ingredient.of(oreTag), RecipeCategory.MISC, result, 0.7f, 100)
                    .unlockedBy("has_ore", has(oreTag))
                    .save(output, ResourceLocation.fromNamespaceAndPath(MOD_ID, "blasting/" + oreType));
        }
    }

    private Item getSmeltResult(String oreType) {
        return switch (oreType) {
            case "iron" -> Items.IRON_INGOT;
            case "gold" -> Items.GOLD_INGOT;
            case "copper" -> Items.COPPER_INGOT;
            case "zinc" -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("create", "zinc_ingot"));
            case "titanium", "tungsten" -> BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("northstar", oreType + "_ingot"));
            case "glowstone" -> Items.GLOWSTONE_DUST;
            case "coal" -> Items.COAL;
            case "diamond" -> Items.DIAMOND;
            case "emerald" -> Items.EMERALD;
            case "lapis" -> Items.LAPIS_LAZULI;
            case "quartz" -> Items.QUARTZ;
            case "redstone" -> Items.REDSTONE;
            default -> null;
        };
    }
}
