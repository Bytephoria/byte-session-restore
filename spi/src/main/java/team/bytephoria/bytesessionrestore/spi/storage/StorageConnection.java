package team.bytephoria.bytesessionrestore.spi.storage;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstraction over a database connection or connection pool.
 * <p>
 * Used by storage implementations (e.g. H2, MySQL) to provide
 * consistent connection management across backends.
 */
public interface StorageConnection {

    /** Opens the underlying database connection or pool. */
    void connect();

    /** Closes all active connections and releases resources. */
    void close();

    /** @return true if the connection is currently active. */
    boolean isConnected();

    /**
     * Returns an active SQL {@link Connection}.
     *
     * @throws SQLException if a connection cannot be obtained
     */
    Connection connection() throws SQLException;
}
