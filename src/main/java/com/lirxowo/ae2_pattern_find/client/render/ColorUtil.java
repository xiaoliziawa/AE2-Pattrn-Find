package com.lirxowo.ae2_pattern_find.client.render;

public final class ColorUtil {

    public static float a(int argb) { return ((argb >>> 24) & 0xFF) / 255f; }
    public static float r(int argb) { return ((argb >>> 16) & 0xFF) / 255f; }
    public static float g(int argb) { return ((argb >>> 8)  & 0xFF) / 255f; }
    public static float b(int argb) { return (argb          & 0xFF) / 255f; }

    public static Rgba unpack(int argb) {
        return new Rgba(r(argb), g(argb), b(argb), a(argb));
    }

    public record Rgba(float r, float g, float b, float a) {}

    private ColorUtil() {}
}
