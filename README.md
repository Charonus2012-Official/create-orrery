# Create: Orrery

A data-driven planet framework for [Create: Northstar Redux](https://github.com/Astronauts-of-Create/Northstar-Redux), adding new explorable worlds to your Create space-exploration experience.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.228+
- [Create](https://www.curseforge.com/minecraft/mc-mods/create) 6.0.10+
- [Create: Northstar Redux](https://modrinth.com/mod/northstar-redux) 0.5.4+1.21.1+

## Planets

### Pluto

The most famous dwarf planet, sitting on the inner edge of the Kuiper Belt. Pluto's surface is a frozen expanse of nitrogen ice and dark reddish sand, broken by gentle rolling hills.

- Very low gravity (~0.063g)
- No oxygen — life support required
- Custom sky: stars always visible, Sun reduced to a bright point of light
- Surface of snow and pluto sand over stone
- Full ore set with overlay textures shared with Northstar

### Eris

The most massive known dwarf planet, drifting far out in the scattered disc. Eris is a desolate, airless world with a faint and distant Sun barely brighter than a star.

- Greatly reduced gravity (~0.084g), with fall damage scaled accordingly
- No oxygen — life support required
- Custom sky: stars always visible, heavily dimmed Sun
- Surface of moon sand over stone
- Full ore set with overlay textures shared with Northstar

## How It Works

Orrery is built around a data-driven planet registry. Each planet is described by a JSON file that Minecraft loads at runtime — meaning new planets can be added through datapacks without writing any new Java code for standard cases.

**The core pieces:**

- `PlanetData` — a record holding a planet's gravity, temperature, atmosphere cost, sky appearance, and other properties
- `PlanetRegistry` — an in-memory map of all loaded planets, keyed by dimension
- `PlanetDataLoader` — a resource reload listener that reads `PlanetData` from `data/<namespace>/orrery_planets/<planet>.json`

Generic mixins for sky rendering, fall damage, and Northstar's planet-property hooks all read from this registry. Adding a new standard planet means:

1. A dimension, biome, and worldgen JSON (standard Minecraft datapack format)
2. An `orrery_planets` JSON for gravity, sky, and other properties
3. A block set registration for the planet's stone, sand, and ores

No new mixins or Java code needed unless the planet has truly novel mechanics.

### Planet JSON format

```json
{
  "dimension": "orrery:eris",
  "planet_name": "Eris",
  "gravity": 0.084,
  "has_oxygen": false,
  "temperature": -230,
  "atmosphere_cost": 0,
  "computing_cost": 900,
  "engine_constant": 15.0,
  "sun_multiplier": 0.0,
  "has_weather": false,
  "seed_offset": 9,
  "wind_multiplier": 0.0,
  "has_sky": true,
  "sky": {
    "has_stars": true,
    "can_see_sky_at_day": true,
    "star_brightness": 1.0,
    "sun_brightness": 0.6
  }
}
```

## Commands

`/orrery` — lists all currently loaded planets and their key properties.

## Building

```
./gradlew build
```

```
./gradlew runClient
./gradlew runServer
```

Generate data (block states, models, language, tags, loot tables):

```
./gradlew runData
```

## Credits

Built on top of [Create](https://github.com/Creators-of-Create/Create) and [Create: Northstar Redux](https://github.com/Astronauts-of-Create/Northstar-Redux). Ore overlay textures adapted from Northstar Redux.

## License

MIT — see `LICENSE`.
