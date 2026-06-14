package net.charonus2012.orrery.planet;

import com.mojang.serialization.JsonOps;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class PlanetDataLoader extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new Gson();

    public PlanetDataLoader() {
        super(GSON, "orrery_planets");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resources, ResourceManager resourceManager, ProfilerFiller profiler) {
        PlanetRegistry.clear();

        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            ResourceLocation id = entry.getKey();
            JsonElement json = entry.getValue();

            PlanetData.CODEC.parse(JsonOps.INSTANCE, json).resultOrPartial(
                    error -> System.err.println("[Orrery] Failed to parse planet " + id + ": " + error)
            ).ifPresent(planet -> {
                PlanetRegistry.register(planet);
                System.out.println("[Orrery] Loaded planet: " + id + " -> " + planet);
            });
        }
        System.out.println("[Orrery] Total planets loaded: " + PlanetRegistry.all().size());
    }
}
