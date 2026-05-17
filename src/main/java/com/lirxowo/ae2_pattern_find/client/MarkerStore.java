package com.lirxowo.ae2_pattern_find.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

import java.util.ArrayList;
import java.util.List;

public final class MarkerStore {

    private static final List<PatternLocationMarker> MARKERS = new ArrayList<>();

    public static void add(PatternLocationMarker marker) {
        MARKERS.removeIf(m -> m.serverId == marker.serverId && m.slot == marker.slot);
        MARKERS.add(marker);
    }

    public static List<PatternLocationMarker> view() {
        return MARKERS;
    }

    public static boolean isEmpty() {
        return MARKERS.isEmpty();
    }

    public static void clear() {
        MARKERS.clear();
    }

    public static void tick() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            MARKERS.clear();
            return;
        }
        long now = level.getGameTime();
        var dim = level.dimension();
        MARKERS.removeIf(m -> now >= m.expireGameTime || !m.dimension.equals(dim));
    }

    private MarkerStore() {}
}
