package net.charonus2012.orrery.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.charonus2012.orrery.planet.PlanetData;
import net.charonus2012.orrery.planet.PlanetRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

import com.lightning.northstar.world.dimension.NorthstarPlanets; // Import the class you provided

import static net.charonus2012.orrery.planet.PlanetRegistry.*;


@Mixin(LevelRenderer.class)
public abstract class PlanetSkyMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow private ClientLevel level;
    @Shadow @Final private VertexBuffer starBuffer;
    @Shadow @Final private VertexBuffer skyBuffer;
    @Shadow @Final private VertexBuffer darkBuffer;

    @Unique
    private static final ResourceLocation BARE_SUN = ResourceLocation.parse("northstar:textures/environment/baresun.png");
    @Unique
    // Track the sun's size across ticks for smooth rendering interpolation
    private float currentSunSize = 5.0f;
    private float lastSunSize = 5.0f;
    @Unique
    private Map<String, Map<String, Double>> Stats = new HashMap<>();

    @Inject(method = "renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true)
    private void renderPlanetSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable setupFog, CallbackInfo ci) {
        assert this.minecraft.level != null;
        ResourceKey<Level> playerDim = this.minecraft.level.dimension();

        Optional<PlanetData> planetOpt = PlanetRegistry.get(playerDim);
        if (planetOpt.isEmpty() || !planetOpt.get().hasSky()) return;

        PlanetData planet = planetOpt.get();

        ci.cancel();
        setupFog.run();

        PoseStack poseStack = new PoseStack();
        poseStack.mulPose(frustumMatrix);

        // Render black sky
        RenderSystem.depthMask(false);
        RenderSystem.setShaderColor(0.0f, 0.0f, 0.0f, 1.0f);
        ShaderInstance shaderinstance = RenderSystem.getShader();
        this.skyBuffer.bind();
        this.skyBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shaderinstance);
        VertexBuffer.unbind();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Stars
        if (planet.hasStars()) {
            float vanillaStars = this.level.getStarBrightness(partialTick);
            float starBrightness = planet.starBrightness() - vanillaStars * 0.5f;
            if (starBrightness > 0.0f) {
                RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
                setupFog.run();
                this.starBuffer.bind();
                this.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
                VertexBuffer.unbind();
            }
        }

        // PLANET RENDERER!!!
        for (String rPlanet : this.Stats.keySet()) {
            if (Objects.equals(rPlanet, planet.planetName())) { continue; }
            double angle = this.Stats.get(rPlanet).get("angle");
            double orbitalSpeed = this.Stats.get(rPlanet).get("orbitalSpeed");
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees((float) angle));
            poseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(partialTick) * 360.0f * (float) orbitalSpeed));
            Matrix4f planetMatrix = poseStack.last().pose();
            float size = 1.0f;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, SkyPlanetTextures.get(rPlanet));
            BufferBuilder buf = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buf.addVertex(planetMatrix, -size, 100.0f, -size).setUv(0f, 0f);
            buf.addVertex(planetMatrix, size, 100.0f, -size).setUv(1f, 0f);
            buf.addVertex(planetMatrix, size, 100.0f, size).setUv(1f, 1f);
            buf.addVertex(planetMatrix, -size, 100.0f, size).setUv(0f, 1f);
            BufferUploader.drawWithShader(buf.buildOrThrow());

            poseStack.popPose();
        }



        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
        poseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(partialTick) * 360.0f));

        Matrix4f matrix4f1 = poseStack.last().pose();

        // --- DYNAMIC SUN SIZE MATH ---
        // Smoothly blend between the size last tick and the size this tick using partialTick
        float sunSize = this.lastSunSize + (this.currentSunSize - this.lastSunSize) * partialTick;

        float sunBrightness = planet.sunBrightness();
        RenderSystem.setShaderColor(sunBrightness, sunBrightness, sunBrightness, 1.0f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BARE_SUN);
        BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.addVertex(matrix4f1, -sunSize, 100.0f, -sunSize).setUv(0.0f, 0.0f);
        bufferbuilder.addVertex(matrix4f1, sunSize, 100.0f, -sunSize).setUv(1.0f, 0.0f);
        bufferbuilder.addVertex(matrix4f1, sunSize, 100.0f, sunSize).setUv(1.0f, 1.0f);
        bufferbuilder.addVertex(matrix4f1, -sunSize, 100.0f, sunSize).setUv(0.0f, 1.0f);
        BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.depthMask(true);
        RenderSystem.enableBlend();
        poseStack.popPose();
    }


    // Tick update hook to catch Northstar's coordinate changes 20 times per second
    @Inject(method = "tick", at = @At("TAIL"))
    private void onSkyTick(CallbackInfo ci) {
        assert this.minecraft.level != null;
        if (isPlanet(this.minecraft.level.dimension())) {
            String planet = getPlanetName(this.minecraft.level.dimension());

            // 1. Roll over the previous tick value
            this.lastSunSize = this.currentSunSize;

            // 2. Fetch the updated coordinates from Northstar
            double myX = NorthstarPlanets.getPlanetX(planet);
            double myY = NorthstarPlanets.getPlanetY(planet);

            // 3. Process the raw coordinates into a usable scale factor
            // Example: Using the sun_distance from coordinate center (0,0) as a size factor
            double sun_distance = Math.sqrt(myX * myX + myY * myY);

            // Map the result into an appropriate float range for your sky element
            this.currentSunSize = (float)(sun_distance * 0.01);

            for (String otherPlanet : PlanetName) {
                Map<String, Double> tempStats = new HashMap<>();
                double otherX = NorthstarPlanets.getPlanetX(otherPlanet);
                double otherY = NorthstarPlanets.getPlanetY(otherPlanet);
                double angle = Math.toDegrees(Math.atan2(otherY - myY, otherX - myX));

                tempStats.put("X", otherX);
                tempStats.put("Y", otherY);
                tempStats.put("angle", angle);
                tempStats.put("orbitalSpeed", ORBITAL_SPEEDS.get(otherPlanet));

                this.Stats.put(otherPlanet, tempStats);
            }

        }
    }

    @Inject(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FDDD)V", at = @At("HEAD"), cancellable = true)
    private void renderPlanetClouds(PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        ResourceKey<Level> playerDim = this.minecraft.level.dimension();
        if (playerDim == null) return;

        PlanetRegistry.get(playerDim).ifPresent(planet -> {
            if (planet.hasSky()) {
                ci.cancel();
            }
        });
    }
}
