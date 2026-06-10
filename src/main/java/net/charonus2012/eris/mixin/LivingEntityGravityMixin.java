package net.charonus2012.eris.mixin;

import com.lightning.northstar.mixin.GravityStuffMixin;
import net.charonus2012.eris.ErisDimensions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GravityStuffMixin.class, remap = false)
public abstract class LivingEntityGravityMixin {
    @Shadow(remap = false)
    double PLANET_GRAV;

    @Inject(method = "northstar$travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isFallFlying()Z", remap = true))
    private void eris$travel(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity.level().dimension().location().getNamespace().equals("eris")) {
            this.PLANET_GRAV = 0.084;
        }
    }

    @Inject(method = "getGravMultiplier", at = @At("HEAD"), cancellable = true)
    private void eris$getGravMultiplier(ResourceKey<Level> level, CallbackInfoReturnable<Double> cir) {
        if (level.location().getNamespace().equals("eris")) {
            cir.setReturnValue(0.084);
        }
    }
}
