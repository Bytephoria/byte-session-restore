package team.bytephoria.bytesessionrestore.platform.commonbukkit;

import team.bytephoria.bytesessionrestore.core.registry.SnapshotSerializerRegistry;
import team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.common.CommonPlayerRestorableSnapshotSerializer;

public final class BukkitSnapshotProvider {

    public static void registerAll() {
        SnapshotSerializerRegistry.register(new CommonPlayerRestorableSnapshotSerializer());
    }

}
