package net.charonus2012.orrery.planet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlanetRegistry {
    private static final Map<ResourceKey<Level>, PlanetData> PLANETS = new HashMap<>();
    private static final List<String> TexturesFromNorthstar = List.of("mercury", "venus", "earth", "mars", "ceres", "jupiter", "saturn", "uranus", "neptune", "pluto", "eris");

    public static final List<String> PlanetName = List.of("mercury", "venus", "earth", "mars", "ceres", "jupiter", "saturn", "uranus", "neptune", "pluto", "eris");
    public static Map<String, ResourceLocation> SkyPlanetTextures = new HashMap<>();

    public static Map<String, Double> ORBITAL_SPEEDS = Map.ofEntries(
            Map.entry("mercury",     4.15),
            Map.entry("venus",       1.62),
            Map.entry("earth",       1.0),
            Map.entry("earth_moon",  13.37),  // moon orbits earth ~13x per year
            Map.entry("moon",        13.37),
            Map.entry("mars",        0.53),
            Map.entry("ceres",       0.30),
            Map.entry("jupiter",     0.084),
            Map.entry("saturn",      0.034),
            Map.entry("uranus",      0.012),
            Map.entry("neptune",     0.006),
            Map.entry("pluto",       0.004),
            Map.entry("eris",        0.003)
    );

    static {
        for (String planet : PlanetName) {
            if (TexturesFromNorthstar.contains(planet)) {
                SkyPlanetTextures.put(
                        planet,
                        ResourceLocation.parse(
                                "northstar:textures/environment/" + planet + "_far.png"
                        )
                );
            } else {
                SkyPlanetTextures.put(
                        planet,
                        ResourceLocation.parse(
                                "northstar:textures/environment/" + planet + "_close.png"
                        )
                );
            }
        }
    }

    public static void register(PlanetData data) {
        PLANETS.put(data.dimension(), data);
    }

    public static Optional<PlanetData> get(ResourceKey<Level> dimension) {
        return Optional.ofNullable(PLANETS.get(dimension));
    }

    public static String getPlanetName(ResourceKey<Level> dimension) {
        return get(dimension)
                .map(PlanetData::planetName)
                .orElse(null);
    }

    public static boolean isPlanet(ResourceKey<Level> dimension) {
        return PLANETS.containsKey(dimension);
    }

    public static Map<ResourceKey<Level>, PlanetData> all() {
        return Map.copyOf(PLANETS);
    }

    public static void clear() {
        PLANETS.clear();
    }
}
