package team.bytephoria.bytesessionrestore.storage.sql.h2;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.storage.sql.AbstractSQLStorageConnection;
import team.bytephoria.bytesessionrestore.storage.sql.common.SQLQueries;

public final class H2StorageConnection extends AbstractSQLStorageConnection {

    public H2StorageConnection(final @NotNull HikariDataSource hikariDataSource) {
        super(hikariDataSource);
    }

    public static final String CREATE_TABLE_SESSIONS_QUERY = """
            CREATE TABLE IF NOT EXISTS %s (
                id INT PRIMARY KEY AUTO_INCREMENT,
                player_id CHAR(36),
                player_name VARCHAR(18),
                event_type TINYINT UNSIGNED NOT NULL,
                created_at TIMESTAMP,
                compression VARCHAR(32) NOT NULL,
                payload BLOB,
                snapshot_type VARCHAR(40) NOT NULL,
                version INT
            );
            """.formatted(SQLQueries.SESSION_TABLE_NAME);

    @Override
    public String createTableQuery() {
        return CREATE_TABLE_SESSIONS_QUERY;
    }
}
