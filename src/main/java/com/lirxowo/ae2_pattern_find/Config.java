package com.lirxowo.ae2_pattern_find;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {

    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue LOAD_TICKS;
    public static final ForgeConfigSpec.IntValue HIGHLIGHT_TICKS;
    public static final ForgeConfigSpec.ConfigValue<String> MARKER_COLOR;
    public static final ForgeConfigSpec.BooleanValue SHOW_ARROW;
    public static final ForgeConfigSpec.IntValue ARROW_RADIUS;
    public static final ForgeConfigSpec.DoubleValue LINE_WIDTH;
    public static final ForgeConfigSpec.BooleanValue SHOW_DISTANCE_TEXT;

    static {
        ForgeConfigSpec.Builder b = new ForgeConfigSpec.Builder();

        b.push("loading");
        LOAD_TICKS = b
                .comment("How many ticks of holding Shift+Alt are required to trigger the locate action (20 ticks = 1 second).")
                .defineInRange("loadTicks", 10, 1, 200);
        b.pop();

        b.push("marker");
        HIGHLIGHT_TICKS = b
                .comment("How long (in ticks) a world marker stays visible after a successful locate (20 ticks = 1 second).")
                .defineInRange("highlightTicks", 600, 20, 24000);
        MARKER_COLOR = b
                .comment("Marker line color, RGB hex like #00FFFF.")
                .define("color", "#00FFFF");
        LINE_WIDTH = b
                .comment("Line thickness of the box outline.")
                .defineInRange("lineWidth", 3.0D, 0.5D, 10.0D);
        SHOW_DISTANCE_TEXT = b
                .comment("Show a floating label above the marker with the pattern result name and distance.")
                .define("showDistanceText", true);
        b.pop();

        b.push("arrow");
        SHOW_ARROW = b
                .comment("Show an on-screen arrow orbiting the crosshair that points toward the located marker.")
                .define("showArrow", true);
        ARROW_RADIUS = b
                .comment("Radius (in screen pixels) of the arrow orbit around the crosshair.")
                .defineInRange("radius", 40, 8, 256);
        b.pop();

        SPEC = b.build();
    }

    private static String cachedHex = null;
    private static int cachedArgb = 0xFF00FFFF;

    public static int markerColorArgb() {
        String hex = MARKER_COLOR.get();
        if (hex == null) return 0xFF00FFFF;
        if (hex.equals(cachedHex)) return cachedArgb;
        String clean = hex.startsWith("#") ? hex.substring(1) : hex;
        int parsed;
        try {
            parsed = 0xFF000000 | (Integer.parseInt(clean, 16) & 0xFFFFFF);
        } catch (NumberFormatException e) {
            parsed = 0xFF00FFFF;
        }
        cachedHex = hex;
        cachedArgb = parsed;
        return parsed;
    }

    private Config() {}
}
