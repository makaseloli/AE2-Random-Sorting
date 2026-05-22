package io.github.makaseloli.ae2randomsorting.mixin;

import java.util.Arrays;

import appeng.api.config.SortOrder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SortOrder.class)
public abstract class SortOrderMixin {
    @Shadow(remap = false)
    @Final
    @Mutable
    private static SortOrder[] $VALUES;

    @Unique
    private static SortOrder ae2randomsorting$random;

    @Invoker(value = "<init>", remap = false)
    private static SortOrder ae2randomsorting$create(String name, int ordinal) {
        throw new AssertionError();
    }

    @Inject(method = "<clinit>", remap = false, at = @At("TAIL"))
    private static void ae2randomsorting$addRandom(CallbackInfo callbackInfo) {
        if (ae2randomsorting$random != null) {
            return;
        }

        var values = $VALUES;
        ae2randomsorting$random = ae2randomsorting$create("RANDOM", values.length);
        var extendedValues = Arrays.copyOf(values, values.length + 1);
        extendedValues[values.length] = ae2randomsorting$random;
        $VALUES = extendedValues;
    }
}
