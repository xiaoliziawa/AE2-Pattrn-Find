package com.lirxowo.ae2_pattern_find.client;

import com.lirxowo.ae2_pattern_find.Config;

public final class HoldProgressTracker {

    private long serverId;
    private int slot;
    private boolean active;
    private int ticks;
    private float partialTicks;

    public void update(boolean conditionMet, long serverId, int slot) {
        if (!conditionMet) {
            reset();
            return;
        }
        if (!active || this.serverId != serverId || this.slot != slot) {
            active = true;
            this.serverId = serverId;
            this.slot = slot;
            ticks = 0;
            partialTicks = 0f;
        }
    }

    public void onClientTick() {
        if (active && ticks < Config.LOAD_TICKS.get()) {
            ticks++;
            partialTicks = 0f;
        }
    }

    public void addRenderPartial(float p) {
        if (active) {
            partialTicks = p;
        }
    }

    public float progress() {
        int max = Math.max(1, Config.LOAD_TICKS.get());
        if (!active) return 0f;
        if (ticks >= max) return 1f;
        return Math.min(1f, (ticks + partialTicks) / (float) max);
    }

    public boolean isReady() {
        return active && ticks >= Config.LOAD_TICKS.get();
    }

    public long currentServerId() {
        return serverId;
    }

    public int currentSlot() {
        return slot;
    }

    public void consume() {
        reset();
    }

    public void reset() {
        active = false;
        ticks = 0;
        partialTicks = 0f;
    }
}
