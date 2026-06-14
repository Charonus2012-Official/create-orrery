# Create: Orrery

A data-driven planet framework for [Create: Northstar Redux](https://github.com/Astronauts-of-Create/Northstar-Redux), adding new explorable worlds to your Create space-exploration experience.

Orrery extends Northstar's planet system so that new celestial bodies — dwarf planets, moons, and beyond — can be added largely through JSON datapacks rather than custom Java/Mixin code for each one.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.228+
- [Create](https://www.curseforge.com/minecraft/mc-mods/create) 6.0.10+
- [Create: Northstar Redux](https://modrinth.com/mod/northstar-redux) 0.5.4+1.21.1+

## Currently Implemented

### Eris

The dwarf planet Eris, one of the most distant known objects in the scattered disc, is now a landable dimension.

- Custom black, star-filled sky with a dimmed, distant Sun (no atmospheric scattering)
- Surface composed of moon sand over stone, brightened/tinted for a distinct look
- Greatly reduced gravity (~0.084g), with fall damage scaled accordingly
- No native oxygen — electrolysis-based life support recommended
- AE2 meteorite generation, with all other structures (igloos, villages, etc.) disabled
- Appears correctly in the Northstar Telescope, including a fix for Pluto's rendering position bug

### Eris Ores

A full ore set for Eris, sharing overlay textures with Northstar's existing materials.

## Planned

Orrery's planet framework is built to scale. Future planets, moons, and dwarf planets are planned, including (subject to change):

- **Pluto** — full landable implementation (currently only a telescope entry via Northstar)
- **Ceres** — largest object in the asteroid belt
- Various moons of the outer planets
- Additional dwarf planets and Kuiper Belt objects

Each new body will reuse the same data-driven planet system described below, minimizing the amount of custom code required per planet.

## The Planet Framework

At the core of Orrery is a data-driven planet registry:

- **`PlanetData`** — a record describing a planet's gravity, temperature, atmosphere, sky appearance (stars, sun brightness), orbital properties, and more
- **`PlanetRegistry`** — an in-memory registry of all loaded planets, keyed by dimension
- **`PlanetDataLoader`** — a resource reload listener that loads `PlanetData` from datapack JSON files at `data/<namespace>/orrery_planets/<planet>.json`

Generic Mixins (sky rendering, fall damage, and Northstar's planet-property hooks) read from this registry, so a new planet can be added by:

1. Creating a dimension + biome + worldgen JSON (standard Minecraft datapack format)
2. Adding a `PlanetData` JSON describing its gravity, sky, and other properties
3. Registering a block set (stone/sand/ores) via the block framework, if the planet needs unique materials

No new Mixins are required for standard planets — only truly novel mechanics need custom code.

### Example `orrery_planets` entry

```json
{
  "dimension": "orrery:eris",
  "planet_name": "eris",
  "gravity": 0.084,
  "has_oxygen": false,
  "temperature": -230,
  "atmosphere_cost": 0,
  "computing_cost": 900,
  "engine_constant": 15.0,
  "sun_multiplier": 0.0,
  "has_sky": true,
  "can_see_sky_at_day": true,
  "has_weather": false,
  "seed_offset": 9,
  "is_in_orbit": true,
  "wind_multiplier": 0.0,
  "sky": {
    "has_stars": true,
    "star_brightness": 1.0,
    "sun_brightness": 0.6
  }
}
```

## Commands

- `/orrery` — lists all currently loaded planets and their key properties (gravity, dimension key, etc.)

## Building from Source

```
./gradlew build
```

Run the client/server dev environment with:

```
./gradlew runClient
./gradlew runServer
```

Generate data (block states, models, language, tags, loot tables) with:

```
./gradlew runData
```

## Documentation

A full wiki covering the planet datapack format, block set generation, and contribution guidelines is in progress.

## Credits

- Built on top of [Create](https://github.com/Creators-of-Create/Create) and [Create: Northstar Redux](https://github.com/Astronauts-of-Create/Northstar-Redux)
- Ore overlay textures adapted from Northstar Redux

## License

MIT License, visit `LICENSE` for full license.
