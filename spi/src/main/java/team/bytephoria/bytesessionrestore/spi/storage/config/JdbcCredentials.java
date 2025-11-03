package team.bytephoria.bytesessionrestore.spi.storage.config;

/**
 * Represents immutable database credentials and connection metadata
 * for any SQL storage adapter (MySQL, MariaDB, PostgreSQL, etc.).
 */
public final class JdbcCredentials {

    private final String hostname;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    public JdbcCredentials(
            String hostname,
            int port,
            String database,
            String username,
            String password
    ) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public String hostname() {
        return hostname;
    }

    public int port() {
        return port;
    }

    public String database() {
        return database;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
