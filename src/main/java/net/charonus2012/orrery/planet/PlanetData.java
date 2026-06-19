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
        boolean hasWeather,
        long seedOffset,
        float windMultiplier,
        // sky rendering
        boolean hasSky,
        boolean hasStars,
        boolean canSeeSkyAtDay,
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
            Codec.BOOL.fieldOf("has_weather").forGetter(PlanetData::hasWeather),
            Codec.LONG.fieldOf("seed_offset").forGetter(PlanetData::seedOffset),
            Codec.FLOAT.fieldOf("wind_multiplier").forGetter(PlanetData::windMultiplier),
            Codec.BOOL.fieldOf("has_sky").forGetter(PlanetData::hasSky),
            PlanetData.SkyData.CODEC.fieldOf("sky").forGetter(p -> new SkyData(p.hasStars(), p.canSeeSkyAtDay(), p.starBrightness(), p.sunBrightness()))
    ).apply(instance, (dimension, planetName, gravity, hasOxygen, temperature, atmosphereCost, computingCost, engineConstant, sunMultiplier, hasWeather, seedOffset, windMultiplier, hasSky, sky) ->
            new PlanetData(dimension, planetName, gravity, hasOxygen, temperature, atmosphereCost, computingCost, engineConstant, sunMultiplier, hasWeather, seedOffset, windMultiplier, hasSky, sky.hasStars(), sky.seeAtDay(), sky.starBrightness(), sky.sunBrightness())
    ));

    private record SkyData(boolean hasStars, boolean seeAtDay, float starBrightness, float sunBrightness) {
        static final Codec<SkyData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.BOOL.fieldOf("has_stars").forGetter(SkyData::hasStars),
                Codec.BOOL.fieldOf("can_see_sky_at_day").forGetter(SkyData::seeAtDay),
                Codec.FLOAT.fieldOf("star_brightness").forGetter(SkyData::starBrightness),
                Codec.FLOAT.fieldOf("sun_brightness").forGetter(SkyData::sunBrightness)
        ).apply(instance, SkyData::new));
    }
}
