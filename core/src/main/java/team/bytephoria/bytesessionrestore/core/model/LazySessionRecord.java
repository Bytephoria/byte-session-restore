package team.bytephoria.bytesessionrestore.core.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.api.model.session.SessionRecord;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;
import team.bytephoria.bytesessionrestore.core.util.LazyWeakReference;

import java.util.function.Supplier;

public final class LazySessionRecord {

    private final SessionRecord delegate;
    private final LazyWeakReference<Snapshot> loader;

    public LazySessionRecord(
            final @NotNull SessionRecord delegate,
            final @NotNull Supplier<Snapshot> snapshotSupplier
    ) {
        this.delegate = delegate;
        this.loader = new LazyWeakReference<>(snapshotSupplier);
    }

    public SessionRecord delegate() {
        return this.delegate;
    }

    public @Nullable Snapshot snapshot() {
        return this.loader.get();
    }

}
