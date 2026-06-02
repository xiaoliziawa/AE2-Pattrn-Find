package com.lirxowo.ae2_pattern_find.client.render;

import com.lirxowo.ae2_pattern_find.AE2PatternFind;
import com.lirxowo.ae2_pattern_find.Config;
import com.lirxowo.ae2_pattern_find.client.MarkerStore;
import com.lirxowo.ae2_pattern_find.client.PatternLocationMarker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.List;

public final class ArrowOverlayRenderer {

    private static final ResourceLocation ARROW_TEX =
            ResourceLocation.fromNamespaceAndPath(AE2PatternFind.MODID, "textures/gui/arrow.png");

    private static final float ARROW_SIZE = 16f;
    private static final float HALF = ARROW_SIZE / 2f;

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (!Config.SHOW_ARROW.get()) return;
        if (MarkerStore.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui || mc.level == null || mc.player == null) return;
        if (mc.screen != null) return;

        List<PatternLocationMarker> markers = MarkerStore.view();

        Camera cam = mc.gameRenderer.getMainCamera();
        Vec3 camPos = cam.getPosition();
        Vector3f forward = cam.getLookVector();
        Vector3f up = cam.getUpVector();
        Vector3f left = cam.getLeftVector();
        float rx = -left.x(), ry = -left.y(), rz = -left.z();

        GuiGraphics gfx = event.getGuiGraphics();
        int sw = gfx.guiWidth();
        int sh = gfx.guiHeight();
        float cx = sw / 2f;
        float cy = sh / 2f;
        float radius = Config.ARROW_RADIUS.get();

        float fov = (float) Math.toRadians(mc.options.fov().get());
        float halfH = (float) Math.tan(fov / 2.0);
        float halfW = halfH * (sw / (float) sh);

        ColorUtil.Rgba color = ColorUtil.unpack(Config.markerColorArgb());
        float r = color.r(), g = color.g(), b = color.b(), baseA = color.a();

        var dim = mc.level.dimension();
        PoseStack pose = gfx.pose();

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        boolean began = false;

        for (PatternLocationMarker m : markers) {
            if (!m.dimension.equals(dim)) continue;

            Vec3 target = Vec3.atCenterOf(m.pos);
            float dx = (float) (target.x - camPos.x);
            float dy = (float) (target.y - camPos.y);
            float dz = (float) (target.z - camPos.z);

            float dRight   = dx * rx          + dy * ry          + dz * rz;
            float dUp      = dx * up.x()      + dy * up.y()      + dz * up.z();
            float dForward = dx * forward.x() + dy * forward.y() + dz * forward.z();

            float angle = (float) Math.atan2(-dUp, dRight);
            float ax = cx + (float) Math.cos(angle) * radius;
            float ay = cy + (float) Math.sin(angle) * radius;

            boolean onScreen = dForward > 0
                    && Math.abs(dRight / dForward) < halfW
                    && Math.abs(dUp / dForward) < halfH;
            float a = baseA * (onScreen ? 0.45f : 1.0f);

            if (!began) {
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.setShaderTexture(0, ARROW_TEX);
                buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                began = true;
            }

            pose.pushPose();
            pose.translate(ax, ay, 0);
            pose.mulPose(Axis.ZP.rotation(angle));

            Matrix4f m4 = pose.last().pose();
            buf.vertex(m4, -HALF, -HALF, 0f).uv(0f, 0f).color(r, g, b, a).endVertex();
            buf.vertex(m4, -HALF,  HALF, 0f).uv(0f, 1f).color(r, g, b, a).endVertex();
            buf.vertex(m4,  HALF,  HALF, 0f).uv(1f, 1f).color(r, g, b, a).endVertex();
            buf.vertex(m4,  HALF, -HALF, 0f).uv(1f, 0f).color(r, g, b, a).endVertex();

            pose.popPose();
        }

        if (began) {
            Tesselator.getInstance().end();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
        }
    }

    private ArrowOverlayRenderer() {}
}
