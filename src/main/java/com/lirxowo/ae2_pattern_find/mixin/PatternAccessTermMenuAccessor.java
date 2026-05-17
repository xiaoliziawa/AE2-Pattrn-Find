package com.lirxowo.ae2_pattern_find.mixin;

import appeng.menu.implementations.PatternAccessTermMenu;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = PatternAccessTermMenu.class, remap = false)
public interface PatternAccessTermMenuAccessor {

    @Accessor("byId")
    Long2ObjectOpenHashMap<Object> ae2pf$getById();
}
