package com.lirxowo.ae2_pattern_find.client.render;

import com.lirxowo.ae2_pattern_find.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public final class LoadingProgressRenderer {

    private static final int SEGMENTS = 36;
    private static final float INNER = 7.5f;
    private static final float OUTER = 9.5f;

    public static void draw(GuiGraphics graphics, float cx, float cy, float progress) {
        if (progress <= 0f) return;

        ColorUtil.Rgba color = ColorUtil.unpack(Config.markerColorArgb());
        float r = color.r(), g = color.g(), b = color.b(), a = color.a();

        PoseStack pose = graphics.pose();
        pose.pushPose();
        pose.translate(0f, 0f, 400f);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        Matrix4f m = pose.last().pose();
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder buf = tess.getBuilder();
        buf.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);

        float totalAngle = (float) (Math.PI * 2.0) * progress;
        float start = (float) (-Math.PI / 2.0);
        int filled = Math.max(1, Math.round(SEGMENTS * progress));
        for (int i = 0; i <= filled; i++) {
            float t = start + totalAngle * (i / (float) filled);
            float sin = (float) Math.sin(t);
            float cos = (float) Math.cos(t);
            buf.vertex(m, cx + cos * OUTER, cy + sin * OUTER, 0f).color(r, g, b, a).endVertex();
            buf.vertex(m, cx + cos * INNER, cy + sin * INNER, 0f).color(r, g, b, a * 0.6f).endVertex();
        }
        tess.end();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        pose.popPose();
    }

    private LoadingProgressRenderer() {}
}
