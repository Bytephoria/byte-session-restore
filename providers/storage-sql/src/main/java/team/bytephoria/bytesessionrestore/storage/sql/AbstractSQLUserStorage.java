package team.bytephoria.bytesessionrestore.storage.sql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import team.bytephoria.bytesessionrestore.api.dto.SessionDto;
import team.bytephoria.bytesessionrestore.api.dto.UserDto;
import team.bytephoria.bytesessionrestore.api.model.event.SessionEventType;
import team.bytephoria.bytesessionrestore.api.model.session.SessionMeta;
import team.bytephoria.bytesessionrestore.api.model.session.SessionRecord;
import team.bytephoria.bytesessionrestore.spi.storage.StorageConnection;
import team.bytephoria.bytesessionrestore.spi.storage.UserStorage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSQLUserStorage implements UserStorage {

    protected final StorageConnection storageConnection;

    public AbstractSQLUserStorage(final @NotNull StorageConnection storageConnection) {
        this.storageConnection = storageConnection;
    }

    public abstract String selectQuery();
    public abstract String insertQuery();

    public abstract String cleanUpQuery();

    @Override
    public @Nullable UserDto collectSessions(final @NotNull UUID userId) {
        try (final Connection connection = this.storageConnection.connection();
             final PreparedStatement preparedStatement = connection.prepareStatement(this.selectQuery())
        ) {
            preparedStatement.setString(1, userId.toString());
            final ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            final List<SessionDto> sessionDtos = new ArrayList<>();

            do {
                final SessionEventType sessionEventType = SessionEventType.values()[resultSet.getInt(1)];
                final Timestamp timestamp = resultSet.getTimestamp(2);
                final String compression = resultSet.getString(3);
                final byte[] payload = resultSet.getBytes(4);
                final String snapshotType = resultSet.getString(5);
                final int version = resultSet.getInt(6);

                sessionDtos.add(new SessionDto(sessionEventType, timestamp, compression, payload, snapshotType, version));
            } while (resultSet.next());
            return new UserDto(userId, sessionDtos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUp(final @NotNull UUID userId, final int limit) {

        final String uuidString = userId.toString();
        try (final Connection connection = this.storageConnection.connection();
             final PreparedStatement preparedStatement =
                     connection.prepareStatement(this.cleanUpQuery())
        ) {
            for (final SessionEventType type : SessionEventType.values()) {
                preparedStatement.setString(1, uuidString);
                preparedStatement.setInt(2, type.ordinal());
                preparedStatement.setInt(3, limit);
                preparedStatement.setInt(4, limit);
                preparedStatement.setString(5, uuidString);
                preparedStatement.setInt(6, type.ordinal());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (final SQLException ignored) {}
    }

    @Override
    public void storeSession(
            final @NotNull UUID userId,
            final @NotNull String userName,
            final @NotNull SessionRecord sessionRecord
    ) {
        try (final Connection connection = this.storageConnection.connection();
             final PreparedStatement preparedStatement = connection.prepareStatement(this.insertQuery())
        ) {
            final SessionMeta sessionMeta = sessionRecord.meta();

            preparedStatement.setString(1, userId.toString());
            preparedStatement.setString(2, userName);
            preparedStatement.setInt(3, sessionMeta.type().ordinal());
            preparedStatement.setTimestamp(4, sessionMeta.createdAt());
            preparedStatement.setString(5, sessionMeta.compression());
            preparedStatement.setBytes(6, sessionRecord.payload());
            preparedStatement.setString(7, sessionMeta.snapshotType());
            preparedStatement.setInt(8, sessionMeta.snapshotVersion());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
