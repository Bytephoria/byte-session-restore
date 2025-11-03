package team.bytephoria.bytesessionrestore.spi.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.api.dto.UserDto;
import team.bytephoria.bytesessionrestore.api.model.session.SessionRecord;

import java.util.UUID;

/**
 * Defines persistence operations for player session data.
 * <p>
 * Implementations handle reading, writing, and cleaning up
 * session records from a specific storage
 */
public interface UserStorage {

    /**
     * Loads all stored sessions for a given player.
     *
     * @param userId player UUID
     * @return a {@link UserDto} or {@code null} if none exist
     */
    @Nullable UserDto collectSessions(final @NotNull UUID userId);

    /**
     * Removes older sessions exceeding the configured limit.
     *
     * @param userId player UUID
     * @param limit  maximum number of sessions to keep
     */
    void cleanUp(final @NotNull UUID userId, final int limit);

    /**
     * Persists a new session record for the given player.
     *
     * @param userId        player UUID
     * @param userName      player name
     * @param sessionRecord session data to store
     */
    void storeSession(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionRecord sessionRecord
    );
}
