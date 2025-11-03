package team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.common;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.InventoryLocationSnapshot;

public final class CommonPlayerRestorableSnapshot implements InventoryLocationSnapshot {

    private final ItemStack[] contents;
    private final Location location;
    private final float experience;

    public CommonPlayerRestorableSnapshot(
            final @Nullable ItemStack @NotNull [] contents,
            final @NotNull Location location,
            final float experience
    ) {
        this.contents = contents;
        this.location = location;
        this.experience = experience;
    }

    @Contract("_ -> new")
    public static @NotNull CommonPlayerRestorableSnapshot createFromPlayer(final @NotNull Player player) {
        return new CommonPlayerRestorableSnapshot(
                player.getInventory().getContents(),
                player.getLocation(),
                player.getExp()
        );
    }

    @Override
    public int version() {
        return 1;
    }

    @Override
    public void restore(final @NotNull Player player) {
        player.getInventory().setContents(this.contents);
        player.teleportAsync(this.location);
        player.setExp(this.experience);
    }

    public float experience() {
        return this.experience;
    }

    @Override
    public ItemStack[] contents() {
        return this.contents;
    }

    @Override
    public Location location() {
        return this.location;
    }
}
