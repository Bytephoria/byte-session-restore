package team.bytephoria.bytesessionrestore.api.dto;

import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;

import java.sql.Timestamp;

/**
 * Represents a data transfer object containing serialized session data.
 * This DTO is designed for persistence or network transmission.
 */
public record SessionDto(
        SessionEventType eventType,
        Timestamp createdAt,
        String compression,
        byte[] payload,
        String snapshotType,
        int version
) {
}
