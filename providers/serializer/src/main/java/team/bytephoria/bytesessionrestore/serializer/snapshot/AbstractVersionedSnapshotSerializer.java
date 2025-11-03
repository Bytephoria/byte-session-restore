package team.bytephoria.bytesessionrestore.serializer.snapshot;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.snapshot.Snapshot;
import team.bytephoria.bytesessionrestore.spi.serializer.snapshot.SnapshotSerializer;

import java.io.*;

/**
 * Base class for all versioned {@link SnapshotSerializer} implementations.
 * <p>
 * This class provides a standard mechanism for serializing and deserializing
 * {@link Snapshot} objects with embedded version metadata, allowing support for
 * multiple snapshot format revisions over time.
 * </p>
 *
 * <p><b>Versioning rules:</b></p>
 * <ul>
 *   <li>Each {@link Snapshot} must include its {@link Snapshot#version()} number
 *       when being serialized.</li>
 *   <li>The serializer defines {@link #latestVersion()} as the highest snapshot
 *       version it can currently read or write.</li>
 *   <li>If a snapshot’s version exceeds {@code latestVersion()}, the serializer
 *       will reject it as unsupported.</li>
 *   <li>Older versions can be handled within {@link #readVersioned(DataInput, int)}
 *       to provide backward compatibility.</li>
 * </ul>
 *
 * @param <T> the concrete {@link Snapshot} type this serializer supports
 */
public abstract class AbstractVersionedSnapshotSerializer<T extends Snapshot> implements SnapshotSerializer<T> {

    /**
     * Returns the unique snapshot type identifier for this serializer.
     * <p>
     * By default, this uses the simple name of the {@link #type()} class,
     * but subclasses may override it to provide a stable string ID
     * (e.g. <code>"common_player_snapshot"</code>).
     * </p>
     *
     * @return the snapshot type identifier (used in registries and payload metadata)
     */
    public @NotNull String typeId() {
        return this.type().getSimpleName();
    }

    /**
     * Returns the {@link Class} type this serializer is responsible for.
     * <p>
     * This defines the exact {@link Snapshot} subclass that this serializer
     * can handle during (de)serialization.
     * </p>
     *
     * @return the concrete {@link Snapshot} class type
     */
    public abstract @NotNull Class<T> type();

    /**
     * Returns the latest snapshot version supported by this serializer.
     * <p>
     * Each time the snapshot format changes (fields added, removed, or reordered),
     * this value should be incremented to indicate a new structure.
     * </p>
     *
     * @return the maximum supported snapshot version
     */
    public abstract int latestVersion();

    /**
     * Writes the snapshot data for a specific version.
     * <p>
     * Subclasses must implement the logic to serialize fields
     * according to the given format version.
     * </p>
     *
     * @param dataOutput the output stream to write bytes to
     * @param snapshot   the snapshot to serialize
     * @param version    the snapshot version to write
     */
    protected abstract void writeVersioned(
            final @NotNull DataOutput dataOutput,
            final @NotNull T snapshot,
            final int version
    );

    /**
     * Reads and reconstructs a snapshot object from a given input stream.
     * <p>
     * Implementations should handle all versions ≤ {@link #latestVersion()}
     * to maintain backward compatibility.
     * </p>
     *
     * @param dataInput the input stream to read from
     * @param version   the version of the serialized snapshot
     * @return the reconstructed {@link Snapshot} instance
     */
    protected abstract @NotNull T readVersioned(
            final @NotNull DataInput dataInput,
            final int version
    );

    /**
     * Serializes a snapshot to a compressed binary payload.
     * <p>
     * The format is:
     * <pre>
     * [int: version]
     * [custom snapshot data...]
     * </pre>
     *
     * @param snapshot the snapshot to serialize
     * @return the encoded binary payload
     */
    @Override
    public byte @NotNull [] serialize(final @NotNull T snapshot) {
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream)) {

            dataOutputStream.writeInt(snapshot.version());
            this.writeVersioned(dataOutputStream, snapshot, snapshot.version());
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize snapshot of type " + this.typeId(), e);
        }
    }

    /**
     * Deserializes a binary payload back into a {@link Snapshot} instance.
     * <p>
     * Automatically validates the version number and delegates reading
     * to {@link #readVersioned(DataInput, int)}.
     * </p>
     *
     * @param bytes the binary payload to read
     * @return the deserialized snapshot
     * @throws RuntimeException if deserialization fails or version is unsupported
     */
    @Override
    public @NotNull T deserialize(final byte @NotNull [] bytes) {
        try (final DataInputStream data = new DataInputStream(new ByteArrayInputStream(bytes))) {
            final int version = data.readInt();
            if (version > this.latestVersion()) {
                throw new IOException("Unsupported snapshot version " + version + " (max " + this.latestVersion() + ")");
            }

            return this.readVersioned(data, version);

        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize snapshot of type " + this.typeId(), e);
        }
    }
}
