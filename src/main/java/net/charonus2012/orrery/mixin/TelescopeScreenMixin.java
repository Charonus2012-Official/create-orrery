package net.charonus2012.orrery.mixin;

import com.lightning.northstar.block.tech.telescope.TelescopeScreen;
import com.lightning.northstar.world.dimension.NorthstarPlanets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TelescopeScreen.class)
public class TelescopeScreenMixin {

    @Redirect(
            method = "renderPlanets",
            at = @At(
                    value = "FIELD",
                    target = "Lcom/lightning/northstar/world/dimension/NorthstarPlanets;pluto_x:D",
                    ordinal = 1 // Targets the SECOND time pluto_x is read in renderPlanets (which is the pluto_y assignment line)
            )
    )
    private double redirectPlutoXToY() {
        // Force the code to read the correct Y coordinate constant instead!
        return NorthstarPlanets.pluto_y;
    }
}
