package team.bytephoria.bytesessionrestore.api.model.session;

/**
 * Represents a serialized session record, combining metadata and binary payload.
 * The payload typically contains encoded player data such as inventory or location.
 */
public record SessionRecord(SessionMeta meta, byte[] payload) {
}
