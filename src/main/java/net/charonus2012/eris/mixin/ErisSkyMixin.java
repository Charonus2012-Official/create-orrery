package net.charonus2012.eris.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class ErisSkyMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private ClientLevel level;

    @Shadow
    @Final
    private VertexBuffer starBuffer;

    @Shadow
    @Final
    private VertexBuffer skyBuffer;

    @Shadow
    @Final
    private VertexBuffer darkBuffer;

    private static final ResourceLocation BARE_SUN = ResourceLocation.parse("northstar:textures/environment/baresun.png");

    @Inject(method = "renderSky(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FLnet/minecraft/client/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true)
    private void renderErisSky(Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, Camera camera, boolean isFoggy, Runnable setupFog, CallbackInfo ci) {
        ResourceKey<Level> playerDim = this.minecraft.level.dimension();
        if (playerDim != null && playerDim.location().getNamespace().equals("eris") && playerDim.location().getPath().equals("eris")) {
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
            float vanillaStars = this.level.getStarBrightness(partialTick);
            float starBrightness = 1.0f - vanillaStars * 0.5f;
            if (starBrightness > 0.0f) {
                RenderSystem.setShaderColor(starBrightness, starBrightness, starBrightness, starBrightness);
                // Fog doesn't affect stars as much in space
                setupFog.run();
                this.starBuffer.bind();
                this.starBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, GameRenderer.getPositionShader());
                VertexBuffer.unbind();
            }

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(this.level.getTimeOfDay(partialTick) * 360.0f));

            Matrix4f matrix4f1 = poseStack.last().pose();

            // Sun - tiny for Eris
            float sunSize = 5.0f;
            float sunBrightness = 0.6f;
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
    }

    @Inject(method = "renderClouds(Lcom/mojang/blaze3d/vertex/PoseStack;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;FDDD)V", at = @At("HEAD"), cancellable = true)
    private void renderErisClouds(PoseStack poseStack, Matrix4f frustumMatrix, Matrix4f projectionMatrix, float partialTick, double camX, double camY, double camZ, CallbackInfo ci) {
        ResourceKey<Level> playerDim = this.minecraft.level.dimension();
        if (playerDim != null && playerDim.location().getNamespace().equals("eris") && playerDim.location().getPath().equals("eris")) {
            ci.cancel();
        }
    }
}
