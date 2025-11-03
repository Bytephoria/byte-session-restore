package team.bytephoria.bytesessionrestore.api.model.event;

/**
 * Represents the type of event that triggered a player session.
 * Each value corresponds to a specific context in which
 * a player's state can be captured or restored.
 */
public enum SessionEventType {

    /** Session created when the player dies. */
    DEATH,

    /** Session created when the player changes world. */
    WORLD_CHANGE,

    /** Session created when the player disconnects. */
    DISCONNECT,

    /** Session created manually through commands or APIs. */
    MANUAL
}
