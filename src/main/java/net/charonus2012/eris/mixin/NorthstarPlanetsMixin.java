package net.charonus2012.eris.mixin;

import com.lightning.northstar.world.dimension.NorthstarPlanets;
import net.charonus2012.eris.ErisConstants;
import net.charonus2012.eris.ErisDimensions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NorthstarPlanets.class)
public class NorthstarPlanetsMixin {
    private static boolean isEris(ResourceKey<Level> level) {
        return ErisDimensions.ERIS_DIM_KEY.equals(level);
    }

    @Inject(method = "getPlanetDimension", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetDimension(String name, CallbackInfoReturnable<ResourceKey<Level>> cir) {
        if ("eris".equals(name)) {
            cir.setReturnValue(ErisDimensions.ERIS_DIM_KEY);
        }
    }

    @Inject(method = "getPlanetName", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetName(ResourceKey<Level> level, CallbackInfoReturnable<String> cir) {
        if (isEris(level)) {
            cir.setReturnValue("eris");
        }
    }

    @Inject(method = "getGravMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getGravMultiplier(ResourceKey<Level> level, CallbackInfoReturnable<Double> cir) {
        if (isEris(level)) {
            cir.setReturnValue(ErisConstants.GRAVITY);
        }
    }

    @Inject(method = "getPlanetOxy", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetOxy(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        if (isEris(level)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "hasNormalGrav", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hasNormalGrav(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        if (isEris(level)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getPlanetTemp", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetTemp(ResourceKey<Level> level, CallbackInfoReturnable<Integer> cir) {
        if (isEris(level)) {
            cir.setReturnValue(ErisConstants.TEMPERATURE);
        }
    }

    @Inject(method = "getPlanetAtmosphereCost", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getPlanetAtmosphereCost(ResourceKey<Level> level, CallbackInfoReturnable<Integer> cir) {
        if (isEris(level)) {
            cir.setReturnValue(0);
        }
    }

    @Inject(method = "getComputingCost", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getComputingCost(ResourceKey<Level> level, CallbackInfoReturnable<Integer> cir) {
        if (isEris(level)) {
            cir.setReturnValue(ErisConstants.COMPUTING_COST);
        }
    }

    @Inject(method = "getEngineConstant", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getEngineConstant(ResourceKey<Level> level, CallbackInfoReturnable<Double> cir) {
        if (isEris(level)) {
            cir.setReturnValue(ErisConstants.ENGINE_CONSTANT);
        }
    }

    @Inject(method = "getSunMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getSunMultiplier(ResourceKey<Level> level, CallbackInfoReturnable<Float> cir) {
        if (isEris(level)) {
            cir.setReturnValue(ErisConstants.SUN_MULTIPLIER);
        }
    }

    @Inject(method = "planetHasSky", at = @At("HEAD"), cancellable = true, remap = false)
    private static void planetHasSky(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        if (isEris(level)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "canSeeSkyAtDay", at = @At("HEAD"), cancellable = true, remap = false)
    private static void canSeeSkyAtDay(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        if (isEris(level)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "hasWeather", at = @At("HEAD"), cancellable = true, remap = false)
    private static void hasWeather(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        if (isEris(level)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "getSeedOffset", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getSeedOffset(ResourceKey<Level> level, CallbackInfoReturnable<Long> cir) {
        if (isEris(level)) {
            cir.setReturnValue(ErisConstants.SEED_OFFSET);
        }
    }

    @Inject(method = "isCustomDimension", at = @At("HEAD"), cancellable = true, remap = false)
    private static void isCustomDimension(ResourceLocation location, CallbackInfoReturnable<Boolean> cir) {
        if (ErisDimensions.ERIS_DIM_KEY.location().equals(location)) {
            cir.setReturnValue(true);
        }
    }
    @Inject(method = "isInOrbit", at = @At("HEAD"), cancellable = true, remap = false)
    private static void isInOrbit(ResourceKey<Level> level, CallbackInfoReturnable<Boolean> cir) {
        if (isEris(level)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getWindMultiplier", at = @At("HEAD"), cancellable = true, remap = false)
    private static void getWindMultiplier(Level level, CallbackInfoReturnable<Float> cir) {
        if (isEris(level.dimension())) {
            cir.setReturnValue(ErisConstants.WIND);
        }
    }
}
