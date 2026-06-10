package net.charonus2012.eris;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ErisDimensions {
    public static final ResourceKey<Level> ERIS_DIM_KEY = ResourceKey.create(
            Registries.DIMENSION,
            ResourceLocation.fromNamespaceAndPath("eris", "eris")
    );
}
