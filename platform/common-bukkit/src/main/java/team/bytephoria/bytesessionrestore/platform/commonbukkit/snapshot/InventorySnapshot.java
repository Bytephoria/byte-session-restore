package team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot;

import org.bukkit.inventory.ItemStack;

public interface InventorySnapshot extends RestorableSnapshot {

    ItemStack[] contents();

}
