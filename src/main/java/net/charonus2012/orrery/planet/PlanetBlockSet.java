package net.charonus2012.orrery.planet;

import java.util.List;

public record PlanetBlockSet(
        String planetName,
        String displayName,
        List<String> oreOverlays
) {
}
