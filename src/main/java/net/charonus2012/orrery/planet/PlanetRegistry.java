package net.charonus2012.orrery.planet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlanetRegistry {
    private static final Map<ResourceKey<Level>, PlanetData> PLANETS = new HashMap<>();

    public static void register(PlanetData data) {
        PLANETS.put(data.dimension(), data);
    }

    public static Optional<PlanetData> get(ResourceKey<Level> dimension) {
        return Optional.ofNullable(PLANETS.get(dimension));
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
