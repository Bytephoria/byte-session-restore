package team.bytephoria.bytesessionrestore.api.snapshot;

/**
 * Represents a snapshot of a player's state at a given time.
 * Snapshots are versioned and identified by type,
 * allowing flexible serialization and compatibility management.
 */
public interface Snapshot {

    /**
     * Returns the unique identifier for this snapshot type.
     * Used to associate serialized payloads with the correct serializer.
     *
     * @return the snapshot type identifier
     */
    default String typeId() {
        return this.getClass().getSimpleName();
    }

    /**
     * Returns the version number of this snapshot structure.
     * Incremented when the internal format changes.
     *
     * @return the snapshot schema version
     */
    int version();
}
