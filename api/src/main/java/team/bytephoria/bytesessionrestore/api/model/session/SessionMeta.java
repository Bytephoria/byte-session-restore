package team.bytephoria.bytesessionrestore.api.model.session;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;

import java.sql.Timestamp;

/**
 * Represents metadata associated with a specific session record.
 * Metadata provides contextual information for the stored payload,
 * such as event type, creation timestamp, compression, and versioning.
 */
public record SessionMeta(
        SessionEventType type,
        Timestamp createdAt,
        String snapshotType,
        int snapshotVersion,
        String compression
) {

    @Override
    public @NotNull String toString() {
        return "{type=" + this.type.name() +
                ", createdAt=" + this.createdAt.toString() +
                ", snapshotType=" + this.snapshotType +
                ", snapshotVersion=" + this.snapshotVersion +
                ", compression=" + this.compression +
                "}";
    }
}
