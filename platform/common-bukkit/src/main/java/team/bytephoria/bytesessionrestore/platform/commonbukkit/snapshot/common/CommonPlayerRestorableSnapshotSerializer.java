package team.bytephoria.bytesessionrestore.platform.commonbukkit.snapshot.common;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.serializer.snapshot.AbstractVersionedSnapshotSerializer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public final class CommonPlayerRestorableSnapshotSerializer
        extends AbstractVersionedSnapshotSerializer<CommonPlayerRestorableSnapshot> {

    @Override
    public @NotNull Class<CommonPlayerRestorableSnapshot> type() {
        return CommonPlayerRestorableSnapshot.class;
    }

    @Override
    public int latestVersion() {
        return 1;
    }

    @Override
    protected void writeVersioned(
            final @NotNull DataOutput dataOutput,
            final @NotNull CommonPlayerRestorableSnapshot commonPlayerRestorableSnapshot,
            final int version
    ) {

        try {
            final ItemStack[] contents = commonPlayerRestorableSnapshot.contents();
            dataOutput.writeInt(contents.length);

            for (final ItemStack itemStack : contents) {
                if (itemStack == null || itemStack.getType() == Material.AIR) {
                    dataOutput.writeBoolean(false);
                    continue;
                }

                dataOutput.writeBoolean(true);

                // Bukkit applies GZIP compression in ItemStack#serializeAsBytes()
                final byte[] bytes = itemStack.serializeAsBytes();
                dataOutput.writeInt(bytes.length);
                dataOutput.write(bytes);
            }

            final Location location = commonPlayerRestorableSnapshot.location();
            final String worldName = location.getWorld() != null ? location.getWorld().getName() : "";

            dataOutput.writeUTF(worldName);
            dataOutput.writeDouble(location.getX());
            dataOutput.writeDouble(location.getY());
            dataOutput.writeDouble(location.getZ());
            dataOutput.writeFloat(location.getYaw());
            dataOutput.writeFloat(location.getPitch());

            dataOutput.writeFloat(commonPlayerRestorableSnapshot.experience());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + this.typeId(), e);
        }

    }

    @Override
    protected @NotNull CommonPlayerRestorableSnapshot readVersioned(
            final @NotNull DataInput dataInput,
            final int version
    ) {
        try {
            final int length = dataInput.readInt();
            final ItemStack[] contents = new ItemStack[length];

            for (int i = 0; i < length; i++) {
                final boolean present = dataInput.readBoolean();
                if (!present) {
                    continue;
                }

                final int size = dataInput.readInt();
                final byte[] bytes = new byte[size];
                dataInput.readFully(bytes);
                contents[i] = ItemStack.deserializeBytes(bytes);
            }

            final String worldName = dataInput.readUTF();
            final double x = dataInput.readDouble();
            final double y = dataInput.readDouble();
            final double z = dataInput.readDouble();
            final float yaw = dataInput.readFloat();
            final float pitch = dataInput.readFloat();

            final World world = worldName.isEmpty() ? null : Bukkit.getWorld(worldName);
            final Location location = new Location(world, x, y, z, yaw, pitch);

            final float experience = dataInput.readFloat();
            return new CommonPlayerRestorableSnapshot(contents, location, experience);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + this.typeId(), e);
        }
    }
}
