package com.lirxowo.ae2_pattern_find.client;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class PatternLocationMarker {

    public final long serverId;
    public final int slot;
    public final BlockPos pos;
    public final ResourceKey<Level> dimension;
    public final ItemStack pattern;
    public final ItemStack icon;
    public final long expireGameTime;

    public PatternLocationMarker(long serverId, int slot, BlockPos pos, ResourceKey<Level> dim,
                                 ItemStack pattern, ItemStack icon, long expireGameTime) {
        this.serverId = serverId;
        this.slot = slot;
        this.pos = pos;
        this.dimension = dim;
        this.pattern = pattern;
        this.icon = icon;
        this.expireGameTime = expireGameTime;
    }
}
