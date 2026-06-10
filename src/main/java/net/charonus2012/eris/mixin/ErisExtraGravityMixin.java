package net.charonus2012.eris.mixin;

import net.charonus2012.eris.ErisDimensions;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = {
    "com.lightning.northstar.mixin.gravitystuff.ItemEntityGravityMixin",
    "com.lightning.northstar.mixin.gravitystuff.AbstractArrowGravityMixin",
    "com.lightning.northstar.mixin.gravitystuff.BoatGravityMixin",
    "com.lightning.northstar.mixin.gravitystuff.AbstractMinecartGravityMixin",
    "com.lightning.northstar.mixin.gravitystuff.ThrowableProjectileGravityMixin"
}, remap = false)
public abstract class ErisExtraGravityMixin {
    @Shadow(remap = false)
    double PLANET_GRAV;

    @Inject(method = {"northstar$tick", "northstar$travel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isNoGravity()Z", remap = true))
    private void eris$gravity(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().dimension().location().getNamespace().equals("eris")) {
            this.PLANET_GRAV = 0.084;
        }
    }

    @Inject(method = {"northstar$tick", "northstar$travel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/Boat;isNoGravity()Z", remap = true), expect = 0)
    private void eris$gravityBoat(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().dimension().location().getNamespace().equals("eris")) {
            this.PLANET_GRAV = 0.084;
        }
    }

    @Inject(method = {"northstar$tick", "northstar$travel"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/vehicle/AbstractMinecart;isNoGravity()Z", remap = true), expect = 0)
    private void eris$gravityMinecart(CallbackInfo ci) {
        Entity entity = (Entity) (Object) this;
        if (entity.level().dimension().location().getNamespace().equals("eris")) {
            this.PLANET_GRAV = 0.084;
        }
    }
}
