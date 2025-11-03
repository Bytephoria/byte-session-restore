package team.bytephoria.bytesessionrestore.storage.sql;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import team.bytephoria.bytesessionrestore.spi.storage.StorageConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractSQLStorageConnection implements StorageConnection {

    private final HikariDataSource hikariDataSource;

    public AbstractSQLStorageConnection(final @NotNull HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
    }

    public abstract String createTableQuery();

    @Override
    public void connect() {
        try (
                final Connection connection = this.connection();
                final PreparedStatement preparedStatement = connection.prepareStatement(this.createTableQuery())
        ) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        this.hikariDataSource.close();
    }

    @Override
    public boolean isConnected() {
        return !this.hikariDataSource.isClosed();
    }

    @Override
    public Connection connection() throws SQLException {
        return this.hikariDataSource.getConnection();
    }
}
