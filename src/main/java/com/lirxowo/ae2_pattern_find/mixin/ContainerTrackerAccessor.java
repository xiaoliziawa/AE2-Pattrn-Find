package com.lirxowo.ae2_pattern_find.mixin;

import appeng.helpers.patternprovider.PatternContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "appeng.menu.implementations.PatternAccessTermMenu$ContainerTracker", remap = false)
public interface ContainerTrackerAccessor {

    @Accessor("container")
    PatternContainer ae2pf$getContainer();
}
