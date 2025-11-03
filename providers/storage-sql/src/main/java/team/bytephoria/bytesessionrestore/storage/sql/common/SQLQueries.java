package team.bytephoria.bytesessionrestore.storage.sql.common;

/**
 * Centralized SQL query definitions used across different storage implementations.
 * <p>
 * Each constant represents a reusable SQL statement to ensure consistency
 * across database engines (H2, MySQL, MariaDB).
 */
public final class SQLQueries {

    private SQLQueries() {
    }

    public static final String SESSION_TABLE_NAME = "bsr_sessions";

}
