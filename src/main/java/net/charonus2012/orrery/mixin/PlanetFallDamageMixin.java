package net.charonus2012.orrery.mixin;

import net.charonus2012.orrery.planet.PlanetData;
import net.charonus2012.orrery.planet.PlanetRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class PlanetFallDamageMixin {

    private static final float SAFE_FALL = 3.0f;

    @Inject(
            method = "causeFallDamage(FFLnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void planetFallDamage(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        var dim = entity.level().dimension();

        PlanetRegistry.get(dim).ifPresent(planet -> {
            float scaledFallDistance = fallDistance * (float) planet.gravity();

            boolean tookDamage = false;
            if (scaledFallDistance > SAFE_FALL) {
                float damage = (scaledFallDistance - SAFE_FALL) * multiplier;
                if (damage > 0) {
                    entity.hurt(source, damage);
                    tookDamage = true;
                }
            }
            cir.cancel();
            cir.setReturnValue(tookDamage);
        });
    }
}
