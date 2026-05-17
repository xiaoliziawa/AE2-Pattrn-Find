package com.lirxowo.ae2_pattern_find.server;

import appeng.helpers.patternprovider.PatternContainer;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class PatternHostResolver {

    private static final Map<String, Optional<Method>> METHOD_CACHE = new ConcurrentHashMap<>();

    @Nullable
    public static BlockEntity resolve(@Nullable PatternContainer container) {
        if (container == null) return null;

        if (container instanceof BlockEntity be) {
            return be;
        }
        if (container instanceof PatternProviderLogicHost h) {
            return h.getBlockEntity();
        }

        BlockEntity direct = invokeNoArg(container, "getBlockEntity", BlockEntity.class);
        if (direct != null) return direct;

        Object host = invokeNoArg(container, "getHost", Object.class);
        if (host != null) {
            BlockEntity viaHost = invokeNoArg(host, "getBlockEntity", BlockEntity.class);
            if (viaHost != null) return viaHost;
        }

        return null;
    }

    @Nullable
    private static <T> T invokeNoArg(Object target, String methodName, Class<T> expected) {
        Optional<Method> cached = METHOD_CACHE.computeIfAbsent(
                target.getClass().getName() + "#" + methodName,
                k -> findPublicNoArg(target.getClass(), methodName));
        if (cached.isEmpty()) return null;
        try {
            Object result = cached.get().invoke(target);
            return expected.isInstance(result) ? expected.cast(result) : null;
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    private static Optional<Method> findPublicNoArg(Class<?> cls, String methodName) {
        try {
            Method m = cls.getMethod(methodName);
            return Optional.of(m);
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    private PatternHostResolver() {}
}
