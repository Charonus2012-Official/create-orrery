package net.charonus2012.orrery.planet;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public record PlanetData(
        ResourceKey<Level> dimension,
        String planetName,
        double gravity,
        boolean hasOxygen,
        int temperature,
        int atmosphereCost,
        int computingCost,
        double engineConstant,
        float sunMultiplier,
        boolean hasSky,
        boolean canSeeSkyAtDay,
        boolean hasWeather,
        long seedOffset,
        boolean isInOrbit,
        float windMultiplier,
        // sky rendering
        boolean hasStars,
        float starBrightness,
        float sunBrightness
) {
    public static final Codec<PlanetData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(PlanetData::dimension),
            Codec.STRING.fieldOf("planet_name").forGetter(PlanetData::planetName),
            Codec.DOUBLE.fieldOf("gravity").forGetter(PlanetData::gravity),
            Codec.BOOL.fieldOf("has_oxygen").forGetter(PlanetData::hasOxygen),
            Codec.INT.fieldOf("temperature").forGetter(PlanetData::temperature),
            Codec.INT.fieldOf("atmosphere_cost").forGetter(PlanetData::atmosphereCost),
            Codec.INT.fieldOf("computing_cost").forGetter(PlanetData::computingCost),
            Codec.DOUBLE.fieldOf("engine_constant").forGetter(PlanetData::engineConstant),
            Codec.FLOAT.fieldOf("sun_multiplier").forGetter(PlanetData::sunMultiplier),
            Codec.BOOL.fieldOf("has_sky").forGetter(PlanetData::hasSky),
            Codec.BOOL.fieldOf("can_see_sky_at_day").forGetter(PlanetData::canSeeSkyAtDay),
            Codec.BOOL.fieldOf("has_weather").forGetter(PlanetData::hasWeather),
            Codec.LONG.fieldOf("seed_offset").forGetter(PlanetData::seedOffset),
            Codec.BOOL.fieldOf("is_in_orbit").forGetter(PlanetData::isInOrbit),
            Codec.FLOAT.fieldOf("wind_multiplier").forGetter(PlanetData::windMultiplier),
            PlanetData.SkyData.CODEC.fieldOf("sky").forGetter(p -> new SkyData(p.hasStars(), p.starBrightness(), p.sunBrightness()))
    ).apply(instance, (dimension, planetName, gravity, hasOxygen, temperature, atmosphereCost, computingCost, engineConstant, sunMultiplier, hasSky, canSeeSkyAtDay, hasWeather, seedOffset, isInOrbit, windMultiplier, sky) ->
            new PlanetData(dimension, planetName, gravity, hasOxygen, temperature, atmosphereCost, computingCost, engineConstant, sunMultiplier, hasSky, canSeeSkyAtDay, hasWeather, seedOffset, isInOrbit, windMultiplier, sky.hasStars(), sky.starBrightness(), sky.sunBrightness())
    ));

    private record SkyData(boolean hasStars, float starBrightness, float sunBrightness) {
    static final Codec<SkyData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.fieldOf("has_stars").forGetter(SkyData::hasStars),
        Codec.FLOAT.fieldOf("star_brightness").forGetter(SkyData::starBrightness),
        Codec.FLOAT.fieldOf("sun_brightness").forGetter(SkyData::sunBrightness)
    ).apply(instance, SkyData::new));
}
}


