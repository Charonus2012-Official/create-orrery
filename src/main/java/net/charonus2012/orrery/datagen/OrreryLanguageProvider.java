package net.charonus2012.orrery.datagen;

import net.charonus2012.orrery.block.OrreryBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

import static net.charonus2012.orrery.OrreryMod.MOD_ID;

public class OrreryLanguageProvider extends LanguageProvider {

    public OrreryLanguageProvider(PackOutput output) {
        super(output, MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        for (var planetEntry : OrreryBlocks.PLANET_BLOCKS.entrySet()) {
            String planet = planetEntry.getKey();
            String displayPlanet = capitalize(planet); // "Eris"

            for (var entry : planetEntry.getValue().entrySet()) {
                String key = entry.getKey(); // "stone", "sand", "zinc_ore"
                String displayKey = capitalize(key.replace("_", " ")).replace("ore", "Ore"); // "Stone", "Sand", "Zinc Ore"
                add(entry.getValue().get(), displayPlanet + " " + displayKey); // "Eris Stone", "Eris Zinc Ore"
            }
        }
        add("creativetab.orrery.blocks", "Orrery Blocks");
    }

    private static String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
