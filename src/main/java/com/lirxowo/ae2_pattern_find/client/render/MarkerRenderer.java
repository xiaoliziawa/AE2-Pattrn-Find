package com.lirxowo.ae2_pattern_find.client.render;

import com.lirxowo.ae2_pattern_find.Config;
import com.lirxowo.ae2_pattern_find.client.MarkerStore;
import com.lirxowo.ae2_pattern_find.client.PatternLocationMarker;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public final class MarkerRenderer {

    private static final BufferBuilder LINE_BUILDER = new BufferBuilder(2048);
    private static final MultiBufferSource.BufferSource LABEL_BUFFERS =
            MultiBufferSource.immediate(new BufferBuilder(512));

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        if (MarkerStore.isEmpty()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        List<PatternLocationMarker> markers = MarkerStore.view();
        Camera camera = event.getCamera();
        Vec3 camPos = camera.getPosition();
        PoseStack pose = event.getPoseStack();

        ColorUtil.Rgba color = ColorUtil.unpack(Config.markerColorArgb());

        var dim = mc.level.dimension();

        drawBoxesThroughWalls(pose, camPos, markers, dim, color);

        if (Config.SHOW_DISTANCE_TEXT.get()) {
            Font font = mc.font;
            for (PatternLocationMarker m : markers) {
                if (!m.dimension.equals(dim)) continue;
                drawLabel(pose, LABEL_BUFFERS, camera, font, m, camPos);
            }
            LABEL_BUFFERS.endBatch();
        }
    }

    private static void drawBoxesThroughWalls(PoseStack pose, Vec3 camPos,
                                              List<PatternLocationMarker> markers,
                                              ResourceKey<Level> dim,
                                              ColorUtil.Rgba color) {
        boolean began = false;

        for (PatternLocationMarker m : markers) {
            if (!m.dimension.equals(dim)) continue;

            if (!began) {
                RenderSystem.setShader(GameRenderer::getRendertypeLinesShader);
                RenderSystem.lineWidth((float) (double) Config.LINE_WIDTH.get());
                RenderSystem.disableDepthTest();
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableCull();
                LINE_BUILDER.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL);
                began = true;
            }

            pose.pushPose();
            pose.translate(m.pos.getX() - camPos.x, m.pos.getY() - camPos.y, m.pos.getZ() - camPos.z);
            LevelRenderer.renderLineBox(pose, LINE_BUILDER, 0, 0, 0, 1, 1, 1,
                    color.r(), color.g(), color.b(), color.a());
            pose.popPose();
        }

        if (began) {
            BufferBuilder.RenderedBuffer rendered = LINE_BUILDER.end();
            BufferUploader.drawWithShader(rendered);

            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.lineWidth(1.0f);
        }
    }

    private static void drawLabel(PoseStack pose, MultiBufferSource buffers, Camera camera,
                                  Font font, PatternLocationMarker marker, Vec3 camPos) {
        double cx = marker.pos.getX() + 0.5;
        double cy = marker.pos.getY() + 1.4;
        double cz = marker.pos.getZ() + 0.5;

        pose.pushPose();
        pose.translate(cx - camPos.x, cy - camPos.y, cz - camPos.z);
        pose.mulPose(camera.rotation());
        pose.scale(-0.025f, -0.025f, 0.025f);

        Component title = !marker.pattern.isEmpty() ? marker.pattern.getHoverName()
                : (!marker.icon.isEmpty() ? marker.icon.getHoverName() : Component.literal(""));
        double dist = Math.sqrt(camPos.distanceToSqr(cx, cy, cz));
        Component sub = Component.literal(String.format("%.1f m", dist));

        float titleW = font.width(title);
        float subW = font.width(sub);
        int bg = 0x66000000;

        font.drawInBatch(title, -titleW / 2f, -10f, 0xFFFFFFFF, false, pose.last().pose(),
                buffers, Font.DisplayMode.NORMAL, bg, 0xF000F0);
        font.drawInBatch(sub, -subW / 2f, 1f, 0xFFCCCCCC, false, pose.last().pose(),
                buffers, Font.DisplayMode.NORMAL, bg, 0xF000F0);

        pose.popPose();
    }

    private MarkerRenderer() {}
}
