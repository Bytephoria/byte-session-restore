package team.bytephoria.bytesessionrestore.storage.sql.h2;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.spi.storage.StorageConnection;
import team.bytephoria.bytesessionrestore.storage.sql.AbstractSQLUserStorage;
import team.bytephoria.bytesessionrestore.storage.sql.common.SQLQueries;

public final class H2UserStorage extends AbstractSQLUserStorage {

    public H2UserStorage(final @NotNull StorageConnection storageConnection) {
        super(storageConnection);
    }

    public static final String SELECT_SESSIONS_BY_PLAYER = """
            SELECT event_type, created_at, compression, payload, snapshot_type, version
            FROM %s
            WHERE player_id = ?
            ORDER BY created_at ASC;
            """.formatted(SQLQueries.SESSION_TABLE_NAME);

    public static final String DELETE_OLDEST_SESSIONS_SMART = """
        DELETE FROM %1$s
        WHERE id IN (
            SELECT id
            FROM %1$s
            WHERE player_id = ?
              AND event_type = ?
            ORDER BY created_at ASC
            LIMIT (
                SELECT CASE WHEN COUNT(*) > ? THEN COUNT(*) - ? ELSE 0 END
                FROM %1$s
                WHERE player_id = ? AND event_type = ?
            )
        );
        """.formatted(SQLQueries.SESSION_TABLE_NAME);

    public static final String INSERT_SESSION = """
            INSERT INTO %s (player_id, player_name, event_type, created_at, compression, payload, snapshot_type, version)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
            """.formatted(SQLQueries.SESSION_TABLE_NAME);

    @Contract(pure = true)
    @Override
    public @NotNull String selectQuery() {
        return SELECT_SESSIONS_BY_PLAYER;
    }

    @Contract(pure = true)
    @Override
    public @NotNull String insertQuery() {
        return INSERT_SESSION;
    }

    @Override
    public String cleanUpQuery() {
        return DELETE_OLDEST_SESSIONS_SMART;
    }
}
