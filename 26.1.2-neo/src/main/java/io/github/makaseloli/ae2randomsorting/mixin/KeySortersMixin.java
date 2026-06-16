package io.github.makaseloli.ae2randomsorting.mixin;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Comparator;

import appeng.api.config.SortDir;
import appeng.api.config.SortOrder;
import appeng.api.stacks.AEKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "appeng.client.gui.me.common.KeySorters")
public abstract class KeySortersMixin {
    @Unique
    private static final SecureRandom AE2RANDOMSORTING_RANDOM = new SecureRandom();

    @Inject(method = "getComparator", remap = false, at = @At("HEAD"), cancellable = true)
    private static void ae2randomsorting$getComparator(
            SortOrder sortOrder,
            SortDir sortDir,
            CallbackInfoReturnable<Comparator<AEKey>> callbackInfo) {
        if (!"RANDOM".equals(sortOrder.name())) {
            return;
        }

        var seed = new byte[32];
        AE2RANDOMSORTING_RANDOM.nextBytes(seed);

        Comparator<AEKey> comparator = Comparator
                .comparing((AEKey key) -> ae2randomsorting$rank(seed, key), KeySortersMixin::ae2randomsorting$compareUnsigned)
                .thenComparing(key -> key.getId().toString())
                .thenComparingInt(Object::hashCode);
        if (sortDir == SortDir.DESCENDING) {
            comparator = comparator.reversed();
        }
        callbackInfo.setReturnValue(comparator);
    }

    @Unique
    private static byte[] ae2randomsorting$rank(byte[] seed, AEKey key) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            digest.update(seed);
            ae2randomsorting$update(digest, key.getType().toString());
            ae2randomsorting$update(digest, key.getId().toString());
            ae2randomsorting$update(digest, String.valueOf(key.getPrimaryKey()));
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is required by the Java runtime", e);
        }
    }

    @Unique
    private static int ae2randomsorting$compareUnsigned(byte[] left, byte[] right) {
        return Arrays.compareUnsigned(left, right);
    }

    @Unique
    private static void ae2randomsorting$update(MessageDigest digest, String value) {
        digest.update((byte) 0);
        digest.update(value.getBytes(StandardCharsets.UTF_8));
    }
}
