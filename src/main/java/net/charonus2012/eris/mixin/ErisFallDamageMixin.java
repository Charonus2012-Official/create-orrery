package net.charonus2012.eris.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class ErisFallDamageMixin {

    @Inject(
            method = "causeFallDamage(FFLnet/minecraft/world/damagesource/DamageSource;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void erisFallDamage(float fallDistance, float multiplier, DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        Level level = entity.level();
        ResourceKey<Level> dim = level.dimension();

        if (dim.location().getNamespace().equals("eris") && dim.location().getPath().equals("eris")) {
            float gravityRatio = 0.025f;
            float scaledFallDistance = fallDistance * gravityRatio;

            boolean tookDamage = false;
            if (scaledFallDistance > 3.0f) {
                float damage = (scaledFallDistance - 3.0f) * multiplier;
                if (damage > 0) {
                    entity.hurt(source, damage);
                    tookDamage = true;
                }
            }
            cir.cancel();
            cir.setReturnValue(tookDamage);
        }
    }
}
