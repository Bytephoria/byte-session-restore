package team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;

public interface RestorableSnapshot extends Snapshot {

    void restore(final @NotNull Player player);

}
