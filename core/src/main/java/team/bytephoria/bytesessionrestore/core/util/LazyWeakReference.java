package team.bytephoria.bytesessionrestore.core.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.function.Supplier;

public final class LazyWeakReference<O> {

    private WeakReference<O> weakReference;
    private final Supplier<@Nullable O> supplier;

    public LazyWeakReference(final @NotNull Supplier<@Nullable O> supplier) {
        this.weakReference = null;
        this.supplier = Objects.requireNonNull(supplier, "supplier");
    }

    public LazyWeakReference(final @Nullable O initial, final @NotNull Supplier<@Nullable O> supplier) {
        this.weakReference = initial == null ? null : new WeakReference<>(initial);
        this.supplier = Objects.requireNonNull(supplier, "supplier");
    }

    public @Nullable O get() {
        final O existing = this.weakReference != null ? this.weakReference.get() : null;
        if (existing != null) {
            return existing;
        }

        final O generated = this.supplier.get();
        if (generated != null) {
            this.weakReference = new WeakReference<>(generated);
        }

        return generated;
    }

    public @Nullable O refresh() {
        final O generated = this.supplier.get();
        this.weakReference = generated == null ? null : new WeakReference<>(generated);
        return generated;
    }
}
