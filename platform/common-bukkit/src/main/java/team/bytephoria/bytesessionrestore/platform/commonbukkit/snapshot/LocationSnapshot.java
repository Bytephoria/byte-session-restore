package team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot;

import org.bukkit.Location;

public interface LocationSnapshot extends RestorableSnapshot {

    Location location();

}
