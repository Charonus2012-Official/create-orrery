package net.charonus2012.orrery.mixin;

import com.lightning.northstar.world.dimension.NorthstarPlanets;
import net.charonus2012.orrery.planet.PlanetData;
import net.charonus2012.orrery.planet.PlanetRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(NorthstarPlanets.class)
public class NorthstarPlanetsMixin {

    @Inject(method = "getPlanetDimension", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetDimension(String name, CallbackInfoReturnable<ResourceKey<Level>> cir) {
        PlanetRegistry.all().values().stream()
                .filter(p -> p.planetName().equals(name))
                .findFirst()
                .ifPresent(p -> cir.setReturnValue(p.dimension()));
    }

    @Inject(method = "getPlanetName", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetName(ResourceKey<Level> level, CallbackInfoReturnable<String> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.planetName()));
    }

    @Inject(method = "getGravMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getGravMultiplier(ResourceKey<Level> level, CallbackInfoReturnable<Double> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.gravity()));
    }

    @Inject(method = "getPlanetOxy", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetOxy(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.hasOxygen()));
    }

    @Inject(method = "hasNormalGrav", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hasNormalGrav(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.gravity() == 1.0));
    }

    @Inject(method = "getPlanetTemp", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetTemp(ResourceKey<Level> level, CallbackInfoReturnable<Integer> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.temperature()));
    }

    @Inject(method = "getPlanetAtmosphereCost", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetAtmosphereCost(ResourceKey<Level> level, CallbackInfoReturnable<Integer> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.atmosphereCost()));
    }

    @Inject(method = "getComputingCost", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getComputingCost(ResourceKey<Level> level, CallbackInfoReturnable<Integer> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.computingCost()));
    }

    @Inject(method = "getEngineConstant", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getEngineConstant(ResourceKey<Level> level, CallbackInfoReturnable<Double> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.engineConstant()));
    }

    @Inject(method = "getSunMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getSunMultiplier(ResourceKey<Level> level, CallbackInfoReturnable<Float> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.sunMultiplier()));
    }

    @Inject(method = "planetHasSky", at = @At("HEAD"), cancellable = true, remap = false)
    private static void planetHasSky(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.hasSky()));
    }

    @Inject(method = "canSeeSkyAtDay", at = @At("HEAD"), cancellable = true, remap = false)
    private static void canSeeSkyAtDay(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.canSeeSkyAtDay()));
    }

    @Inject(method = "hasWeather", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hasWeather(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.hasWeather()));
    }

    @Inject(method = "getSeedOffset", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getSeedOffset(ResourceKey<Level> level, CallbackInfoReturnable<Long> cir) {
        PlanetRegistry.get(level).ifPresent(p -> cir.setReturnValue(p.seedOffset()));
    }

    @Inject(method = "isCustomDimension", at = @At("HEAD"), cancellable = true, remap = false)
    private static void isCustomDimension(ResourceLocation location, CallbackInfoReturnable<Boolean> cir) {
        boolean match = PlanetRegistry.all().keySet().stream()
                .anyMatch(key -> key.location().equals(location));
        if (match) cir.setReturnValue(true);
    }

    @Inject(method = "isInOrbit", at = @At("HEAD"), cancellable = true, remap = false)
    private static void isInOrbit(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        if (PlanetRegistry.isPlanet(level)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getWindMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getWindMultiplier(Level level, CallbackInfoReturnable<Float> cir) {
        PlanetRegistry.get(level.dimension()).ifPresent(p -> cir.setReturnValue(p.windMultiplier()));
    }
}
