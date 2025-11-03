package team.bytephoria.bytesessionrestore.api.model.user;

import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.api.model.session.SessionRecord;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a user within the session system.
 * Each user is identified by a unique {@link UUID} and
 * maintains a collection of session records organized by their {@link SessionEventType}.
 */
public interface User {

    /**
     * Returns the unique identifier of this user.
     *
     * @return the user UUID
     */
    @NotNull UUID userId();

    /**
     * Returns all session records grouped by their {@link SessionEventType}.
     *
     * @return an immutable mapping of event types to session record lists
     */
    @NotNull Map<SessionEventType, List<SessionRecord>> sessions();

    /**
     * Returns all sessions associated with the specified {@link SessionEventType}.
     *
     * @param sessionEventType the session type to filter by
     * @return a non-null list of session records for the given type (may be empty)
     */
    @NotNull List<SessionRecord> sessionsOf(final @NotNull SessionEventType sessionEventType);

    /**
     * Checks whether this user has recorded any sessions for the given type.
     *
     * @param sessionEventType the session type to check
     * @return true if this user contains sessions of the given type, false otherwise
     */
    boolean hasSessionsOf(final @NotNull SessionEventType sessionEventType);
}
